package com.dropconnect.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.telephony.gsm.SmsManager;
import android.util.Log;


/**
 * @author faizan
 *This is a class that is used to perform action based on the message that is recieved
 */

public class MessHandler {
String message;
JSONArray msgArray;



// Constructor
public MessHandler(String Message){
	message= new String(Message);
	
	try {		
		msgArray= new JSONArray(message);	
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(),e.toString());
	}
}


//******************************************************************************
//Performtask returns 1 when action performed
//******************************************************************************
public int performTask(){
	try {
		JSONObject obj = msgArray.getJSONObject(0);
		String command= new String(obj.getString("command"));
		if(command.equalsIgnoreCase("sendSms")){
			sendSMS(obj.getJSONObject("data"));
		}
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(),e.toString());
	}
	return 1;
}
//******************************************************************************
//**************************Handlers*******************************************

//************SMS Handler*******
private void sendSMS(JSONObject obj) {
	// TODO Auto-generated method stub
	
	try {
		String destination = new String(obj.getString("to_number"));
		String text = new String(obj.getString("message"));		
	SmsManager smsManager = SmsManager.getDefault();
	SmsManager sms= smsManager;	
	sms.sendTextMessage(destination,"", text,null,null );
	Log.i(getClass().getSimpleName(), "Message sent to "+destination);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.i("json",e.toString());
	}
}
//******************************************************************************
}//Class ends
