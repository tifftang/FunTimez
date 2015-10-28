package parse;

import java.util.ArrayList;
import java.util.List;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
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
		ArrayList<Object> chatroomIDs = getUserChatroomList();
		
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
	private ArrayList<Object> getUserChatroomList() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		return (ArrayList<Object>) currentUser.get("chatrooms");
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
		ParseUser currentUser = ParseUser.getCurrentUser();
		int hostCount = (Integer) currentUser.get("numChatroomHosted");
		u.setNumHostChatrooms(hostCount);
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
		ParseUser currentUser = ParseUser.getCurrentUser();
		currentUser.increment("numChatroomHosted");
		currentUser.add("chatrooms", cr.getID());
		currentUser.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if(e == null){
			    	Log.i(TAG, "Successfully updated User in Parse.");
				}
				else
					Log.e(TAG, e.toString());
			}
		});
	}
	
	public void deleteChatroom(Chatroom cr, User u){
		boolean isHost = cr.isHost(u.getUsername());
		//decrement number of chatrooms this user is hosting
		//and delete chatroom ID from User's chatroom list in Parse
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(isHost)
			currentUser.increment("numChatroomHosted", -1);
		//idList contains the id value that needs to be removed
		ArrayList<String> idList = new ArrayList<String>();
		idList.add(cr.getID());
		currentUser.removeAll("chatrooms", idList);
		currentUser.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if(e == null){
			    	Log.i(TAG, "Successfully updated User in Parse.");
				}
				else
					Log.e(TAG, e.toString());
			}
		});
		
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
	
	public boolean friendRequest(User user, String recipient){
		ParseObject request = new ParseObject("FriendRequest");
		request.put("friendRequestFrom", user.getUsername());
		request.put("friendRequestTo", recipient);
		request.put("status", "pending");
		request.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if(e == null){
			    	Log.i(TAG, "Successfully sent friend request.");
				}
				else
					Log.e(TAG, e.toString());
			}
		});
		return false;
	}
	
	public boolean chatroomInvite(User user, String recipient, String chatroomID){
		ParseObject request = new ParseObject("ChatroomInvite");
		request.put("from", user.getUsername());
		request.put("to", recipient);
		request.put("chatroomID", chatroomID);
		request.put("status", "pending");
		request.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if(e == null){
			    	Log.i(TAG, "Successfully sent friend request.");
				}
				else
					Log.e(TAG, e.toString());
			}
		});
		return false;
	}
	
	public void logout(){
		//TODO: If need to save user data before logout
		ParseUser.logOut();
	}
	
	public void refresh(User u){
		ParseUser currentUser = ParseUser.getCurrentUser();
		//TODO: incomplete method
	}
	
}	

