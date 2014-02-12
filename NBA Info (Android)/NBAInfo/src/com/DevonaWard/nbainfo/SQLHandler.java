package com.DevonaWard.nbainfo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHandler extends SQLiteOpenHelper {
 
    
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    //Database Name
    private static final String DATABASE_NAME = "TeamInfo";
 
    //Table name
    private static final String TABLE_TEAMS = "Teams";
 
    //Table Columns names
    private static final String _ID = "id";
    private static final String TEAM_NAME = "name";
    private static final String TEAM_ARENA = "arena";
    private static final String TEAM_DIVISION = "division";
    private static final String TEAM_CONFERENCE = "conference";
    private static final String TEAM_STATE = "state";
    private static final String TEAM_CITY = "city";
    private static final String TEAM_ABBREVIATION = "abbreviation";
 
    public SQLHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    //Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TEAMINFO_TABLE = "CREATE TABLE " + TABLE_TEAMS + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TEAM_ABBREVIATION + " TEXT,"
                + TEAM_NAME + " TEXT," + TEAM_STATE + "TEXT," + TEAM_CITY + "TEXT," + TEAM_DIVISION + "TEXT,"
                + TEAM_CONFERENCE + "TEXT," + TEAM_ARENA + "TEXT" +")";
        db.execSQL(CREATE_TEAMINFO_TABLE);
    }
 
    //Upgrade table
    @Override
    public void onUpgrade(SQLiteDatabase SQLiteDB, int oldVersion, int newVersion) {
        // Drop older table if existed
        SQLiteDB.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
 
        //Upgrade with new table
        onCreate(SQLiteDB);
    }
    
    //Add team
    public void addTeam(Team team) {
    	SQLiteDatabase SQLiteDB = this.getWritableDatabase();
    	 
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_ABBREVIATION, team.getAbbreviation());
        contentValues.put(TEAM_NAME, team.getName());
        contentValues.put(TEAM_CITY, team.getCity());
        contentValues.put(TEAM_STATE, team.getState());
        contentValues.put(TEAM_DIVISION, team.getDivision());
        contentValues.put(TEAM_CONFERENCE, team.getConference());
        contentValues.put(TEAM_ARENA, team.getArena());
     
        //Insert data to row
        SQLiteDB.insert(TABLE_TEAMS, null, contentValues);
        //Close SQL DB
        SQLiteDB.close();
    }
     
    // Get a team
    public Team getTeam(int ID) {
    	SQLiteDatabase SQLiteDB = this.getReadableDatabase();
    	 
        Cursor cursor = SQLiteDB.query(TABLE_TEAMS, new String[] { _ID,
                TEAM_ABBREVIATION, TEAM_NAME,TEAM_CITY,TEAM_STATE,TEAM_DIVISION,TEAM_CONFERENCE,TEAM_ARENA }, _ID + "=?",
                new String[] { String.valueOf(ID) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
     
        Team team = new Team(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), 
                cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return team
        return team;
    }
     
    // Get All Teams
    public List<Team>getTeams() {
    	    List<Team> nbaList = new ArrayList<Team>();
    	    
    	    // Select All Query
    	    String selectQuery = "SELECT  * FROM " + TABLE_TEAMS;
    	 
    	    SQLiteDatabase SQLiteDB = this.getWritableDatabase();
    	    Cursor cursor = SQLiteDB.rawQuery(selectQuery, null);
    	 
    	    //Looping through rows and populating list
    	    if (cursor.moveToFirst()) {
    	        do {
    	            Team team = new Team();
    	            team.setID(Integer.parseInt(cursor.getString(0)));
    	            team.setAbbreviation(cursor.getString(1));
    	            team.setName(cursor.getString(2));
    	            team.setCity(cursor.getString(3));
    	            team.setState(cursor.getString(4));
    	            team.setDivision(cursor.getString(5));
    	            team.setConference(cursor.getString(6));
    	            team.setArena(cursor.getString(7));
    	            
    	            // Add team to list
    	            nbaList.add(team);
    	        } while (cursor.moveToNext());
    	    }
    	 
    	    // return NBA List
    	    return nbaList;
    }
     
    //Get team count
    public int getNumberTeams() {
        String countQuery = "SELECT  * FROM " + TABLE_TEAMS;
        SQLiteDatabase SQLiteDB = this.getReadableDatabase();
        Cursor cursor = SQLiteDB.rawQuery(countQuery, null);
        cursor.close();
 
        //return count
        return cursor.getCount();
    }
    
    //Update a team
    public int updateTeam(Team team) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_ABBREVIATION, team.getAbbreviation());
        contentValues.put(TEAM_NAME, team.getName());
        contentValues.put(TEAM_CITY, team.getCity());
        contentValues.put(TEAM_STATE, team.getState());
        contentValues.put(TEAM_DIVISION, team.getDivision());
        contentValues.put(TEAM_CONFERENCE, team.getConference());
        contentValues.put(TEAM_ARENA, team.getArena());    
        // updating row
        return db.update(TABLE_TEAMS, contentValues, _ID + " = ?",
                new String[] { String.valueOf(team.getID()) });
    }
     
    //Delete a team
    public void deleteTeam(Team team) {
    	SQLiteDatabase SQLiteDB = this.getWritableDatabase();
        SQLiteDB.delete(TABLE_TEAMS, _ID + " = ?",
                new String[] { String.valueOf(team.getID()) });
        SQLiteDB.close();
    }
}