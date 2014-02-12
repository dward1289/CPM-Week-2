//
//  ViewController.h
//  NBA Info
//
//  Created by Devona Ward on 2/9/14.
//  Copyright (c) 2014 Devona Ward. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <sqlite3.h>

@interface ViewController : UIViewController
{
    IBOutlet UIPickerView *divisionsList;
    IBOutlet UITableView *teamsList;
    IBOutlet UIButton *beginQuery;
    IBOutlet UISegmentedControl *EWSelect;
    NSArray *theNBAInfo;
    NSString *selectedDivision;
    NSArray *divisionsItems;
    NSString *SQLPath;
    NSString *EntryAddedStatus;
    sqlite3 *SQLinfo;
    NSMutableArray *theTeams;
    NSString* teamName;
    NSString* teamArena;
    NSString* teamAbbreviation;
    NSString* teamCity;
    NSString* teamState;
    NSString* teamDivision;
    NSString* teamConference;
    NSString *ABBREVIATION;
    NSString *FULLNAME;
    NSString *CITY;
    NSString *STATE;
    NSString *DIVISION;
    NSString *CONFERENCE;
    NSString *SITENAME;
    NSString *EWTxt;
    

}

-(IBAction)onQuery:(id)sender;
-(void)dataToQuery;
@end
