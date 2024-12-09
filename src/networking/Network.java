package networking;

import game.Game;
import game.Move;
import game.PossibleMove;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * The network implementation.
 *
 */
public abstract class Network {

	final static int soTimeout  =    10; // ms to wait for socket read
	final static int bufferSize =   128; // # chars in line 

	public static final int port = 40013;
	protected static InputStream socketInput;
	protected static OutputStream socketOutput;
	protected static Socket socket;
	
	public static boolean isActive;
	
	static String sendingText;
	static String recievedText;
	
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in),Network.bufferSize);

	public static void init(Socket socket) throws IOException{
		Network.socket = socket;
		socket.setSoTimeout(Network.soTimeout);
		socket.setTcpNoDelay(true);
		socketInput = socket.getInputStream();
		socketOutput = socket.getOutputStream();

	}

	public static void addText(String networkMessage){
		sendingText = networkMessage;
	}
	
	public static void processText(String networkMessage){
		if (networkMessage.equals("you-win; bye")){
			Network.close();
			return;
		}
		/**
		 * Uses regular expressions to convert the received text into a better format.
		 */
		String processedText = networkMessage
				.replaceAll(":",",")
				.replaceAll(";","")
				.replaceAll("\\(","")
				.replaceAll("\\)","")
				.replaceFirst("\\-", "|")
				.trim();
		String[] turn = processedText.split(",");
		int[][] turnInts = new int[turn.length][2];
		for (int i = 0; i < turn.length; i++){
			try {
				turnInts[i][0] = Integer.parseInt(turn[i].split("\\|")[0]);
				turnInts[i][1] = Integer.parseInt(turn[i].split("\\|")[1]);				
			} catch (NumberFormatException e){
				System.out.println("Badly formatted turn string :(");
			}
		}
		Move.setDice(Game.gameBoard,turnInts[0]);
		for (int i = 1; i < turnInts.length; i++){
			if (turnInts[i][0] == -1 && turnInts[i][1] == -1){
				Move.passTurn(Game.gameBoard);
				return;
			}
			PossibleMove move = Move.find(turnInts[i][0],turnInts[i][1]);
			Move.executeMove(Game.gameBoard,move, false);
		}
	}
	
	/**
	 * Writes to the network if there is a new message, and checks for received data.
	 * @throws IOException
	 */
	public static void run() throws IOException {
		if (isActive){
			recievedText = readLine();
			if (sendingText != null){
				writeLine(sendingText);
				sendingText = null;
			}
			if (recievedText != null){
				System.out.println("New move received: " + recievedText);
				processText(recievedText);
			}			
		}
	}

	public static void writeLine(String line) {
		if (line == null || line.length() < 0){
			return;
		}

		try {
			int l = line.length();
			if (l > Network.bufferSize) {
				l = Network.bufferSize;
			}
			socketOutput.write(line.getBytes(), 0, l);
		}
		catch (java.io.IOException e) {
			System.err.println("There was a problem writing to the network.");			
		}
	}


	public static String readLine() throws IOException {
		String line = null;
		try{
			byte buffer[] = new byte[Network.bufferSize];
			int l = socketInput.read(buffer);
			if (l > 0) {
				line = new String(buffer, 0, l);
			}
		} catch (SocketTimeoutException e) {
			//ignore :)
		}
		return line;

	}
	
	public static String getHostName(){
		InetAddress h;
		try {
			h = InetAddress.getLocalHost();
			String s = h.getCanonicalHostName();
			return ("" + InetAddress.getByName(s));
		} catch (UnknownHostException e) {
			return "???";
		} 
	}
	
	public static void close(){
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("IO error occured when closing connection.");
		}
	}
	
}
