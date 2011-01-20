package serverside;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.burris.extol.bps.BurrisFunctions;
import com.burris.util.DBConnection;

/**
 * <p>Title: Talkman Listener Server Side </p>
 * <p>Description: This is constantly running listening for socket connection requests</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Modifications: 1-4-05: commented out hard coded default functionality, will now use the cfg file or not start </p>
 * @author Josh Krupka
 * @version 1.0
 */

public class ServerSide {

   private static final String VERSION_NUM = "2.1.6";
   protected static final String CONFIG_FILE = "config.cfg";
   
   public static final String TCA_HOST_NAME_KEY = "tcaHostName";
   public static final String TCA_PORT_NUM_KEY = "tcaPortNum";
   public static final String TALKMAN_PORT_NUM_KEY = "talkmanPortNum";
   public static final String TCA_TIME_OUT_KEY = "tcaTimeout";
   public static final String ERROR_PAUSE_LENGTH_KEY = "errorPauseLength";
   public static final String REQ_TIME_FOR_RETRY_KEY = "reqTimeForRetry";
   public static final String JAVA_TCA_PORT_NUM_KEY = "javaTcaPortNum";
   public static final String HOWAMIDOING_DISABLED_PREFIX = "performance.disabled.";
   public static final String SERIAL_NUMBER_QUERY = "SELECT * FROM VOC001";

   private volatile boolean keepListening = true;
   private String tcaHostName;
   private int tcaPortNum;
   private int talkmanPortNum;
   private int tcaTimeout;
   private int errorPauseLength;
   private int reqTimeForRetry;
   private int javaTcaPortNum;
   private ServerSocket socket;
   private static ConcurrentHashMap<String, String> locations = new ConcurrentHashMap<String, String>();
   /**
    * Store the prop object from config in case there are any other general settings that need to be stored
    */
   private static Properties props = new Properties();
   
   //for Cedarville
   private CedarvilleConnect cc;
   
   //try pre-populating the serial # hash
   static {
	   System.out.println("Populating serial # cache...");
	   long start = System.currentTimeMillis();
	   DBConnection controller = new DBConnection();
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   try {
		   controller.load(ServerSide.CONFIG_FILE);
		   conn = controller.connect();
		   stmt = conn.createStatement();
		   rs = stmt.executeQuery(SERIAL_NUMBER_QUERY);
		   while (rs.next()) {
			   locations.put(rs.getString("TRM_SER").trim(), rs.getString("LOC").trim());
		   }
		   long end = System.currentTimeMillis();
		   System.out.println("Serial # cache populated in " + (end-start)/1000.0);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("Non fatal error in pre-populating the serial number cache:");
		e.printStackTrace();
	}
	finally {
		BurrisFunctions.closeObject(rs);
		BurrisFunctions.closeObject(stmt);
		BurrisFunctions.closeObject(conn);
	}
   }
   /**
    * The server socket listener is started, listening on port portNum.
    */
   public ServerSide() {
//      tcaHostName = DEFAULT_TCA_HOST_NAME;
//      tcaPortNum = DEFAULT_TCA_PORT_NUM;
//      talkmanPortNum = DEFAULT_TALKMAN_PORT_NUM;
//      start();
   }
   
   public static ConcurrentHashMap<String, String> getLocations() {
	   return locations;
   }
   
   public static Properties getProps() {
	   return props;
   }
   

   /**
    * This method starts up the server side object and creates a server socket.
    */
   protected void start() {
//      ServerSocket s = null;
	  
	   	
      try {
         if (new File(CONFIG_FILE).exists()) {
            loadConfigFile(CONFIG_FILE);
            System.out.println("Configuration file loaded successfully.");
         }
         else {
            System.err.println(getDate() + " " + CONFIG_FILE + " does not exist.  Server will not be started.");
            System.exit(0);

         }
      }
      catch (Exception e) {
         System.err.println(getDate() + " " + CONFIG_FILE + " failed to load properly.  Server will not be started.");
         e.printStackTrace();
         System.exit(0);
      }
      try {
         socket = new ServerSocket(talkmanPortNum);
         System.out.println("Version: " + VERSION_NUM);
         System.out.println(getDate() + ": Server started on port " + talkmanPortNum);
         System.out.println("Database using " + tcaHostName + " on port " + tcaPortNum);
         System.out.println("Error pause time is " + errorPauseLength);
         System.out.println("Database timeout is " + tcaTimeout);
         System.out.println("Required time for retry is " + reqTimeForRetry);
         System.out.println("javaTcaPortNum is " + javaTcaPortNum);
         
         System.out.println("Starting Virtual Warehouse server");
         
         //CedarvilleConnect cc = new CedarvilleConnect(40000);
         //CedarvilleConnect cc = new CedarvilleConnect(31416);
         //CedarvilleConnect cc = new CedarvilleConnect(42211);
         //cc.start();
        
         listeningLoop();
         // if return from the listeningLoop then exit
         System.out.println("Ending Server.");
      }
      catch (IOException e) {
         System.err.println(getDate() + "Error creating server socket, terminating server.");
         e.printStackTrace();
      }
      if (socket != null && !socket.isClosed()) {
	      try {
	         socket.close();
	      }
	      catch (Exception ex) {
	         System.err.println(getDate() + "Error disconnecting server socket.");
	      }
      }

   }

   public static String getDate() {
      return Calendar.getInstance().getTime().toString();
   }
   /**
    * This loop will run continuously, listening for incoming socket connection requests
    * or until the server encounters a fatal error.
    * @param listeningSocket ServerSocket
    */
   public void listeningLoop() {
//      boolean keepListening = true;

      while (keepListening) {

         try {
            Socket newConnection = socket.accept();
            System.out.println(Calendar.getInstance().getTime().toString() + ": Client connect.");
            serviceNewConnection(newConnection);
         }
         catch (Exception e) {
            System.err.println(getDate() + "Error encountered while waiting for connections:");
            e.printStackTrace();
            keepListening = false;
         }

      }
   }
   /**
    * @param s Socket
    */
   private void serviceNewConnection(Socket s) {
	   
      System.out.println("Creating new thread.");

      ServerThread t = new ServerThread(s, tcaHostName, tcaPortNum, tcaTimeout,
                                        errorPauseLength, reqTimeForRetry, javaTcaPortNum, cc);
      t.start();
   }

   /**
    * This method will redirect stdOut and stdErr to files of the specified names
    * and if the append is true it will write to the files in append mode, otherwise
    * the files will be overwritten each time.
    * If either file name is null, the respective stream (stdOut or stdErr) will
    * not be redirected.
    * @param stdOutFileName String
    * @param stdErrFileName String
    * @param append boolean
    * @return hadError boolean
    */
   protected boolean redirectOutput(String stdOutFileName, String stdErrFileName, boolean append) {
      boolean hadError = false;
      try {
         if (stdOutFileName != null) {
            File outFile = new File(stdOutFileName);
            outFile.createNewFile();
            System.setOut(new PrintStream(new FileOutputStream(outFile, append)));
         }
      }
      catch (FileNotFoundException e) {
         System.err.println("File " + stdOutFileName + " not found.");
         hadError = true;
      }
      catch (IOException e) {
         e.printStackTrace();
         hadError = true;
      }
      try {
         if (stdErrFileName != null) {
            File errFile = new File(stdErrFileName);
            errFile.createNewFile();
            System.setOut(new PrintStream(new FileOutputStream(errFile, append)));
         }
      }
      catch (FileNotFoundException e) {
         System.err.println("File " + stdErrFileName + " not found.");
         hadError = true;
      }
      catch (IOException e) {
         e.printStackTrace();
         hadError = true;
      }
      return hadError;

   }

   /**
    * Loads the configuration file
    * @param fileName
    * @throws FileNotFoundException
    * @throws IOException
    * @throws InvalidConfigFileException
    */
   protected void loadConfigFile(String fileName) 
   throws FileNotFoundException, IOException, InvalidConfigFileException {
	   Properties p = new Properties();
	   p.load(new FileInputStream(fileName));
	   if (!(p.containsKey(ERROR_PAUSE_LENGTH_KEY) && p.containsKey(REQ_TIME_FOR_RETRY_KEY)
			   && p.containsKey(TALKMAN_PORT_NUM_KEY) && p.containsKey(TCA_HOST_NAME_KEY)
			   && p.containsKey(TCA_PORT_NUM_KEY) && p.containsKey(TCA_TIME_OUT_KEY)
			   && p.containsKey(JAVA_TCA_PORT_NUM_KEY))) {
		   throw new InvalidConfigFileException("Missing required attributes.  Required fields: " +
				   ERROR_PAUSE_LENGTH_KEY + ", " + REQ_TIME_FOR_RETRY_KEY + ", " + 
				   TALKMAN_PORT_NUM_KEY + ", " + TCA_HOST_NAME_KEY + ", " +
				   TCA_PORT_NUM_KEY + ", " + TCA_TIME_OUT_KEY + ", " + JAVA_TCA_PORT_NUM_KEY + 
				   ". Properties specified were: " + p.keySet());
	   }
	   this.errorPauseLength = Integer.parseInt(p.getProperty(ERROR_PAUSE_LENGTH_KEY));
	   this.reqTimeForRetry = Integer.parseInt(p.getProperty(REQ_TIME_FOR_RETRY_KEY));
	   this.talkmanPortNum = Integer.parseInt(p.getProperty(TALKMAN_PORT_NUM_KEY));
	   this.tcaHostName = p.getProperty(TCA_HOST_NAME_KEY);
	   this.tcaPortNum = Integer.parseInt(p.getProperty(TCA_PORT_NUM_KEY));
	   this.tcaTimeout = Integer.parseInt(p.getProperty(TCA_TIME_OUT_KEY));
	   this.javaTcaPortNum = Integer.parseInt(p.getProperty(JAVA_TCA_PORT_NUM_KEY));
	   props = p;
   }
   /**
    * This method loads configuration properties from the specified file.  Properties
    * must be in the format of property = value  and comments are anything after a ##
    * If it fails for any reason, the default values for ALL properties will be used.
    * Any properties which are not specified in the file will use the default value.
    * @param fileName String
    * @throws FileNotFoundException
    * @throws IOException
    * @throws InvalidConfigFileException
    
   protected void loadConfigFile(String fileName) throws FileNotFoundException, IOException, InvalidConfigFileException{
      String line = "";
      String goodLine = "";
      String property = "";
      String value = "";
      StringTokenizer tok = null;
      boolean hasTcaNum = false;
      boolean hasTcaName = false;
      boolean hasTalkmanNum = false;
      boolean hasTcaTimeout = false;
      boolean hasErrorPause = false;
      boolean hasReqTimeForRetry = false;


      //tmp varibles are used to assure that either all or none of the values in the config file are used
//      int tmpTalkPortNum = DEFAULT_TALKMAN_PORT_NUM;
//      int tmpTcaPortNum = DEFAULT_TCA_PORT_NUM;
//      String tmpTcaHost = DEFAULT_TCA_HOST_NAME;

      try {
         BufferedReader reader = new BufferedReader(new FileReader(fileName));
         line = reader.readLine();
         while (line != null) {
            if (line.indexOf("##") != -1) {
               goodLine = line.substring(0, line.indexOf("##")).trim();
            }
            else {
               goodLine = line.trim();
            }
            if (goodLine.length() < 1) {
               line = reader.readLine();
               continue;
            }

            tok = new StringTokenizer(goodLine, "=");
            if (tok.countTokens() < 2) {
               throw new InvalidConfigFileException("Too few parameters on line: " + line);
            }
            else if (tok.countTokens() > 2) {
               throw new InvalidConfigFileException("Too many parameters on line: " + line);
            }
            property = tok.nextToken().trim();
            value = tok.nextToken().trim();

            if (property.length() < 1) {
               throw new InvalidConfigFileException("Invalid property on line: " + line);
            }
            if (value.length() < 1) {
               throw new InvalidConfigFileException("Invalid value on line: " + line);
            }
            /** @todo if more properties will be added, a different method of implementation
             * such as one with hashmap(s) or a switch statement should be used 
            if (property.equalsIgnoreCase("tcaHostName")) {
               tcaHostName = value;
               hasTcaName = true;
            }
            else if (property.equalsIgnoreCase("tcaPortNum")) {
               tcaPortNum = Integer.parseInt(value);
               hasTcaNum = true;
            }
            else if (property.equalsIgnoreCase("talkmanPortNum")) {
               talkmanPortNum = Integer.parseInt(value);
               hasTalkmanNum = true;
            }
            else if (property.equalsIgnoreCase("tcaTimeout")) {
               tcaTimeout = Integer.parseInt(value);
               hasTcaTimeout = true;
            }
            else if (property.equalsIgnoreCase("errorPauseLength")) {
               errorPauseLength = Integer.parseInt(value);
               hasErrorPause = true;
            }
            else if (property.equalsIgnoreCase("reqTimeForRetry")) {
               reqTimeForRetry = Integer.parseInt(value);
               hasReqTimeForRetry = true;
            }
            else {
               throw new InvalidConfigFileException("Unknown property: " + property);
            }
            line = reader.readLine();
         }

         if (!hasTalkmanNum) {
            throw new InvalidConfigFileException("Missing TalkmanPortNum property");
         }
         if (!hasTcaName) {
            throw new InvalidConfigFileException("Missing TcaHostName property");
         }
         if (!hasTcaNum) {
            throw new InvalidConfigFileException("Missing TcaPortNum property");
         }
         if (!hasTcaTimeout) {
            throw new InvalidConfigFileException("Missing TcaTimeout property");
         }
         if (!hasErrorPause) {
            throw new InvalidConfigFileException("Missing errorPauseLength property");
         }
         if (!hasReqTimeForRetry) {
            throw new InvalidConfigFileException("Missing reqTimeForRetry property");
         }



      }
      catch (FileNotFoundException e) {
         System.err.println("File " + CONFIG_FILE + " not found.");
         throw new FileNotFoundException(e.getMessage());
      }
      catch (IOException e) {
         System.err.println("Failed to read " + fileName + ".");
         throw new IOException(e.getMessage());
      }
      catch (InvalidConfigFileException ex) {
         System.err.println(ex.getMessage());
         throw new InvalidConfigFileException(ex.getMessage());
      }
//      finally {
//         talkmanPortNum = tmpTalkPortNum;
//         tcaHostName = tmpTcaHost;
//         tcaPortNum = tmpTcaPortNum;
//      }



   }*/
   /**
    * Adds the shutdown hook to make sure all the resources get cleaned up at shutdown
    */
   private void addShutDownHook() {
      try {
         ShutdownHook shutdownHook = new ShutdownHook();
         Runtime.getRuntime().addShutdownHook(shutdownHook);
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
      catch (Throwable t) {
         t.printStackTrace();
      }
   }
   public class ShutdownHook extends Thread {
	   public void run() {
		   System.out.println(new Date().toString() +
				   ": Shutting down...");
		   try {
			   keepListening = false;
			   if (socket != null && !socket.isClosed()) {
				   socket.close();
			   }
			   System.gc();
		   }
		   catch (Exception ex) {
			   ex.printStackTrace();
		   }
	   }
   }

   public static void main(String[] args) {
//	   PropertyConfigurator.configure("log4j.properties");
//	   System.out.println(System.getProperty("));
	   ServerSide serverSide1 = new ServerSide();
	   try {
		   serverSide1.addShutDownHook();
		   serverSide1.start();
	   }
	   catch (Exception ex){
		   ex.printStackTrace();
	   }
	   
   }
   

}
