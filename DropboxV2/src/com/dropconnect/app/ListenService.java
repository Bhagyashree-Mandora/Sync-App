package com.dropconnect.app;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

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
			MessHandler messHandler= new MessHandler(str);
			messHandler.performTask();
		//delete read message
		dropbox.write("mobileread", "");
		}
	}




   
 
    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            stopSelf();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            stopSelf();
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }
    private void showToast(Context mContext,String msg) {
        Toast error = Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_LONG);
        error.show();
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

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void storeKeys(String key, String secret) {
        // Save the access key for later
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, MODE_WORLD_WRITEABLE);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, MODE_WORLD_WRITEABLE);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
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
}