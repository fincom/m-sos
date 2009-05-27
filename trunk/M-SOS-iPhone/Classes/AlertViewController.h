//
//  FirstViewController.h
//  tabex
//
//  Created by Shannon Appelcline on 6/19/08.
//  Copyright __MyCompanyName__ 2008. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "LocationListener.h"
#import "AlertListener.h"
#import "HTTPServices.h"

@interface AlertViewController : UIViewController <UIAlertViewDelegate> {
	IBOutlet UIButton* alertButton;
	IBOutlet UIButton* personButton;
	IBOutlet UIButton* fireButton;
	IBOutlet UIButton* accidentButton;
	
	LocationListener* sharedLocation;
	AlertListener* alertManager;
	
	Boolean isVictime;
	NSString* alertType;
	
}

@property(nonatomic, retain) NSString* alertType;
@property(nonatomic, retain) LocationListener* sharedLocation;
@property(nonatomic, retain) AlertListener* alertManager;

- (void)setTypeAlert:(id) sender;
- (void)setTypeAccident:(id) sender;
- (void)setTypeFire:(id) sender;
- (void)setTypePerson:(id) sender;

- (void)sendAlert;
- (void)preSendAlert;

@end
