//
//  LocationReverseGeoCoder.m
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LocationReverseGeoCoder.h"


@implementation LocationReverseGeoCoder
@synthesize placemark;

- (id) init
{
	self = [super init];
	
	return self;
}

- (id) initWithLocation:(CLLocationCoordinate2D) location
{
	self = [super init];
	
	if(self)
	{
		MKReverseGeocoder* rGeoCode = [[MKReverseGeocoder alloc] initWithCoordinate:location]; 
		rGeoCode.delegate = self;
		[rGeoCode start];
	}
	
	return self;
}

//Reverse Coder for lacalisation
- (void)reverseGeocoder:(MKReverseGeocoder *)geocoder didFindPlacemark:(MKPlacemark *)placemarks{
	self.placemark = placemarks;
}

- (void)reverseGeocoder:(MKReverseGeocoder *)geocoder didFailWithError:(NSError *)error{
	NSLog(error.localizedDescription);
}


@end
