package clientside;

import java.awt.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;


/**
 * <p>Title: IMApplication</p>
 * <p>Description: instant messaging application</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Josh Krupka
 * @version 1.0
 */
/*
 95415360

 prTaskLUTGetAssignment('06-02-04 07:57:48', '64166008', 'rburns', '', '', '0', '')
  prTaskLUTGetAssignment('06-02-04 07:57:48', '64166007', 'rburns', '', '', '0', '')

 prTaskLUTGetPicks('06-02-04 07:57:48', '64166008', 'rburns', '', '0', '0', '0')
 prTaskLUTPicked('06-02-04 07:57:48', '64133649', 'jkrupka', '67210', '67210', '5001', '1', '2', '30', '1', '0', '0', '0')
prTaskLUTVariableWeight('06-02-04 07:57:48', '64166009', 'kgarth', '7784', '7784', '4H00500', '2', '0', '78.7', '1', '019078533103663032010007871509092721140952075973')
prTaskLUTValid128('06-05-09 08:20:29', '64333822', 'gwalstea', '7785', '7785', '4G09700', '2', '0', '019007080004921232010003401509081221124949106725')

prTaskLUTConfiguration('09-05-04 12:30:20', '64133649')
prTaskLUTConfiguration('09-05-04 12:30:20', '095415360')
prTaskLUTHowAmIDoing('07-14-08 10:30:20', '095415360', 'omolina')
prTaskLUTGetCntTp('07-14-08 10:30:20', '64333822', 'kgarth')
prTaskLUTHowAmIDoing('07-14-08 10:30:20', '095415360', 'kwarren')
prTaskLUTHowAmIDoing('07-14-08 10:30:20', '64133649', 'omolina')
prTaskLUTRegionPermissionsForWorkType('06-02-04 07:57:48', '64133649', 'jkrupka', 'N')
prTaskLUTPickingRegion('06-02-04 07:57:48', '64133649', 'jkrupka', '1', 'N')

prTaskLUTRequestWork('06-02-04 07:57:48', '64133649', 'jkrupka', '9636', '1', '0')
prTaskLUTGetAssignment('06-02-04 07:57:48', '64133649', 'jkrupka', '', '', '2', '1')
prTaskLUTGetPicks('06-02-04 07:57:48', '64133649', 'jkrupka', '286319', '0', '0', '0')
prTaskODRPicked('06-02-04 07:57:48', '64133649', 'jkrupka', '342', '9636', '5001', '1', '2', '30', '1', '0', '0', '0')
prTaskLUTPicked('06-02-04 07:57:48', '64133649', 'jkrupka', '342', '9636', '5001', '1', '2', '30', '1', '0', '0', '0')
prTaskODRVariableWeight('06-02-04 07:57:48', '64133649', 'jkrupka', '342', '9636', '5001', '14.2', '1')

prTaskODRUpdateStatus('06-02-04 07:57:48', '64133649', 'jkrupka', '342', '', '0', 'N')

prTaskODRUpdateStatus([08-04-04 16:44:54, 64166009, jkrupka, 29037, 6H03910, 8, 1, S])

prTaskLUTGetPIDQuantity('04-02-05 05:57:48', '64133649', 'jkrupka', 'AssignID', 'WorkID', 'LocID', 'Sequence', 'CustPID', 'QtyPicked')
prTaskLUTUpdateStatus('06-02-04 07:57:48', '64133649', 'jkrupka', '264', '', '2', 'N')
prTaskLUTCheckOperatorStatus('06-02-04 07:57:48', '64133649', 'jkrupka', '264')
prTaskLUTPassAssignment('06-02-04 07:57:48', '64133649', 'jkrupka', '264')
prTaskLUTUndoLastPick('06-02-04 07:57:48', '64133649', 'jkrupka', '264', '11', '21284')
prTaskLUTReviewCasePicks('06-02-04 07:57:48', '64133649', 'jkrupka', '264', '11')
prTaskLUTNewContainer('06-02-04 07:57:48', '64133649', 'jkrupka', '264', '11', '0')
prTaskLUTGetLocationToInvestigate('06-02-04 07:57:48', '64133649', 'jkrupka')
prTaskLUTInvestigated('06-02-04 07:57:48', '64133649', 'jkrupka', '4894', '1')
prTaskLUTPrint('06-02-04 07:57:48', '64133649', 'jkrupka', '342', '1', '9636')
prTaskLUTPrint('01-04-07 14:46:43', '64166009', 'gwalstea', '421309', '0', '')
prTaskLUTContainerReview('06-02-04 07:57:48', '64133649', 'jkrupka', '342', '11', '0')
prTaskLUTStopAssignment('06-02-04 07:57:48', '64133649', 'jkrupka', '62', '0')

prTaskLUTSignOn('09-05-04 12:30:20', '64133649', 'jkrupka', '1234')
prTaskLUTSignOff('09-05-04 12:30:20', '64133649', 'jkrupka')
prTaskLUTContainerTypes('10-21-09 11:34:31', '64333822', 'kgarth')
prTaskLUTPickingRegion('10-22-09 09:24:26', '64333822', 'kgarth', '1', '3')
prTaskLUTGetPicks('10-27-09 14:54:44', '64333822', 'kgarth', '697031', '0', '1', '0')
prTaskODRUpdateStatus('10-29-09 13:07:41', '64333822', 'kgarth', '697031', '6M00900', '0', 'S')


//These are all part of Voice Direct 3.x - won't work with older versions
prTaskLUTCoreConfiguration('10-13-09 12:03:15', '64333822', 'kgarth', 'en_US', '073',  'CT-30-01-015')
prTaskLUTCoreBreakTypes('10-20-09 13:14:29', '64333822', 'kgarth')
prTaskLUTCoreValidFunctions('10-20-09 15:52:06', '64333822', 'kgarth', '0')


*/
public class IMWindow extends JDialog {
	public static final String HOSTNAME = "localhost";
	public static final int PORTNUMBER = 31416;
//	public static final String HOSTNAME = "wms";
//	public static final int PORTNUMBER = 31473;
	
  
  public static boolean disconnect = false;


  private Socket mySock;
  private OutputStream myOut;
  private ClientThread myInputThread;
  private InputStream myIn;



  // GUI stuff:
  private JPanel jPanel1 = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  protected JTextPane jTextPane2 = new JTextPane();
  private JButton sendButton = new JButton();
  private JTextPane jTextPane1 = new JTextPane();
  private static final String DELIMITER = String.valueOf((char)128);
  JScrollPane jScrollPane1 = new JScrollPane();
  JScrollPane jScrollPane2 = new JScrollPane();

  public Socket getSocket() {
	  return mySock;
  }
  public void setSocket(Socket newSock) {
    mySock = newSock;
  }
  public InputStream getInStream() {
     return myIn;
   }
   public void setInStream(InputStream newIn) {
     myIn = newIn;
   }
   public OutputStream getOutStream() {
      return myOut;
    }
    public void setOutStream(OutputStream newOut) {
      myOut = newOut;
    }

  public IMWindow(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
      jbInit();
      pack();
      jTextPane1.setText("Welcome to the Talkman emulation application.\n");
//      sendButton.addActionListener(new AWTEventListenerProxy(AWTEvent.));
//      myInputThread = new ClientThread(in , jTextPane1, choice1);
//      myInputThread.start();
//      myOut = out;
//      mySock = s;
    }

  public IMWindow() {
    this(null, "", false);
  }

  private void jbInit() {
    jPanel1.setLayout(xYLayout1);
    jTextPane2.setFont(new java.awt.Font("SansSerif", 0, 15));
    jTextPane2.setNextFocusableComponent(sendButton);
    jTextPane2.setText("");
    sendButton.addActionListener(new IMWindow_sendButton_actionAdapter(this));
    jTextPane1.setEnabled(false);
    jTextPane1.setFont(new java.awt.Font("SansSerif", 0, 15));
    jTextPane1.setText("");
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    jScrollPane1.setNextFocusableComponent(null);
    jScrollPane2.setFont(new java.awt.Font("Dialog", 0, 11));
    jScrollPane2.setNextFocusableComponent(sendButton);
    sendButton.setText("Send");
    jPanel1.add(jScrollPane2,     new XYConstraints(9, 251, 669, 135));
    jPanel1.add(jScrollPane1,     new XYConstraints(8, 7, 670, 193));
    jPanel1.add(sendButton,  new XYConstraints(607, 397, 70, 64));
    jScrollPane1.getViewport().add(jTextPane1, null);
    jScrollPane2.getViewport().add(jTextPane2, null);
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    this.setVisible(true);
  }


  protected void processWindowEvent(WindowEvent event) {
    super.processWindowEvent(event);
    if (event.getID() == WindowEvent.WINDOW_CLOSING) {
      disconnectTalkman();
      myInputThread = null;

      System.exit(0);
    }
  }
  public void connectTalkman(){
    try {
      mySock = new Socket(HOSTNAME, PORTNUMBER);
      myIn = mySock.getInputStream();
      myOut = mySock.getOutputStream();
//      myOut.close();
//      OutputStream myOut2 = mySock.getOutputStream();
//      myOut2.write(1);
      
      myInputThread = new ClientThread(myIn , jTextPane1, this);
      myInputThread.start();
    }
    catch (UnknownHostException ex) {
      System.out.println("Unknown host");
      ex.printStackTrace();
    }
    catch (IOException ex) {
      System.out.println("IOException");
      ex.printStackTrace();
    }

  }
  public void disconnectTalkman() {
    System.out.println("disconnecting talkman...");
    try {
      if ((myIn != null) && (myOut != null)) {
        myIn.close();
        myOut.close();
        System.out.println("io streams closed");
      }
    }
    catch (IOException ex) {
      System.out.println("Error closing io streams");
      ex.printStackTrace();
    }
    try{
      if (mySock != null) {
        mySock.close();
        System.out.println("socket closed");
      }

    }
    catch(Exception e) {
      System.out.println("Error closing socket.");
      e.printStackTrace();
    }

//    if(myInputThread.isAlive()) {
//      System.out.println("Didn't receive disconnect.");
//      myInputThread.stop();
//    System.out.println("thread was still alive");
//    myInputThread.interrupt();
//    }


//    System.exit(0);

  }

  private void send(String toUser, String msg) {
    String message = DELIMITER + toUser + DELIMITER + msg + DELIMITER;
    try {
      myOut.write(toByteArray(message));
    }
    catch(Exception e) {
      System.out.println("Failed to send message.");
      System.out.println(e);
      e.printStackTrace();
    }
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

  void sendButton_actionPerformed(ActionEvent e) {
    connectTalkman();
    disconnect  = false;
    String cr = String.valueOf ((char)13);
    jTextPane1.setText("");
//    String text = jTextPane2.getText();

//    text = text.substring(0, text.indexOf("("));
//    jTextPane2.setText("");

    try {

      myOut.write(toByteArray(jTextPane2.getText().trim() + cr + cr));
      //check to see if func call being made is to sign off



    }
    catch (IOException ex) {
    }
  }

  public static void main(String[] args) {
//    Socket theSock = null;
//    try {
//      theSock = new Socket(HOSTNAME, PORTNUMBER);
      IMWindow win = new IMWindow();
//    }
//    catch (UnknownHostException ex) {
//    }
//    catch (IOException ex) {
//    }

    }

}

class IMWindow_sendButton_actionAdapter implements java.awt.event.ActionListener {
  IMWindow adaptee;

  IMWindow_sendButton_actionAdapter(IMWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.sendButton_actionPerformed(e);
  }
}
