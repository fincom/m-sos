//
//  FirstViewController.m
//  tabex
//
//  Created by Shannon Appelcline on 6/19/08.
//  Copyright __MyCompanyName__ 2008. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AlertViewController.h"
#import "SOS_IphoneAppDelegate.h"
#import "Alert.h"


@implementation AlertViewController

@synthesize alertType;
@synthesize sharedLocation;
@synthesize alertManager;
@synthesize alertType;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
		// Initialization code
	}
	return self;
}

 - (void)viewDidLoad {
 
	//Gestion du click bouton
	[alertButton addTarget:self action:@selector(setTypeAlert:)  
		  forControlEvents:UIControlEventTouchDown]; 
	[accidentButton addTarget:self action:@selector(setTypeAccident:) 
		forControlEvents:UIControlEventTouchDown]; 
	[fireButton addTarget:self action:@selector(setTypeFire:) 
		forControlEvents:UIControlEventTouchDown]; 
	[personButton addTarget:self action:@selector(setTypePerson:) 
		forControlEvents:UIControlEventTouchDown];
	 
	//Gestion de la localisation
	id appDelegate = [UIApplication sharedApplication].delegate;
	sharedLocation = [appDelegate sharedLocation];
	
	//Alert Manager
	alertManager = [appDelegate alertManager];
	 
	//IS Victime
	isVictime = FALSE;

 }

- (void)setTypeAlert:(id) sender { 
	self.alertType = @"DOMESTIC";
	[self preSendAlert];
}
- (void)setTypeAccident:(id) sender {    
	self.alertType = @"ACCIDENT";
	[self preSendAlert];
}
- (void)setTypeFire:(id) sender {    
	self.alertType = @"FIRE";
	[self preSendAlert];
}
- (void)setTypePerson:(id) sender {    
	self.alertType = @"SANTE";
	[self preSendAlert];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	
	//On gère le cas d'nne victime
	if(buttonIndex +1 == 1){
		isVictime = TRUE;
	}
	
	[self sendAlert];
}

- (void)preSendAlert {   
	
	//Type d'applelant
	UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Mise en contact" message:@"Vous êtes le ?" delegate:self cancelButtonTitle:@"Malade" otherButtonTitles:@"Témoin", nil];
	[alertView show];
	[alertView release];
	
}

- (void)sendAlert {   
	
	//On crée une nouvelle alerte
	Alert *alert = [[Alert alloc] init];
	alert.alertType = self.alertType;
	
	//On l'envoit
	[alertManager sendAlert:alert isVictime:isVictime];
	
	//On remet le who call à nil
	isVictime = FALSE;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	// Return YES for supported orientations
	return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
	// Release anything that's not essential, such as cached data
}


- (void)dealloc {
	[super dealloc];
}

@end
