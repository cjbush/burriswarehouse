package serverside;

import java.io.*;
import java.net.*;

/** 
 * This is the server-side component of the Vocollect client/server communication socket
 * used by the Cedarville University Virtual Warehouse program.
 * 
 * This is the threaded version of the server - it opens a new thread for every connection
 */

public class CedarvilleConnectThreaded {
	
	private int listenPort;
	private boolean listening;
	ServerSocket server;
	
	public CedarvilleConnectThreaded(int port) {
		listenPort = port;
	}
	
	public void start() {
		try {
			server = new ServerSocket(listenPort);
		} catch (IOException e) {
			System.out.println("Unable to listen on port " + listenPort);
			e.printStackTrace();
		}
		while (listening) {
			    try {
					new CedarvilleThread(server.accept()).start();
				} catch (IOException e) {
					System.out.println("Problem servicing connection thread");
					e.printStackTrace();
				}
		}

		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Server did not close cleanly");
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) throws IOException {
        CedarvilleConnect myServer = new CedarvilleConnect(42211);
        myServer.start();        
    }
}


	