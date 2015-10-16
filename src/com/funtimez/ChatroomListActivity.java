package com.funtimez;

import com.funtimez.R;
import com.funtimez.R.id;
import com.funtimez.R.layout;
import com.funtimez.R.menu;

import android.app.Activity;
import android.app.ListActivity;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

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
public class ChatroomListActivity extends ListActivity {

	SimpleCursorAdapter myAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom_list);
		
//		SQLiteCursor cursor = createTestCursor();
		
		String[] from = createChatroomList();
		int[] to = {android.R.id.text1};

		//getApplicationContext()
		//myAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);//, 0);
		myAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
		//getListView().setAdapter(myAdapter);
		setListAdapter(myAdapter);
	}
//============================================================
	
//---testing purposes------------------------
/*private SQLiteCursor createTestCursor()
{
	SQLiteCursor cursor = new SQLiteCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query);
	return ;
}*/
	
private String[] createChatroomList()
{
	//chatroomList = {chatroom name, chatroom id, list of users using chatroom}
	String [] chatroomList = {"Bunnies Rule", "00000001", "u", "does this work"};
	return chatroomList;
}
//-------------------------------------------
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getApplicationContext(), id + "clicked", Toast.LENGTH_SHORT).show();

	}
	
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
