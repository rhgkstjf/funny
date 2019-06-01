package gradgg;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Server {
	static DatagramSocket sendsocket;
	static InetAddress address = null;
	static byte[] buf;
	
	public static Map<String,String> userList = new HashMap<>();
	public static ArrayList<String> userList2 = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException
	{
		sendsocket = new DatagramSocket();
		address = InetAddress.getByName("127.0.0.1");
		
		try(DatagramSocket socket = new DatagramSocket(5010))
		{
			buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			
			while(true) {
				buf = new byte[256];
				packet = new DatagramPacket(buf,buf.length);
				
				socket.receive(packet);
				
				String message = new String(buf).trim();
				System.out.println(message);
				
				
				if(message.contains("new"))
				{
					String[] impo = message.substring(3).split("&&");
					userList.put(impo[0],impo[1]);
					
					userList2.add(impo[1].trim());
					send("user Enter - username : " + impo[1]);
					System.out.println("port : " + impo[0]);
				}

				else
				{
					System.out.println("userlist2: " + userList2);
					
					String tmp = String.join("&", userList2);
					String k = message+"(id)"+tmp;
					send(k);
					System.out.println(k);
				}
				
			}
		}
	}
	
	public static void send(String message)
	{
		System.out.println("send message: " + message);
		byte[] buffer = (message).getBytes();
		
		try {
			Set<String> user = userList.keySet();
			Iterator<String> iterator = user.iterator();
			while(iterator.hasNext())
			{
				sendsocket.send(new DatagramPacket(buffer,buffer.length,address,Integer.parseInt(iterator.next())));
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
	}
}
