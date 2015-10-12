package com.funtimez;

import com.parse.Parse;

import android.app.Application;
import core.User;

public class FunTimezApp extends Application {
	private User user;
	private String ip;
	public void onCreate() {
		// Set up connection to Parse
		Parse.initialize(this, "wOkzFyuJ9aur1K2ZbvP8YGjtDAegt93Di4a6T4nV", "fz2O6WxTp3dE9yIZmJbaDsgswPXNIfmHdhlchxTy");
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}
	
	public void setIP(String ip){
		this.ip = ip;
	}
	
	public String getIP(){
		return ip;
	}
	
	
}
