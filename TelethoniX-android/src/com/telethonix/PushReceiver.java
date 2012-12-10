package com.telethonix;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PushReceiver extends BroadcastReceiver {
	
	public final static String TAG = PushReceiver.class.getSimpleName();
	
	@Override
    public void onReceive(Context context, Intent intent) {   		
		try {
			Bundle extras = intent.getExtras();        
			String message = extras != null ? extras.getString("com.parse.Data") : "";    
			JSONObject map;
			map = new JSONObject(message);
			if(map.getString("uuid").equals(MainActivity.uuid)==false) {
				Toast toast = Toast.makeText(context, "Received messages: "+message, Toast.LENGTH_LONG);        
				toast.show();			
			}else {
				Log.i(TAG, "Received a message sent by current user.");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
    }
}
