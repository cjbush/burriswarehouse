package clientside;

import java.awt.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;



/**
 * <p>Title: IMApplication</p>
 * <p>Description: instant messaging application</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Czechowski & Krupka
 * @version 1.0
 */

public class Login extends JDialog {
  // Socket Pieces:
  public static final String HOSTNAME = "localhost";
  public static final int PORTNUMBER = 31415;
  Socket mySock;

  // GUI Pieces:
  JPanel mainBorder = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainLayout = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JTextField usernameTextbox = new JTextField();
  JLabel usernameLabel = new JLabel();
  JLabel logoLabel = new JLabel();
  JButton startLogin = new JButton();
  boolean packFrame = false;

  public Login(Frame frame, String title, boolean modal) {
    // obligatory code:
    super(frame, title, modal);
    try {
      mySock = new Socket(HOSTNAME, PORTNUMBER);
      // if a connection fails, the window won't be created
      jbInit();
      pack();
    }
    catch(Exception e) {
      System.out.println(e);
      e.printStackTrace();
    }

  }

  public Login() {
    this(null, "", false);
  }

  private void jbInit() throws Exception {
    mainBorder.setLayout(borderLayout1);
    mainLayout.setLayout(xYLayout1);
    usernameTextbox.setText("<enter username>");
    usernameTextbox.addFocusListener(new Login_usernameTextbox_focusAdapter(this));
    usernameLabel.setRequestFocusEnabled(false);
    usernameLabel.setText("Username:");
    startLogin.setText("inst-inst-inst");
    startLogin.addActionListener(new Login_startLogin_actionAdapter(this));
    mainBorder.setMinimumSize(new Dimension(209, 298));
    mainBorder.setPreferredSize(new Dimension(209, 298));
    mainLayout.setPreferredSize(new Dimension(209, 298));
    getContentPane().add(mainBorder);
    mainBorder.add(mainLayout, BorderLayout.CENTER);
    mainLayout.add(logoLabel,   new XYConstraints(6, 7, 198, 169));
    mainLayout.add(startLogin, new XYConstraints(48, 260, 113, 35));
    mainLayout.add(usernameTextbox, new XYConstraints(52, 214, 107, 30));
    mainLayout.add(usernameLabel, new XYConstraints(50, 183, 86, 24));
    ImageIcon logoIcon = new ImageIcon("logo.png");
    logoLabel.setIcon(logoIcon);
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    this.setVisible(true);
  }

  protected void processWindowEvent(WindowEvent event) {
     super.processWindowEvent(event);
     if (event.getID() == WindowEvent.WINDOW_CLOSING) {
       try {
         mySock.close();
       }
       catch (Exception e) {
          e.printStackTrace();
       }
       System.exit(0);
     }
   }

  public static void main(String[] args) {
    Login myLogin = new Login();
  }

  void startLogin_actionPerformed(ActionEvent event) {
    String user = usernameTextbox.getText();
    if(!user.equals("login") && !user.equals("logout") && !user.equals("system") && !user.equals("") &&  !user.equals("connect") && !user.equals("disconnect") ) {
      String message = (char) 128 + "connect" + (char) 128 + user + (char) 128;
      try {
        OutputStream out = mySock.getOutputStream();
        out.write(toByteArray(message));
        this.setVisible(false);
        IMWindow myWindow = new IMWindow();
      }
      catch(Exception e) {
        System.out.println(e);
        e.printStackTrace();
      }
    }
    // else something went wrong
  }

  public static byte [] toByteArray(String s) {
    int len = s.length();
    byte [] b = new byte[len];
    for(int i = 0; i < len; i++) {
      char c = s.charAt(i);
      b[i] = (byte)c;
    }
    return b;
  }

  void usernameTextbox_focusGained(FocusEvent e) {
    usernameTextbox.setText("");
  }

}

class Login_startLogin_actionAdapter implements java.awt.event.ActionListener {
  Login adaptee;

  Login_startLogin_actionAdapter(Login adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.startLogin_actionPerformed(e);
  }
}

class Login_usernameTextbox_focusAdapter extends java.awt.event.FocusAdapter {
  Login adaptee;

  Login_usernameTextbox_focusAdapter(Login adaptee) {
    this.adaptee = adaptee;
  }
  public void focusGained(FocusEvent e) {
    adaptee.usernameTextbox_focusGained(e);
  }
}
