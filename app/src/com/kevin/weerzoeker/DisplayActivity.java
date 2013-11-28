package com.kevin.weerzoeker;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayActivity extends Activity {
	
	String main, descr, icon;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		Intent i = getIntent();
		String stad = i.getStringExtra("plaats");
		
		Intent li = getIntent();
		String land = li.getStringExtra("land");
		
		TextView locatie = (TextView) findViewById(R.id.locatieText);
				 locatie.setText(stad + ", " + land);
				 
		String searchURL = "http://api.openweathermap.org/data/2.5/weather?q=" + stad + "," + land;
		
		try {
			URL url = new URL (searchURL);
			Reader weerReader = new InputStreamReader(url.openStream());
			
			JsonReader reader = new JsonReader(weerReader);
			
			reader.beginObject();
			
			while(reader.hasNext()) {
				String readout = reader.nextName();
				if(readout.equals("coord")) {
					reader.skipValue();
				}
				else if (readout.equals("sys")) {
					reader.skipValue();
				}
				else if (readout.equals("weather")) {
					reader.beginArray();
					reader.beginObject();
					
					readout = reader.nextName();
					reader.skipValue();
					
					readout = reader.nextName();
					main = reader.nextString();
					
					readout = reader.nextName();
					descr = reader.nextString();
					
					readout = reader.nextName();
					icon = reader.nextString();
					
					reader.endObject();
					
					if(reader.hasNext()) {
						//openweathermap api change
						//comment/uncomment this when app crashes
						reader.beginObject();
						
						readout = reader.nextName();
						reader.skipValue();
						
						readout = reader.nextName();
						reader.skipValue();
						
						readout = reader.nextName();
						reader.skipValue();
						
						readout = reader.nextName();
						reader.skipValue();
						
						reader.endObject();
						//comment/uncomment this when app crashes
						//openweathermap api change
					}
					
					reader.endArray();
					
				}
				else {
					reader.skipValue();
				}
			}
			
			reader.endObject();
			reader.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.v("URL fout", e.toString());
		} catch (IOException e) {
			Log.v("IOException", e.toString());
		}
		
		TextView mt = (TextView) findViewById(R.id.mainText);
		mt.setText(main);
		
		TextView dsc = (TextView) findViewById(R.id.descrText);
		dsc.setText(descr);
		
		new DownloadImageTask((ImageView) findViewById(R.id.wImage)).execute("http://openweathermap.org/img/w/" + icon + ".png");
		//Toast.makeText(getApplicationContext(), ophaal, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}

}
