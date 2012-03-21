package com.unify.app;

import java.util.Calendar;

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
import android.text.format.Time;
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
						
			JSONObject data = new JSONObject();
			

				try {
					
					data.put("from_number", smsMessage[0].getDisplayOriginatingAddress());
					data.put("message", smsMessage[0].getDisplayMessageBody());
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
					    if(number.equalsIgnoreCase(smsMessage[0].getDisplayOriginatingAddress())){
					    	DisplayName= new String(name);
					    	break;
					    }
					    // Do work...
					} while (people.moveToNext());
					data.put("display_name", DisplayName);
					if(smsMessage[0].isEmail())
						data.put("type", "Email");
					else
						data.put("type", "sms/mms");
					Time t= new Time();
					data.put("time", t.hour+":"+t.minute+":"+t.second);
					data.put("date", t.month+"/"+t.monthDay);
					data.put("message_id", smsMessage[0].getIndexOnIcc());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e(getClass().getSimpleName(), e.toString());
				}
				
					
					//***************************Done with data.>>>>>>>>>>>>>>>>>>>>>>>>>>
					
					//************************Create sender>>>>>>>>>>>>>
					Device sender= new Device("mobile");
					sender.DeviceReadFile=new String("mobileread");
					//************************Create sender>>>>>>>>>>>>>
					Device receiver= new Device("desktop");
					receiver.DeviceReadFile=new String("desktopread");
					//____________________________________________
					Command command=new Command("msgapp");
					
				Send send=new Send(sender, receiver, command, data, context);
				send.write();
				}
			
		}
	}


