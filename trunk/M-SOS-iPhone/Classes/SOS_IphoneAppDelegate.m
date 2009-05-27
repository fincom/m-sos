//
//  SOS_IphoneAppDelegate.m
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/7/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "SOS_IphoneAppDelegate.h"


@implementation SOS_IphoneAppDelegate

@synthesize window;
@synthesize tabBarController;
@synthesize signupView;
@synthesize sharedLocation;
@synthesize alertManager;

- (void)applicationDidFinishLaunching:(UIApplication *)application {
	//User default setting
	NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
	
	//LocationListener & Alert Manager singleton
	self.sharedLocation = [LocationListener sharedLocation];
	self.alertManager = [AlertListener alertManager];
	
	//Signup view Controller
	signupView = [[SignupControllerView alloc] initWithNibName:@"SignupControllerView" bundle:nil];
	
	//user has signup?
	if([userDefaults boolForKey:@"userRegistered"] == FALSE)
	{
		//Are information completed ?
		if([signupView verifSettings:userDefaults] == FALSE)
		{
			//Invite user to fill the setting
			[window addSubview:signupView.view];
		}else{
		    [signupView signup:userDefaults];
		}
	}else{
		//Save Settings
		[signupView update:userDefaults];
		
		//init MSOS
		[self launchMSOS:TRUE];
	}
}

-(void)launchMSOS: (BOOL) isRegistered {
	// Add the tab bar controller's current view as a subview of the window
	[window addSubview:tabBarController.view];
}

/*
// Optional UITabBarControllerDelegate method
- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
}
*/

/*
// Optional UITabBarControllerDelegate method
- (void)tabBarController:(UITabBarController *)tabBarController didEndCustomizingViewControllers:(NSArray *)viewControllers changed:(BOOL)changed {
}
*/


- (void)dealloc {
	[window release];
    [super dealloc];
}

@end

