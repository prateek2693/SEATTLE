import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class ConnectionManagerThread extends Thread {

	private static String bootstrap_ip=null;
	private static Vector<String> list_ip = new Vector<String>();
    private static Vector<String> mac_list =new Vector<String>();

    private static HashMap<String,String> ip_mac = new HashMap<String,String>();
    private static HashMap<String,String> ip_location = new HashMap<String,String>();

    static byte[] getMACAddress() throws UnknownHostException,SocketException{
        
        InetAddress ip;
        ip = InetAddress.getLocalHost();
        
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte[] mac = network.getHardwareAddress();
        
        return mac;
        
    }
    
    static String getMACAddressPrintable(byte[] mac){
        
       StringBuilder sb = new StringBuilder();

       for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));        
        } 
       
       return sb.toString();
        
    }

	ConnectionManagerThread(String hostName,HashMap<String,String> ip_mac,HashMap<String,String> ip_location){
		super("ConnectionManagerThread");
		bootstrap_ip=hostName;
        this.ip_mac=ip_mac;
        this.ip_location=ip_location;

		 try (
            Socket bsSocket = new Socket(bootstrap_ip, 30000);
            PrintWriter out = new PrintWriter(bsSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(bsSocket.getInputStream()));
        ) {
            String fromBootStrapServer;
            

            while ((fromBootStrapServer = in.readLine()) != null) {
                
                if (!fromBootStrapServer.equals("END")){

                    int pos=fromBootStrapServer.indexOf(";");
                    String ip_from_bs=fromBootStrapServer.substring(0,pos);
                    String mac_from_bs=fromBootStrapServer.substring(pos+1,fromBootStrapServer.length());

                    list_ip.add(ip_from_bs);
                    mac_list.add(mac_from_bs);

                   System.out.println("Received from bootstrap:"+fromBootStrapServer);
                }
                else{
                	out.println(getMACAddressPrintable(getMACAddress()));
                	break;
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


	public void run() {

		/*for(String x:list_ip){
            System.out.println(x);
			new SwitchClient(x).start();
		}*/

        for(int j=0;j<list_ip.size();j++){
            //System.out.println(list_ip.elementAt(j));
            //System.out.println(mac_list.elementAt(j));
            new SwitchClient(list_ip.elementAt(j),mac_list.elementAt(j),ip_mac,ip_location).start();

        }
	}

}
