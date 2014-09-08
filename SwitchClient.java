import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class SwitchClient extends Thread {

	private  String ip_to_connect=null;
    private  String mac_to_connect=null;

    private static Vector<String> ips_connected_to = new Vector<String>();
    private static Vector<String> macs_connected_to = new Vector<String>();
    private static Vector<String> hash_macs_connected_to = new Vector<String>();

    private Vector<String> ip_of_hosts=new Vector<String>();
    private Vector<String> mac_of_hosts=new Vector<String>();
    private Vector<String> hash_mac_of_hosts=new Vector<String>();

    /*private Vector<String> ip_connected_to_me;
    private Vector<String> ip_connected_to_me;
    private Vector<String> hash_hosts_connected_to_me;*/

    private static HashMap<String,String> ip_mac = new HashMap<String,String>();
    private static HashMap<String,String> ip_location = new HashMap<String,String>();


	SwitchClient(String ip,String mac,HashMap<String,String> ip_mac,HashMap<String,String> ip_location){
		ip_to_connect=ip;
        
        mac_to_connect=mac;
        macs_connected_to.add(mac_to_connect);
        try{
        hash_macs_connected_to.add(sha1(mac_to_connect));
        }catch(NoSuchAlgorithmException e){
            System.out.println(e);
        }

        this.ip_mac=ip_mac;
        this.ip_location=ip_location;
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
            Socket switchSocket = new Socket(ip_to_connect, 32000);
            PrintWriter out = new PrintWriter(switchSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(switchSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
  
            String fromUser,outputLine;

            ips_connected_to.add(ip_to_connect);


            //Senfing my own mac address to SwitchServerThread which will inturn use it for SwitchClient2
            out.println(getMACAddressPrintable(getMACAddress()));

            String mac_from_ss_thread;
            mac_from_ss_thread = in.readLine();

            macs_connected_to.add(mac_from_ss_thread);
            try{
            hash_macs_connected_to.add(sha1(mac_from_ss_thread));
            }catch(NoSuchAlgorithmException e){
                System.out.println(e);
            }

            ip_of_hosts.add("10.200.200.149");
            ip_of_hosts.add("10.200.200.150");
            ip_of_hosts.add("10.200.200.155");
            ip_of_hosts.add("10.200.200.163");
            ip_of_hosts.add("10.200.200.167");
            ip_of_hosts.add("10.200.200.172");
            ip_of_hosts.add("10.200.200.185");
            ip_of_hosts.add("10.200.200.180");
            ip_of_hosts.add("10.200.200.181");
            ip_of_hosts.add("10.200.200.182");
            ip_of_hosts.add("10.200.200.183");

            mac_of_hosts.add("34:d9:03:c2:72:57");
            mac_of_hosts.add("e4:ea:09:51:89:d0");
            mac_of_hosts.add("1a:01:07:4a:ec:be");
            mac_of_hosts.add("e6:e5:03:f2:b8:eb");
            mac_of_hosts.add("13:81:08:34:a7:07");
            mac_of_hosts.add("10:2b:08:a4:74:7e");
            mac_of_hosts.add("17:f1:00:3d:6c:d6");
            mac_of_hosts.add("fc:d9:09:56:e2:db");
            mac_of_hosts.add("73:16:05:16:99:c7");
            mac_of_hosts.add("fe:af:08:34:c0:4e");
            mac_of_hosts.add("56:df:02:83:c7:b3");

            try{
                hash_mac_of_hosts.add(sha1("34:d9:03:c2:72:57"));
                hash_mac_of_hosts.add(sha1("e4:ea:09:51:89:d0"));
                hash_mac_of_hosts.add(sha1("1a:01:07:4a:ec:be"));
                hash_mac_of_hosts.add(sha1("e6:e5:03:f2:b8:eb"));
                hash_mac_of_hosts.add(sha1("13:81:08:34:a7:07"));
                hash_mac_of_hosts.add(sha1("10:2b:08:a4:74:7e"));
                hash_mac_of_hosts.add(sha1("17:f1:00:3d:6c:d6"));
                hash_mac_of_hosts.add(sha1("fc:d9:09:56:e2:db"));
                hash_mac_of_hosts.add(sha1("73:16:05:16:99:c7"));
                hash_mac_of_hosts.add(sha1("fe:af:08:34:c0:4e"));
                hash_mac_of_hosts.add(sha1("56:df:02:83:c7:b3"));
            }catch(NoSuchAlgorithmException e){
                System.out.println(e);
            }
            System.out.println("Connected to the switch with IP:"+ip_to_connect);

            int pos1;

            int host_index=0;   //0 to 10 and not 1 to 11

            while ((fromUser = stdIn.readLine()) != null) {
               
                if (fromUser.equals("Bye")) {
                    out.println(fromUser);
                    break;
                }
                else if((pos1=fromUser.indexOf(" "))!=-1){
                    
                    String command_type=fromUser.substring(0,pos1);
                    String host_number=fromUser.substring(pos1+1,fromUser.length());

                    switch(host_number){

                        case "H1": host_index=0; break;
                        case "H2": host_index=1; break;
                        case "H3": host_index=2; break;
                        case "H4": host_index=3; break;
                        case "H5": host_index=4; break;
                        case "H6": host_index=5; break;
                        case "H7": host_index=6; break;
                        case "H8": host_index=7; break;
                        case "H9": host_index=8; break;
                        case "H10": host_index=9; break;
                        case "H11": host_index=10; break;
                    }

                    String changing_host_ip=ip_of_hosts.elementAt(host_index);
                    String changing_host_mac=mac_of_hosts.elementAt(host_index);
                    String changing_hash_mac=hash_mac_of_hosts.elementAt(host_index);

                    Vector<String> temp_compare = new Vector<String> (hash_macs_connected_to);
                    temp_compare.add(changing_hash_mac);

                    try{
                        temp_compare.add(sha1(getMACAddressPrintable(getMACAddress())));

                    }catch(NoSuchAlgorithmException e){
                        System.out.println(e);
                    }

                    //temp_compare.sort();
                    Collections.sort(temp_compare);

                    String hash_switch_to_send;

                    if(temp_compare.indexOf(changing_hash_mac)!=0){
                        hash_switch_to_send=temp_compare.elementAt(temp_compare.indexOf(changing_hash_mac)-1);
                    }

                    else{
                        hash_switch_to_send=temp_compare.elementAt(temp_compare.size()-1);
                    }

                    try{
                        if(hash_switch_to_send.equals(sha1(getMACAddressPrintable(getMACAddress())))){

                            if(command_type.equals("Join")){
                                ip_mac.put(changing_host_ip,changing_host_mac);
                                ip_location.put(changing_host_ip,getMACAddressPrintable(getMACAddress()));
                                display_hash_table();
                            }

                            else if(command_type.equals("Leave")){
                                ip_mac.remove(changing_host_ip);
                                ip_location.remove(changing_host_ip);
                                display_hash_table();
                            } 

                        }
                    }catch(NoSuchAlgorithmException e){
                        System.out.println(e);
                    }



                    String ip_switch=null;
                    
                    try{
                        if(!hash_switch_to_send.equals(sha1(getMACAddressPrintable(getMACAddress()))))
                            ip_switch=ips_connected_to.elementAt(hash_macs_connected_to.indexOf(hash_switch_to_send));

                        else ip_switch=getIPAddressPrintable(InetAddress.getLocalHost().getAddress());
                    }catch(NoSuchAlgorithmException e){
                        System.out.println(e);
                    }

                    if(ip_switch.equals(ip_to_connect)){
                        if(command_type.equals("Join")){
                           
                                outputLine="Join," + changing_host_ip + "<" + changing_host_mac + ">" + getMACAddressPrintable(getMACAddress());
                                out.println(outputLine);
                        }

                        else if(command_type.equals("Leave")){

                                outputLine="Leave," + changing_host_ip + "<" + changing_host_mac + ">" + getMACAddressPrintable(getMACAddress());
                                out.println(outputLine);
                        }
                    }

                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + ip_to_connect);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                ip_to_connect);
            System.exit(1);
        }



	}


}
