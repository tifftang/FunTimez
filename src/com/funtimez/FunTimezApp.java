package com.funtimez;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.app.Application;
import core.User;
import parse.ParseDatabase;

public class FunTimezApp extends Application {
	private User user;
	private ParseDatabase data;
	private String ip;

	public void onCreate() {
		data = new ParseDatabase();
		// Set up connection to Parse
		Parse.initialize(this, "wOkzFyuJ9aur1K2ZbvP8YGjtDAegt93Di4a6T4nV", "fz2O6WxTp3dE9yIZmJbaDsgswPXNIfmHdhlchxTy");
		ParseInstallation.getCurrentInstallation().saveInBackground();
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

	public void setIP(String ip){
		this.ip = ip;
	}
	
	public String getIP(){
		return ip;
	}
}
