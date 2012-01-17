package com.dropconnect.app;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED"))
		{

        	//Call service
        	Intent myIntent = new Intent(context,ListenService.class);        	
        	Log.i(getClass().toString(),"intent created");
        	context.startService(myIntent);        	
        	Toast.makeText(context, "Service Started", Toast.LENGTH_LONG).show();
		}
		
	}

}
