package com.unify.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ViewDebug.FlagToString;
import android.widget.Toast;

public class SmsHandler extends MessageHandler {

	public SmsHandler(JSONObject data,Context context) {
		super(data,context);
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
	//	PendingIntent onDelivery= PendingIntent.getActivity(context, 0, new Intent(context,ondelivery.class ),0);
		
		sms.sendTextMessage(destination,null, text,null,null );
		Log.i(getClass().getSimpleName(), "Message sent to "+destination);
		//***************************Add the message into sent of default app
		 ContentValues values = new ContentValues();
		   values.put("address", destination);
		   values.put("body", text);
		   context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i(getClass().getSimpleName(),e.toString());
		}
	}
	
}
