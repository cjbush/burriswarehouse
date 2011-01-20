package serverside;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import services.BurrisVocApplication;

import com.burris.tca.services.BurrisJavaApplication;

import java.sql.*;

/**
 * <p>Title: ServerThread</p>
 * <p>Description: Class that is spun off to handle individual clients that connect</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Josh Krupka
 * @version 1.0
 */

//ODR Confirmation byte is 0x100, which means any character will be accepted

public class ServerThread
    extends Thread {

   public static final String LF = String.valueOf( (char) 10);
   public static final String CR = String.valueOf( (char) 13);
   public static final String DOUBLE_TERMINATE = CR + LF + CR + LF;
   public static final String SINGLE_TERMINATE = CR + LF;
   public static final String LFLF = LF + LF;
   public static final String CRCR = CR + CR;
   public static final String COMMA = ",";
   public static final String S_QUOTE = "'";


   private static final int GETTING_PROC_NAME = 0;
   private static final int GETTING_PARAM_LIST = 1;
   private static final int GETTING_A_PARAM = 2;
   private static final int DONE_CALL = 3;
   private static final int CLOSING = 4;
   private static final int LF_STATE = 5;
   private static final int CR_STATE = 6;
   private static final int CR_LF_STATE = 7;
   private static final int CR_LF_CR_STATE = 8;

   public static final int SOCKET_TIMEOUT_MS = 18000;


   private BurrisVocApplication tca;
   private TheServices serv;
   private int myState;
   private Socket talkmanSocket;
   private Socket tcaSocket;
   private InputStream talkmanIn;
   private OutputStream talkmanOut;
   private String tcaHostName;
   private int tcaPortNum;
   private int javaTcaPortNum;
   private int tcaTimeout;
   private int errorPauseLength;
   private int reqTimeForRetry;
   private boolean tcaConnected;;
   private Connection conn;


   private String talkProcedureName;
   private ArrayList inputs;
   private String parameter;
   private StringBuffer sb;
   private CedarvilleConnect cc;

   /**
    * This thread handles communication between a client on Socket s and the
    * tca object tca.
    * @param s Socket
    * @param tcaHost String
    * @param tcaPort String
    */
   public ServerThread(Socket s, String tcaHost, int tcaPort, int timeout,
                       int pauseLength, int timeForRetry, int javaTcaPort, CedarvilleConnect cc) {
      tca = new BurrisVocApplication();
      tcaConnected = false;
      tcaHostName = tcaHost;
      tcaPortNum = tcaPort;
      tcaTimeout = timeout;
      reqTimeForRetry = timeForRetry;
      errorPauseLength = pauseLength;
      this.javaTcaPortNum = javaTcaPort;
      talkmanSocket = s;
      myState = GETTING_PROC_NAME;
      serv = new TheServices(tca,cc);
      talkProcedureName = "";
      inputs = new ArrayList(15);
      parameter = "";
      sb = new StringBuffer();
      talkmanIn = null;
      talkmanOut = null;
      tcaSocket = null;
      conn=null;
      this.cc = cc;
   }

   /**
    * This overrides the run() method of Thread.
    */
   public void run() {

	   long start = System.currentTimeMillis();
      try {
         talkmanIn = talkmanSocket.getInputStream();
         talkmanOut = talkmanSocket.getOutputStream();
//11/14/05 jck moved the connectTCA call to the callService function, right before the service is called
//         connectTCA(tcaHostName, tcaPortNum);
         boolean keepListening = true;
         try {
            // this is the loop that keeps waiting for input from the client
            while (keepListening && myState != CLOSING) {
               int newInt = talkmanIn.read();
               if (newInt != -1) {
                  parseInput( (char) newInt);
               }
            }
         }
         catch (SocketException e) {
            System.err.println(ServerSide.getDate() + ": Error setting socket timeout, socket and thread will be ended.");
         }
         catch (InvalidCharSequenceException e) {
            System.err.println(ServerSide.getDate() + ": Input sequence failed, thread will be ended.");
            e.printStackTrace();
         }
         finally {
            if (tcaConnected) {
//               long start = System.currentTimeMillis();
               disconnectTca();
//               System.out.println("disconnect: " + (System.currentTimeMillis()-start)/1000.000);
            }
            disconnectTalkman();
         }

      }
      catch (IOException e) {
         System.err.println(ServerSide.getDate() + "InputStream Failed on my socket connection.");
         e.printStackTrace();
      }
      catch (TCAException e) {
         System.err.println(ServerSide.getDate() + "TCA Error: ");
         e.printStackTrace();
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }

      tca = null;
      serv = null;
      talkmanSocket = null;
      talkmanIn = null;
      sb = null;
      
      System.out.println("Thread ended in " + (System.currentTimeMillis() - start)/1000.00);
   }
   

   /**
    * Disconnects tca and explicitly closes the socket since under some situations tca doesn't completely recycle the 
    * socket resources.
    * @throws TCAException
    */
   public void disconnectTca() throws  TCAException{

         try{
        	 conn.close ();
             System.out.println ("Database connection terminated");
//        	 tca.disconnect();
//        	 if (tcaSocket != null) {
//        		 tcaSocket.close();
//        	 }
         }
         catch (Exception e) {
            throw new TCAException(e.getMessage());
         }
   }

   /**
    * Closes the streams and socket associated with the talkman communication
    * @throws IOException
    */
   public void disconnectTalkman() throws IOException {
	   if (talkmanIn != null) {
		   talkmanIn.close();
	   }
	   if (talkmanOut != null) {
		   talkmanOut.close();
	   }
	   if (talkmanSocket != null) {
		   talkmanSocket.close();
	   }
   }
   /**
    * This calls the TCA service that corresponds to the given talkman procedure name
    * and passes the values in params as the inputs.
    * @param talkProcName String
    * @param params ArrayList
    * @return boolean
    */
   public boolean callService(String talkProcName, ArrayList params) {
      boolean hadError = false;
      int procNum = 0;
      boolean retryRequest = true;
      boolean transactionSuccessful = false;
      int numAttempts = 0;
      int timeout = 0;
      long iterationStartTime = 0;
      long originalStartTime = System.currentTimeMillis();
      while (retryRequest && !transactionSuccessful/* &&
             ((System.currentTimeMillis()-originalStartTime) < 8000)*/) {//JCK 12-30-05 time check moved to the catch block
         numAttempts++;
         try {
            if (serv.procNameToNumHash.containsKey(talkProcName)) {
               procNum = ( (Integer) serv.procNameToNumHash.get(talkProcName)).
                   intValue();
               //12-12-05 jck added so that this value is set even if connectTCA fails
               setNumOutFields(procNum);
            }
            else {
               throw new Exception("Procedure " + talkProcName +
                                   " does not exist.");
            }
//11-14-05 jck moved the connectTCA call here so that a failure to connect still returns error msg to talkman
//         long start = System.currentTimeMillis();
            if (!tcaConnected) {
               if (numAttempts < 2) {
                  timeout = tcaTimeout;
               }
               else {
                  timeout = timeout - (int)(System.currentTimeMillis() - iterationStartTime);
                  System.err.println("timeout= " + timeout);
               }
               iterationStartTime = System.currentTimeMillis();

               connectTCA(tcaHostName, tcaPortNum, timeout);
//               if (numAttempts==2) {
//           s       Thread.sleep(12000);
//               }
            }
//         System.out.println("connect: " + (System.currentTimeMillis()-start)/1000.000);
            switch (procNum) {
               case TheServices.LUT_CHECK_OPERATOR_STATUS:
                  serv.callLutCkOperSts(params, sb);
                  break;
               case TheServices.LUT_CONFIGURATION:
                  serv.callLutConfig(params, sb);
                  break;
               case TheServices.LUT_CONTAINER_REVIEW:
                  serv.callLutCntReview(params, sb);
                  break;
               case TheServices.LUT_GET_ASSIGNMENT:
                  serv.callLutGetAssign(params, sb);
                  break;
               case TheServices.LUT_GET_LOCATION_TO_INVESTIGATE:
                  serv.callLutGetLocInv(params, sb);
                  break;
               case TheServices.LUT_GET_PICKS:
                  serv.callLutGetPicks(params, sb);
                  break;
               case TheServices.LUT_INVESTIGATED:
                  serv.callLutInvestgtd(params, sb);
                  break;
               case TheServices.LUT_NEW_CONTAINER:
            	   //points to new new container service
                  serv.callLutNewContService(params, sb);
                  break;
               case TheServices.LUT_PASS_ASSIGNMENT:
                  serv.callLutPassAssgn(params, sb);
                  break;
               case TheServices.LUT_PICKED:
                  serv.callLutPicked(params, sb);
                  break;
               case TheServices.LUT_PICKING_REGION:
                  serv.callLutPckRegion(params, sb);
                  break;
               case TheServices.LUT_PRINT:
                  serv.callLutPrint(params, sb);
                  break;
               case TheServices.LUT_REGION_PERMISSIONS_FOR_WORK_TYPE:
                  serv.callLutRgWkPerm(params, sb);
                  break;
               case TheServices.LUT_REQUEST_WORK:
                  serv.callLutRequestWk(params, sb);
                  break;
               case TheServices.LUT_REVIEW_CASE_PICKS:
                  serv.callLutReviewPck(params, sb);
                  break;
               case TheServices.LUT_SIGN_ON:
                  serv.callLutSignOn(params, sb);
                  break;
               case TheServices.LUT_SIGN_OFF:
                  serv.callLutSignOff(params, sb);
                  break;
               case TheServices.LUT_STOP_ASSIGNMENT:
                  serv.callLutStopAssgn(params, sb);
                  break;
               case TheServices.LUT_UNDO_LAST_PICK:
                  serv.callLutUndoLPick(params, sb);
                  break;
               case TheServices.LUT_UPDATE_STATUS:
                  serv.callLutUpdateSts(params, sb);
                  break;
               case TheServices.LUT_GET_SHORT_REASONS:
                  serv.callLutGetShortReasons(params, sb);
                  break;
               case TheServices.LUT_VALIDATE_PID:
                  serv.callLutValidatePid(params, sb);
                  break;
               case TheServices.ODR_PICKED:
                  serv.callOdrPicked(params, sb);
                  break;
               case TheServices.LUT_GET_PID_QTY:
                  serv.callLutGetPidQty(params, sb);
                  break;
               case TheServices.ODR_UPDATE_STATUS:
                  serv.callOdrUpdateSts(params, sb);
                  break;
               case TheServices.ODR_VARIABLE_WEIGHT:
                  serv.callLutVarWeight(params, sb);
                  break;
               case TheServices.LUT_VARIABLE_WEIGHT:
                  serv.callLutVarWeight(params, sb);
                  break;
               case TheServices.LUT_CALL_HELP:
                  serv.callLutCallHelp(params, sb);
                  break;
               case TheServices.LUT_VALID_128:
                  serv.callLutValid128(params, sb);
                  break;
               case TheServices.ODR_DEBUG_INFO:
                  System.out.println(
                      "Debug info only, no communication with host taken.");
                  break;
               case TheServices.LUT_HOW_AM_I_DOING:
            	   //modified for Cedarville...original code below
                   serv.callLutHowAmIDoing(params, sb, null);
//            	   BurrisJavaApplication javaTca = new BurrisJavaApplication();
//            	   Socket s= null;
//            	   try {
//            		   s = new Socket(tcaHostName, javaTcaPortNum);
//            		   s.setSoTimeout(tcaTimeout);
//            		   javaTca.connect(s);
//            		   serv.callLutHowAmIDoing(params, sb, javaTca);
//            	   }
//            	   finally {
//            		   try {
//            			   javaTca.disconnect();
//            			   if (s != null) {
//            				   s.close();
//            			   }
//            		   } catch (Exception ex) {
//            			   System.err.println("Error in closing connection to java tca");
//            		   }
//            	   }
            	   break;
               case TheServices.LUT_GET_CONTAINER_TYPES:
                   serv.callLutGetCntTp(params, sb);
                   break;
               default:
                  System.err.println(ServerSide.getDate() +
                                     ": Error, no match for number " + procNum +
                                     "corresponding to procedure name: " +
                                     talkProcName);
            }

            myState = CLOSING;
            transactionSuccessful = true;
         }
         catch (Exception ex) {
            if ((numAttempts < 3) && ((ex.getMessage() != null && ex.getMessage().indexOf("has died") >= 0) ||
                (ex.getMessage() != null && ex.getMessage().indexOf("Failed to connect") >= 0)) &&
                ((System.currentTimeMillis()-originalStartTime) < reqTimeForRetry)) {
               /** @todo should be (tcaTimeout-reqTimeForRetry) */
               retryRequest = true;
               String message = ServerSide.getDate() + ": ";
               if (ex.getMessage().indexOf("has died") >= 0) {
                  message += "Dead component encountered for: ";
               }
               else {
                  message += "Connect failure encountered for: ";
               }
               message += talkProcName + " " + params.toString() +
                                  "\nretrying ";

               if (numAttempts == 1) {
                  message += "a first time...";
               }
               else if (numAttempts == 2){
                  message += "a second time...";
               }
               System.err.println(message);
               try {
                  if (tcaConnected) {
                     disconnectTca();
                  }
               }
               catch (Exception exc){
                   System.err.println("Error encountered disconnecting tca before retry.");
               }
               finally {
                  tcaConnected = false;
               }
            }
            else {
               retryRequest = false;
               System.err.println(ServerSide.getDate() + ": Error in service " +
                                  talkProcName);
               System.err.println("Inputs: " + params.toString());

               ex.printStackTrace();
               String errorMsg = ex.getMessage();
               int errorNum;
               try {
                  if (errorPauseLength > 0) {
                     Thread.sleep(errorPauseLength);
                  }
               }
               catch (Exception exc) {
                  System.err.println("Error in pausing thread:");
                  exc.printStackTrace();
               }
               if (errorMsg.substring(0, 2).equals("##")) {
                  errorMsg = errorMsg.substring(2, errorMsg.length()).trim();
                  errorNum = TheServices.HOST_STOPPED_TRANSACTION_ERROR_NUM;
               }
               else {
                  errorMsg = TheServices.CRITICAL_ERROR_MSG;
                  errorNum = TheServices.CRITICAL_ERROR_NUM;
               }
               hadError = true;
               sb = new StringBuffer();
               serv.setErrorOutput(sb, procNum, errorNum, errorMsg);
            }
         }
      }

      if (talkProcName.substring(6,9).equalsIgnoreCase("lut")){
         try {
            serv.formatOutput(sb);
            if (serv.doubleTerminate) {
               sendToStream(talkmanSocket.getOutputStream(),
                            sb.toString() + DOUBLE_TERMINATE);
//                sendToStream(talkmanSocket, sb.toString() + DOUBLE_TERMINATE);
               sendToStream(System.out, sb.toString() + DOUBLE_TERMINATE);
            }
            else {
               sendToStream(talkmanSocket.getOutputStream(),
                            sb.toString() + SINGLE_TERMINATE);
               sendToStream(System.out, sb.toString() + SINGLE_TERMINATE);
            }
         }
         catch (IOException e) {
            System.err.println(ServerSide.getDate() + ": Output failed on socket connection");
            e.printStackTrace();
         }
         finally {
            serv.doubleTerminate = true;
         }
      }
      else if (!hadError) {
         try{
            sendToStream(talkmanSocket.getOutputStream(), "\n");
            System.out.println("\n");
         }
         catch (IOException e) {
            System.err.println(ServerSide.getDate() + ": Output failed on socket connection");
            e.printStackTrace();
         }
      }
      else {
         System.err.println(ServerSide.getDate() + ": Service failed, ODR Confirmation byte was not sent to Talkman.");
      }

      return hadError;
   }
   /**
    * This method is called when a complete command from the talkman is recieved.  It sets the
    * state to closing, prints the command to stdout, and calls the corresponding service.
    */
   private void completeCall() {
      myState = CLOSING;
      System.out.println(talkProcedureName + "(" + inputs.toString() +
                         ")");
      callService(talkProcedureName, inputs);
      inputs.clear();
      talkProcedureName = "";

   }

   /**
    * Sets the number of output fields so that in the event of an error the JSL
    * can build the correct ouput string
    * @param servNum int
    */
   private void setNumOutFields(int servNum) {
      switch (servNum) {
         case TheServices.LUT_CHECK_OPERATOR_STATUS :
            serv.setNumOut(TheServices.NUM_OUT_LUT_CHECK_OPERATOR_STATUS);
            break;
         case TheServices.LUT_CONFIGURATION:
            serv.setNumOut(TheServices.NUM_OUT_LUT_CONFIGURATION);
            break;
         case TheServices.LUT_CONTAINER_REVIEW:
            serv.setNumOut(TheServices.NUM_OUT_LUT_CONTAINER_REVIEW);
            break;
         case TheServices.LUT_GET_ASSIGNMENT:
            serv.setNumOut(TheServices.NUM_OUT_LUT_GET_ASSIGNMENT);
            break;
         case TheServices.LUT_GET_LOCATION_TO_INVESTIGATE:
            serv.setNumOut(TheServices.NUM_OUT_LUT_GET_LOCATION_TO_INVESTIGATE);
            break;
         case TheServices.LUT_GET_PICKS:
            serv.setNumOut(TheServices.NUM_OUT_LUT_GET_PICKS);
            break;
         case TheServices.LUT_INVESTIGATED:
            serv.setNumOut(TheServices.NUM_OUT_LUT_INVESTIGATED);
            break;
         case TheServices.LUT_NEW_CONTAINER:
            serv.setNumOut(TheServices.NUM_OUT_LUT_NEW_CONTAINER);
            break;
         case TheServices.LUT_PASS_ASSIGNMENT:
            serv.setNumOut(TheServices.NUM_OUT_LUT_PASS_ASSIGNMENT);
            break;
         case TheServices.LUT_PICKED:
            serv.setNumOut(TheServices.NUM_OUT_LUT_PICKED);
            break;
         case TheServices.LUT_PICKING_REGION:
            serv.setNumOut(TheServices.NUM_OUT_LUT_PICKING_REGION);
            break;
         case TheServices.LUT_PRINT:
            serv.setNumOut(TheServices.NUM_OUT_LUT_PRINT);
            break;
         case TheServices.LUT_REGION_PERMISSIONS_FOR_WORK_TYPE:
            serv.setNumOut(TheServices.NUM_OUT_LUT_REGION_PERMISSIONS_FOR_WORK_TYPE);
            break;
         case TheServices.LUT_REQUEST_WORK:
            serv.setNumOut(TheServices.NUM_OUT_LUT_REQUEST_WORK);
            break;
         case TheServices.LUT_REVIEW_CASE_PICKS:
            serv.setNumOut(TheServices.NUM_OUT_LUT_REVIEW_CASE_PICKS);
            break;
         case TheServices.LUT_SIGN_ON:
            serv.setNumOut(TheServices.NUM_OUT_LUT_SIGN_ON);
            break;
         case TheServices.LUT_SIGN_OFF:
            serv.setNumOut(TheServices.NUM_OUT_LUT_SIGN_OFF);
            break;
         case TheServices.LUT_STOP_ASSIGNMENT:
            serv.setNumOut(TheServices.NUM_OUT_LUT_STOP_ASSIGNMENT);
            break;
         case TheServices.LUT_UNDO_LAST_PICK:
            serv.setNumOut(TheServices.NUM_OUT_LUT_UNDO_LAST_PICK);
            break;
         case TheServices.LUT_UPDATE_STATUS:
            serv.setNumOut(TheServices.NUM_OUT_LUT_UPDATE_STATUS);
            break;
         case TheServices.LUT_GET_SHORT_REASONS:
            serv.setNumOut(TheServices.NUM_OUT_LUT_SHORT_REASONS);
            break;
         case TheServices.LUT_VALIDATE_PID:
            serv.setNumOut(TheServices.NUM_OUT_LUT_VALIDATE_PID);
            break;
         case TheServices.ODR_PICKED:
            break;
         case TheServices.LUT_GET_PID_QTY:
            serv.setNumOut(TheServices.NUM_OUT_LUT_GET_PID_QUANTITY);
            break;
         case TheServices.ODR_UPDATE_STATUS:
            break;
         case TheServices.ODR_VARIABLE_WEIGHT:
            break;
         case TheServices.LUT_VARIABLE_WEIGHT:
            serv.setNumOut(TheServices.NUM_OUT_LUT_VARIABLE_WEIGHT);
            break;
         case TheServices.LUT_CALL_HELP:
            serv.setNumOut(TheServices.NUM_OUT_LUT_CALL_HELP);
            break;
         case TheServices.LUT_VALID_128:
            serv.setNumOut(TheServices.NUM_OUT_LUT_VALID_128);
            break;
         case TheServices.ODR_DEBUG_INFO:
            System.out.println("Debug info only, no communication with host taken.");
            break;
         case TheServices.LUT_HOW_AM_I_DOING:
        	 serv.setNumOut(TheServices.NUM_OUT_LUT_HOW_AM_I_DOING);
        	 break;
         case TheServices.LUT_GET_CONTAINER_TYPES:
        	 serv.setNumOut(TheServices.NUM_OUT_LUT_GET_CON_TYPES);
        	 break;	 
         default:
            System.err.println(ServerSide.getDate() + ": Error, no match for number " + servNum);
      }

   }
   /**
    * This method keeps track of states one character at a time and takes the
    * necessary state specific actions.  The terminating sequence in sequence of inputs
    * is CR_LF_LF or CR_CR.
    * @param c char The input that the client sent.
    * @throws InvalidCharSequenceException if an invalid character sequence is encountered
    */
   /* */
   private void parseInput(char c) throws InvalidCharSequenceException {
      String s = String.valueOf(c);
      try {
         // State transitions:
         switch (myState) {
            //waiting for/getting procedure name
            case GETTING_PROC_NAME:
               if (s.equals(" ")) {
                  myState = GETTING_PROC_NAME;
               }
               else if (s.equals("(")) {
                  myState = GETTING_PARAM_LIST;
               }
               else if (s.equals(CR) || s.equals(LF)) {
                  myState = GETTING_PROC_NAME;
               }
               else {
                  talkProcedureName += s;
               }
               break;

            case GETTING_PARAM_LIST:
               if (s.equals(S_QUOTE)) {
                  myState = GETTING_A_PARAM;
               }
               else if (s.equals(COMMA) || s.equals(" ")) {
                  myState = GETTING_PARAM_LIST;
               }
               else if (s.equals(")")) {
                  myState = DONE_CALL;
               }
               else {
                  throw new InvalidCharSequenceException(
                      ServerSide.getDate() +  ": Error: illegal character " + s +
                      "received in procedure call " + talkProcedureName + "()");
               }
               break;

            case GETTING_A_PARAM:
               if (s.equals(S_QUOTE)) {
                  myState = GETTING_PARAM_LIST;
                  inputs.add(parameter); //trimming removed 9-15-04
                  parameter = "";
               }
               else {
                  parameter += s;
               }
               break;
            case DONE_CALL:
               if (s.equals(CR)) {
                  myState = CR_STATE;
               }
               else if (s.equals(LF)) {
                  myState = LF_STATE;
               }
               else {
                  throw new InvalidCharSequenceException(ServerSide.getDate() +  ": Error: Procedure " +
                      talkProcedureName + " not terminated properly");
               }
               break;
            case CR_STATE:
               if (s.equals(CR)) {
                  completeCall();
               }
               else if (s.equals(LF)) {
                  myState = CR_LF_STATE;
               }
               else {
                  throw new InvalidCharSequenceException(
                      ServerSide.getDate() +  ": Error in CR_STATE, recieved: " + s + "  ascii:  " +
                      ( (int) c));
               }
               break;
            case LF_STATE:
               if (s.equals(LF)) {
                  completeCall();
               }
               else {
                  throw new InvalidCharSequenceException(
                      ServerSide.getDate() +  ": Error in CR_LF_STATE, recieved: " + s + "  ascii:  " +
                      ( (int) c));
               }

               break;

            case CR_LF_STATE:
               if (s.equals(LF)) {
                  completeCall();
               }
               else if (s.equals(CR)) {
                  myState = CR_LF_CR_STATE;
               }
               else {
                  throw new InvalidCharSequenceException(
                      ServerSide.getDate() +  ": Error in CR_LF_STATE, recieved: " + s + "  ascii:  " +
                      ( (int) c));
               }
               break;
            case CR_LF_CR_STATE:
               if (s.equals(LF)) {
                  completeCall();
               }
               else {
                  throw new InvalidCharSequenceException(
                      ServerSide.getDate() +  ": Error in CR_LF_CR_STATE, recieved: " + s + "  ascii:  " +
                      ( (int) c));

               }
               break;
            default:
               myState = CLOSING;
               throw new InvalidCharSequenceException(ServerSide.getDate() +  ": Unexpected case.  Received: " + s +
                                                      "Case failed to match switch statement in parseInput");
         }
      }
      catch (InvalidCharSequenceException e) {
         serv.setErrorOutput(sb, talkProcedureName, TheServices.CRITICAL_ERROR_NUM, TheServices.CRITICAL_ERROR_MSG);
         System.err.println("When InvalidCharSequenceException encountered, proc name = " + talkProcedureName +
                            " and inputs = " + inputs.toString());
         System.err.println(e.getMessage());
         throw new InvalidCharSequenceException("Invalid sequence");
      }

   }

/*   private void sendToStream(Socket s, String output) {
      try {
         System.out.println("is connected: " + s.isConnected());

         sendToStream(s.getOutputStream(), output);
      }
      catch (Exception e) {
         System.err.println(ServerSide.getDate() +
             ": Couldn't Send Message because of bad OutputStream");
         System.err.println("Output was:");
         System.err.println(output);
         e.printStackTrace();
      }
   }*/
   /**
    * This method puts the String output into a byte array buffer and then writes
    * that array to the specified output stream.
    * @param out OutputStream
    * @param output String
    */
   private static void sendToStream(OutputStream out, String output) {
      // create byte buffer:
      int len = output.length();
      byte[] buff = new byte[len];
      for (int i = 0; i < len; i++) {
         char c = output.charAt(i);
         buff[i] = (byte) c;
      }
      try {
         out.write(buff);

      }
      catch (Exception e) {
         System.err.println(ServerSide.getDate() +  ": Couldn't Send Message because of bad OutputStream");
         System.err.println("Output was:");
         System.err.println(output);
         e.printStackTrace();
      }
   }
   /**
       * This connects the main tca class to the host and on the port specified by the fields tcaHostName
       * and tcaPortNum
       * @param host  The host for TCA to connect to.
       * @param port  The port on which TCA will connect.
       * @throws TCAException
       */
      public void connectTCA(String host, int port, int timeout) throws TCAException{
         try {
        	 port = 3306;
        	 //host = "joseph.cedarville.edu";
        	 //host = "localhost";
        	 host = "192.168.1.5";
        	 String database = "talkman";
////            tca.connect(host, port);
//            tcaSocket = new Socket(host, port);
//            tcaSocket.setSoTimeout(timeout);
//            tca.connect(tcaSocket);
//            tcaConnected = true;
        	 String userName = "warehouse";
             String password = "vwburr15";
             String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
             Class.forName ("com.mysql.jdbc.Driver").newInstance ();
             conn = DriverManager.getConnection (url, userName, password);
             System.out.println ("Database connection established");
             tcaConnected = true;

         }
//         catch (SocketTimeoutException e) {
////            System.err.println(ServerSide.getDate() +": Socket is closed");
//            tcaConnected = false;
//            throw new TCAException(e.getMessage()+  ": Socket to " + host + " on port " + port + " is closed.");
//         }
         catch (Exception ex) {
            tcaConnected = false;
//            System.err.println(ServerSide.getDate() + ": Original stacktrace: ");
            ex.printStackTrace();
            throw new TCAException(ex.getMessage()+  ": Failed to connect to " + host + " on port " + port);
         }
      }


}
