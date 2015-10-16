package parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.Context;
import com.parse.ParseException;
import android.util.Log;
import core.Chatroom;
import core.User;

public class ParseDatabase {
	
	public static final String TAG = "ParseDatabase";
	
	public void setChatroomList(User u){
		ArrayList<Chatroom> userChatrooms = new ArrayList<Chatroom>();
		ArrayList<Object> chatroomIDs = new ArrayList<Object>();
		
		ParseQuery<ParseUser> q = ParseUser.getQuery();
		q.whereEqualTo("username", u.getUsername());
		try {
			List<ParseUser> users = q.find();
			if(users != null){
				chatroomIDs = (ArrayList<Object>) users.get(0).get("chatrooms");
			}else
				Log.e(TAG, "No such user in Parse.");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int index = 0; index < chatroomIDs.size(); index++){
			String id = (chatroomIDs.get(index)).toString();
			String chatroomName = "";
			ArrayList<Object> userList = new ArrayList<Object>();
			String host = "";
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Chatroom");
			query.whereEqualTo("objectId", id);
			try {
				List<ParseObject> chatrooms = query.find();
				if(chatrooms != null){
					chatroomName = chatrooms.get(0).getString("name");
					host = chatrooms.get(0).getString("hostName");
					userList = (ArrayList<Object>) chatrooms.get(0).get("userList");
				}
				else
					Log.e(TAG, "No chatroom with that ID.");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArrayList<String> userListString = new ArrayList<String>();
			
			for (int i = 0; i < userList.size(); i++)
			{
				if(userList.get(i) != null)
				{
					userListString.add(userList.get(i).toString());
				}
			}
			Chatroom cr = new Chatroom(id, chatroomName, userListString, host);
			userChatrooms.add(cr);
		}

		u.setChatroomList(userChatrooms);
	}
	
public boolean isHost(String chatroomID){
		boolean isChatroomHost = false;
		ParseQuery<ParseObject> q = ParseQuery.getQuery("Chatroom");
		q.whereEqualTo("objectId", chatroomID);
		try {
			List<ParseObject> chatrooms = q.find();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isChatroomHost;
	}
}	