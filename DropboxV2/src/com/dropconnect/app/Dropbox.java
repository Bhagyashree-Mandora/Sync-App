package com.dropconnect.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;
import com.dropbox.client2.session.Session.AccessType;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

public class Dropbox {
	DropboxAPI<AndroidAuthSession> mApi;	
	String keys[];
	boolean AccountLinked;
	Context context;
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
    
public Dropbox(Context context){
	this.context=context;
	// We create a new AuthSession so that we can use the Dropbox API.
    AccountLinked=false;
    AndroidAuthSession session = buildSession();
    mApi = new DropboxAPI<AndroidAuthSession>(session);
    keys=getKeys();
	if(keys!=null)//Authorization needed
	{
		this.keys[0]=new String(keys[0]);
		this.keys[1]=new String(keys[1]);
		session = buildSession();
	    mApi = new DropboxAPI<AndroidAuthSession>(session);
	    AccountLinked=true;
	}
	else
	{
		Log.e(getClass().getSimpleName(),"Account Not Linked,complete authorization first");
	}
}
//*********************************************************************************************
/**
 *append(Filename, ContentObject)
 *  Adds to contents of file
 *  
 */
public int append(String Filename,JSONObject ContentObj)
{
	String Message= new String(this.read(Filename));
	if(!Message.equalsIgnoreCase("") & !Message.equalsIgnoreCase(" ") )
		this.write(Filename, ContentObj);
	else{
	try {
		JSONArray MsgArray= new JSONArray(Message);
		MsgArray.put(ContentObj);
		this.write(Filename, MsgArray);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(), e.toString());
	}
	
	}
	return 1;
}
//*****************************************
/**
 * append(Filename, ContentObjectArray)
 *  Adds to contents of file
 *  
 */
public int append(String Filename,JSONArray ContentArray)
{
	String Message= new String(this.read(Filename));
	if(!Message.equalsIgnoreCase("") & !Message.equalsIgnoreCase(" ") )
		this.write(Filename, ContentArray);
	else{
	try {
		JSONArray MsgArray= new JSONArray(Message);
		for (int i=0;i< ContentArray.length();i++)
			MsgArray.put(ContentArray.get(i));
		this.write(Filename, MsgArray);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(), e.toString());
	}
	
	}
	return 1;
}

//*****************************************************************************************
/**
 *read(Filename)
 *  Reads contents of file
 *  
 */
public String read(String Filename)
{
	// Downloading content.
	if(this.AccountLinked=false)
	{
		Log.e(getClass().getSimpleName(),"Cannot read Account not linked");
		return "";
	}
	String message=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) (8*Math.pow(2, 10)));
			mApi.getFile("/"+Filename, null, outputStream, null);
			message= new String(outputStream.toByteArray());
			
	} catch (DropboxUnlinkedException e) {
	   // User has unlinked, ask them to link again here.
	   Log.e(getClass().getSimpleName(), "User has unlinked.");
	   return "";
	} catch (DropboxException e) {
	   Log.e(getClass().getSimpleName(), "Problem uploading:"+e.toString());
	   return "";
	}
	return message;
}

//*********************************************************************************************
/**
 *Write(String Filename,String Content)
 *  Overwrites contents of file
 */
public int write(String Filename,String Content){
	// Uploading content.
	if(this.AccountLinked=false)
	{
		Log.e(getClass().getSimpleName(),"Cannot write to file"+Filename+" :Account not linked");
		return 0;
	}
	ByteArrayInputStream inputStream = new ByteArrayInputStream(Content.getBytes());
	try {
	   Entry newEntry = mApi.putFileOverwrite("/"+Filename, inputStream,
	           Content.length(), null);
	   
	} catch (DropboxUnlinkedException e) {
	   // User has unlinked, ask them to link again here.
	   Log.e(getClass().getSimpleName(), "User has unlinked.");
	   return 0;
	} catch (DropboxException e) {
	   Log.e(getClass().getSimpleName(), "Problem uploading:"+e.toString());
	   return 0;
	}
	return 1;
}
//*****************************************
/**
 *Write(Filename, ContentObject)
 *  Overwrites contents of file
 *  
 */
public int write(String Filename,JSONObject ContentObj){
	// Uploading content.
	if(AccountLinked=false)
	{
		Log.e(getClass().getSimpleName(),"Cannot write to file"+Filename+" :Account not linked");
		return 0;
	}
		try {
			String Content = new JSONArray("["+ContentObj.toString()+"]").toString();
	
	ByteArrayInputStream inputStream = new ByteArrayInputStream(Content.getBytes());

	   Entry newEntry = mApi.putFileOverwrite("/"+Filename, inputStream,
	           Content.length(), null);
	   
	} catch (DropboxUnlinkedException e) {
	   // User has unlinked, ask them to link again here.
	   Log.e(getClass().getSimpleName(), "User has unlinked.");
	   return 0;
	} catch (DropboxException e) {
	   Log.e(getClass().getSimpleName(), "Problem uploading:"+e.toString());
	   return 0;
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.e(getClass().getSimpleName(),e.toString());
	}
	return 1;
}
//*****************************************
/**
 *Write(Filename, ContentObjectArray)
 *  Overwrites contents of file
 *  
 */
public int write(String Filename,JSONArray ContentArray){
	// Uploading content.
	if(AccountLinked=false)
	{
		Log.e(getClass().getSimpleName(),"Cannot write to file"+Filename+" :Account not linked");
		return 0;
	}
		try {
			String Content = ContentArray.toString();	
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Content.getBytes());
			Entry newEntry = mApi.putFileOverwrite("/"+Filename, inputStream, Content.length(), null);
	   
	} catch (DropboxUnlinkedException e) {
	   // User has unlinked, ask them to link again here.
	   Log.e(getClass().getSimpleName(), "User has unlinked.");
	   return 0;
	} catch (DropboxException e) {
	   Log.e(getClass().getSimpleName(), "Problem uploading:"+e.toString());
	   return 0;
	}
	return 1;
}

//*****************************************************************************************

/**
 * Shows keeping the access keys returned from Trusted Authenticator in a local
 * store, rather than storing user name & password, and re-authenticating each
 * time (which is not to be done, ever).
 *
 * @return Array of [access_key, access_secret], or null if none stored
 */
private String[] getKeys() {
	 SharedPreferences prefs = new ContextWrapper(context).getSharedPreferences(ACCOUNT_PREFS_NAME, 2);
     String key = prefs.getString(ACCESS_KEY_NAME, null);
     String secret = prefs.getString(ACCESS_SECRET_NAME, null);
     //Log.i(getClass().getSimpleName(),"key "+key+" secret :"+secret);
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
private AndroidAuthSession buildSession() {
    AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
    AndroidAuthSession session;

    String[] stored = getKeys();
    if (stored != null) {
        AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
        session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
    } else {
        session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
    }

    return session;
}

}
