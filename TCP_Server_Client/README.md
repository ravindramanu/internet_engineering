# internet_engineering
Programs on TCP and UDP protocols

TCP Server and Client
Author: Ravindra, Manu Srivatsa

This folder has two programs written in the linux environment using  the Eclipse IDE in Java

rcmd.java - Remote command issuing client

rrcmd.java - Remote command accepting server. An rrcmd server can host mulitple clients over TCP IP. 

The programs rely on the fact that the server and the client have an agreed port number that they will operate on.
The client issues commands to execute on the server and the server returns the output of the commands back to the client. 

An example of running the setup is shown below:

1. Copy or clone the TCP_Server_Client folder on to your local machine
2. Both these files have been compiled using open-jdk-11 (JAVA SE 10)
3. You can compile them for correctness or run the pre-compiled .class files
4. Open a terminal and navigate to the copied folder. Start the server by using the following command:

   rrcmd <portnumber>
   e.g. rrcmd 50000

5. Open another terminal and navigate to the copied folder. Start the clinet by using the following command:

   rcmd <ipaddressOfServer> <portnumber> <numberOfTimesToExecuteCommand> <TimeDelayInSecondsRuns> <command>
   e.g. rcmd 127.0.0.1 50000 2 4 ifconfig

6. While clients close on thier own the server is set up to run continuosly and has to be closed manually (Ctrl + C).
