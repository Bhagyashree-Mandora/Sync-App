package com.dropconnect.app;

import java.util.ArrayList;
import java.util.List;


import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

public class Device {
	public static Device thisDevice = new Device("");
	
	String DeviceName;
	int DeviceId;
	String DeviceReadFile;
	
	public Device(String DeviceName)
	{
	
	}
	public static List<Device> getDeviceList()
	{	
		List<Device> Darray= new ArrayList<Device>();
		
		return Darray;
	}
	
	private static void registerDevice()
	{
		
	}
	private static void removeDevice()
	{
		
	}

}