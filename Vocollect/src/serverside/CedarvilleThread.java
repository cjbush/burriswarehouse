package serverside;

import java.net.*;

public class CedarvilleThread extends Thread {
    private Socket socket = null;

    public CedarvilleThread(Socket socket) {
    	super("CedarvilleThread");
    	this.socket = socket;
    }
    
    public void run() {
    	
    }

}
