package core;

import java.util.ArrayList;

import android.util.Log;

public class Chatroom {
	private String myID;
	private String myName;
	private ArrayList<String> myUserList;
	private String myHostName;
	
	private static final String TAG = "Chatroom";
	private static final int ID_LENGTH_MIN_LIMIT = 5; //if we know what it is
	private static final int ID_LENGTH_MAX_LIMIT = 20; //if we know what it is
	private static final int NAME_LENGTH_MIN_LIMIT = 5;
	private static final int NAME_LENGTH_MAX_LIMIT = 20;
	
	public void Chatroom(){
		myID = "";
		myName = "";
		myUserList = new ArrayList<String>();
		myHostName = "";
	}
	
	public void Chatroom(String id, String name, ArrayList<String> users, String hostName){
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
		else if(id.length() <= ID_LENGTH_MIN_LIMIT){
			Log.e(TAG, "Chatroom ID length needs to be greater than " + ID_LENGTH_MIN_LIMIT);
			return false;
		}
		else{
			myID = id;
			return true;
		}
	}
	
	//true = succeed; false = failed
	public boolean setName(String name){
		if(name.length() > NAME_LENGTH_MAX_LIMIT){
			Log.e(TAG, "Chatroom Name length needs to be less than " + NAME_LENGTH_MAX_LIMIT);			
			return false;
		}
		else if(name.length() <= NAME_LENGTH_MIN_LIMIT){
			Log.e(TAG, "Chatroom Name length needs to be greater than " + NAME_LENGTH_MIN_LIMIT);
			return false;
		}
		else{
			myName = name;
			return true;
		}
	}
	
	public void setUsers(ArrayList<String> users){
		myUserList.clear();
		myUserList.addAll(users);
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
	
	public boolean isHost(String name){
		if(name.equals(myHostName))
			return true;
		return false;
	}
}
