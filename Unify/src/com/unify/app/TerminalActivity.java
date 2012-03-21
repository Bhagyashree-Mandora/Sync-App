package com.unify.app;


import org.json.JSONException;
import org.json.JSONObject;

import com.unify.app.R;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TerminalActivity extends ListActivity {
Button request;
TextView textResult;
EditText textCommand;
Menu menu;
static String StartText;
SQLiteDatabase db;
public static StringBuffer Screen=new StringBuffer(); 
private int mCommandNumber = 1;
private static TerminalDbAdapter mDbHelper;
/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            
       setContentView(R.layout.terminal);
       mDbHelper = new TerminalDbAdapter(this);
       mDbHelper.open();
       fillData();
       
      //Request Button
        request = (Button)findViewById(R.id.buttonRun);
        request.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            		runCommand();
            		
			}
            
           		
        });//On Click for run ends
        
        //Text View Result
        //textResult=(TextView)findViewById(R.id.textViewResult);
        
        
        //Text Edit Command
        textCommand=(EditText)findViewById(R.id.TextCommand);
        //Set the start text 
        StartText=new String("faizan@faizan-ubuntu:-$");
        Screen.append(StartText);
       
    }
    private void createCommand() {
        String command = StartText + textCommand.getText();
        mDbHelper.createCommand(command, "");
        fillData();
    }
    public static void append(String result){
    	//Append result string to the screen
    	Screen.append(result);
    	//Now make space for the new command
    	Screen.append("\n"+StartText);    	
    }
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllCommands();
        startManagingCursor(c);
if(c.getCount()!=0){
        String[] from = new String[] { TerminalDbAdapter.KEY_COMMAND,TerminalDbAdapter.KEY_RESULT};
        int[] to = new int[] { R.id.text1,R.id.text2 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.command_row, c, from, to);
        setListAdapter(notes);}
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
			fillData();
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
    MenuItem clear=menu.add("Clear");
    clear.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			mDbHelper.deleteAll();
			return false;
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
    	//Add Command in database
    	long rowid=mDbHelper.createCommand(textCommand.getText().toString(), "");
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
			data.put("rowid", rowid);
			
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
				String command=Data.getString("command");
				String rowid=Data.getString("rowid");
				TerminalActivity.mDbHelper.updateCommand(Long.parseLong(rowid), command, result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		};
		fillData();
		//clear the text box
	    textCommand.setText("");
		r.write();
    }
    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }
}
