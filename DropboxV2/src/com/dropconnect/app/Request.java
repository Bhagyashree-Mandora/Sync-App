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
		Message.put("type", "request");
		Message.put("command", command.commandName);
		Message.put("sender", Sender.DeviceName);
		Message.put("receiver", Reciever.DeviceName);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(),e.toString());
	}
	ListenService.addRequest(this);
	Api.write(Reciever.DeviceReadFile, Message);
}

public Request(Device sender, Device reciever, Command command, JSONObject data,Context context) {
	
	Sender = sender;
	Reciever = reciever;
	this.command = command;
	Data = data;
	this.context=context;
	
	//Generate Request ID
	requestID=((int)( Math.random()*1000));
	
	//Add request Id to data....this will be returned in response
	try {
		Data.put("requestId", new Integer(requestID).toString());
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(), e.toString());
	}
	
	
}
}
