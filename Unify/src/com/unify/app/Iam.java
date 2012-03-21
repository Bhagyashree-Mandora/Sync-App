package com.unify.app;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Iam {
Device myself;
List<String> capability;
String timestamp;
Context context;


public Iam(Device myself, List<String> capability,Context context) {

	this.myself = myself;
	this.capability = capability;
	this.context=context;
}



public boolean registerToCloud(){
	JSONObject packet=new JSONObject();
	JSONArray capabilityJson=new JSONArray();
	try {
		packet.put("name", myself.DeviceName);
		packet.put("readfile", myself.DeviceReadFile);
		
		if(capability!=null)
		for(int i=0;i<capability.size();i++){
			JSONObject temp=new JSONObject();
			temp.put("appname", capability.get(i));
		capabilityJson.put(temp);	
		}		
		else{
			
		}
		
		//Now append this Device Json to Device List File
		Dropbox api=new Dropbox(context);
		api.append("devicelist", packet);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(), e.toString());
	}
	
	return true;
	
}
}
