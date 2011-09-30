package net.practice.dropbox;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

public class DropboxActivity extends Activity {
	private final static String APP_KEY = "iypm3vb6d6vxha0";
    private final static String APP_SECRET = "e1kq1rhpwu6dfhx";
    
    
    
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
        
        /** Set the onClickListener to call the onClick */
        buttonSave.setOnClickListener(new Button.OnClickListener() {
    
        	public void onClick(View v) {
			// TODO Auto-generated method stub
			text.setText(mainText.getText().toString()+"1");
			
			try {
				OAuthProvider provider = new DefaultOAuthProvider
	            ("https://api.dropbox.com/0/oauth/request_token",
	             "https://api.dropbox.com/0/oauth/access_token",
	             "https://www.dropbox.com/0/oauth/authorize");
				//provider.setOAuth10a(true);
	        OAuthConsumer consumer = new DefaultOAuthConsumer
	            (APP_KEY, APP_SECRET);

	        /*
	         * Brings Acess Token from Dropbox
	         * String url = provider.retrieveRequestToken(consumer, "MobConnect://dropbox");
	        mainText.setText(url);
	        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        startActivity(browserIntent);*/
	        
	        //Now we use that token which user enters into text box for getting access_token
	        String oauthToken= mainText.getText().toString();
	        provider.retrieveAccessToken(consumer, oauthToken);
	        mainText.setText("token="+consumer.getToken()+" token secret="+
                           consumer.getTokenSecret());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				mainText.setText(mainText.getText().toString()+e.getLocalizedMessage());
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