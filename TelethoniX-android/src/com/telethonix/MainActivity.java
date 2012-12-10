package com.telethonix;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SendCallback;

public class MainActivity extends Activity {
	
	public static final String TAG = MainActivity.class.getSimpleName();
	
	public static int[] heartBad = new int[] {
		R.drawable.a_ithonix_bad_ld0001, R.drawable.a_ithonix_bad_ld0002, R.drawable.a_ithonix_bad_ld0003, R.drawable.a_ithonix_bad_ld0004, R.drawable.a_ithonix_bad_ld0005,
		R.drawable.a_ithonix_bad_ld0006, R.drawable.a_ithonix_bad_ld0007, R.drawable.a_ithonix_bad_ld0008, R.drawable.a_ithonix_bad_ld0009, R.drawable.a_ithonix_bad_ld0010,
		R.drawable.a_ithonix_bad_ld0011, R.drawable.a_ithonix_bad_ld0012};
	public static int[] heartGood = new int[] {
		R.drawable.a_ithonix_good_ld0001, R.drawable.a_ithonix_good_ld0002, R.drawable.a_ithonix_good_ld0003, R.drawable.a_ithonix_good_ld0004, R.drawable.a_ithonix_good_ld0005,
		R.drawable.a_ithonix_good_ld0006, R.drawable.a_ithonix_good_ld0007, R.drawable.a_ithonix_good_ld0008, R.drawable.a_ithonix_good_ld0009, R.drawable.a_ithonix_good_ld0010,
		R.drawable.a_ithonix_good_ld0011};
	
	public static String uuid = UUID.randomUUID().toString();
	
	//protected ImageView imgHeart;
	//protected ImageView imgBody;
	protected ImageView imgHuman;
	protected int index = 0;
	protected long period = 200l;
	protected Timer heartBeating;
	protected boolean isGood = true;
	protected boolean pushed = false;
	
	protected MediaPlayer good_sound;
	protected MediaPlayer bad_sound;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        String app_id     = "u9pFpfosdfVrsSRtNhZQhlqjB96XchJgrrTBULOA";
        String client_key = "Y3XuiiHK8rv9JRLsexkp1QpVhczz7L9FXWKlX0kL";
        Parse.initialize(this, app_id, client_key);
        /*
        // Obtain the installation object for the current device
        ParseInstallation myInstallation = ParseInstallation.getCurrentInstallation();                 
        myInstallation.put("scoreUpdates", true); // Save some data       
        // Save or Create installation object
        myInstallation.saveInBackground(new SaveCallback() {			
			@Override
			public void done(ParseException arg0) {
				Log.i(TAG, "installation done!");
		        // When users indicate they are Giants fans, we subscribe them to that channel
		        //		        		        	
			}
		});
        */
        good_sound = MediaPlayer.create(MainActivity.this, R.raw.good);  
        bad_sound = MediaPlayer.create(MainActivity.this, R.raw.bad); 
        
        PushService.subscribe(MainActivity.this, "Alarm", MainActivity.class);
               
        //imgBody = (ImageView) findViewById(R.id.img_body);
        //imgHeart = (ImageView) findViewById(R.id.img_heart);
        imgHuman = (ImageView) findViewById(R.id.img_human);
        imgHuman.setImageResource(heartGood[index++]);
        heartBeating = new Timer();
        heartBeating.schedule(new TimerTask() {			
			@Override
			public void run() {				
				runOnUiThread(new Runnable() {					
					public void run() {
						if(isGood) {
							if(bad_sound.isPlaying())
								bad_sound.stop();
							good_sound.start();
					        
							pushed = false;
							imgHuman.setImageResource(heartGood[index++]);							
							if(index>=heartGood.length)
								index = 0;
						}else {
							if(good_sound.isPlaying())
								good_sound.stop();
							bad_sound.start();
							
							imgHuman.setImageResource(heartBad[index++]);							
							if(index>=heartBad.length)
								index = 0;
							if(!pushed) {
								push();
								pushed = true;
							}							
						}
					}
				});			
			}
		}, period, period);
        new Timer().schedule(new TimerTask() {			
			@Override
			public void run() {				
				isGood = beats()==-1 ? false:true;		
			}
		}, period, 2*period);
/*        
        imgHuman.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				isGood = !isGood;
				if(isGood==false) {
					push();
				}
			}
		});   
*/		             
    }

    @Override
    public void onDestroy() {
    	if(good_sound!=null && good_sound.isPlaying())
			good_sound.stop();
    	if(bad_sound!=null && bad_sound.isPlaying())
			bad_sound.stop();
    	super.onDestroy();
    }
    
    protected void push() {
    	JSONObject data;
		try {
			data = new JSONObject("{\"action\": \"com.telethonix.NOTIFICATION\", \"uuid\": \""+uuid+"\", \"message\": \"Heart stopped beating\"}");

	        ParsePush push = new ParsePush();
	        //push.setQuery(injuryReportsQuery);
	        push.setChannel("Alarm");
	        push.setData(data);
	        push.sendInBackground(new SendCallback() {			
				@Override
				public void done(ParseException arg0) {					
					Log.i(TAG, "push sent done!");
			        // When users indicate they are Giants fans, we subscribe them to that channel
			        //		        		        	
				}
	        });
	        
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
/*
    protected void push() {
    	ParsePush push = new ParsePush();
        push.setChannel("Alarm");
        push.setMessage("Heart stopped beating.");
        push.sendInBackground(new SendCallback() {			
			@Override
			public void done(ParseException arg0) {
				Log.i(TAG, "push sent done!");
		        // When users indicate they are Giants fans, we subscribe them to that channel
		        //		        		        	
			}
		});
    }
*/    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public int beats() {    	
    	int beats = -1;
    	try {
            String url = "http://woowmom.appspot.com/";			
			HttpClient hc = new DefaultHttpClient();			
			HttpGet get = new HttpGet(url);			
			HttpResponse rp = hc.execute(get);
			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = EntityUtils.toString(rp.getEntity());			
				beats = Integer.parseInt(str);
			}		
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
        return beats;
    }
}
