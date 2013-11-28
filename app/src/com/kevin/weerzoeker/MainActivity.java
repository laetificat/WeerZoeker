package com.kevin.weerzoeker;

import java.util.List;

import com.kevin.weerzoeker.DisplayActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	//Setup the variables
	Button saveButton, clearTagsButton, newDeleteButton, newEditButton, nameButton;
	EditText queryEditText, tagEditText;
	String name, plaats, land;
	static String finalplaats;
	WeerZoeker wz;
	WeerZoekerDatabaseHandler db;
	Spinner spinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new WeerZoekerDatabaseHandler(this);
		List<WeerZoeker> list = db.getWeerZoeker();
		for(WeerZoeker bw : list) {
			addEntry(bw.getPlaats(), bw.getNaam(), bw.getLand());
		}
		
		//Link the function to the variables
		saveButton = (Button) findViewById(R.id.saveButton);
		clearTagsButton = (Button) findViewById(R.id.clearTagsButton);
		queryEditText = (EditText) findViewById(R.id.queryEditText);
		tagEditText = (EditText) findViewById(R.id.tagEditText);
		newEditButton = (Button) findViewById(R.id.newEditButton);
		newDeleteButton = (Button) findViewById(R.id.newDeleteButton);
		nameButton = (Button) findViewById(R.id.newTagButton);
		
		final Spinner spinner = (Spinner) findViewById(R.id.country_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.country_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		//Create an onclick listener
		saveButton.setOnClickListener(new OnClickListener() {
			
			//Make a call to the layout inflater
			LayoutInflater layoutInflater = (LayoutInflater)
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			//Create the onclick and set it to current view
			public void onClick(View v) {
				//Inflate the innertablerow.xml to show it in the main view
				View newRow = layoutInflater.inflate(R.layout.innertablerow, null);
				
				//Create a table layout
				TableLayout tagsTableLayout = (TableLayout)
						findViewById(R.id.queryTableLayout);
				
				//Get the contents of the first input field and make it into a string and trim white spaces
				name = queryEditText.getText().toString().trim();
				plaats = tagEditText.getText().toString().trim();
				land = String.valueOf(spinner.getSelectedItem());
				
				//If-else statement to check if the first input field is empty so you can't create empty buttons
				if(name.isEmpty()){
					//Toast a warning instead of creating a button
					Toast.makeText(getApplicationContext(), "Vul alle velden in!", Toast.LENGTH_LONG).show();
				} else {
					//Create the button and input the contents from the name field
					Button newTagButton = (Button) newRow.findViewById(R.id.newTagButton);
					newTagButton.setText(name);
					
					
					//Call the table layout to add a view from innertablerow.xml
					tagsTableLayout.addView(newRow);
					
					//Throw data in database
					wz = new WeerZoeker();
					wz.setNaam(name);
					wz.setPlaats(plaats);
					wz.setLand(land);
					db.addRecord(wz);
					
					//Set both the input fields to empty
					queryEditText.setText("");
					tagEditText.setText("");
					
					onCreate(new Bundle());
					//View myView = findViewById(R.id.hiddenLayout);
		            //ViewGroup parent = (ViewGroup) myView.getParent();
		            //parent.removeView(myView);
		 
				}
			}
		});
		
		
		//Empty button, should edit items when clicked
		//newEditButton.setOnClickListener(new OnClickListener() {
		//	
		//	@Override
		//	public void onClick(View v) {
		//		
		//	}
		//});
		
		
		//Doesn't work yet lel
		clearTagsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				db.emptyWeerZoeker();
				Toast.makeText(getApplicationContext(), "Dit zou alles moeten verwijderen in de database!", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private void addEntry(String plaats, String naam, String land) {
		//Make a call to the layout inflater
		LayoutInflater layoutInflater = (LayoutInflater)
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//Inflate the innertablerow.xml to show it in the main view
		View newRow = layoutInflater.inflate(R.layout.innertablerow, null);
		
		//Create a table layout
		TableLayout tagsTableLayout = (TableLayout)
				findViewById(R.id.queryTableLayout);
		
		//Create the button and input the contents from the name field
		Button newTagButton = (Button) newRow.findViewById(R.id.newTagButton);
		Button newDeleteButton = (Button) newRow.findViewById(R.id.newDeleteButton);
		newTagButton.setText(naam);
		
		//Call the table layout to add a view from innertablerow.xml
		tagsTableLayout.addView(newRow);

		final String finalplaats = plaats;
		final String finalland = land;
		final String finalnaam = naam;
		
		newTagButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), finalplaats, Toast.LENGTH_LONG).show();
				//Toast.makeText(getApplicationContext(), finalland, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
				String s = finalplaats.toString();
				String l = finalland.toString();
				intent.putExtra("land", l);
				intent.putExtra("plaats", s);
				startActivity(intent);
			}
		});
		
		newDeleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), finalnaam + " is verwijderd!", Toast.LENGTH_LONG).show();
				db.removeRecord(finalnaam);
				onCreate(new Bundle());
			}
		});

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
