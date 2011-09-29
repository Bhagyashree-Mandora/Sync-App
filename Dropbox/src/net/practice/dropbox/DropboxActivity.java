package net.practice.dropbox;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
//import java.util.Map;
import net.oauth.consumer.OAuth2Consumer;
import net.oauth.enums.GrantType;
import net.oauth.enums.ResponseType;
import net.oauth.exception.OAuthException;
import net.oauth.parameters.OAuth2Parameters;
import net.oauth.provider.OAuth2ServiceProvider;

@SuppressWarnings("unused")
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
			OAuth2ServiceProvider provider = new OAuth2ServiceProvider("http://api.dropbox.com/0/oauth/request_token", "https://api.dropbox.com/0/token");
			try{
            OAuth2Consumer consumer = new OAuth2Consumer(APP_KEY, APP_SECRET, provider);
			}catch(Exception e)
			{
				text.setText(e.getMessage());
			}
            
            
              //  text.setText(consumer.generateRequestAuthorizationUrl(ResponseType.CODE, "http://localhost:8080/my_app/oauth_redirect", null, (String[])null).toString());
                
                //OAuth2Parameters parameters = new OAuth2Parameters();
                //parameters.setCode("3f61aa47b915215a938d2722-682316653|5OPOkmKew_W8vybb9sccIPoivAg.");
                //parameters.setRedirectUri("http://localhost:8080/my_app/oauth_redirect");
                
                //HttpResponse oauthResponse = (HttpResponse) consumer.requestAcessToken(GrantType.AUTHORIZATION_CODE, parameters, (String[])null);
                
                //text.setText(oauthResponse.toString());
        } catch (Exception e) {
                // TODO Auto-generated catch block
                mainText.setText(e.toString());
        }

            
            
		}
      
        });

    }
    
    
}