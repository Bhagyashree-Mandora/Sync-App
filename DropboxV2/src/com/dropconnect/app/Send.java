package com.dropconnect.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Send {
	Device Sender;
	Device Reciever;
	Command command;
	JSONObject Data;
	Context context;
	
	public Send(Device sender, Device reciever, Command command,
			JSONObject data, Context context) {
		super();
		Sender = sender;
		Reciever = reciever;
		this.command = command;
		Data = data;
		this.context = context;
	}

	//************************************************Write send Message/command to Dropbox
	public void write(){
		Dropbox Api = new Dropbox(context);
		JSONObject Message= new JSONObject();		
		try {
			//Add Data
			Message.put("data", Data);		
			Message.put("type", "send");
			Message.put("command", command.commandName);
			Message.put("sender", Sender.DeviceName);
			Message.put("receiver", Reciever.DeviceName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(getClass().getSimpleName(),e.toString());
		}
		
		Api.write(Reciever.DeviceReadFile, Message);
	}
}
