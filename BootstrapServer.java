import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class BootstrapServer {
    public static void main(String[] args) throws IOException {

    
        boolean listening = true;
        
        try (ServerSocket serverSocket = new ServerSocket(30000)) { 
            while (listening) {
	            new BootstrapServerThread(serverSocket.accept()).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + 30000);
            System.exit(-1);
        }
    }
}
