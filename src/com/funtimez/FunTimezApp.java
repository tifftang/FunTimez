package com.funtimez;

import com.parse.Parse;

import android.app.Application;
import core.User;
import parse.ParseDatabase;

public class FunTimezApp extends Application {
	private User user;
	private ParseDatabase data;
	
	public void onCreate() {
		data = new ParseDatabase();
		// Set up connection to Parse
		Parse.initialize(this, "wOkzFyuJ9aur1K2ZbvP8YGjtDAegt93Di4a6T4nV", "fz2O6WxTp3dE9yIZmJbaDsgswPXNIfmHdhlchxTy");
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}
	
	public ParseDatabase getParseData(){
		return data;
	}
}
