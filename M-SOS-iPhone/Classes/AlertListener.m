//
//  AlertListener.m
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "AlertListener.h"
#import "HTTPServices.h"


static LocationListener *sharedMyAlert = nil;

@implementation AlertListener

@synthesize sharedLocation;
@synthesize mobile;
@synthesize alert;

@synthesize isVictime;

- (id) init
{
	self = [super init];
	
	if(self)
	{
		//Start updating location
		id appDelegate = [UIApplication sharedApplication].delegate;
		sharedLocation = [appDelegate sharedLocation];
		
		//Device Manager init
		mobile = [[DeviceManager alloc] init];
		
		//Initialisation isVictime
		isVictime = FALSE;
		
		//Init Alert to nil
		alert = nil;
	}
	
	return self;
}

-(void)sendAlert:(Alert *)alertToSend isVictime:(Boolean)whoCall{
	
	self.isVictime = whoCall;
	self.alert = alertToSend;
	
	NSString *uaMessage = [[NSString alloc] 
						   initWithFormat:@"Vous allez être mis en relation avec le %@", 
						   [self getEmergencyNumber]
						   ];
	
	UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"M-SOS" message:uaMessage delegate:self cancelButtonTitle:@"Annuler" otherButtonTitles:@"Confirmer", nil];
	[alertView show];
	[alertView release];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	
	//On gère le cas d'nne victime
	if(buttonIndex +1 == 2){
		[self sendAlertToJSONServer];
		//[self sendTwittAlert];
		//[self doCall];
	}else{
		alert = nil;
	}
}

//Call Emergency
-(void) doCall{
	
   NSString *emergencyNumber = [[NSString alloc] 
						   initWithFormat:@"tel://%@", 
						   [self getEmergencyNumber]
						   ];
   
   [[UIApplication sharedApplication] openURL:[NSURL URLWithString:emergencyNumber]];
	
}

//Get Emergency number depending of alert Type
-(NSString *) getEmergencyNumber{
	NSString *emergencyNumber;
	
	if(alert.alertType == 1){
		emergencyNumber = @"18";
	}else if(alert.alertType == 2){
		emergencyNumber = @"15";
	}else if(alert.alertType == 3){
		emergencyNumber = @"18";
	}else if(alert.alertType == 4){
		emergencyNumber = @"15";
	}else{
		emergencyNumber = @"112";
	}
	
	MKPlacemark *currentPlaceMark = [sharedLocation getCurrentPlacemark];
	if([currentPlaceMark.countryCode compare:@"FR"]){
		NSLog(@"true");
		emergencyNumber = @"112";
	}
	
	NSLog(currentPlaceMark.countryCode);
	
	return emergencyNumber;
}

//Send Twitter notification to someone :)
-(void)sendTwittAlert{
	if([[NSUserDefaults standardUserDefaults] boolForKey:@"sms_notify_enabled"] == TRUE){
		NSString* twittProche = [[NSUserDefaults standardUserDefaults] stringForKey:@"twitter_other"];
		
		//MSOS webservice Alert url
		NSString* url = @"http://sos.geeek.org/twitter.php";
		
		NSString *requestString;
		requestString = 
		[
		 [NSString alloc] 
		 initWithFormat:@"&twittId=%@&imei=%@", 
		 twittProche, [mobile getUniqueID]
		 ];
		
		NSLog(requestString);
		
		HTTPServices *httpservice = [[HTTPServices alloc] sendHTTPRequest:url httpMethod:@"POST" requestString:requestString];
		if([httpservice hasError]){
			NSLog(@"La requête n'a pu aboutir");
		}
		
	}
}

-(void)sendAlertToJSONServer{
		
		//Récupération des coordonnées GPS
		CLLocationCoordinate2D locationde = [sharedLocation getCurrentLocation];
	
	NSLog(@"%f,%f,%@", locationde.latitude, locationde.longitude, [mobile getUniqueID]);
		
		NSString *longitude = 
		[
		 [NSString alloc] 
		 initWithFormat:@"%f", 
		 locationde.longitude
		 ];
		
		NSString *latitude = 
		[
		 [NSString alloc] 
		 initWithFormat:@"%f", 
		 locationde.latitude
		 ];
	
		NSString *alert_type = 
		[
		 [NSString alloc] 
		 initWithFormat:@"%d", 
		 alert.alertType
		 ];
	
		NSString *service = @"createAlert";
		
		NSString *keyArray[4];
		NSString *valueArray[4];
		
		keyArray[0] = @"alertType";
		valueArray[0] = alert_type;
		keyArray[1] = @"uniqueId";
		valueArray[1] = [mobile getUniqueID];
		keyArray[2] = @"latitude";
		valueArray[2] = latitude;
		keyArray[3] = @"longitude";
		valueArray[3] = longitude;
	
		
		NSMutableDictionary *params = [NSDictionary dictionaryWithObjects:(id *)valueArray
											forKeys:(id *)keyArray count:4];
	
		NSArray *keys = [params allKeys];
		for (id value in keys) {
			NSLog(value);
		}
	
		NSString *url = @"http://www.m-sos.com/json/alert";
		
		HTTPServices *httpservice = [[HTTPServices alloc] sendJSONRequest:url httpMethod:@"POST" service:service params:params];
		if([httpservice hasError]){
			NSLog(@"La requête n'a pu aboutir");
		}
}

-(void)sendAlertToServer{
	
	//Récupération des coordonnées GPS
	CLLocationCoordinate2D locationde = [sharedLocation getCurrentLocation];
	
	//MSOS webservice Alert url
	NSString* url = @"http://sos.geeek.org/alert.php";
	
	NSString* locality;
	
	if([[sharedLocation getCurrentCity] isKindOfClass:[NSString class]]){
		locality = [sharedLocation getCurrentCity];
	}else{
		
		locality = @"Inconnue";
	}
	
	NSString *requestString;
	
	requestString = 
	[
	 [NSString alloc] 
	 initWithFormat:@"&latitude=%f&longitude=%f&imei=%@&city=%@&msisdn=%@&alertType=%d", 
	 locationde.latitude, locationde.longitude, [mobile getUniqueID], locality, [mobile getPhoneNumber], alert.alertType
	 ];
	
	NSLog(requestString);
	
	HTTPServices *httpservice = [[HTTPServices alloc] sendHTTPRequest:url httpMethod:@"POST" requestString:requestString];
	if([httpservice hasError]){
		NSLog(@"La requête n'a pu aboutir");
	}
}

//
-(NSString *)getAlertTypeCategory:(NSInteger) alertType
{
	NSString *category;
	//Cas de la catégorie pour l'alerte courante
	if(alertType == (NSInteger)nil && alert != nil)
	{
		alertType = alert.alertType;
	}
	
	if(alertType != (NSInteger)nil)
	{
		//Cas D'un alertType connu
		if(alert.alertType == 1){
			category = @"Pompiers";
		}else if(alert.alertType == 2){
			category = @"Hospitals";
		}else if(alert.alertType == 3){
			category = @"Pompiers";
		}else if(alert.alertType == 4){
			category = @"Pompiers";
		}
		
		return category;
	}
	else
	{
		return nil;
	}
}
/*
 * Singleton methods 
 */
+ (id)alertManager {
	@synchronized(self) {
		if(sharedMyAlert == nil) 
			[[self alloc] init];
	}
	return sharedMyAlert;
}

+ (id)allocWithZone:(NSZone *)zone {
	@synchronized(self) {
		if(sharedMyAlert == nil)  {
			sharedMyAlert = [super allocWithZone:zone];
			return sharedMyAlert;
		}
	}
	return nil;
}

- (id)copyWithZone:(NSZone *)zone {
	return self;
}

- (id)retain {
	return self;
}

- (unsigned)retainCount {
	return UINT_MAX; //denotes an object that cannot be released
}

- (void)release {
	// never release
}

- (id)autorelease {
	return self;
}

@end
