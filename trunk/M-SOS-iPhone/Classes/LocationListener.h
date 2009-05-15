//
//  LocationListener.h
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 M-SOS. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import "LocationReverseGeoCoder.h"
#import <MapKit/MKPlacemark.h>

@interface LocationListener : NSObject <CLLocationManagerDelegate> {
	CLLocationCoordinate2D location;
	MKPlacemark* placemark;
	LocationReverseGeoCoder* geoCoder;
}

@property (nonatomic, retain) MKPlacemark* placemark;
@property (nonatomic, retain) LocationReverseGeoCoder* geoCoder;

+ (id)sharedLocation;

- (CLLocationCoordinate2D)getCurrentLocation;
- (Float32)getCurrentLongitude;
- (Float32)getCurrentLatitude;

- (MKPlacemark *)getCurrentPlacemark;
- (NSString *)getCurrentCity;

- (NSArray *)getServicesForCategory:(NSString *) category;
- (NSArray *)getServicesForKeyword:(NSString *) keyword;
- (NSArray *)getServicesFor:(NSString *) query;

- (MKPlacemark *)getPlacemarkByLocation:(CLLocationCoordinate2D) pLocation;

@end
