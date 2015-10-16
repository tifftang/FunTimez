package com.funtimez;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import core.User;
import parse.ParseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.funtimez.R;
import com.funtimez.R.id;
import com.funtimez.R.layout;
import com.funtimez.R.string;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.LogInCallback;

/**
 * Login Activity
 * Requires the user to login or register an account on parse. 
 * An email is required. 
 * @author Tiffany, Anne
 *
 */
public class LoginActivity extends Activity  {
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

	    Button btnRegister = (Button)findViewById(R.id.register);
	    btnRegister.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	       	    intent = new Intent(LoginActivity.this, com.funtimez.SignUpActivity.class);
	    	    startActivity(intent);
	        }
	    });
		
	    Button btnSignIn = (Button)findViewById(R.id.email_sign_in_button);
	    btnSignIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//need to verify login info is correct
				checkLoginInfo();
			}
		});
	}

	public void checkLoginInfo(){
		final String username = ((EditText)findViewById(R.id.username)).getText().toString();
		String pw = ((EditText)findViewById(R.id.password)).getText().toString();

		ParseUser.logInInBackground(username, pw, new LogInCallback(){
			public void done(ParseUser user, ParseException e){
				if (user != null){
					//if correct, update user then proceed to ChatroomListActivity
					updateUser(username);
					intent = new Intent(LoginActivity.this, com.funtimez.ChatroomListActivity.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
					
					if(e.getCode() == 101){
						//Make a dialog pop up to notify the user that the info they input is incorrect.
						AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
						builder.setMessage(R.string.error_incorrect_login_info)
							   .setTitle(R.string.dialog_box_login_error)
						       .setCancelable(false)
						       .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						        	   dialog.dismiss();
						           }
						       });
						builder.create();
						builder.show();
					}
				}
			}
		});
	}
	
	private void updateUser(String username){
		User u = new User(username);

		//set the user of this app
		FunTimezApp app = ((FunTimezApp)getApplicationContext());		
		app.setUser(u);

		//update user's chatroom list
		ParseDatabase data = app.getParseData();
		data.setChatroomList(u);
	}
}
