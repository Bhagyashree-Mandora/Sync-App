package com.dropconnect.app;


import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TerminalActivity extends Activity {
Button request;
TextView textResult;
EditText textCommand;
Menu menu;
static String StartText;
public static StringBuffer Screen=new StringBuffer(); 
/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            
       setContentView(R.layout.terminal);
           
       
      //Request Button
        request = (Button)findViewById(R.id.buttonRun);
        request.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            		runCommand();
			}
            
           		
        });//On Click for run ends
        
        //Text View Result
        textResult=(TextView)findViewById(R.id.textViewResult);
        
        
        //Text Edit Command
        textCommand=(EditText)findViewById(R.id.TextCommand);
        //Set the start text 
        StartText=new String("faizan@faizan-ubuntu:-$");
        Screen.append(StartText); 
    }
    
    public static void append(String result){
    	//Append result string to the screen
    	Screen.append(result);
    	//Now make space for the new command
    	Screen.append("\n"+StartText);    	
    }
    
    //oN CREATE OPTION MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    super.onCreateOptionsMenu(menu);

    MenuItem refresh=menu.add("Refresh");    
    refresh.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			textResult.setText(Screen.toString());	
			return true;
		}
	});
    MenuItem run=menu.add("Run");    
    run.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			runCommand();
			return true;
		}
	});
    return true;
    }
    
    
    
//**************************************RUN function>>>>>>>>>>>>>>>>>>>>>>>>>>>>>    
    void runCommand(){
    	if(textCommand.getText().toString().equalsIgnoreCase("")){
    		showToast("Please Enter some command");
    		return;
    	}
    	//Print command on Screen
    	textResult.setText(textResult.getText()+"\n"+StartText+textCommand.getText());
    	
    	//***************************************************************
    	//****Create and send a request to execute the command on desktop
    	//_______________________________________________________________
		Device Sender= new Device("Mobile");
		Sender.DeviceReadFile="mobileread";
		//_______________________________________________________________
		Device Reciever= new Device("Desktop");
		Reciever.DeviceReadFile="desktopread";
		//_______________________________________________________________
		Command command=new Command("terminalapp");
		//_________________Put Some Data_________________________________
		JSONObject data=new JSONObject();
		JSONObject session=new JSONObject();				
		try {
			session.put("location","");
			data.put("session", session);
			data.put("command", textCommand.getText().toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(getClass().getSimpleName()+":Put Some data", e.toString());
		}
		//_________________Create Request________________________________
		Request r= new Request(Sender,Reciever,command,data,getApplicationContext()) {
			
			@Override
			public void onResponse() {
				// TODO Auto-generated method stub
			try {
				String result=Data.getString("result");
				TerminalActivity.append(result);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(getClass().getSimpleName()+":onResponse",e.toString());
			}
			}
		};
		r.write();
		//Save screen contents
		Screen.append(textCommand.getText());
		//clear the text box
    textCommand.setText("");
    }
    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }
}
