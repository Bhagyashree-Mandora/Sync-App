package com.dropconnect.app;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class Listen extends AsyncTask<Void,Long, Boolean> {

	
	private Context mContext;
    private DropboxAPI<?> mApi;
    private String mPath;
    private String mErrorMsg;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Listen(Context context, DropboxAPI<AndroidAuthSession> api, String path) {
		super();
		this.mContext = context.getApplicationContext();
		this.mApi = api;
		this.mPath = path;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
	
		try {
			showToast(mApi.accountInfo().country);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
    @Override
    protected void onPostExecute(Boolean result) {
            // Listen Ends
            showToast("End");
    }


	private void showToast(String msg) {
        Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        error.show();
    }

}
