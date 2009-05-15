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
@synthesize sharedLocation;
@synthesize alertManager;

- (void)applicationDidFinishLaunching:(UIApplication *)application {
    //LocationListener singleton
	self.sharedLocation = [LocationListener sharedLocation];
	self.alertManager = [AlertListener alertManager];
	
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
    [tabBarController release];
    [window release];
    [super dealloc];
}

@end

