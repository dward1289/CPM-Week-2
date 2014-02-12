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
import android.util.Log;
import android.view.Menu;



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
	SQLHandler SQL = new SQLHandler(this);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        LoadJSON loadJSON = new LoadJSON();
		loadJSON.execute();
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
                     //Add data to array list
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
	  
	    		SQL.addTeam(new Team(AbbreviationList.get(0), FullNameList.get(0), CityList.get(0),StateList.get(0), ConferenceList.get(0), DivisionList.get(0), ArenaList.get(0)));
	    	
	    	List<Team> teams = SQL.getTeams();      
	         
	      
	        Log.d("DATA IN SQLite: ", teams.toString());
        
	}
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
