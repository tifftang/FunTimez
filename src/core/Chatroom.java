package core;

import java.util.ArrayList;

import android.util.Log;

public class Chatroom {
	private String myID;
	private String myName;
	private ArrayList<String> myUserList;
	private String myHostName;
	
	private static final String TAG = "Chatroom";
	public static final int ID_LENGTH_MIN_LIMIT = 5; //if we know what it is
	public static final int ID_LENGTH_MAX_LIMIT = 20; //if we know what it is
	public static final int NAME_LENGTH_MIN_LIMIT = 0;
	public static final int NAME_LENGTH_MAX_LIMIT = 20;
	
	public Chatroom(){
		myID = "";
		myName = "";
		myUserList = new ArrayList<String>();
		myHostName = "";
	}
	
	public Chatroom(String id, String name, ArrayList<String> users, String hostName){
		myID = id;
		myName = name;
		myUserList = users;
		myHostName = hostName;
	}
	
	//true = succeed; false = failed
	public boolean setID(String id){
		if(id.length() > ID_LENGTH_MAX_LIMIT){
			Log.e(TAG, "Chatroom ID length needs to be less than " + ID_LENGTH_MAX_LIMIT);			
			return false;
		}
		else if(id.length() < ID_LENGTH_MIN_LIMIT){
			Log.e(TAG, "Chatroom ID length needs to be greater than " + ID_LENGTH_MIN_LIMIT);
			return false;
		}
		else{
			myID = id;
			return true;
		}
	}
	
	//0 = success
	//1 = failed due to name exceeding maximum character limit
	//2 = failed due to name not meeting minimum character limit
	public int setName(String name){
		if(name.length() > NAME_LENGTH_MAX_LIMIT){
			Log.e(TAG, "Chatroom Name length needs to be less than " + NAME_LENGTH_MAX_LIMIT);			
			return 1;
		}
		else if(name.length() < NAME_LENGTH_MIN_LIMIT){
			Log.e(TAG, "Chatroom Name length needs to be greater than " + NAME_LENGTH_MIN_LIMIT);
			return 2;
		}
		else{
			myName = name;
			return 0;
		}
	}
	
	public void setUsers(ArrayList<String> users){
		myUserList = users;
	}
	
	public void setHost(String name){
		myHostName = name;
	}
	
	public void addUsers(String user){
		myUserList.add(user);
	}
	
	//true = remove successful; false = remove failed
	public boolean removeUser(String user){
		return myUserList.remove(user);
	}
	
	public String getID(){
		return myID;
	}
	
	public String getName(){
		return myName;
	}
	
	public ArrayList<String> getUserList(){
		return myUserList;
	}
	
	public String getHost(){
		return myHostName;
	}
	
	public boolean isHost(String name){
		if(name.equals(myHostName))
			return true;
		return false;
	}
}
