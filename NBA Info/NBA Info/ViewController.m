//
//  ViewController.m
//  NBA Info
//
//  Created by Devona Ward on 2/9/14.
//  Copyright (c) 2014 Devona Ward. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    
    divisionsItems = (NSArray*)@[@"Southeast",@"Atlantic",@"Central",@"Southwest",@"Northwest",@"Pacific"];
    
    //Get teams info
    NSData *nbaData = [[NSData alloc] initWithContentsOfURL:[NSURL URLWithString:@"https://erikberg.com/nba/teams.json"]];
    NSError *theError;
    NSArray *theNBAInfo = [NSJSONSerialization JSONObjectWithData:nbaData options:kNilOptions error:&theError];
    
    if(theError)
    {
        NSLog(@"THERE WAS AN ERROR %@", [theError localizedDescription]);
    }
    else {
        NSLog(@"DATA IS HERE");
    }
    
    //Documents directory
    NSArray *theDirectoryPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *DocumentsDirectory = theDirectoryPaths[0];
    
    //Path of database file created
    SQLPath = [[NSString alloc]initWithString:[DocumentsDirectory stringByAppendingPathComponent:@"NBATeams.db"]];
    
    //Create SQLite table
    NSFileManager *FileManager = [NSFileManager defaultManager];
    if([FileManager fileExistsAtPath:SQLPath] == NO){
        const char *DatabasePath = [SQLPath UTF8String];
        if(sqlite3_open(DatabasePath, &SQLinfo) == SQLITE_OK){
            char *ErrorMessage;
            const char *SQL_STATMENT = "CREATE TABLE IF NOT EXISTS TeamInfo (ID INTEGER PRIMARY KEY AUTOINCREMENT, ABBREVIATION TEXT, FULL_NAME TEXT, CITY TEXT, STATE, DIVISION_ID INTEGER, CONFERENCE_ID INTEGER, SITE_NAME TEXT)";
            
            if(sqlite3_exec(SQLinfo, SQL_STATMENT, NULL,NULL, &ErrorMessage)!= SQLITE_OK){
                EntryAddedStatus = @"Unable to create table.";
                NSLog(@"%@",EntryAddedStatus);
            }
            else{
                EntryAddedStatus = @"Successfully created table.";
                NSLog(@"%@",EntryAddedStatus);
            }
            sqlite3_close(SQLinfo);
        }else{
            EntryAddedStatus = @"Unable to open/create SQLite database.";
            NSLog(@"%@",EntryAddedStatus);
        }
    }
    
    //Add data to SQLite Database
    for(NSUInteger i = 0; i < 30; i++){
        NSDictionary* nbaDictionary = [theNBAInfo objectAtIndex:i];
        NSString* teamName = [nbaDictionary objectForKey:@"full_name"];
        NSString* teamArena = [nbaDictionary objectForKey:@"site_name"];
        NSString* teamAbbreviation = [nbaDictionary objectForKey:@"abbreviation"];
        NSString* teamCity = [nbaDictionary objectForKey:@"city"];
        NSString* teamState = [nbaDictionary objectForKey:@"state"];
        NSString* teamDivision = [nbaDictionary objectForKey:@"division"];
        NSString* teamConference = [nbaDictionary objectForKey:@"conference"];
        
        sqlite3_stmt *SQLStatement;
        const char *DatabasePath = [SQLPath UTF8String];
        
        if (sqlite3_open(DatabasePath, &SQLinfo) == SQLITE_OK) {
            NSString *insertSQL = [NSString stringWithFormat:
                                   @"INSERT INTO TeamInfo (ABBREVIATION) VALUES (\"%@\")",
                                   teamAbbreviation];
            
            const char*insert_stmt = [insertSQL UTF8String];
            sqlite3_prepare_v2(SQLinfo, insert_stmt,  -1, &SQLStatement, NULL);
            if (sqlite3_step(SQLStatement) == SQLITE_DONE) {
                NSLog(@"Data entry successful.");
            } else {
                NSLog(@"Data entry failed.");
            }
            sqlite3_finalize(SQLStatement);
            sqlite3_close(SQLinfo);
        }
    }
    
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
}
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1; // For one column
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [divisionsItems count]; // Numbers of rows
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    return [divisionsItems objectAtIndex:row]; // If it's a string
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
