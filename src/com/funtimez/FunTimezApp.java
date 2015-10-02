package com.funtimez;

import com.parse.Parse;

import android.app.Application;

public class FunTimezApp extends Application {
	public void onCreate() {
		// Set up connection to Parse
		Parse.initialize(this, "wOkzFyuJ9aur1K2ZbvP8YGjtDAegt93Di4a6T4nV", "fz2O6WxTp3dE9yIZmJbaDsgswPXNIfmHdhlchxTy");
	}
}
