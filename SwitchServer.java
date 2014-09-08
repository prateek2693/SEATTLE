import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class SwitchServer {

    private static ConnectionManagerThread cmt = null;

    private static HashMap<String,String> ip_mac = new HashMap<String,String>();
    private static HashMap<String,String> ip_location = new HashMap<String,String>();

    public static void main(String[] args) throws IOException {

    if (args.length != 1) {
            System.err.println(
                "Usage: java SwitchServer <BootstrapIP>");
            System.exit(1);
        }

        String hostName = args[0];
                
        cmt=new ConnectionManagerThread(hostName,ip_mac,ip_location);
        cmt.start();

        try (ServerSocket serverSocket = new ServerSocket(32000)) { 
            while (true) {
	            new SwitchServerThread(serverSocket.accept(),ip_mac,ip_location).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + 32000);
            System.exit(-1);
        }
    }
}
