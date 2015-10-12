package core;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String myUsername;
	private ArrayList<Chatroom> myChatrooms;
		
	public User(String username){
		myUsername = username;
	}
	
	public User(String username, ArrayList<Chatroom> chatrooms){
		
		myUsername = username;
		myChatrooms = chatrooms;
	}
		
	public void setChatroomList(List<Chatroom> chList){
		myChatrooms.clear();
		myChatrooms.addAll(chList);
	}

	public boolean addChatroom(Chatroom room){
		return myChatrooms.add(room);
	}
	
	//true = removed chatroom
	//false = if chatroom did not exist in the list to begin with
	public boolean removeChatroom(String roomName){
		if(myChatrooms.isEmpty())
			return false;
		for(int index = 0; index < myChatrooms.size(); index++){
			if(myChatrooms.get(index).getName().equals(roomName)){
				myChatrooms.remove(index);
				return true;
			}
		}
		return false;
	}
	
	public String getUsername(){
		return myUsername;
	}
	
	public ArrayList<Chatroom> getChatrooms(){
		return myChatrooms;
	}

}
