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
    NSArray *divisionsItems;
    NSString *SQLPath;
    NSString *EntryAddedStatus;
    sqlite3 *SQLinfo;

}
@end
