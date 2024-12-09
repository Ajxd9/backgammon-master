package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server side of the network.
 *
 */
public class Server extends Network {
	public static final int port = 40013;
	private static boolean isPlaying;
	private static ServerSocket serverSocket;
	
	public static void main(String[] args) throws IOException, InterruptedException{
		start(Server.port);
	}

	public static void start(int portNumber) throws IOException{
		serverSocket = new ServerSocket(portNumber);
		
		InetAddress h = InetAddress.getLocalHost();
		String s = h.getCanonicalHostName();
		System.out.println("host: " + InetAddress.getByName(s));

		System.out.println("Waiting for players...");
		Socket clientSocket = serverSocket.accept();
		
		init(clientSocket);
		
		System.out.println("Found player!");
		
		if(!handshake()){
			System.out.println("Handshake failed :(");
			System.exit(-1);
		} else {
			System.out.println("Handshake succeeded! :)");
		}
		
		isActive = true;
	}
	
	public static boolean handshake() throws IOException{
		if (readLine().equals("hello")){
			writeLine("hello");
			if (readLine().equals("newgame")){
				if (!isPlaying){
					writeLine("ready");
					return true;
				} else {
					writeLine("reject");
				}
			}
		}
		return false;
	}

}
