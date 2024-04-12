import java.io.*;
import java.net.*;
import java.lang.Exception;

	
public class SThread extends Thread 
{
	private Object [][] RTable; // routing table
	OutputStream outStream = null; // for writing to ServerRouter
	DataOutputStream out = null;
	DataOutputStream outTo = null;
	InputStream inStream = null; // for reading form ServerRouter
	DataInputStream in = null;
	private String inputLine, outputLine, destination, addr; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int ind; // indext in the routing table

	// Constructor
	SThread(Object [][] Table, Socket toClient, int index) throws IOException
	{
			outStream = outSocket.getOutputStream();
			out = new DataOutputStream(outStream);
			inStream =  outSocket.getInputStream();
			in = new DataInputStream(inStream);
			RTable = Table;
			addr = toClient.getInetAddress().getHostAddress();
			RTable[index][0] = addr; // IP addresses 
			RTable[index][1] = toClient; // sockets for communication
			ind = index;
	}
	
	// Run method (will run for each machine that connects to the ServerRouter)
	public void run()
	{
		try
		{
		// Initial sends/receives
		destination = in.readUTF(); // initial read (the destination for writing)
		System.out.println("Forwarding to " + destination);
		out.writeUTF("Connected to the router."); // confirmation of connection
		out.flush();
		
		// waits 10 seconds to let the routing table fill with all machines' information
		try{
    		Thread.currentThread().sleep(10000); 
	   }
		catch(InterruptedException ie){
		System.out.println("Thread interrupted");
		}
		
		// loops through the routing table to find the destination
		FileWriter logger = new FileWriter("ThreadLog.txt");
		double t0, t1, t;
		t0 = System.currentTimeMillis();
		for ( int i=0; i<10; i++) 
				{
					if (destination.equals((String) RTable[i][0])){
						outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
						System.out.println("Found destination: " + destination);
						outTo = new DataOutputStream(outStream);
				}}
		t1 = System.currentTimeMillis();
		t = t1 - t0;
		logger.write("Routing Table Lookup Time (MS): "+(int) t);
		logger.flush();
		// Communication loop
		inputLine = in.readUTF();
		if(inputLine =="mp4" || inputLine == "mp3"){
			byte[] passer = new byte[1024];
			while ((passer = in.readNBytes(1024)) != null) {
				
				System.out.println("Client/Server said: ");
				for(int i = 0; i<passer.length; i++){
					System.out.print(passer[i]);
				}
				System.out.println("");
				if ( outSocket != null){				
					outTo.write(passer); // writes to the destination
				}			
		   }
		}
		else{
			while ((inputLine = in.readUTF()) != null) {
				System.out.println("Client/Server said: " + inputLine);
				if (inputLine.equals("Bye.")) // exit statement
						break;
				outputLine = inputLine; //passes the input from the machine to the output string for the destination
					
				if ( outSocket != null){				
					outTo.writeUTF(outputLine); // writes to the destination
				}			
		   }// end while
		}
	   logger.close();		 
		 }// end try
			catch (IOException e) {
               System.err.println("Could not listen to socket.");
               System.exit(1);
         }
	}
}