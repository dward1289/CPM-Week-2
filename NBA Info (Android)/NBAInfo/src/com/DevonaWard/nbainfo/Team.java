package com.DevonaWard.nbainfo;

public class Team {
	
    int _id;
    String _fullname;
    String _city;
    String _state;
    String _conference;
    String _division;
    String _arena;
    String _abbreviation;
     
    //Empty constructor
    public Team(){
         
    }
    //Constructor with ID
    public Team(int id, String abbreviation, String fullname, String city, String state, String conference, String division, String arena){
        this._id = id;
        this._fullname = fullname;
        this._city = city;
        this._state = state;
        this._conference = conference;
        this._division = division;
        this._arena = arena;
        this._abbreviation = abbreviation;
    }
     
    //Constructor without ID
    public Team(String abbreviation, String fullname, String city, String state, String conference, String division, String arena){
    	this._fullname = fullname;
        this._city = city;
        this._state = state;
        this._conference = conference;
        this._division = division;
        this._arena = arena;
        this._abbreviation = abbreviation;
    }
    //Get ID
    public int getID(){
        return this._id;
    }
     
    //Set ID
    public void setID(int id){
        this._id = id;
    }
     
    //Get Team Name
    public String getName(){
        return this._fullname;
    }
     
    //Set Team Name
    public void setName(String fullname){
        this._fullname = fullname;
    }
     
    //Get Team City
    public String getCity(){
        return this._city;
    }
     
    //Set Team City
    public void setCity(String city){
        this._city = city;
    }
    
    //Get Team State
    public String getState(){
        return this._state;
    }
     
    //Set Team State
    public void setState(String state){
        this._state = state;
    }
    
    //Get Team Abbreviation
    public String getAbbreviation(){
        return this._abbreviation;
    }
     
    //Set Team Abbreviation
    public void setAbbreviation(String abbreviation){
        this._abbreviation = abbreviation;
    }
    
    //Get Team Conference
    public String getConference(){
        return this._conference;
    }
     
    //Set Team Conference
    public void setConference(String conference){
        this._conference = conference;
    }
    
    //Get Team Division
    public String getDivision(){
        return this._division;
    }
     
    //Set Team Division
    public void setDivision(String division){
        this._division = division;
    }
    
    //Get Team Arena
    public String getArena(){
        return this._arena;
    }
     
    //Set Team Arena
    public void setArena(String arena){
        this._arena = arena;
    }

}
