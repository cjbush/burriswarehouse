package code.autoplay.replay.tracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.util.Timer;

import code.autoplay.records.EndReplayEntry;
import code.autoplay.records.TransformEntry;
import code.autoplay.records.IMUTransformEntry;
import code.autoplay.records.base.RecordEntry;
import code.autoplay.replay.Reader;
import code.autoplay.replay.tracking.math.DecimalMatrix;
import code.autoplay.replay.tracking.math.DecimalVector;
import code.model.player.Player;

/**
 * Defines a class that reads stored information from a file. This class is intended to read IMU
 * information. This class runs on a separate thread, creating <code>IMURecordEntry</code>s that
 * are placed in a <code>BlockingQueue</code>. The thread blocks when the Queue is full.  
 * 
 * @author Jordan Hinshaw
 *
 */
public class MicroStrainReader extends Reader {

	//Class Constants
	public static final String TEST_FILE = "data/autoplay/test1.txt";
	private static final String DEFAULT_VEHICLE = "PalletJack1";
	
	private static final long TICKS_PER_SEC = 19660800;
	//private static final Matrix3f COORD_MULT = new Matrix3f(1, 0, 0, 0, 0, 1, 0, 1, 0);
	private static final Matrix3f COORD_MULT = 
		new Quaternion(0.7071067811865476f, 0f, 0f, 0.7071067811865476f).toRotationMatrix();
		//new Quaternion().fromAngleAxis(90*FastMath.DEG_TO_RAD, new Vector3f(1,0,0)).toRotationMatrix();
	
	private static long UNSIGNED_INT_MAX = Long.valueOf("FFFFFFFF", 16);
	
	private static final int ACCEL_READ = 3;
	private static final int ROT_READ = 9;
	
	public static final double STANDARD_G = 9.80665;
	public static final double SCALE = 100;
	public static final double METERS_TO_INCHES = 0.0254;
	
	/**
	 * The initial position of the test pallet jack on game startup.
	 */
	public static final Vector3f INIT_TEST_LOC = new Vector3f(10f, .01f, -5f);
	
	/**
	 * The initial rotation of the test pallet jack on game startup. This Quaternion represents
	 * a 90 degree rotation around the Y-axis.
	 */
	public static final Quaternion INIT_TEST_ROT 
						= new Quaternion(0f, 0.7071067811865476f, 0f, 0.7071067811865476f);
	
	
	//class variables
	private Scanner sc;
	private double prevTime = 0;
	private double currTime = 0;
	private int resetCntr = 0;
	
	private DecimalMatrix rot;
	
	private DecimalVector acc;
	private DecimalVector pos;
	private DecimalVector vel;
	
	private boolean isFirstRead = true;
	
	public MicroStrainReader(String filename, int bufferSize) throws IOException, InterruptedException {
		super(bufferSize);
		
		InputStream inFile = MicroStrainReader.class.getClassLoader().getResourceAsStream(filename);
		
		if(inFile == null) {
			throw new FileNotFoundException(filename);
		}
		else {
			sc = new Scanner(inFile);
			sc.useDelimiter(" |\r\n|\n");
			//sc.useDelimiter("\\s");
		}
		
		//timerResolution = Timer.getTimer().getResolution();
		
		//set initial acceleration to 0,0,0
		//acc = new DecimalVector();
		
		//set the initial velocity to 0,0,0
		vel = new DecimalVector();
				
		//set the initial position to Player.INITIAL_LOCATION
		pos = new DecimalVector(INIT_TEST_LOC);
		
		//set the initial rotation
		rot = new DecimalMatrix( INIT_TEST_ROT.toRotationMatrix() );
	}
	
	public MicroStrainReader(String filename) throws IOException, InterruptedException {
		this(filename, DFLT_BUFFER_SIZE);
	}
	
	/**
	 * @return acceleration in G's
	 */
	private void readAccel(Scanner line) {
		//get acceleration readings
		float[] f = new float[ACCEL_READ];
		
		for(int i = 0; i < ACCEL_READ; i++) {
			f[i] = line.nextFloat();
		}
		
		//Since acceleration is Gs, therefore acceleration due to gravity
		//should equal -1 if the sensor was correctly calibrated. 
		//Adding 1 to the measurement will thus cancel this acceleration. 
		f[2] += 1;
		
		//switch coordinates 
		float temp = f[2];
		f[2] = f[1];
		f[1] = temp;
		
		
		acc = new DecimalVector(f);
	}
	
	private void readRot(Scanner line) {
		float[] f = new float[ROT_READ];
		
		//read the information from the file
		for(int i = 0; i < ROT_READ; i++) {
			f[i] = line.nextFloat();
		}
		
		
		//generate rotation matrix from given info
		DecimalMatrix mat = new DecimalMatrix(f);
		
		//****TEST
		/*Matrix3f curRot = rot.toMatrix3f();
		Matrix3f r3f = mat.toMatrix3f();
		r3f.multLocal(COORD_MULT);
		
		curRot.multLocal(r3f);
		rot.setMatrix(curRot);*/
		//****END TEST
		
		
		/*
		 * Changes the coordinate system to the coordinate system used by JME.
		 * The MicroStrain IMU uses a right-handed coordinate system with Z pointing
		 * up. However, JME uses a right-handed coordinate system in which the Y and
		 * Z axes are switch. The matrix COORD_MULT accomplishes this transformation.
		 */
		if( !mat.isIdentity() ) {
			mat.multBy(COORD_MULT);
		}

		System.out.println(mat);
		
		//multiply current rotation by change it rotation, yielding the new current
		//rotation
		rot.multBy(mat);
		
		System.out.println(rot);
		
		//convert to Matrix3f
		//Matrix3f res = rot.toMatrix3f();
		
		//creates a Quaterion from supplied rotation matrix. Since the method is not
		//static this awkward syntax is needed to create the same effect.
		//return new Quaternion().fromRotationMatrix(curRot);
	}
	
	
	private void readTime(Scanner line) {
		
		String str = Integer.toHexString( line.nextInt() );
		long time = Long.valueOf(str, 16); 
		
		//convert MicroStrain timer ticks to seconds and update
		prevTime = currTime;
		currTime =  ((double) time + UNSIGNED_INT_MAX * resetCntr) / TICKS_PER_SEC;
		
		
		if(currTime < prevTime) {
			/*
			 * The IMU timer resets when it reaches the maximum value of an unsigned int.
			 * When that occurs, the value of previous time will be greater than that of
			 * of the current time. This if statement accounts for that reset.
			 */
			currTime += ((double) UNSIGNED_INT_MAX) / TICKS_PER_SEC;
			resetCntr++;
		}
		else if(isFirstRead) {
			prevTime = currTime;
			isFirstRead = false;
		} 
	}
	
	
	private void calcPos() {
		//Update the current position using: x - x0 = .5at^2 + v0t
		
		//calculate time interval
		double timeDiff = currTime - prevTime;
		
		/*
		 * Calculate .5t^2 * STANDARD_G * METERS_TO_INCHES / SCALE
		 * (STANDARD_G * METERS_TO_INCHES / SCALE) is used to convert from acceleration
		 * in Gs to acceleration in JME units per second
		 */
		double halfTime = Math.pow(timeDiff, 2);
		halfTime *= .5 * STANDARD_G * METERS_TO_INCHES / SCALE;
			
		//calculate change in position
		DecimalVector dx = acc.mult(halfTime);
		dx.addLocal( vel.mult(timeDiff) );
		
		//add the change in position to the current position.
		//This yields the new current position.
		pos.addLocal(dx);	
		
		
		//Update the current velocity using: v = vo + at
		vel.addLocal( acc.mult(timeDiff) );
	}

	@Override
	protected RecordEntry readNext() {
		isEOF = !sc.hasNextLine();
		
		Scanner line = null;
		
		if(!isEOF) {
			line = new Scanner( sc.nextLine() );
			line.useDelimiter(" ");
			
			isEOF = !line.hasNextFloat();
		}
		
		if(!isEOF) {
			//THE FOLLOWING MUST BE DONE IN THIS ORDER
			
			//Reads in acceleration info. Does not update position or velocity because
			//time information is needed before these two values can be calculated.
			this.readAccel(line);
			
			//Reads in rotation change info and updates current rotation
			this.readRot(line);
			
			//Reads in current time stamp. Updates both current time and previous time
			this.readTime(line);
			
			//updates positions and velocity
			this.calcPos();
			
			
			Vector3f tran = pos.toVector3f();
			Quaternion rotQ = rot.toQuaternion();
			
			//System.out.println(acc);
			
			
			return new IMUTransformEntry((float) currTime, "ReplayJack", tran, rotQ);
			//return new IMUTransformEntry(jmeTime, null, tran, q);
		}
		else {
			return new EndReplayEntry( (float) currTime );
		}
		
	}
	
	@Override
	public void run() {
		RecordEntry rec;
		
		while(!isEOF) {
			rec = readNext();
			this.put(rec);
		}
		
		sc.close();
	}

	public static void main(String[] arg) {
		try {
			MicroStrainReader mic = new MicroStrainReader(TEST_FILE);
			mic.start();
			
			//System.out.println(new Quaternion().fromAngleAxis(90 * FastMath.DEG_TO_RAD, new Vector3f(0,1,0)));
			System.out.println(new Quaternion().fromRotationMatrix(0f, 1f, 0f, .017452406f, 0f, -.9998477f, 
					-.9998477f, 0, .017452406f));
			
			PrintWriter out = new PrintWriter("src/data/autoplay/testOutput.txt");
			
			while( !mic.hasEnded() ){
				RecordEntry r = mic.nextRecord();
				//System.out.println(r);
				out.println(r);
				out.flush();
			}
			
			out.close();
			System.out.println("end");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
