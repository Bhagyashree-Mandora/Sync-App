package net.practice.dropbox;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

public class DropboxActivity extends Activity {
	//MobConnect app
	private final static String APP_KEY = "iypm3vb6d6vxha0";
    private final static String APP_SECRET = "e1kq1rhpwu6dfhx";
    //SyncApp app
  
    /*private final static String APP_KEY = "9xok2kilzjn2r1p";
    private final static String APP_SECRET = "szsiqick0cqrcu8";*/
    
    
	private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
	private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";
	private static final String ACCOUNT_PREFS_NAME = "prefs";
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /** Create the Button and Text View
        */
        
        final TextView text = (TextView) findViewById(R.id.textView1);
        final EditText mainText = (EditText) findViewById(R.id.mainText1);
        final Button buttonSave = (Button) findViewById(R.id.buttonSave);
        final Button buttonRegister = (Button) findViewById(R.id.button1);
        final Button buttonRead = (Button) findViewById(R.id.button2);
        
        
        /** Set the onClickListener to call the onClick for register button*/
        buttonRegister.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				OAuthProvider provider = new DefaultOAuthProvider
		        ("https://api.dropbox.com/0/oauth/request_token",
		         "https://api.dropbox.com/0/oauth/access_token",
		         "https://www.dropbox.com/0/oauth/authorize");

	        OAuthConsumer consumer = new DefaultOAuthConsumer
	            (APP_KEY, APP_SECRET);

				String url;
				try {
					url = provider.retrieveRequestToken(consumer, "dropbox:///");
			        mainText.setText(url);
			        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			        startActivity(browserIntent);	

				} 			 catch (ClassCastException e) {
					// TODO: handle exception
					mainText.setText(mainText.getText().toString()+e.toString());
				}catch (Exception e) {
					// TODO Auto-generated catch block
					mainText.setText(mainText.getText().toString()+e.toString());
				}
				

			}
        });
        /** Set the onClickListener to call the onClick */
        buttonSave.setOnClickListener(new Button.OnClickListener() {
    
        	public void onClick(View v) {
			// TODO Auto-generated method stub
			//text.setText(mainText.getText().toString()+"1");
        		OAuthConsumer consumer = new DefaultOAuthConsumer
	            (APP_KEY, APP_SECRET);
        		
      SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
			if(prefs.contains(ACCESS_KEY_NAME)){   
				mainText.setText(prefs.getAll().toString());
	        consumer.setTokenWithSecret(prefs.getString(ACCESS_KEY_NAME, ""), prefs.getString(ACCESS_SECRET_NAME, ""));
			try {
				URL url = new URL("https://api.dropbox.com/0/account/info");
			
	    	
	        HttpURLConnection request = (HttpURLConnection) url.openConnection();
	        consumer.sign(request);
	        text.setText(request.getResponseMessage());
	        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        text.setText(text.getText()+br.readLine());
	        
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			}
			}//if ends here
			else{
				text.setText("You are not loggedIn");
			}
			
			
	        
	       /* //Now we use that token which user enters into text box for getting access_token
	        String oauthToken= new String("vjeywjhaz82qfg0");
	        //consumer.setTokenWithSecret(oauthToken, "41409623");
	        consumer.setTokenWithSecret(oauthToken, "");
	        provider.retrieveAccessToken(consumer, oauthToken);
	        mainText.setText("token="+consumer.getToken()+" token secret="+
                           consumer.getTokenSecret());*/
			
		}
      
        });
        /** Set the onClickListener to call the onClick */
        buttonRead.setOnClickListener(new Button.OnClickListener() {
    
        	public void onClick(View v) {
			// TODO Auto-generated method stub
			//text.setText(mainText.getText().toString()+"1");
        		OAuthConsumer consumer = new DefaultOAuthConsumer
	            (APP_KEY, APP_SECRET);
        		
      SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
			if(prefs.contains(ACCESS_KEY_NAME)){   
				mainText.setText(prefs.getAll().toString());
	        consumer.setTokenWithSecret(prefs.getString(ACCESS_KEY_NAME, ""), prefs.getString(ACCESS_SECRET_NAME, ""));
			try {
				URL url = new URL("https://api-content.dropbox.com/0/files/dropbox/ourfile");
			
	    	
	        HttpURLConnection request = (HttpURLConnection) url.openConnection();
	        consumer.sign(request);
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        mainText.setText(br.readLine());
	        
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.toString());
			}
			}//if ends here
			else{
				text.setText("You are not loggedIn");
			}
			
	    	
		}
      
        });
        
        
    }
     
   
    /*@Override
    public void onResume()
    {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        String oauthToken = uri.getQueryParameter("oauth_token");
        final TextView text = (TextView) findViewById(R.id.textView1);        
     
        OAuthProvider provider = new DefaultOAuthProvider
        ("https://api.dropbox.com/0/oauth/request_token",
         "https://api.dropbox.com/0/oauth/access_token",
         "https://www.dropbox.com/0/oauth/authorize");
        OAuthConsumer consumer = new DefaultOAuthConsumer
        (APP_KEY, APP_SECRET);
try{
        provider.retrieveAccessToken(consumer, oauthToken);
}catch(OAuthCommunicationException e){
	text.setText(e.toString());
} catch (OAuthMessageSignerException e) {
	// TODO Auto-generated catch block
	text.setText(e.toString());
} catch (OAuthNotAuthorizedException e) {
	// TODO Auto-generated catch block
	text.setText(e.toString());
} catch (OAuthExpectationFailedException e) {
	// TODO Auto-generated catch block
	text.setText(e.toString());
}
    }*/
}