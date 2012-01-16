package com.dropconnect.app;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;



public class Writer {
DropboxAPI<AndroidAuthSession> mApi;

public Writer(DropboxAPI<AndroidAuthSession> mApi) {
	this.mApi = mApi;
}

}
