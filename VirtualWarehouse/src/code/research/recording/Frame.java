package code.research.recording;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import code.app.WarehouseTrainer;

/**
 * Creates images (not used)
 * 
 * @author PickSim Team (Chris Bush, Dan Jewett, Caleb Mays)
 * 
 */

public class Frame {
	public BufferedImage image;
	private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private GraphicsDevice[] gs = ge.getScreenDevices();
	private DisplayMode mode = gs[1].getDisplayMode();
	private int x0 = (mode.getWidth()/2) - (WarehouseTrainer.getWidth()/2);
	private int x1 = (mode.getWidth()/2) + (WarehouseTrainer.getWidth()/2);
	private int y0 = (mode.getHeight()/2) - (WarehouseTrainer.getHeight()/2);
	private int y1 = (mode.getHeight()/2) + (WarehouseTrainer.getHeight()/2);
	private final Rectangle screen = new Rectangle(x0, y0, x1, y1);
	private Robot robot;
	public int id = 0;
	
	public Frame(){
		try {
			robot = new Robot();
			image = robot.createScreenCapture(screen);
			id++;
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
