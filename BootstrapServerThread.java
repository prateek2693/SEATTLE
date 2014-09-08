import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class BootstrapServerThread extends Thread {
    private Socket socket = null;
    private static Vector<String> ip_list = new Vector<String>();
    private static Vector<String> mac_list = new Vector<String>();
    private static InputWaitThread inp_wait_thread=null;

    public BootstrapServerThread(Socket socket) {
        super("BootstrapServerThread");
        this.socket = socket;
    }

    static byte[] getIPAddress(InetAddress ip_add_obj) throws UnknownHostException{

        byte ip_add_bytes[]=ip_add_obj.getAddress();
        
        return ip_add_bytes;
    }
    
    static String getIPAddressPrintable(byte[] ip_add_bytes){
        
        String str="";
        
        for(int i=0;i<4;i++){
            if(i!=3)
                    str=str+ip_add_bytes[i]+".";
            else
                    str=str+ip_add_bytes[i];
        } 
        
        return str;      
     }
    
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine, outputLine;

            
            System.out.println("Current List:");

            /*for(String x:ip_list){
                outputLine = x;
                out.println(outputLine);
                System.out.println(outputLine);
            }*/


            for(int i=0;i<ip_list.size();i++){
                outputLine=ip_list.elementAt(i)+";"+mac_list.elementAt(i);
                out.println(outputLine);
                System.out.println(outputLine);
            }

                out.println("END");           
                //System.out.println("Size:"+ip_list.size());

            String ip_incoming = getIPAddressPrintable(getIPAddress(socket.getInetAddress())); 

            //out.println(ip_incoming);
            ip_list.add(ip_incoming);
            //System.out.println("Size:"+ip_list.size());
            System.out.println("Incoming ip:"+ip_incoming);

            //inp_wait= new InputWaitThread(socket).start();


            /*while ((inputLine = in.readLine()) != null) {
                outputLine = "Bootstrap Server: "+inputLine;
                out.println(outputLine);
                if (outputLine.equals("Bye"))
                    break;
            }*/

            while((inputLine=in.readLine())!=null){

               
                if(inputLine.equals("Bye")){
                    ip_list.remove(ip_incoming);
                    break;
                }

                else{
                    mac_list.add(inputLine);
                }
                    
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
