package com.funtimez;

import java.util.ArrayList;

import com.funtimez.R;
import com.funtimez.R.id;
import com.funtimez.R.layout;
import com.funtimez.R.menu;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import core.Chatroom;

import core.User;
import parse.ParseDatabase;

//===ArrayAdapter works, but cannot display the columns===============
/*public class ChatroomListActivity extends ListActivity {

	ArrayAdapter<String> myAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom_list);
		
		//getApplicationContext()
		myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, createChatroomList());
		ListView lView = (ListView) findViewById(android.R.id.list);
		lView.setAdapter(myAdapter);
	}*/
//=========================================================
	
//===To revert back to using SimpleCursorAdapter=================
public class ChatroomListActivity extends Activity {
	
	SimpleCursorAdapter myAdapter;

	public static final String TAG = "ChatroomListActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom_list);
		
//		SQLiteCursor cursor = createTestCursor();
		

/*		String[] from = createChatroomList();
		int[] to = {android.R.id.text1};

		//getApplicationContext()

		//myAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
		//getListView().setAdapter(myAdapter);*/

		//setListAdapter(myAdapter);
		FunTimezApp app = ((FunTimezApp)getApplicationContext());
		User user = app.getUser();
		ArrayList<Chatroom> chats = user.getChatrooms();

		Log.i("FDSFSF", String.valueOf(chats.size()));
		Button bSend = (Button) findViewById(R.id.button1);
		bSend.setText(chats.get(0).getName());
	    bSend.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	       	    Intent intent = new Intent(ChatroomListActivity.this, ChatroomActivity.class);
	    	    startActivity(intent);
	        }
	    });

	    addNewChatroomButtonListener();
	    
		Button testDelete = (Button) findViewById(R.id.button3);
		testDelete.setText(chats.get(1).getName());
		testDelete.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {

	        }
	    });
	    
	    addDeleteChatroomButtonListener(testDelete);
	    addShowChatroomIDButtonListener();
	}
//============================================================

	//when pressed:
	//-add new room to User class
	//-add new room to Parse
	private void addNewChatroomButtonListener() {
	    Button bNewChatroom = (Button) findViewById(R.id.addChatroom);
	    bNewChatroom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditText input = new EditText(ChatroomListActivity.this);
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				input.setMaxLines(1);
				
				//Make a dialog pop up asking for chatroom name
				AlertDialog.Builder builder = new AlertDialog.Builder(ChatroomListActivity.this);
				builder.setMessage(R.string.dialog_add_chatroom_msg)
					   .setTitle(R.string.dialog_add_chatroom_title)
					   .setView(input)
				       .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   String chatroomName = input.getText().toString();
				        	   User user = ((FunTimezApp)getApplicationContext()).getUser();
				        	   ParseDatabase data = ((FunTimezApp)getApplicationContext()).getParseData();
				        	   				   
				        	   //check to see if user is hosting max number of chatrooms already
				        	   if(user.isAtMaxChatroomsToHost()){
			        			   Toast.makeText(getApplicationContext(), "You have already reached maximum chatrooms you can to host at a given time.", Toast.LENGTH_LONG).show();
			        			   dialog.dismiss();
				        	   }
				        	   else{
					        	   //attempt to add chatroom to User object
				        		   Chatroom cr = new Chatroom();
				        		   
				        		   int setNameResult = cr.setName(chatroomName);
				        		   //results > 0 require user to re-enter chatroom name
				        		   if(setNameResult == 1){
				        			   Toast.makeText(getApplicationContext(), "Chatroom name needs to be under " + Chatroom.NAME_LENGTH_MAX_LIMIT + " characters long.", Toast.LENGTH_SHORT).show();
				        		   }
				        		   else if(setNameResult == 2){
				        			   Toast.makeText(getApplicationContext(), "Chatroom name needs to be at least " + Chatroom.NAME_LENGTH_MIN_LIMIT + " characters long.", Toast.LENGTH_SHORT).show();
				        		   }
				        		   //continue with adding chatroom in User class
				        		   else{
					        		   cr.addUsers(user.getUsername());
					        		   cr.setHost(user.getUsername());
					        		   user.addChatroom(cr);
					        		   user.incrementNumHostChatrooms();
						        	   
					        		   //add chatroom to Parse
						        	   data.createNewChatroom(cr, user);
printAllValues();					        	   
						        	   dialog.dismiss();
				        		   }
				        	   }
				           }
				       })
				       .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();							
							}
				       });
				builder.create();
				builder.show();
			}
	    });
	}
	
	private void addShowChatroomIDButtonListener() {
	    Button bChatroom = (Button) findViewById(R.id.button4);
	    bChatroom.setText("< ID");
	    bChatroom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Chatroom cr = ((FunTimezApp)getApplicationContext()).getUser().getChatrooms().get(1);

				//Make a dialog pop up asking to confirm
				AlertDialog.Builder builder = new AlertDialog.Builder(ChatroomListActivity.this);
				builder.setMessage("Chatroom ID: " + cr.getID())
					   .setTitle(R.string.dialog_chatroom_id_title)
					   .setCancelable(false)
				       .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   dialog.dismiss();
				           }
				       });
				builder.create();
				builder.show();
			}
	    });
	}
	
	//long press to delete chatroom
	private void addDeleteChatroomButtonListener(Button bChatroom) {
		bChatroom.setOnLongClickListener(new View.OnLongClickListener() {
	        @Override
	        public boolean onLongClick(View v) {
				//Make a dialog pop up asking for chatroom name
				AlertDialog.Builder builder = new AlertDialog.Builder(ChatroomListActivity.this);
				builder.setMessage(R.string.dialog_delete_chatroom_msg)
					   .setTitle(R.string.dialog_delete_chatroom_title)
				       .setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   User user = ((FunTimezApp)getApplicationContext()).getUser();
				        	   ParseDatabase data = ((FunTimezApp)getApplicationContext()).getParseData();

				        	   //TODO: need to be dynamic later
				        	   Chatroom cr = user.getChatrooms().get(1);
			        		   
			        		   //attempt to delete chatroom from Parse User's chatroom list and delete chatroom from parse if no one else is using
			        		   data.deleteChatroom(cr, user);
				        	   
			        		   //attempt to delete chatroom locally in User object
			        		   
			        		   //if user is host to the chatroom, decrement counter that keeps track of number of chatrooms the user is hosting for
			        		   boolean isHost = cr.isHost(user.getUsername());
				        	   if(isHost)
			        			   user.decrementNumHostChatrooms();
			        		   //delete chatroom from user's list of chatrooms
			        		   user.removeChatroom(cr.getID());

			        		   //TODO: next time make it find new host. for now delete chatroom if host deletes
			        		   //attempt to delete user from Chatroom object's userlist
			        		   cr.removeUser(user.getUsername());
			        		   if(isHost)
			        			   cr.setHost("");
			        		   
				        	   dialog.dismiss();
	printAllValues();
			        	   }

				       })
				       .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();							
							}
				       });
				builder.create();
				builder.show();
				return true;
	        }
	    });
	}
	
//Left off at debug! why buttons showing weird stuff	
	
	//TODO: ====code invite button next!===========
	
//---testing purposes------------------------

	private void printAllValues(){
		FunTimezApp app = ((FunTimezApp)getApplicationContext());
		User user = app.getUser();
		ArrayList<Chatroom> chatroomList = user.getChatrooms();
		
		//print User values
		Log.d("**PRINTING VARIABLE VALUES**", "User Class -- Username: " + user.getUsername());
		Log.d("**PRINTING VARIABLE VALUES**", "User Class -- Hosting # of Chatrooms: " + user.getNumHostChatrooms());
		String chatrooms = "";
		for(Chatroom cr : chatroomList){
			chatrooms += cr.getID() + ", ";
		}
		Log.d("**PRINTING VARIABLE VALUES**", "User Class -- Chatroom IDs: " + chatrooms);
		
		//print Chatroom values
		String chatroomValues = "";
		String userListString = "";
		for(int index = 0; index < chatroomList.size(); index++){
			Chatroom cr = chatroomList.get(index);
			for(String username : cr.getUserList()){
				userListString += username + ", ";
			}
			chatroomValues += "[" + index + "]: [ID: " + cr.getID() + "][Name: " + cr.getName() + "][Host: " + cr.getHost() + "][User List: " + userListString + "]";
			Log.d("**PRINTING VARIABLE VALUES**", "Chatroom Class -- " + chatroomValues);
			chatroomValues = "";
			userListString = "";
		}
	}
	
/*private SQLiteCursor createTestCursor()
{
	SQLiteCursor cursor = new SQLiteCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query);
	return ;
}*/

	/*
private String[] createChatroomList()
{
	//chatroomList = {chatroom name, chatroom id, list of users using chatroom}
	String [] chatroomList = {"Bunnies Rule", "00000001", "u", "does this work"};
	return chatroomList;
}*/
//-------------------------------------------
	
	/*protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getApplicationContext(), id + "clicked", Toast.LENGTH_SHORT).show();

	}*/
	

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chatroom_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	*/
}
