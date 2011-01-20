package clientside;
import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 * <p>Title: IMApplication</p>
 * <p>Description: instant messaging application</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Czechowski & Krupka
 * @version 1.0
 */

public class ClientThread extends Thread {
  private static final int GETTING_DATA = 0;
  private static final int CR_STATE = 1;
  private static final int CR_LF_STATE = 2;
  private static final int CR_LF_CR_STATE = 3;
  private static final int CLOSING = 4;
  private static final String CR = String.valueOf(((char)13));
  private static final String LF = String.valueOf(((char)10));

  private InputStream myInput;
  private JTextPane myOutPane;
  private int myState;
  private IMWindow myWin;

  public ClientThread(InputStream in, JTextPane outBox, IMWindow win) {
    myInput = in;
    myOutPane = outBox;
    myState = GETTING_DATA;
    myWin = win;
  }

  public void run() {
    try {
      while(myState != CLOSING) {
        int newInt = myInput.read();
        if(newInt != -1) {
          parseInput((char)newInt);
        }
      }
      System.out.println("Ending server input stream");

      myInput.close();
      myWin.jTextPane2.setText("");
//      myOutPane.setCaretPosition(myOutPane.getText().length());
      myWin.disconnectTalkman();

    }
    catch(Exception e) {
      System.out.println("InputStream Failed on my socket connection.");
      System.out.println(e);
      e.printStackTrace();
    }
    if (this.isAlive()) {
      System.out.println("thread still alive");
    }
    System.out.println("Thread ended.");
    myWin = null;
  }


  private void parseInput(char c) {
    String s = String.valueOf(c);
    //System.out.println("client sent: " + s);
    myOutPane.setText(myOutPane.getText() + s);
//    if (s.equals("\n")) {
//      myWin.disconnectTalkman();
//      myState = CLOSING;
//    }
    switch (myState) {
      case GETTING_DATA:
        if (s.equals(CR)) {
          myState = CR_STATE;
        }
        break;

      case CR_STATE:
        if (s.equals(LF)) {
          myState = CR_LF_STATE;
        }
        else {
          System.out.println("Error: invalid character recieved while in CR_STATE: (" + s + ") "+ ((int)c));
        }
        break;

      case CR_LF_STATE:
        if (s.equals(CR)) {
          myState = CR_LF_CR_STATE;
        }
        else {
          myState = GETTING_DATA;
        }
        break;

      case CR_LF_CR_STATE:
        if (s.equals(LF)) {
          myState = CLOSING;
        }
        else {
          System.out.println("Error: invalid character recieved while in CR_LF_CR_STATE: (" + s + ") "+ ((int)c));
        }
    }
  }
}
