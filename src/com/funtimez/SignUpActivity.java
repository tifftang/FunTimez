package com.funtimez;

import java.util.ArrayList;

import com.funtimez.R;
import com.funtimez.R.id;
import com.funtimez.R.layout;
import com.funtimez.R.menu;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {
	private Button bRegister;
	private long timeClick = System.currentTimeMillis();
	private String username, password, email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		bRegister = (Button) findViewById(R.id.register);
		
		bRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Limit one click each second
				if (System.currentTimeMillis() - timeClick < 1000) return;
				else timeClick = System.currentTimeMillis();
				// User Name
				username = ((EditText)findViewById(R.id.username)).getText().toString();
				// Email Address
				email = ((EditText)findViewById(R.id.email)).getText().toString();
				// Password
				password = ((EditText)findViewById(R.id.password)).getText().toString();
				// Confirm Password
				//String confirmPassword = ((EditText) findViewById(R.id.ConfirmPassword_Register)).getText().toString();
				// Update
				registerUserOnParse();
			}
		});
	}
	
	public boolean checkValidInput(){
		return isEmailValid(this.email) && isPasswordValid(this.password) && isUserValid(this.username);
	}
	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}	
	private boolean isUserValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}	
	public void registerUserOnParse(){
		ArrayList<String> chatrooms = new ArrayList<String>();
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.put("chatrooms", chatrooms);
		user.put("numChatroomHosted", 0);
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException e) {
		    if (e == null) {
		      // Hooray! Let them use the app now.
		    	Toast.makeText(getApplicationContext(), "Registeration successful", Toast.LENGTH_SHORT).show();
		    } else {
		      // Sign up didn't succeed. Look at the ParseException
		      // to figure out what went wrong
		    	Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		    }
		  }
		});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
}
