package com.dropconnect.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.telephony.gsm.SmsManager;
import android.util.Log;

public class SmsHandler extends MessageHandler {

	public SmsHandler(JSONObject data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onRecieve() {
		// TODO Auto-generated method stub
		sendSMS(data);
	}

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
			Log.i(getClass().getSimpleName(),e.toString());
		}
	}
}
