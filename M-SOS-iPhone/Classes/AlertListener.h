//
//  AlertListener.h
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LocationListener.h"
#import "Alert.h";
#import "DeviceManager.h"

@interface AlertListener : NSObject <UIAlertViewDelegate> {
	
	LocationListener* sharedLocation;
	DeviceManager *mobile;
	Alert *alert;
	
	Boolean isVictime;
}

@property (nonatomic, retain) LocationListener* sharedLocation;
@property (nonatomic, retain) DeviceManager *mobile;
@property (nonatomic, retain) Alert *alert;
@property Boolean isVictime;

+ (id)alertManager;

-(void)sendAlert:(Alert *)alert isVictime:(Boolean)whoCall;
-(void)sendAlertToServer;
-(void)sendAlertToJSONServer;
-(void)sendTwittAlert;
-(NSString *)getAlertTypeCategory:(NSInteger) alertType;

-(NSString *) getEmergencyNumber;
-(void) doCall;

@end
