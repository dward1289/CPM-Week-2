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
    
    divisionsItems = (NSArray*)@[@"--Select A Division--",@"Southeast",@"Atlantic",@"Central",@"Southwest",@"Northwest",@"Pacific"];
    
    //Get teams info
    NSData *nbaData = [[NSData alloc] initWithContentsOfURL:[NSURL URLWithString:@"https://erikberg.com/nba/teams.json"]];
    NSError *theError;
    theNBAInfo = [NSJSONSerialization JSONObjectWithData:nbaData options:kNilOptions error:&theError];
    
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
    SQLPath = [[NSString alloc]initWithString:[DocumentsDirectory stringByAppendingPathComponent:@"NBATeams.sqlite3"]];
    
    //Create SQLite table
    NSFileManager *FileManager = [NSFileManager defaultManager];
    if([FileManager fileExistsAtPath:SQLPath] == NO){
        const char *DatabasePath = [SQLPath UTF8String];
        if(sqlite3_open(DatabasePath, &SQLinfo) == SQLITE_OK){
            char *ErrorMessage;
            const char *SQL_STATMENT = "CREATE TABLE IF NOT EXISTS TeamInfo (ID INTEGER PRIMARY KEY AUTOINCREMENT, ABBREVIATION TEXT, FULL_NAME TEXT, CITY TEXT, STATE, DIVISION TEXT, CONFERENCE TEXT, SITE_NAME TEXT)";
            
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
        teamName = [nbaDictionary objectForKey:@"full_name"];
        teamArena = [nbaDictionary objectForKey:@"site_name"];
        teamAbbreviation = [nbaDictionary objectForKey:@"abbreviation"];
        teamCity = [nbaDictionary objectForKey:@"city"];
        teamState = [nbaDictionary objectForKey:@"state"];
        teamDivision = [nbaDictionary objectForKey:@"division"];
        teamConference = [nbaDictionary objectForKey:@"conference"];
        
        sqlite3_stmt *SQLStatement;
        const char *DatabasePath = [SQLPath UTF8String];
        
        if (sqlite3_open(DatabasePath, &SQLinfo) == SQLITE_OK) {
            NSString *insertSQL = [NSString stringWithFormat:
                                   @"INSERT INTO TeamInfo (ABBREVIATION,FULL_NAME,CITY,STATE,DIVISION,CONFERENCE,SITE_NAME) VALUES (\"%@\",\"%@\", \"%@\", \"%@\",\"%@\", \"%@\", \"%@\")",
                                   teamAbbreviation, teamName,teamCity,teamState,teamDivision,teamConference,teamArena];
            
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

//Populate picker view with Divisions.
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [divisionsItems count];
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    
    return [divisionsItems objectAtIndex:row];
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    selectedDivision = [divisionsItems objectAtIndex:row];
    
    NSLog(@"%@", selectedDivision);
    
}

//Get the data from SQLite database
-(void)dataToQuery{
    theTeams = [[NSMutableArray alloc] init];
    //Get SQLite file
    const char *DatabasePath = [SQLPath UTF8String];
    sqlite3_stmt *SQLStatement;
    
    if (sqlite3_open(DatabasePath, &SQLinfo) == SQLITE_OK)
    {
        
        //String from East West selector
        EWTxt = [NSString stringWithFormat: @"%@",[EWSelect titleForSegmentAtIndex:EWSelect.selectedSegmentIndex]];
        
        NSString *querySQL = [NSString stringWithFormat:@"SELECT * FROM TeamInfo WHERE CONFERENCE='%@' AND DIVISION='%@'",EWTxt,selectedDivision];
        
        const char *query_stmt = [querySQL UTF8String];
        
        if (sqlite3_prepare_v2(SQLinfo,query_stmt, -1, &SQLStatement, NULL) == SQLITE_OK)
        {
            while(sqlite3_step(SQLStatement) == SQLITE_ROW)
            {
    
                ABBREVIATION = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(SQLStatement, 1)];
                FULLNAME = [[NSString alloc]initWithUTF8String:(const char *) sqlite3_column_text(SQLStatement, 2)];
                CITY = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(SQLStatement, 3)];
                STATE = [[NSString alloc]initWithUTF8String:(const char *) sqlite3_column_text(SQLStatement, 4)];
                DIVISION = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(SQLStatement, 5)];
                CONFERENCE = [[NSString alloc]initWithUTF8String:(const char *) sqlite3_column_text(SQLStatement, 6)];
                SITENAME = [[NSString alloc]initWithUTF8String:(const char *) sqlite3_column_text(SQLStatement, 7)];
                
                //Get data retrieved from SQLite DB Query
                theInfo = [[NSString alloc]initWithFormat:@"(%@) %@ \r %@, %@ \r Divison: %@ \r Conference: %@ \r Arena: %@",ABBREVIATION,FULLNAME,CITY,STATE,DIVISION,CONFERENCE,SITENAME];
                
                [theTeams addObject:theInfo];
                
            }
            sqlite3_finalize(SQLStatement);
        }
        sqlite3_close(SQLinfo);
    }
    //Reload list with query data
    [teamsList reloadData];
    NSLog(@"%@",[theTeams description]);
}

//Populate table view with query
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [theTeams count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"SimpleTableItem";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
        cell.textLabel.numberOfLines = 0;
        cell.textLabel.font = [UIFont fontWithName:@"Helvetica" size:12.0];
    }
    
    cell.textLabel.text = [theTeams objectAtIndex:indexPath.row];
    return cell;
}

//Begin query function
-(IBAction)onQuery:(id)sender{
    [self dataToQuery];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
