package com.dropconnect.app;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ListenService extends Service {

	

	///////////////////////////////////////////////////////////////////////////
    //                      Your app-specific settings.                      //
    ///////////////////////////////////////////////////////////////////////////

    // Replace this with your app key and secret assigned by Dropbox.
    // Note that this is a really insecure way to do this, and you shouldn't
    // ship code which contains your key & secret in such an obvious way.
    // Obfuscation is good.
    final static private String APP_KEY = "iypm3vb6d6vxha0";
    final static private String APP_SECRET = "e1kq1rhpwu6dfhx";

    // If you'd like to change the access type to the full Dropbox instead of
    // an app folder, change this value.
    final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;

    ///////////////////////////////////////////////////////////////////////////
    //                      End app-specific settings.                       //
    ///////////////////////////////////////////////////////////////////////////

    // You don't need to change these, leave them alone.
    final static private String ACCOUNT_PREFS_NAME = "prefs";
    final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    private static ArrayList<Request> requestMade=new ArrayList<Request>();
    private static ArrayList<Handlers> handlers=new ArrayList<Handlers>();
    private static DropboxAPI<AndroidAuthSession> mApi;
  

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
	//code to execute when the service is first created
		super.onCreate();
		Log.i("called onCreate","");		
		String keys[] =getKeys();
		AndroidAuthSession session = buildSession(keys);
        mApi = new DropboxAPI<AndroidAuthSession>(session);
		}
	@Override
	public void onDestroy() {
	//code to execute when the service is shutting down
		}
	
	@Override
	public void onStart(Intent intent, int startid) {
		//Listen if there was any changes in the file
		
	/*	Log.i("Onstart","start");
		if(mApi!=null)
		{
			Log.i("Onstart mapi value",mApi.toString());
			if(!mApi.getSession().isLinked())
			{
				Log.i("Onstart mapi auth","not linked");
				String[] keys=getKeys();
				if(keys!=null){
					Log.i("key values",keys[0]+" : "+keys[1]);
					AndroidAuthSession session = buildSession(keys);
			        mApi = new DropboxAPI<AndroidAuthSession>(session);
			        Log.i("Onstart ","reAuth1 with keys");
				}
				else
				{
					Log.i("Onstart ","reAuth with no keys");
					AndroidAuthSession session = buildSession(null);
			        mApi = new DropboxAPI<AndroidAuthSession>(session);
			        Log.i("Onstart ","reAuth with null keys");
				}
			}
			else
				Log.i("Onstart mapi auth","linked");
		}
		
		
		Timer t =new Timer("autoListen", true);
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ListenService.listen();
				Log.i("task","run");
				//Recurring
				Handler mHandler = new Handler();
				mHandler.postDelayed(this, 2000);
			}
		};
		//Before calling the handler we put the keys inside the static variable keys so that it can be used by listen
		String keys[]=getKeys();
		//repeating using handler
		Handler mHandler = new Handler();        
        mHandler.postDelayed(task, 1000);
      */

		}	
	
	@Override
public int onStartCommand(Intent intent ,int flags,int startid){
		//Listen if there was any changes in the file
		///Here we make the list of all Classes than are present in our app that can handle some sort of command
		ListenService.handlers.add(new Handlers("send", "SendSMS", "SmsHandler"));
		Log.i("start command","start");
		if(mApi!=null)
		{
			Log.i("start command mapi value",mApi.toString());
			if(!mApi.getSession().isLinked())
			{
				Log.i("start command mapi auth","not linked");
				String[] keys=getKeys();
				if(keys!=null){
					Log.i("key values",keys[0]+" : "+keys[1]);
					AndroidAuthSession session = buildSession(keys);
			        mApi = new DropboxAPI<AndroidAuthSession>(session);
			        Log.i("start command","reAuth1 with keys");
				}
				else
				{
					Log.i("start command ","reAuth with no keys");
					AndroidAuthSession session = buildSession(null);
			        mApi = new DropboxAPI<AndroidAuthSession>(session);
			        Log.i("start command","reAuth with null keys");
				}
			}
			else
				Log.i("start command mapi auth","linked");
		}
		
		
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				listen();
				//Log.i("task","run");
				//Recurring
				Handler mHandler = new Handler();
				mHandler.postDelayed(this, 2000);
			}
		};
		
		//repeating using handler
		Handler mHandler = new Handler();        
        mHandler.postDelayed(task, 1000);
      
	return 1;
	
}
private  void listen() {
		
		Dropbox dropbox= new Dropbox(this);
		String str= dropbox.read("mobileread");
		//Log.i("Listening", "About to show message");
		Log.i("Message found",str);	
		if(!str.equalsIgnoreCase(" ") & !str.equalsIgnoreCase(""))
		{
			//Perform action Here...or send data to handler
			try {
				JSONArray array= new JSONArray(str);
				int i=0;
				while(i<array.length()){
					JSONObject Message= array.getJSONObject(i);
					String type=new String(Message.getString("type"));
					String command=new String(Message.getString("command"));
					if(type.equalsIgnoreCase("send")||type.equalsIgnoreCase("request"))
					{
						//Search for a class that can handle this
						//Key for searching is command
						String ClassName=ListenService.search(command);
						MessageHandler h= null;
						Class.forName(ClassName).getMethod(ClassName, JSONObject.class).invoke((Object)h, Message.getJSONObject("data"));
					}
					if(type.equalsIgnoreCase("response")){
						Log.i(getClass().getSimpleName(),"Handling Response");
						int requestId=Message.getInt("requestId");
						Request r =search(requestId);
						r.Data=Message.getJSONObject("data");
						r.onResponse();
						requestMade.remove(r);
					}
				i++;}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(getClass().getSimpleName(), e.toString());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				Log.e(getClass().getSimpleName(), e.toString());
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				Log.e(getClass().getSimpleName(), e.toString());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				Log.e(getClass().getSimpleName(), e.toString());
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				Log.e(getClass().getSimpleName(), e.toString());
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				Log.e(getClass().getSimpleName(), e.toString());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e(getClass().getSimpleName(), e.toString());
			}
		//delete read message
		dropbox.write("mobileread", "");
		}

	}		
private Request search(int requestId) {
	// TODO Auto-generated method stub
	for(int i=0;i<requestMade.size();i++){
		Request r =requestMade.get(i);
		if (r.requestID==requestId)
		{
				return r;
		}
	}
	return null;
}
private static String search(String command) {
	// TODO Auto-generated method stub
	Handlers temp;
	for(int i=0;i<handlers.size();i++)
	{
		temp=handlers.get(i);
		if(temp.command.equalsIgnoreCase(command))
			return temp.className;
	}
	//else return default class than handles all type of data
	return "DefaultHandler";
}
public  static void addRequest(Request r){
	requestMade.add(r);
	Log.i("ListenSer--addRequest", "Request-"+r.requestID+" added");
}




   
 
    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     *
     * @return Array of [access_key, access_secret], or null if none stored
     */
    private  String[] getKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, MODE_WORLD_WRITEABLE);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        Log.i(getClass().getSimpleName(),"key "+key+" secret :"+secret);
        if (key != null && secret != null) {
        	String[] ret = new String[2];
        	ret[0] = key;
           	ret[1] = secret;
        	return ret;
        } else {
        	return null;
        }
    }

   
    //Builds Android Session
    private AndroidAuthSession buildSession(String[] keys) {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session;

        
        if (keys != null) {
            AccessTokenPair accessToken = new AccessTokenPair(keys[0], keys[1]);
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }

        return session;
    }
//Hidden Class(hidden from package)
//used to make a list of Handlers that are registered to this listener     
class Handlers{
public Handlers(String type, String command, String className) {
		super();
		this.type = type;
		this.command = command;
		this.className = className;
	}
String type;
String command;
String className;
}

}

