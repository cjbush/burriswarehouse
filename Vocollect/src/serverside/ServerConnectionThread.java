package serverside;

import java.io.*;
import java.net.*;

public class ServerConnectionThread extends Thread {
    private Socket mySocket;
    public ServerConnectionThread(Runnable serverObject, Socket serverSocket) {
        super(serverObject);
        mySocket = serverSocket;
    }

    public Socket getSocket() {
        return mySocket;
    }
}
