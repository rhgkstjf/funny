package gradgg;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client2{
	
	protected JTextField textField;
	protected JTextArea textArea;
	protected JTextArea userArea;
	
	static DatagramPacket packet;
	static InetAddress address = null;
	DatagramSocket socket;
	private final int myport = 10021;
	private final int serverport = 5010;
	private Boolean first = false;
	private String title;
	
	private String name = null;
	private Set<String> usernameList = new HashSet<>();
	
	public Client2(String name) throws IOException{
		this.title = name;
		WindowFrame window = new WindowFrame();
		socket = new DatagramSocket(myport);
		address = InetAddress.getByName("127.0.0.1");
		this.name = name;
		send("port"+myport +"&&"+ name);
	}
	
	class WindowFrame extends JFrame implements ActionListener{

		public WindowFrame() {
			super();
			this.setTitle(title);
			textField = new JTextField(20);
			textField.addActionListener(this);
			
			textArea = new JTextArea(10,30);
			textArea.setEditable(false);
			
			userArea = new JTextArea(5,10);
			
			add(textField,BorderLayout.PAGE_END);
			add(textArea,BorderLayout.CENTER);
			add(userArea,BorderLayout.PAGE_START);
			pack();
			setVisible(true);
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String message = textField.getText();
			String names = "";
			for(String a : usernameList)
			{
				a.replaceAll(" ","");
				String tmp = a + " ";
				names = a + tmp;
			}
			System.out.println(names);
			send(name+":"+message+"::"+names);
		}
	}
	
	public void send(String message) {
		byte[] buf = message.getBytes();
		DatagramPacket packet;
		
		packet = new DatagramPacket(buf,buf.length,address,serverport);
		try {
			socket.send(packet);
		}catch(Exception e)
		{
			System.out.println("socket initializing error");
		}
	}
		
	public void receive(){
		while(true)
		{
			try {
				byte[] severmsg = new byte[256];
				DatagramPacket pack = new DatagramPacket(severmsg,severmsg.length);
				socket.receive(pack);
				
				if(new String(severmsg).contains("user Enter"))
				{
					usernameList.add(new String(severmsg).substring(24));
					userArea.setText("");
					for(String k :  usernameList)
						userArea.append("id : "+k+"\n");
					first = true;
				}
				else
				{
					String[] n = new String(severmsg).split("::");
					String msg = n[0];
					String p = n[1];
					String[] names = p.split(" ");
					for(String a : names)
					{
						a.replaceAll(" ","");
						if(!usernameList.contains(a))
						{
							usernameList.add(a);
						}
					}
					userArea.setText("");
					for(String i : usernameList)
					{
						userArea.append("id : "+i+"\n");
					}

					textArea.append(new String(msg) + "\n");
				}
				
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		Client2 k = new Client2("kim");
		k.receive();
	}
}
