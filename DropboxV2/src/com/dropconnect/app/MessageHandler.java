package com.dropconnect.app;

import org.json.JSONObject;

public abstract class MessageHandler {
JSONObject data;
public abstract void onRecieve();
public MessageHandler(JSONObject data) {
	super();
	this.data = data;
}

}
