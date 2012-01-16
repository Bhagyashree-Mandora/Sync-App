package com.dropconnect.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.entity.mime.MinimalField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;


import com.dropconnect.app.R;
import com.dropconnect.app.R.id;
import com.dropconnect.app.R.layout;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;
import com.dropbox.client2.session.Session.AccessType;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.Toast;
import android.util.Base64;
public class DropboxV2Activity extends Activity {
	
	
	private static final String TAG = "SMSApp";

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


    DropboxAPI<AndroidAuthSession> mApi;

    private boolean mLoggedIn;
    private Button mSubmit; 
    private Button mListen;
    private Button bGetImage;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Checks whether app key secret valid
        
        checkAppKeySetup();
        
        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        setContentView(R.layout.main);
      
       
        
        mSubmit = (Button)findViewById(R.id.auth_button);
        mSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // This logs you out if you're logged in, or vice versa
                if (mLoggedIn) {
                    logOut();
                } else {
                    // Start the remote authentication
                    mApi.getSession().startAuthentication(DropboxV2Activity.this);
                }
            	
            }
        });
        
        
        //Listen Button
        mListen = (Button)findViewById(R.id.buttonListen);
        mListen.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//Listen if there was any changes in the file
            	/*try {
					DropboxInputStream file= mApi.getFileStream("mobileread","");
					byte[] temp=new byte[200];	
					int count=file.read(temp);
					String str=new String(temp,0,count);
					showToast(str);
					JSONObject obj= new JSONArray(str).getJSONObject(0);	
					sendSMS(obj.getJSONObject("data"));
					//sendSMS(obj.getJSONObject("data").getString("to_number"),obj.getJSONObject("data").getString("message"));
				} catch (DropboxException e) {
					// TODO Auto-generated catch block
					showToast(e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					showToast(e.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					showToast(e.toString());
				}*/

            	
            	//Call service
            	Intent myIntent = new Intent(getApplicationContext(),ListenService.class);            	
            	Log.i(getClass().getSimpleName(),"intent created");
            	startService(myIntent);            	
            	Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();
            }

			

			
        });
        
        //Button to get the image
        bGetImage = (Button)findViewById(R.id.buttonGetimage);
        bGetImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//____________________________________________
				Device Sender= new Device("Mobile");
				Sender.DeviceReadFile="mobileread";
				//____________________________________________
				Device Reciever= new Device("Desktop");
				Reciever.DeviceReadFile="desktopread";
				//____________________________________________
				Command command=new Command("GetImageFromWebcam");
				//_________________Put Some Data______________
				JSONObject data=new JSONObject();
				Request r= new Request(Sender,Reciever,command,data,getApplicationContext()) {
					
					@Override
					public void onResponse() {
						// TODO Auto-generated method stub
						try {
							//Decode and store the file on SD Card
							String file=this.Data.getString("File");
							//showToast(file);
							byte[] decodedFile=Base64.decode(file, Base64.DEFAULT);
							File root = Environment.getExternalStorageDirectory();
							FileOutputStream f = new FileOutputStream(new File(root, "image.jpg"));
							f.write(decodedFile, 0, decodedFile.length);
				            f.close();
							//Open the file in default app
				            Intent intent = new Intent();
				            intent.setAction(android.content.Intent.ACTION_VIEW);
				            File file1 = new File(root,"image.jpg");
				            intent.setDataAndType(Uri.fromFile(file1), "image/*");
				            startActivity(intent);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.e(getClass().getSimpleName(),e.toString());
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							Log.e(getClass().getSimpleName(),e.toString());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Log.e(getClass().getSimpleName(),e.toString());
						}
					}
				};
				r.write();
			}
			
		});
        // Display the proper UI state if logged in or not
        //used to set text on the auth_button
        setLoggedIn(mApi.getSession().isLinked());
        
        
       //while(true){
        try {
			showToast(mApi.accountInfo().displayName);			
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       //}
       
    }
    
  /*   @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mCameraFileName", mCameraFileName);
        super.onSaveInstanceState(outState);
    }
*/
    private void sendSMS(JSONObject obj) {
				// TODO Auto-generated method stub
				try {
					sendSMS(obj.getString("to_number"),obj.getString("message"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("json",e.toString());
				}
			}
	private void sendSMS(String destination, String text) {
		// TODO Auto-generated method stub
    	PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, DropboxV2Activity.class), 0);
	SmsManager smsManager = SmsManager.getDefault();
	SmsManager sms= smsManager;	
	sms.sendTextMessage(destination,"", text,null,null );
	}
    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i(TAG, "Error authenticating", e);
            }
        }
    }
    
    

    private void logOut() {
        // Remove credentials from the session
        mApi.getSession().unlink();
        Log.i(getClass().getSimpleName(),"Logged Out");
        // Clear our stored keys
        clearKeys();
        // Change UI state to display logged out version
        setLoggedIn(false);
    }

    /**
     * Convenience function to change UI state based on being logged in
     */
    private void setLoggedIn(boolean loggedIn) {
    	mLoggedIn = loggedIn;
    	if (loggedIn) {
    		mSubmit.setText("Unlink from Dropbox");
            
    	} else {
    		mSubmit.setText("Link with Dropbox");
            
    	}
    }
 
    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
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
            finish();
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     *
     * @return Array of [access_key, access_secret], or null if none stored
     */
    private String[] getKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, MODE_WORLD_WRITEABLE);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
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
    	Log.i("store key","storing keys");
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, MODE_WORLD_WRITEABLE);        
        Editor edit = prefs.edit();
        edit.clear();
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