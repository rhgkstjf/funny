package gradgg;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client1{
	
	protected JTextField textField;
	protected JTextArea textArea;
	protected JTextArea userArea;
	static DatagramPacket packet;
	static InetAddress address = null;
	DatagramSocket socket;
	private final int myport = 10020;
	private final int serverport = 5010;
	
	private String name = null;
	private ArrayList<String> userlist = new ArrayList<>();
	
	public Client1(String name) throws IOException{
		WindowFrame window = new WindowFrame();
		socket = new DatagramSocket(myport);
		address = InetAddress.getByName("127.0.0.1");
		this.name = name;
		send("new"+myport +"&&"+ name);
	}
	
	class WindowFrame extends JFrame implements ActionListener{

		public WindowFrame() {
			super();
			this.setTitle(name);
			textField = new JTextField(20);
			textField.addActionListener(this);
			
			textArea = new JTextArea(10,30);
			textArea.setEditable(false);
			
			userArea = new JTextArea(10,10);
			
			add(textField,BorderLayout.PAGE_END);
			add(textArea,BorderLayout.CENTER);
			add(userArea,BorderLayout.WEST);
			pack();
			setVisible(true);
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String message = textField.getText();
			send(name+":"+message);
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

				if(new String(severmsg).trim().contains("user Enter"))
				{
					textArea.append(new String(severmsg).trim() + "\n");
				}
				else
				{
					System.out.println(new String(severmsg));
					String[] names = new String(severmsg).trim().split("\\(id\\)");
					System.out.println(String.join(", ", names));
					textArea.append(names[0]+"\n");
					String name = names[1];
					String[] n = name.split("&");

					for(String a : n)
					{
						if(!userlist.contains(a))
						{
							userlist.add(a);
						}
					}

					userArea.setText(null);
					for(String a : userlist)
					{
						userArea.append(a+"\n");
					}
				}
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		Client1 k = new Client1("이한범");
		k.receive();
	}
}
