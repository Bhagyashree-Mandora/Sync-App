package com.dropconnect.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public abstract class Request {
Device Sender;
Device Reciever;
Command command;
JSONObject Data;
Context context;
	public Request(){
		
	}
	
public abstract void onResponse();
public void write(){
	Dropbox Api = new Dropbox(context);
	JSONObject Message= new JSONObject();
	
	
	try {
		//Add Data
		Message.put("data", Data);		
		Message.put("Type", "Request");
		Message.put("Command", command.commandName);
		Message.put("Sender", Sender.DeviceName);
		Message.put("Reciever", Reciever.DeviceName);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(),e.toString());
	}
	
	Api.append(Reciever.DeviceReadFile, Message);
}

public Request(Device sender, Device reciever, Command command, JSONObject data,Context context) {
	super();
	Sender = sender;
	Reciever = reciever;
	this.command = command;
	Data = data;
	this.context=context;
}
}
