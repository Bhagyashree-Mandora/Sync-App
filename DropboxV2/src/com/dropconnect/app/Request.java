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
int requestID;
	
	
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
	ListenService.addRequest(this);
	Api.append(Reciever.DeviceReadFile, Message);
}

public Request(Device sender, Device reciever, Command command, JSONObject data,Context context) {
	super();
	Sender = sender;
	Reciever = reciever;
	this.command = command;
	Data = data;
	this.context=context;
	
	//Generate Request ID
	requestID=(int) Math.random();
	
	//Add request Id to data....this will be returned in response
	try {
		Data.put("requestId", requestID);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(), e.toString());
	}
	
	
}
}
