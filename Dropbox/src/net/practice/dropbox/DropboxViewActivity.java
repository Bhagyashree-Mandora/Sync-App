package net.practice.dropbox;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.OAuthConsumer;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;


public class DropboxViewActivity extends Activity{
	private final static String APP_KEY = "iypm3vb6d6vxha0";
    private final static String APP_SECRET = "e1kq1rhpwu6dfhx";
	private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
	private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";
	private static final String ACCOUNT_PREFS_NAME = "prefs";

	/*protected void onResume() {
		
        // this must be places in activity#onResume()
    	try{
    		OAuthProvider provider = new DefaultOAuthProvider
            ("https://api.dropbox.com/0/oauth/request_token",
             "https://api.dropbox.com/0/oauth/access_token",
             "https://www.dropbox.com/0/oauth/authorize");
			//provider.setOAuth10a(true);
        OAuthConsumer consumer = new DefaultOAuthConsumer
            (APP_KEY, APP_SECRET);
        Uri uri = (Uri) this.getIntent().getData();  
        provider.retrieveAccessToken(consumer, uri.parse("oauth_token").toString());
    	}catch(Exception e){
    		
    	}
        super.onResume();
    }	   */  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dropboxview);
		//TextView text = (TextView) findViewById(R.id.textView1);
		//text.setText("im in create");
		//Process response
		
		EditText mainText = (EditText) findViewById(R.id.editText1);
		TextView text = (TextView) findViewById(R.id.textView1);
		OAuthProvider provider = new DefaultOAuthProvider
        ("https://api.dropbox.com/0/oauth/request_token",
         "https://api.dropbox.com/0/oauth/access_token",
         "https://www.dropbox.com/0/oauth/authorize");
		
		//provider.setOAuth10a(true);
    OAuthConsumer consumer = new DefaultOAuthConsumer(APP_KEY, APP_SECRET);
    Uri uri = (Uri) this.getIntent().getData();
    
    try{
    	SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        mainText.setText(uri.toString()+"and token is'"+uri.getQueryParameters("oauth_token").get(0)+"'");
    	String token =uri.getQueryParameters("oauth_token").get(0).toString();
    	String secret =uri.getQueryParameters("uid").get(0).toString();
    		consumer.setTokenWithSecret(token, secret);
    	if(prefs.getBoolean(ACCESS_KEY_NAME, false)){    		
    provider.retrieveAccessToken(consumer, token);
    storeKeys(consumer.getToken(),	consumer.getTokenSecret());
    text.setText(consumer.getTokenSecret()+"and token is"+consumer.getToken());
    	}
    
    }catch(OAuthCommunicationException e)
    {
    	
    } catch (OAuthMessageSignerException e) {
		// TODO Auto-generated catch block
		mainText.setText(e.toString());
		text.setText(e.toString());
	} catch (OAuthNotAuthorizedException e) {
		// TODO Auto-generated catch block
		mainText.setText(e.toString());
		text.setText(e.toString());
	} catch (OAuthExpectationFailedException e) {
		// TODO Auto-generated catch block
		//mainText.setText(e.toString());
		text.setText(e.toString());
	} /*catch (IOException e) {
		// TODO Auto-generated catch block
		text.setText(e.toString());
	}*/
    finally{}
	}
	/*protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

	}*/
	public void storeKeys(String key, String secret) {
        // Save the access key for later
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }
    
    public void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
}
