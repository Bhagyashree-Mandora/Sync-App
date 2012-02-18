package com.dropconnect.app;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

public abstract class MessageHandler{
JSONObject data;
Context context;
public abstract void onRecieve();
public MessageHandler(JSONObject data,Context context) {
	super();
	this.data = data;
	this.context=context;
}

}
