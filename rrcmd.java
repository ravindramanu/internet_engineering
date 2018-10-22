/*
 * TCP Server Program JAVA
 * Author: Ravindra, Manu Srivatsa
 * 
*/

import java.net.*; // packages for socket programming
import java.io.*;  // packages for stream reader objects
import java.util.concurrent.TimeUnit; // package for time

/* TCP Server Class - Remote Receive Command (rrcmd)
 * 
 * This TCP server will host connections to several TCP clients and will perform the commands
 * that the clients request of it.
 * 
 * The server accepts 1 arguments:
 * args[0] = portNumber to use for hosting 
 * 
 */
public class rrcmd {
	public static void main(String[] args) throws Exception {
	
		System.out.println("The server is now up and running.");
		
		//*************Process Command Line Arguments and create program variables**********//

		int clientNumber = 0;                              // Keep count of number of clients
		int receivePortNumber = Integer.parseInt(args[0]); // Port number to host all connections
		
		// Server socket declaration to only listen on the specified port.
		ServerSocket serverListeningSocket = new ServerSocket(receivePortNumber);
		
		//**********************************************************************************//
		
		//***********************TCP Server Logic*******************************************//
		try {
			while (true) { // indefinitely run the server.
				//Create a new thread for every client and put the server socket to listening.
				//This is done by passing parameters to the ChildServer constructor.
				new ChildServer (serverListeningSocket.accept(), clientNumber++).start();
			}
		}
		catch (IOException e1) {
			//Socket program must have try and catch block to handle exceptions.
			e1.printStackTrace();
		}
		finally{	
			// Close server listening socket
			serverListeningSocket.close();
			System.out.println("Server will now offline.");
		}
		//**********************************************************************************//
	} 
	
	/* Child Server class (ChildServer)
	 * This is a thread that is private to the parent server rrcmd. An instance of this
	 * thread is created for every new client that wants to connect to rrcmd and all
	 * information exchange therefore is private between a server thread and a particular
	 * end client.
	 */
	private static class ChildServer extends Thread {
		
		//*************Create Program Variables*********************************************//
		
		private Socket childServerSocket; // Private socket for each thread
		private int servingClientNumber;  // Private client number for each thread
		
		//**********************************************************************************//

		// Get parsed data from the constructor.
		public ChildServer (Socket psocket, int pClientNumber){
			childServerSocket = psocket;
			servingClientNumber = pClientNumber;
		}
		
		/*
		 * (Thread#run)
		 * @see java.lang.Thread#run()
		 */
		public void run () {
			try {
				
				//***************Creation of program variables***********************************//
				
				// Print stream object to continually send out data to the server via the client socket output stream
				PrintStream printStreamToClient = new PrintStream (childServerSocket.getOutputStream());
				
				// Input stream object and buffer for reading information from the client								
				BufferedReader buffserv = new BufferedReader(new InputStreamReader(childServerSocket.getInputStream()));
				
				// Compute and looping variables
				boolean loopExitVariable = false;
				int loopCounter = 1;
				
				//*******************************************************************************//
				
				//********************Send and Receive*******************************************//
				// Send client greeting
				printStreamToClient.println("Hello. You are Client number : " + servingClientNumber);
				// Receive client greeting
				System.out.println(buffserv.readLine());
				
				// Decode received command, execution times and delay
				String receivedMessage = buffserv.readLine();         // Read message from buffer
				String[] decodedMessage = receivedMessage.split(" "); // Split message using space as 
				int execTimes = Integer.parseInt(decodedMessage[0]);  // First argument is execution times
				int timeDelay = Integer.parseInt(decodedMessage[1]);  // Second argument is time delay in seconds
				String command = decodedMessage[2];                   // Final argument is command
				
				System.out.println("Client sent: " + receivedMessage);

				while (loopExitVariable == false) { // Use exitvar as a variable to trigger loop exit
				
						System.out.println("Current time : " + System.currentTimeMillis() + " Source IP : " + childServerSocket.getRemoteSocketAddress() + " Command entered : " + command + " Status of client " + servingClientNumber + " : Connected");
						
						//Execute the required command as many times as requested.
						if (command != null){
							while (execTimes != 0) {
									//To keep track of execution time declare variables start and duration.
									long start = 0, duration = 0;
									
									//Display execution message
									printStreamToClient.println("Executing : [" + command + " ] " + loopCounter + " time(s).");
									printStreamToClient.println(" ");
									
									// Start timer
									start = System.currentTimeMillis();
									
									//Using Java runtime, execute the requested command and output the STDOUT to a stream
									InputStream executionInputStream = Runtime.getRuntime().exec(command).getInputStream();
									
									// Calculate time taken to execute the command
									duration = System.currentTimeMillis() - start;
									
									//Using Stream reader print the output of the executed command to the client.
									InputStreamReader executionOutputStreamReader = new InputStreamReader(executionInputStream);
									BufferedReader executionOutputBufferReader = new BufferedReader (executionOutputStreamReader);
									
									// Send the output to the client
									String line; 
									while((line = executionOutputBufferReader.readLine()) != null)
									     printStreamToClient.println(line);
									
									printStreamToClient.println(" ");
									printStreamToClient.println("The execution time was " + duration + " ms.");
									
									//Displaying count of execution message on the server.
									System.out.println(" ");
									System.out.println("Returning output to client : " + loopCounter + " time(s).");
									System.out.println(" ");
									
									loopCounter++;
									execTimes--;
									
									//Introduced requested delay into the program.
									if (timeDelay != 0) {
										
										printStreamToClient.println(" ");
										printStreamToClient.println("Sleeping for " + timeDelay + " seconds.");
										printStreamToClient.println(" ");
										
										TimeUnit.SECONDS.sleep(timeDelay); // Wait for timeDelay seconds
									}
						}
							
							//Second display of status. connection closed.
							System.out.println("Current time : " + System.currentTimeMillis() + " Source IP : " + childServerSocket.getRemoteSocketAddress() + " Command entered : " + command + " Status of client " + servingClientNumber + " : Closed");
							loopExitVariable = true;
						}
						
						else {
							printStreamToClient.println("Enter the appropriate String please.");
						}
						
						// Closing connection and Terminating socket.
						printStreamToClient.println("Close Connection.");
						childServerSocket.close();
					}
			}
			catch (IOException | InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}