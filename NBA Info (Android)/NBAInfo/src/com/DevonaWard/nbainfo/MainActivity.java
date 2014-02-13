package com.DevonaWard.nbainfo;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;




public class MainActivity extends Activity {

	InputStream InputStream;
	String BufferString;
	String ABBREVIATION;
	String FULLNAME;
	String CITY;
	String STATE;
	String DIVISION;
	String CONFERENCE;
	String ARENA;
	ArrayList<String>AbbreviationList = new ArrayList<String>();
	ArrayList<String>FullNameList = new ArrayList<String>();
	ArrayList<String>CityList = new ArrayList<String>();
	ArrayList<String>StateList = new ArrayList<String>();
	ArrayList<String>DivisionList = new ArrayList<String>();
	ArrayList<String>ConferenceList = new ArrayList<String>();
	ArrayList<String>ArenaList = new ArrayList<String>();
	ArrayList<String>SQLiteList = new ArrayList<String>();
	ArrayList<String>QueryList = new ArrayList<String>();
	SQLHandler SQL = new SQLHandler(this);
	ListView theListView;
	List<Team> teams;
	RadioButton radioEW;
	RadioGroup EWGroup;
	String EWtxt;
	Spinner divisionSpinner;
	String spinnerTxt;
	Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Get radio group
        EWGroup = (RadioGroup) findViewById(R.id.radio_group); 
        //Get spinner
        divisionSpinner = (Spinner) findViewById(R.id.spinner);
        
        //Load JSON data file        
        LoadJSON loadJSON = new LoadJSON();
		loadJSON.execute();
		
		//Get button
		button = (Button) findViewById(R.id.button1);
		//Execute the query
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	openAndQueryDatabase();
            }
        });
		
		
		//Add SQL DB data to list
		theListView = (ListView) findViewById(R.id.list);
	    
		
    }

    private class LoadJSON extends AsyncTask<String, Void, String> {

		@Override
	    protected void onPreExecute() {                       
	    }

		//Get JSON File 
	    @Override
	    protected String doInBackground(String... strings) {

	    	 try{
	    		 InputStream = getAssets().open("nbainfo.json");
                 BufferedInputStream bin = new BufferedInputStream(InputStream);
                 
                 byte[] contentBytes = new byte[1024];
                 int bytesRead = 0;
                 StringBuffer responseBuffer = new StringBuffer();
                 
                 while((bytesRead = bin.read(contentBytes)) != -1){
                         BufferString = new String(contentBytes,0,bytesRead);
                         responseBuffer.append(BufferString);
                 }
                 
                 try {
                     JSONArray jsonArray = new JSONArray(responseBuffer.toString());
                     
                     int n = jsonArray.length();
                     //Add JSON data to array list
                     for(int i = 0;i<n; i++){
                             JSONObject jsonObject = jsonArray.getJSONObject(i);

                         	ABBREVIATION = jsonObject.getString("abbreviation");
                         	AbbreviationList.add(ABBREVIATION);
                         	FULLNAME = jsonObject.getString("full_name");
                         	FullNameList.add(FULLNAME);
                         	CITY = jsonObject.getString("city");
                         	CityList.add(CITY);
                         	STATE = jsonObject.getString("state");
                         	StateList.add(STATE);
                         	CONFERENCE = jsonObject.getString("conference");
                         	ConferenceList.add(CONFERENCE);
                            DIVISION = jsonObject.getString("division");
                         	DivisionList.add(DIVISION);
                            ARENA= jsonObject.getString("site_name");
                         	ArenaList.add(ARENA);
                     }
                 } catch (JSONException e1) {
                     // TODO Auto-generated catch block
                     e1.printStackTrace();
             }
                 
                 return responseBuffer.toString();
         }catch (Exception e){
                 Log.e("DATA READ ERROR", "getURLStringResponse");
         }
			return null;
		}

	    @Override
        protected void onPostExecute(String result){
	    	
	    	//Add data to SQL DB
	    	int n = AbbreviationList.size();
	    	for(int i=0; i<n; i++ ){
	  
	    		SQL.addTeam(new Team(AbbreviationList.get(i), FullNameList.get(i), CityList.get(i),StateList.get(i), ConferenceList.get(i), DivisionList.get(i), ArenaList.get(i)));
	    	}
	    	
	    	//Get data from SQL DB
	    	teams = SQL.getTeams();  
	    	for (Team TI : teams) {
	            String theData = "("+ TI.getAbbreviation() + ") " +TI.getName() + "\n" + TI.getCity() + ", " + 
	    	TI.getState() + "\n" + "Conference: "+ TI.getConference() + "\n" + "Divison: " + TI.getDivision() + "\n" + "Arena: " + TI.getArena();
          	            
	            //Add data to array list.
	            SQLiteList.add(theData);
	    }
	    	 
	    	// Writing SQL to log
            Log.i("SQLite Working", "DATA ADDED TO SQL DB.");
	}
}

    
    private void openAndQueryDatabase() {
    	//Clear QueryList if it contains data.
    	if(QueryList.size() != 0){
    		QueryList.removeAll(QueryList);
    	}
    	//Get selected id of radio button
        int selected = EWGroup.getCheckedRadioButtonId();
        //Get selected button
        radioEW = (RadioButton) findViewById(selected);
        //Text of selected radio button
        EWtxt = radioEW.getText().toString();
        //Get spinner text
        spinnerTxt = divisionSpinner.getSelectedItem().toString();
        try {
            SQL.getWritableDatabase();
            Cursor cursor = SQL.getWritableDatabase().rawQuery("SELECT  * FROM Teams WHERE conference='"+EWtxt+"' AND division='"+spinnerTxt+"'",null);
 
            if (cursor != null ) {
                if  (cursor.moveToFirst()) {
                    do {
                        String teamAbbreviation = (cursor.getString(1));
                        String teamName = (cursor.getString(2));
                        String teamCity = (cursor.getString(3));
                        String teamState = (cursor.getString(4));
                        String teamDivision = (cursor.getString(5));
                        String teamConference = (cursor.getString(6));
                        String teamArena = (cursor.getString(7));
                        QueryList.add("("+ teamAbbreviation+ ") " +teamName+ "\n" + teamCity + ", " + 
                    	    	teamState + "\n" + "Conference: "+ teamConference + "\n" + "Divison: " + teamDivision + "\n" + "Arena: " + teamArena);
                    }while (cursor.moveToNext());
                } 
            }
            //Check data in QueryList
            Log.i("THE REAL QUERY", "QUERY HAS BEEN COMPLETED.");
            //Populate list view with query data.
            ArrayAdapter<String> simpleAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, QueryList);
    	    theListView.setAdapter(simpleAdpt);
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } 
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
