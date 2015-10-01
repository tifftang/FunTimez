package com.funtimez;


import android.app.Activity;
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
import java.util.ArrayList;
import java.util.List;

import com.funtimez.R;
import com.funtimez.R.id;
import com.funtimez.R.layout;
import com.funtimez.R.string;
import com.parse.Parse;

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

		// Set up connection to Parse
		//Parse.initialize(this, "wOkzFyuJ9aur1K2ZbvP8YGjtDAegt93Di4a6T4nV", "fz2O6WxTp3dE9yIZmJbaDsgswPXNIfmHdhlchxTy");
	    Button btn = (Button)findViewById(R.id.register);
	    btn.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	       	    intent = new Intent(LoginActivity.this, com.funtimez.SignUpActivity.class);
	    	    startActivity(intent);
	        }
	    });
		
	}


}
