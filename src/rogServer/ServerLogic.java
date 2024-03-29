package rogServer;

import java.util.ArrayList;

import rogShared.Message;
import rogShared.User;

/**
 * Library of functions to break apart the given objects and call the necessary queries to the DB. 
 * 
 * @author Sarah F.
 *
 */
public class ServerLogic 
{	
	/* Debugging main
	public static void main (String args[]) throws IOException 
	{
		User auth = authenticate("saf04231@gmail.com","safo4231@gmail.com");
		
		if(auth != null)
			System.out.println("User Logged In.");
		
		User sender = authenticate("txj@gmail.com", "txj@gmail.com");
		
		ArrayList<User> recip = new ArrayList<User>();
		recip.add(auth);
		
		Message msg = new Message("Testing", "", "");
		
		newMsg(sender, msg, recip);
		
		ArrayList<Message> msgs = new ArrayList<Message>();
		msgs = getMsgs(auth);
		
		System.out.println(msgs.get(1).getStringMsg());
		
	}
	*/
	
	
	/**
	 * Authentication process to have a user log into their client application. 
	 * <p>
	 * Returns the User object of the authenticated user. 
	 * 
	 * @param email != null
	 * @param pass != null
	 * @return User object of the authenticated user. Returns null if failed login. 
	 */
	static public User authenticate(String email, String pass) 
	{
		DBQueries.connectDB();
		
		if(email != null && pass != null)
		{
			User user = DBQueries.authentUser(email, pass);
			DBQueries.disconnectDB();
			return user;
		}
		else
		{
			DBQueries.disconnectDB();
			return null;
		}
			
	}
	
	/**
	 * Adds a User to the database.
	 * <p>
	 * Password of the new user is default set to the email of the account.
	 * 
	 * @param username size > 0. Username to assign the new user. 
	 * @param email size > 0. Email of the account. MUST BE UNIQUE. Any repeats will result in returning null. 
	 * @param groupName size > 0. The name of the group to add the User to. 
	 * If the group does not already exist, a new group will be made. 
	 * @returns User object of new user, null if failed.
	 */
	static public User newUser(String username, String email, String groupName) 
	{
		DBQueries.connectDB();
		
		if(username!=null && email!=null && groupName!=null)
		{
			if(DBQueries.getGroupID(groupName) == -1)
				DBQueries.addGroup(groupName);

			DBQueries.addUser(groupName, username, email);
			int groupID = DBQueries.getGroupID(groupName);
			User newUser = DBQueries.getUser(groupID, username);
			
			DBQueries.disconnectDB();
			return newUser;
		}
		else
		{
			DBQueries.disconnectDB();
			return null;
		}
			
		
	}
	
	/**
	 * Breaks down the Message into it's components and adds it to the Database.
	 * 
	 * @param sender != null.
	 * @param msg != null. 
	 * @param recipients !isEmpty. 
	 * @returns 1 if success, -1 if failed.
	 */
	static public int newMsg(User sender, Message msg, ArrayList<User> recipients) 
	{
		DBQueries.connectDB();
		
		if(sender!=null && !recipients.isEmpty() && msg!=null)
		{
			int i = 0;
			int r = -1;
			
			while(i < recipients.size())
			{
				String text = msg.getStringMsg();
				String image = msg.getImageLoc();
				String audio = msg.getAudioLoc();
				
				r = DBQueries.addMsg(recipients.get(i), text, image, audio);
				i++;
			}
			DBQueries.disconnectDB();
			return r;
		}
		else
		{
			DBQueries.disconnectDB();
			return -1;
		}
			
	}
	
	/**
	 * Queries the DB for all pending Messages for the given user.
	 * <p>
	 * Once the messages have been found, they are then deleted from the DB 
	 * before being returned. 
	 * 
	 * @param user !=null
	 * @returns Array of all the pending Messages for the user. Returns null if no Messages were found.
	 */
	static public ArrayList<Message> getMsgs(User user) 
	{
		DBQueries.connectDB();
		
		if(user!=null)
		{
			//retrieve msgs
			ArrayList<Message> pending = DBQueries.getMsg(user.getIDNo());
			
			// delete msgs
			DBQueries.delMsg(user.getIDNo());
			
			DBQueries.disconnectDB();
			return pending;
		}
		else
		{
			DBQueries.disconnectDB();
			return null;
		}
		
	}
	
	
	public static ArrayList<User> getUsers(User user)
	{
		if(user != null)
		{
			int groupID = DBQueries.getUserGroupID(user.getIDNo());
			
			ArrayList<User> groupMem = new ArrayList<User>();
			groupMem = DBQueries.getUsers(groupID);
			return groupMem;
		}
		else
			return null;
	}
	
}
