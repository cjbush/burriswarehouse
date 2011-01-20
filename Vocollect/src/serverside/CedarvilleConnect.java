package serverside;

import java.io.*;
import java.net.*;

/** 
 * This is the server-side component of the Vocollect client/server communication socket
 * used by the Cedarville University Virtual Warehouse program.
 */

public class CedarvilleConnect extends Thread {
	
	private int listenPort;
	private Socket mySocket;
	private BufferedReader in;
	private static PrintWriter out;
	private static boolean hasConnection;
	
	public CedarvilleConnect(int port) {
		listenPort = port;
	}
	
	public void run() {
		try {
			ServerSocket server = new ServerSocket(listenPort);
			System.out.println("Listening on port: "+listenPort);
			while (true) {
                mySocket = server.accept();
                System.out.println("Accepted " + mySocket.getInetAddress());
                System.out.flush();
                serviceConnection(mySocket);
            }
		} catch (IOException e) {
			System.out.println("Unable to listen on port " + listenPort);
			e.printStackTrace();
		}
	}
	
	private void serviceConnection(Socket connection) throws IOException {
	    hasConnection = true;
		boolean done = false;
		
		in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    out = new PrintWriter(connection.getOutputStream(), true);
	    
        writeMessage("-1 Connection to server successful");
        
        //This message cheats the system slightly...the talkman doesn't send a configuration message
        //when I thought it did, so this makes the prompt that I want appear in the Virtual Warehouse app.
        //The manual setting of "Cedarville University" is also a poor implementation - it should probably
        //grab the info from the database.       -MSK
        writeMessage("0 Cedarville University");
        
	    while (!done) {
	    	if (in.ready()) {
	    		String line = in.readLine();	
	    		if (line.equals("DONE")) {
	    			done = true;
	    		}
	    	}
	    }
	    System.out.println("closing connection");
	    connection.close();
	    hasConnection = false;
	}
	
	public static void writeMessage(String message) {
		out.println(message);
	}
	
	public static boolean hasConnection() {
		return hasConnection;
	}

    public static void main(String[] args) throws IOException {
        //CedarvilleConnect myServer = new CedarvilleConnect(42211);
        //myServer.start();
        //new Thread(new CedarvilleConnect(42211)).start();
        
    }
}


	