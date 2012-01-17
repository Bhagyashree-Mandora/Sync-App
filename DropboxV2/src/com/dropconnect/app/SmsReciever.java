package com.dropconnect.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED"))
		{
			Bundle bundle = intent.getExtras();
			Object messages[] = (Object[]) bundle.get("pdus");
			Log.i(getClass().getSimpleName(),"Recieved an SMS");
			SmsMessage smsMessage[] = new SmsMessage[messages.length];
			for (int n = 0; n < messages.length; n++) {
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
			}
			// sends notification
			Dropbox Api= new Dropbox(context);
			JSONObject Message= new JSONObject();
			JSONObject data = new JSONObject();
			JSONArray MessageArray = new JSONArray();
			for (int n = 0; n < messages.length; n++) {
				JSONObject Message1 = new JSONObject();
				try {
					
					Message1.put("from_number", smsMessage[n].getDisplayOriginatingAddress());
					Message1.put("Message_body", smsMessage[n].getDisplayMessageBody());
					//***************************Find Display name for the from_number.........
					String DisplayName =new String("");
					Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
					String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
					                ContactsContract.CommonDataKinds.Phone.NUMBER};

					Cursor people = context.getContentResolver().query(uri, projection, null, null, null);

					int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
					int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

					people.moveToFirst();
					do {
					    String name   = people.getString(indexName);
					    String number = people.getString(indexNumber);
					    if(number.equalsIgnoreCase(smsMessage[n].getDisplayOriginatingAddress())){
					    	DisplayName= new String(name);
					    	break;
					    }
					    // Do work...
					} while (people.moveToNext());
					Message1.put("Display_name", DisplayName);
					if(smsMessage[n].isEmail())
						Message1.put("type", "Email");
					else
						Message1.put("type", "sms/mms");
					//************************************Add the message to the message array
					MessageArray.put(Message1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e(getClass().getSimpleName(), e.toString());
				}
				
				try {
					data.putOpt("Message_array", MessageArray);
					data.put("Message_count", messages.length);
					//***************************Done with data.>>>>>>>>>>>>>>>>>>>>>>>>>>
					Message.put("sender", "mobile");
					Message.put("reciever", "desktop");
					Message.put("data", data);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e(getClass().getSimpleName(), e.toString());
				}
				Log.i(getClass().getSimpleName(), "Writing:"+Message.toString()+",to desktop read");
				Api.append("desktopread", Message);
				
				}
			
		}
	}

}
