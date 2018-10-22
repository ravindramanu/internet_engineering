/*
 * TCP Client Program JAVA
 * Author: Ravindra, Manu Srivatsa
 * 
*/

import java.net.*; // packages for socket programming
import java.io.*;  // packages for stream reader objects

/* TCP Client Class - Remote Command (rcmd)
 * This TCP Client will connect to a remote server over IPv4 via a TCP protocol.
 * After a connection is established the remote client can issue commands over the connection to 
 * be run at the server.
 * 
 * The client accepts 4 arguments:
 * args[0] = IP Address of the server 
 * args[1] = Port of the server
 * args[2] = Number of times the command need to be run or execution count
 * args[3] = Wait time in seconds between successive commands
 * args[4] = Command to be executed
 * 
 */
public abstract class rcmd {	
	public static void main(String[] args){
		
		System.out.println("The client is now up and running.");
		
		//*******************Process command line arguments**********************************//
		
		String serverIPAddress = args[0];                 // IP of the server
		int serverPortNumber = Integer.parseInt(args[1]); // Port Number
		int executionCount = Integer.parseInt(args[2]);   // Execution Count 
		int executionDelay = Integer.parseInt(args[3]);   // Execution Delay
		String command = args[4];                         // Command to be executed at Server
		
		//***********************************************************************************//
		
		System.out.println("Command to be executed: " + command + " for a total of " + executionCount + " with a delay of " + executionDelay);
		System.out.println("Connecting to Remote Server IP: " + args[0] + " Port: " + args[1]); 
		
		//******************************Client Logic*****************************************//
		
		/* Since we are creating sockets and stream objects which are objects of other   
		 * classes that implement exception handling, its imperative that we put their   
		 * declarations in try and catch blocks as well
		 */
		
		try{
			// Client Socket for connecting to server
			Socket clientSocket = new Socket(serverIPAddress, serverPortNumber);
			
			try {
				//***************Creation of program variables***********************************//
				
				// Print stream object to continually send out data to the server via the client socket output stream
				PrintStream printStreamToServer = new PrintStream(clientSocket.getOutputStream());
				
				// Input stream object and buffer for reading information from the Server
				BufferedReader inputStreamBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				//*******************************************************************************//

				System.out.println("Sending: Command " + command + " " + executionCount + " times, with a delay of " + executionDelay + " second(s)");
				
				//***********************Send and Receive****************************************//
				
				// Write to the PrintStream	
				printStreamToServer.println("Hello Server");
				printStreamToServer.println(executionCount + " " + executionDelay + " " + command);
				
				System.out.println("Output Trace from Server:");

				// Read data from server
				String line;
				while((line = inputStreamBuffer.readLine())!= null)
					{
						System.out.println(line);
					}
				
				//*******************************************************************************//
			}
			catch (IOException e){
				e.printStackTrace();
			}
			finally {
				// Close client socket
				clientSocket.close();
			}
		}
		catch (IOException ex){
			// Nested try catch block to catch exceptions with creating client socket 
			ex.printStackTrace();
		}	
	}
}
