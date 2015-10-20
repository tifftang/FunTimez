package parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.funtimez.ChatroomListActivity;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.parse.ParseException;
import android.util.Log;
import core.Chatroom;
import core.User;

public class ParseDatabase {
	
	public static final String TAG = "ParseDatabase";
	
	@SuppressWarnings("unchecked")
	public void setChatroomList(User u){
		ArrayList<Chatroom> userChatrooms = new ArrayList<Chatroom>();
		ArrayList<Object> chatroomIDs = getUserChatroomList(u);
		
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
			
			ArrayList<String> userListString = userObjectToString(userList);
		
			Chatroom cr = new Chatroom(id, chatroomName, userListString, host);
			userChatrooms.add(cr);
		}

		u.setChatroomList(userChatrooms);
		Log.i("hihi", userChatrooms.get(0).toString());
		
	}

	//returns an empty list if there are no chatrooms for that user
	private ArrayList<Object> getUserChatroomList(User u) {
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
		return chatroomIDs;
	}

	//returns an empty list if there are no User objects to convert to string
	private ArrayList<String> userObjectToString(ArrayList<Object> userList) {
		ArrayList<String> userListString = new ArrayList<String>();
		
		for (int i = 0; i < userList.size(); i++)
			if(userList.get(i) != null)
				userListString.add(userList.get(i).toString());
		
		return userListString;
	}
	
	public void setNumHostChatrooms(User u){
		ParseQuery<ParseUser> q = ParseUser.getQuery();
		q.whereEqualTo("username", u.getUsername());
		try {
			List<ParseUser> users = q.find();
			int hostCount = (Integer) users.get(0).get("numChatroomHosted");
			u.setNumHostChatrooms(hostCount);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//true: User u = host of chatroom with chatroomID
	//false: User u != host of chatroom with chatroomID
	public boolean isHost(String chatroomID, User u){
		boolean isChatroomHost = false;
		ParseQuery<ParseObject> q = ParseQuery.getQuery("Chatroom");
		q.whereEqualTo("objectId", chatroomID);
		try {
			List<ParseObject> chatrooms = q.find();
			String chatroomHost = chatrooms.get(0).getString("hostName");
			if(u.getUsername().equals(chatroomHost))
				isChatroomHost = true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isChatroomHost;
	}
	
	//store new chatroom information in Parse and update related variables in User class and Parse
	public void createNewChatroom(Chatroom cr, User u){
		ArrayList<String> userList = new ArrayList<String>();
		userList.add(u.getUsername());
		ParseObject chatroom = new ParseObject("Chatroom");
		chatroom.put("name", cr.getName());
		chatroom.put("userList", userList);
		chatroom.put("hostName", u.getUsername());
		chatroom.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if(e == null){
			    	Log.i(TAG, "Successfully created chatroom.");
				}
				else
					Log.e(TAG, e.toString());
			}
		});
		
		try {
			chatroom.fetch();
			//update chatroom ID in chatroom object in User object
			u.setChatroomID(cr, chatroom.getObjectId());
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//increment number of chatrooms this user is hosting
		//and add chatroom ID to User's chatroom list in Parse
		ParseQuery<ParseUser> q = ParseUser.getQuery();
		q.whereEqualTo("username", u.getUsername());
		try {
			List<ParseUser> users = q.find();
			users.get(0).increment("numChatroomHosted");
			users.get(0).add("chatrooms", cr.getID());
			users.get(0).saveInBackground(new SaveCallback() {
				public void done(ParseException e) {
					if(e == null){
				    	Log.i(TAG, "Successfully updated User in Parse.");
					}
					else
						Log.e(TAG, e.toString());
				}
			});
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void deleteChatroom(Chatroom cr, User u){
		boolean isHost = cr.isHost(u.getUsername());
		//decrement number of chatrooms this user is hosting
		//and delete chatroom ID from User's chatroom list in Parse
		ParseQuery<ParseUser> q = ParseUser.getQuery();
		q.whereEqualTo("username", u.getUsername());
		try {
			List<ParseUser> users = q.find();
			
			if(isHost)
				users.get(0).increment("numChatroomHosted", -1);
			//idList contains the id value that needs to be removed
			ArrayList<String> idList = new ArrayList<String>();
			idList.add(cr.getID());
			users.get(0).removeAll("chatrooms", idList);
			users.get(0).saveInBackground(new SaveCallback() {
				public void done(ParseException e) {
					if(e == null){
				    	Log.i(TAG, "Successfully updated User in Parse.");
					}
					else
						Log.e(TAG, e.toString());
				}
			});
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//delete chatroom if User u is host
		if(isHost){
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Chatroom");
			query.whereEqualTo("objectId", cr.getID());
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> chatrooms, ParseException e) {
					if(e == null){
				    	chatrooms.get(0).deleteInBackground(new DeleteCallback(){
							@Override
							public void done(ParseException e2) {
								if(e2 == null){
									Log.i(TAG, "Successfully deleted Chatroom in Parse.");
								}
								else{
									Log.e(TAG, e2.toString());
								}
							}
				    	});
					}
					else
						Log.e(TAG, e.toString());
				}
			});
		}
		//otherwise delete User u from userList of Chatroom
		else{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Chatroom");
			query.whereEqualTo("objectId", cr.getID());
			List<ParseObject> chatrooms;
			try {
				chatrooms = query.find();
				//users = array list of users to be removed
				ArrayList<String> users = new ArrayList<String>();
				users.add(u.getUsername());
				chatrooms.get(0).removeAll("userList", users);
				chatrooms.get(0).saveInBackground(new SaveCallback(){
					@Override
					public void done(ParseException e3) {
						if(e3 == null){
							Log.i(TAG, "Successfully deleted User from Chatroom's userList in Parse.");
						}
						else{
							Log.e(TAG, e3.toString());
						}
					}
				});
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void logout(){
		//TODO: If need to save user data before logout
		ParseUser.logOut();
	}
}	

