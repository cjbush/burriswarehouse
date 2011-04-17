package code.research.recording;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Takes screenshots of the game and creates a movie from it
 * Can be used for ingame footage (see BetterRecording)
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class Recording implements Runnable{
	private int id = 0;	
	private boolean record;
	
	public Recording(){
		System.out.println("Setting up recording.");
		record = true;
		System.out.println("Cleaning up any previous screenshots.");
		Runtime r = Runtime.getRuntime();
		try {
			r.exec("cmd /c del C:\\images\\*.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Recording created.");
		this.run();
	}
	
	public void run(){
		
			Frame f = new Frame();
			String filename = "C:/images/"+(id++)+".jpg";
			File file = new File(filename);
			//System.out.println("Saving image to "+file.getPath());
			try {
				if(!file.exists()){
					file.createNewFile();
				}
				ImageIO.write(f.image, "jpg", file);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		
	}
	
	public void cleanup(){
		System.out.println("Compressing to MP4");
		record = false;
		
		String generated = ((Long)System.currentTimeMillis()).toString();
		String output = "C:\\images\\"+generated+".mp4";
		
		System.out.println("Creating file "+output);
		
		Runtime r = Runtime.getRuntime();
		try {
			String command = "cmd /C ffmpeg -f image2 -r 10 -b 1800 -i C:\\images\\%d.jpg "+output;
			String cleanup = "cmd /C del C:\\images\\*.jpg";
			System.out.println("Executing command: "+command);
			r.exec(command);
			r.exec(cleanup);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
