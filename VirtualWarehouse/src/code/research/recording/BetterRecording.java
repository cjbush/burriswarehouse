package code.research.recording;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;

/**
 * Takes screenshots of the game and creates a movie from it
 * Can be used for ingame footage
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class BetterRecording implements Runnable{
	
	Robot robot;
	final Toolkit toolkit = Toolkit.getDefaultToolkit();
	final Rectangle screenBounds = new Rectangle(0, 0, 1680, 1050);
	
	final long start;
	long current;
	
	int index;
	
	TimeUnit t;
	
	boolean recording = true;
	
	final IMediaWriter writer = ToolFactory.makeWriter("C:\\images\\output.mp4");
	
	public BetterRecording(){
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		index = writer.addVideoStream(0, 0, 5000, 1);
		start = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		if(recording){
			current = System.currentTimeMillis();
			BufferedImage image = robot.createScreenCapture(screenBounds);
			BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			image2.getGraphics().drawImage(image, 0, 0, null);
			writer.encodeVideo(index, image2, current-start, TimeUnit.MILLISECONDS);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void cleanup(){
		recording = false;
		writer.close();		
	}

}
