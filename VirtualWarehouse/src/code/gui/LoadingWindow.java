package code.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 * This class provides a splash-screen like window that displays an image and
 * some text at the bottom.  The window is self-sufficient in that it displays
 * itself.  Simply call the dispose method to close it when appropriate.
 * 
 * @author VirtualVille Team (Richard Bradt, Gabe Greve, Eric Smith, and Ben Wiley)
 * Modified by Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class LoadingWindow extends JWindow {

	private static final String MESSAGE = 
		"<html><center><font size = 4><b>" + 
		"Created by Jordan Hinshaw, Matt Kent, and Aaron Ramsey<br />" +
		"Updated by Chris Bush, Dan Jewett, Caleb Mays<br />" +
		"</b></font></center></html>";
	private static final String LOGO_PATH = "data/gui/gamelogo.png";
	private static final Color BG_COLOR = new Color(.2f,.2f,.2f,1f);
	private static final Color TEXT_COLOR = Color.GRAY;
	
	private JProgressBar pBar;
	
	/**
	 * This constructor creates a loading window with the logo of the game
	 * featured.  It also displays the designers' names.  The window will
	 * automatically make itself visible in the center of the screen and
	 * will stay visible until the dispose method is called.
	 */
	public LoadingWindow(int width, int height) {
		// Get the image to display
        URL logoURL = LoadingWindow.class.getClassLoader().getResource(LOGO_PATH);
        ImageIcon logo = new ImageIcon(logoURL);
        int logoWidth = (int) (.80 * width);
        int logoHeight = (int) (.80 * height);
        Image scaledLogo = logo.getImage().getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
        logo = new ImageIcon(scaledLogo);
        JLabel logoHolder = new JLabel();
        logoHolder.setIcon(logo);
        logoHolder.setHorizontalAlignment(SwingConstants.CENTER);
        logoHolder.setVerticalAlignment(SwingConstants.CENTER);
        
        // Get the text to display
        JLabel text = new JLabel(MESSAGE);
        text.setForeground(TEXT_COLOR);
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout());
        textPanel.add(text);
        textPanel.setBackground(BG_COLOR);
        
        // Build a bottom panel with the text and progress
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(textPanel, BorderLayout.CENTER);
        pBar = new JProgressBar();
        pBar.setStringPainted(true);
        bottomPanel.add(pBar, BorderLayout.SOUTH);
        
		// Build the window, adding the picture and text
		JPanel panel = (JPanel) this.getContentPane();
		panel.setLayout(new BorderLayout());
		panel.add(logoHolder, BorderLayout.CENTER);
		panel.add(bottomPanel, BorderLayout.SOUTH);
		panel.setBackground(BG_COLOR);
		setSize(width, height);
		setVisible(true);
		//setAlwaysOnTop(alwaysOnTop);
		
		// Center window on the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();

        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        setLocation((screenSize.width - frameSize.width) / 2 + 3, 
        		(screenSize.height - frameSize.height) / 2 + 25);
        
        // Make mouse cursor the wait cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	/**
	 * This method allows the progress bar's value to be increased, but
	 * the text that is displayed remains.
	 * 
	 * @param value The amount to increase the progress bar by (max progress
	 * is 100)
	 */
	public void addProgress(int value) {
		pBar.setValue(value + pBar.getValue());
	}
	
	/**
	 * Allows the progress bar's text to be altered as well as updating the
	 * value of the progress.
	 * 
	 * @param value The amount to increase the progress bar by (max progress
	 * is 100)
	 * @param message The new string to display on the progress bar
	 */
	public void addProgress(int value, String message) {
		addProgress(value);
		pBar.setString(message);
	}
}
