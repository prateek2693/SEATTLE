import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;


public class HostClient {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println(
                "Usage: java HostClient <host name> ");
            System.exit(1);
        }

        String hostName = args[0];

        try (
            Socket kkSocket = new Socket(hostName, 32000);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(kkSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
                
            String fromUser,fromServer;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server(Switch): " + fromServer);
                if (fromServer.equals("Bye"))
                    break;
                
                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Host Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}
