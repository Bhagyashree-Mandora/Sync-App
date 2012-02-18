package com.dropconnect.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class MarkRead extends MessageHandler {

	public MarkRead(JSONObject data, Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onRecieve() {
		// TODO Auto-generated method stub
		try {
			String fromNumber=data.getString("from_number");
			String message=data.getString("message");
			setRead(context, fromNumber, message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i(getClass().getSimpleName()+"onReciver", e.getMessage());
		}
		
	}
	public void setRead(Context context,String from_number, String smsContent) { 
		try { 
		ContentResolver cr = context.getContentResolver(); 
		Uri rowUri = Uri.parse("content://sms"); 
		Cursor c = context.getContentResolver().query(rowUri, null,null,null,null); 

		ContentValues values = new ContentValues(); 
		values.put("READ","1"); 
		cr.update(rowUri, values, " address='"+from_number+"'and body='"+smsContent+"'", null) ; 



		} catch (Exception e) { 
		Log.i("exception in MarkRead:", e.getMessage()); 
		} 
		} 

}
