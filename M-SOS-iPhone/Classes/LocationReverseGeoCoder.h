//
//  LocationReverseGeoCoder.h
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MKReverseGeocoder.h>
#import <MapKit/MKPlacemark.h>

@interface LocationReverseGeoCoder : NSObject <MKReverseGeocoderDelegate> {
	MKPlacemark* placemark;
}

@property (nonatomic, retain) MKPlacemark* placemark;

- (id) initWithLocation:(CLLocationCoordinate2D) location;

@end
