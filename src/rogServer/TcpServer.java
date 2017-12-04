package rogServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class TcpServer {

	// Array of all connection sockets
    public static ArrayList<Socket> connThreads =new ArrayList<Socket>();
    // Array of online users
    public static ArrayList<String> onlineUsers =new ArrayList<String>();


    public static void main (String args[]) throws IOException{
    	
    	LoginListener ll = new LoginListener(1023);
    	Thread listenerThread = new Thread(ll);
    	listenerThread.start();
    	
    	
    }
    
    public static void AddUser(User user, int port)
    {
    	
    }
    
    //will be added to a thread once all connection is working properly
    public void loginListener()
    {
    	
    	//moved to its own class keeping here in comment for now
    	/*try {
			ServerSocket loginListener = new ServerSocket(listenPort);
			Socket socket = loginListener.accept();
			
	        BufferedReader input =
	            new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String username = input.readLine();
	        String password = input.readLine();
	        
	        //put stuff here to check if the user is valid. if so, send the user object for that user. (need serverlogic.
	        
	        if(username.equals("test")&&password.equals("pass")) //in future check if user exists and password is valid for user.
	        {
	        	PrintWriter responce = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
	        	responce.println("authenticated");
	        	
	        	User testUser = new User();
	        	testUser.setEmail("testemail@email.com");
	        	testUser.setIDNo(1);
	        	testUser.setName("test name");
	        	
	        	ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
	        	
	        	outToClient.writeObject(testUser); 
	        	
	        	
	        	
	        }else {
	        	
	        }
	        
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
    
    
        public static void AddUserName(Socket msgGrp) throws IOException     {
            Scanner input = new Scanner(msgGrp.getInputStream());
            String Username = input.nextLine();
            onlineUsers.add(Username);

        // Updates an informs when user leaves or joins the chat application.
        for (int i = 1; i <= TcpServer.connThreads.size(); i++) {
            Socket tmpsocket = TcpServer.connThreads.get(i -1);
            PrintWriter out = new PrintWriter(tmpsocket.getOutputStream());
            out.println("?#!" + onlineUsers);
            out.flush();
        }
    }
	
}
