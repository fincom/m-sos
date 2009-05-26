//
//  DeviceManager.m
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "DeviceManager.h"


@implementation DeviceManager

-(NSString *)getUniqueID{
	return [[UIDevice currentDevice] uniqueIdentifier];
}

-(NSString *)getPhoneNumber{
	return [[NSUserDefaults standardUserDefaults] stringForKey:@"SBFormattedPhoneNumber"];
}

-(NSString *)getCurrentLanguage{
	NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
	
	NSArray *languages = [defaults objectForKey:@"AppleLanguages"];
	NSString *currentLanguage = [languages objectAtIndex:0];
	
	return currentLanguage;
}

@end
