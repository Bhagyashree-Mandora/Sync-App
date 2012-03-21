package com.unify.app;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.unify.app.R;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;
import com.dropbox.client2.session.Session.AccessType;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.util.Base64;
public class UnifyActivity extends Activity {
	
	
	private static final String TAG = "SMSApp";

///////////////////////////////////////////////////////////////////////////
    //                      Your app-specific settings.                      //
    ///////////////////////////////////////////////////////////////////////////

    // Replace this with your app key and secret assigned by Dropbox.
    // Note that this is a really insecure way to do this, and you shouldn't
    // ship code which contains your key & secret in such an obvious way.
    // Obfuscation is good.
    /*final static private String APP_KEY = "iypm3vb6d6vxha0";
    final static private String APP_SECRET = "e1kq1rhpwu6dfhx";

    // If you'd like to change the access type to the full Dropbox instead of
    // an app folder, change this value.
    final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;*/
	
	final static private String APP_KEY = "jpweiyz9h76wc87";
    final static private String APP_SECRET = "aopz5rzvv6pngk3";

    // If you'd like to change the access type to the full Dropbox instead of
    // an app folder, change this value.
    final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

    ///////////////////////////////////////////////////////////////////////////
    //                      End app-specific settings.                       //
    ///////////////////////////////////////////////////////////////////////////

    // You don't need to change these, leave them alone.
    final static private String ACCOUNT_PREFS_NAME = "prefs";
    final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    final static private String ON_INSTALL = "ON_INSTALL";


    DropboxAPI<AndroidAuthSession> mApi;

    private boolean mLoggedIn;   
    private Button bGetImage;
    private Button bOpenTerminal;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Checks whether app key secret valid
        
        checkAppKeySetup();
        
        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);
        
        //Check whether app is running for the first time...
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, MODE_WORLD_WRITEABLE);        
        if(!prefs.contains(ON_INSTALL)){
        	showToast("Running Just Installed application for the first time");
        	if(startInstall()){
        		showToast("Installation Successful");
        	//Mark as Successfully installed
        	Editor edit = prefs.edit();
            edit.clear();
            edit.putString(ON_INSTALL, "TRUE");            
            edit.commit();
            }
        	else{
        		//Unsuccessful Installation
        	}
        }
        else{
        	showToast("Running already installed application");
        }
        
        setContentView(R.layout.main);
      
    
        
        
        
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
				Command command=new Command("takephoto");
				//_________________Put Some Data______________
				JSONObject data=new JSONObject();
				Request r= new Request(Sender,Reciever,command,data,getApplicationContext()) {
					
					@Override
					public void onResponse() {
						// TODO Auto-generated method stub
						try {
							//Decode and store the file on SD Card
							String file=this.Data.getString("file");
							//showToast(file);
							byte[] decodedFile=Base64.decode(file, Base64.DEFAULT);							
							File root = Environment.getExternalStorageDirectory();
							FileOutputStream f = new FileOutputStream(new File(root,"image"+this.requestID+".jpg"));
							f.write(decodedFile, 0, decodedFile.length);
				            f.close();
							//Open the file in default app
				            Intent intent = new Intent();
				            intent.setAction(android.content.Intent.ACTION_VIEW);
				            File file1 = new File(root,"image"+this.requestID+".jpg");
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
				showToast("Your Request for the Image has been sent.Please wait For the Image to come.It will be stored on your SD as image"+r.requestID+".jpg");				
			}
			
		});
        
       //Button to open Terminal
        bOpenTerminal= (Button)findViewById(R.id.buttonTerminal); 
        bOpenTerminal.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               Intent MyIntent = new Intent(getApplicationContext(), com.unify.app.TerminalActivity.class);             
                startActivity(MyIntent);
            	
            }
        });
        
        //Testing connection
        try {
			showToast(mApi.accountInfo().displayName);			
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			Log.e(this.getClass().getSimpleName(), e.toString());
		}
      
       
    }
    
  private boolean startInstall() {
	  
	  //Start Authorization
	  mApi.getSession().startAuthentication(UnifyActivity.this);
	  setLoggedIn(mApi.getSession().authenticationSuccessful());  
	  return true;
		
	}


private boolean onSuccessfullAuth(){

	  
	  //Create read file on Successful Authentication>>>>>>>>>
	  ByteArrayInputStream inputStream = new ByteArrayInputStream(new String("").getBytes());
		try {
			String Filename=new String("mobileread");
		   Entry newEntry = this.mApi.putFileOverwrite("/"+Filename, inputStream,0, null);
		   showToast(Filename+" File is created on Dropbox");
		} catch (DropboxUnlinkedException e) {
		   // User has unlinked, ask them to link again here.
		   Log.e(getClass().getSimpleName(), "User has unlinked.");
		   return false;
		} catch (DropboxException e) {
		   Log.e(getClass().getSimpleName(), "Problem uploading:"+e.toString());
		   return false;
		}
		//_______________File created_______________________________________
		
		
		//Start the listen service automatically
		//Call service
  	Intent myIntent = new Intent(getApplicationContext(),ListenService.class);            	
  	Log.i(getClass().getSimpleName(),"intent created");
  	startService(myIntent);            	
  	Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
		//___________________________________________________________________
	  //________CAPABILITY
  	///Get the number of apps that can handle stuff
  	//ClassLoader classloader=this.getClassLoader();    	
  	
	//Add to device list file
  	Device myself=new Device("mobile");
  	myself.DeviceReadFile=new String("mobileread");
  	List<String>capability = null;
	Iam command=new Iam(myself, capability,getApplicationContext());
	command.registerToCloud();
	//Refresh cached copy-download device list file

		
	//Broadcast to other devices
	
return true;	

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
                onSuccessfullAuth();//does remaining task of on install whenever linked with a new account
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
    //oN CREATE OPTION MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    super.onCreateOptionsMenu(menu);

    MenuItem refresh=menu.add("Link");
    if(mLoggedIn==true)
    	refresh.setTitle("UnLink");
    else
    	refresh.setTitle("Link");
    refresh.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			 if (mLoggedIn) {
                 logOut();
                 item.setTitle("Link");
             } else {
                 // Start the remote authentication
                 mApi.getSession().startAuthentication(UnifyActivity.this);
                 item.setTitle("Unlink");
             }
			return true;
		}
	});
    MenuItem run=menu.add("listen");    
    run.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			//Call service
        	Intent myIntent = new Intent(getApplicationContext(),ListenService.class);            	
        	Log.i(getClass().getSimpleName(),"intent created");
        	startService(myIntent);            	
        	Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();
			return true;
		}
	});
    
   
    return true;
    
    }    
}