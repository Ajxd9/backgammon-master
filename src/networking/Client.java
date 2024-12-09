package networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The client side of the network implementation.
 *
 */
public class Client extends Network {

	public static void start(String hostname, int portNumber) throws IOException{
		Socket call;
		try{
			call = new Socket(hostname, portNumber);
		} catch (UnknownHostException e){
			System.err.println("The host could not be reached.");
			return;
		}

		init(call);
		
		if(!handshake()){
			System.out.println("Handshake failed :(");
			System.exit(-1);
		} else {
			System.out.println("Handshake succeeded! :)");
		}
		
		isActive = true;
	}
	
	public static boolean handshake() throws IOException{
		writeLine("hello");
		if (readLine().equals("hello")){
			writeLine("newgame");
			if (readLine().equals("ready")){
				return true;
			}
		}
		return false;
	}
}
