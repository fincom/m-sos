//
//  SOS_IphoneAppDelegate.h
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/7/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LocationListener.h"
#import "AlertListener.h"
#import "SignupControllerView.h"

@interface SOS_IphoneAppDelegate : NSObject <UIApplicationDelegate, UITabBarControllerDelegate> {
    UIWindow *window;
    UITabBarController *tabBarController;
	SignupControllerView *signupView;
	LocationListener *sharedLocation;
	AlertListener *alertManager;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UITabBarController *tabBarController;
@property (nonatomic, retain) SignupControllerView *signupView;

@property (nonatomic, retain) LocationListener* sharedLocation;
@property (nonatomic, retain) AlertListener *alertManager;

-(void)launchMSOS: (BOOL) isRegistered ;

@end
