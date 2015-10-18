package core;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class User {
	private String myUsername;
	private ArrayList<Chatroom> myChatrooms;
	private int numHostChatrooms;
	
	public static final int MAX_CHATROOMS_HOST = 5;
	
	public User(String username){
		myUsername = username;
		myChatrooms = new ArrayList<Chatroom>();
		numHostChatrooms = 0;
	}
	
	public User(String username, ArrayList<Chatroom> chatrooms, int numHostChatrooms){
		myUsername = username;
		myChatrooms = chatrooms;
		this.numHostChatrooms = numHostChatrooms;
	}
		
	public void setChatroomList(ArrayList<Chatroom> chList){
		myChatrooms = new ArrayList<Chatroom> (chList);
	}

	public boolean addChatroom(Chatroom room){
		return myChatrooms.add(room);
	}
	
	//true = removed chatroom
	//false = if chatroom did not exist in the list to begin with
	public boolean removeChatroom(String roomID){
		if(myChatrooms.isEmpty())
			return false;
		for(int index = 0; index < myChatrooms.size(); index++){
			if(myChatrooms.get(index).getID().equals(roomID)){
				myChatrooms.remove(index);
				return true;
			}
		}
		return false;
	}
	
	public boolean removeChatroom(Chatroom obj){
		return myChatrooms.remove(obj);
	}
	
	public void setNumHostChatrooms(int numHostChatrooms){
		this.numHostChatrooms = numHostChatrooms;
	}
	
	public void incrementNumHostChatrooms(){
		numHostChatrooms++;
	}
	
	public void decrementNumHostChatrooms(){
		numHostChatrooms--;
	}
	
	public String getUsername(){
		return myUsername;
	}
	
	public ArrayList<Chatroom> getChatrooms(){
		return myChatrooms;
	}
	
	public boolean setChatroomID(Chatroom obj, String id){
		for(int i = 0; i < myChatrooms.size(); i++){
			if(myChatrooms.get(i).equals(obj)){
				myChatrooms.get(i).setID(id);
				return true;
			}
		}
		return false;
	}
	
	public int getNumHostChatrooms(){
		return numHostChatrooms;
	}
	
	//true: user is hosting 
	public boolean isAtMaxChatroomsToHost(){
		if(numHostChatrooms >= MAX_CHATROOMS_HOST){
			return true;
		}
		return false;
	}

}
