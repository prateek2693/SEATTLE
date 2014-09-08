import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class SwitchServerThread extends Thread {
    private Socket socket = null;
    private String ip_add;

    private static HashMap<String,String> ip_mac = new HashMap<String,String>();
    private static HashMap<String,String> ip_location = new HashMap<String,String>();

    private static Vector<String> ips_connected_to =new Vector<String>();
    private static Vector<String> macs_connected_to=new Vector<String>();
    private static Vector<String> hash_macs_connected_to=new Vector<String>();

    public SwitchServerThread(Socket socket,HashMap<String,String> ip_mac,HashMap<String,String> ip_location) {
        super("SwitchServerThread");
        this.socket = socket;
        this.ip_mac=ip_mac;
        this.ip_location=ip_location;
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

    static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
    }

    static  void display_hash_table(){

        Set<Map.Entry<String,String>> set =ip_mac.entrySet();

        System.out.println("\nIP-MAC Table:");
        for(Map.Entry<String,String> me :set){
            System.out.println(me.getKey()+"\t"+me.getValue());
        } 

        set =ip_location.entrySet();

        System.out.println("\nIP-Location Table:");
        for(Map.Entry<String,String> me :set){
            System.out.println(me.getKey()+"\t"+me.getValue());
        }

    }

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

    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {


            String inputLine;



            ip_add = getIPAddressPrintable(getIPAddress(socket.getInetAddress()));
            System.out.println(ip_add+" initiated a connection with me");
            ips_connected_to.add(ip_add);

            //Reading the mac address sent by the SwitchClient when it initiates a new socket
            inputLine=in.readLine();
            macs_connected_to.add(inputLine);

            try{
            hash_macs_connected_to.add(sha1(inputLine));
            }catch(NoSuchAlgorithmException e){
                System.out.println(e);
            }

            //Sending back my mac address now
            out.println(getMACAddressPrintable(getMACAddress()));

            SwitchClient2 sc2 = new SwitchClient2(socket,ips_connected_to,macs_connected_to,hash_macs_connected_to,ip_mac,ip_location);
            sc2.start();

            while ((inputLine = in.readLine()) != null) {

                if (inputLine.equals("Bye"))
                    break;

                else{

                    int pos1,pos2,pos3;
                    String command_type,changing_ip,changing_mac,sending_switch_mac;

                    pos1=inputLine.indexOf(",");
                    pos2=inputLine.indexOf("<");
                    pos3=inputLine.indexOf(">");

                    command_type=inputLine.substring(0,pos1);
                    changing_ip=inputLine.substring(pos1+1,pos2);
                    changing_mac=inputLine.substring(pos2+1,pos3);
                    sending_switch_mac=inputLine.substring(pos3+1,inputLine.length());

                    if(command_type.equals("Join")){
                        ip_mac.put(changing_ip,changing_mac);
                        ip_location.put(changing_ip,sending_switch_mac);
                        display_hash_table();
                    }
                    else if(command_type.equals("Leave")){
                        ip_mac.remove(changing_ip);
                        ip_location.remove(changing_ip);
                        display_hash_table();
                    }

                } 
            }


            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
