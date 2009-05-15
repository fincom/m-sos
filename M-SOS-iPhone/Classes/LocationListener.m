//
//  LocationListener.m
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 M_SOS. All rights reserved.
//

#import "LocationListener.h"
#import "HTTPServices.h";
#import "SBJSON.h"

static LocationListener *sharedMyLocation = nil;


@implementation LocationListener
@synthesize placemark;
@synthesize geoCoder;

//Listen to location change and updating location
- (id) init
{
	self = [super init];
	
	if(self)
	{
		//Start updating location
		CLLocationManager *locationManager=[[CLLocationManager alloc] init];
		locationManager.delegate=self;
		locationManager.desiredAccuracy=kCLLocationAccuracyNearestTenMeters;
		
		[locationManager startUpdatingLocation];
	}
	
	return self;
}

//Set new location coordinates
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation{
	
	location = newLocation.coordinate;
	geoCoder = [[LocationReverseGeoCoder alloc] initWithLocation:location];
	
	//Post notification for current location
	[[NSNotificationCenter defaultCenter] 
		postNotificationName:@"set_NewLocation" object:newLocation];
	NSLog(@"Notification envoyé");

}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
}

//Get current Location
//@return location
- (CLLocationCoordinate2D)getCurrentLocation{
	return location;
}

//Get current location longitude
- (Float32)getCurrentLongitude{
	return location.longitude;
}

//Get current location latitude
- (Float32)getCurrentLatitude{
	return location.latitude;
}


//Get Current Placemark
- (MKPlacemark *)getCurrentPlacemark{
	return geoCoder.placemark;
}

//Get Current City
- (NSString *)getCurrentCity{
	NSLog(geoCoder.placemark.countryCode);
	return geoCoder.placemark.locality;
}

//Get Placemark for a given location
- (MKPlacemark *)getPlacemarkByLocation:(CLLocationCoordinate2D) pLocation{
	LocationReverseGeoCoder* rGeoCode = [[LocationReverseGeoCoder alloc] initWithLocation:pLocation]; 
	return rGeoCode.placemark;
}

//Get Available Services in current Location
- (NSArray *)getServicesForCategory:(NSString *) category{
	NSString *query = [[NSString alloc] 
						initWithFormat:@"category:%@", 
						category
					   ];
	return [self getServicesFor:query];
}

//Get Available Services in current Location
- (NSArray *)getServicesForKeyword:(NSString *) keyword{
	return [self getServicesFor:keyword];
}

//Get Available Services in current Location
- (NSArray *)getServicesFor:(NSString *) query{
	
	NSString *url = 
	[
	 [NSString alloc] 
	 initWithFormat:@"http://ajax.googleapis.com/ajax/services/search/local?v=1.0&q='%@'&sll=%f,%f&rsz=large&start=0", 
	 query, location.latitude, location.longitude
	 ];
	
    HTTPServices *httpservice = [[HTTPServices alloc] sendHTTPRequest:url httpMethod:@"GET" requestString:nil];
	
	if(![httpservice hasError]){
		
		NSString *jsonString = [[NSString alloc] initWithData:httpservice.dataReply encoding:NSUTF8StringEncoding];
		SBJSON *json = [[SBJSON alloc] init];
		NSError* errorf = nil;
		
		id responseD = [json objectWithString:jsonString error:&errorf];
		NSDictionary *responseDD = (NSDictionary *)responseD;
		
		NSDictionary *responseData = (NSDictionary *)[responseDD valueForKey:@"responseData"];
		
		
		NSArray *results = (NSArray *)[responseData valueForKey:@"results"];
		
		return results;		
		
		[json release];
		[jsonString release];
		
	}else{
		return nil;
	}
}

/*
* Singleton methods 
*/
+ (id)sharedLocation {
	@synchronized(self) {
		if(sharedMyLocation == nil) 
			[[self alloc] init];
	}
	return sharedMyLocation;
}

+ (id)allocWithZone:(NSZone *)zone {
	@synchronized(self) {
		if(sharedMyLocation == nil)  {
			sharedMyLocation = [super allocWithZone:zone];
			return sharedMyLocation;
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
