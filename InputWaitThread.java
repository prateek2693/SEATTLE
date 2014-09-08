import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class InputWaitThread extends Thread{

	private Socket socket = null;

	public InputWaitThread(Socket socket) {
        super("InputWaitThread");
        this.socket = socket;
    }

    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                
                if (inputLine.equals("Bye"))
                    break;
            }


    	}catch (IOException e) {
            e.printStackTrace();
        }


    }
}
