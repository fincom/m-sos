//
//  FirstViewController.h
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/7/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MKMapView.h>
#import "LocationListener.h"
#import "AlertListener.h";

@interface MapViewController : UIViewController <MKMapViewDelegate> {
	
	IBOutlet MKMapView *mapView;
	IBOutlet UIToolbar *toolbar;
	
	LocationListener *sharedLocation;
	AlertListener *alertManager;
	
	NSMutableArray *placemarks;
	
}

@property(nonatomic, retain) LocationListener* sharedLocation;
@property(nonatomic, retain) AlertListener *alertManager;

@property(nonatomic, retain) IBOutlet UIToolbar *toolbar;
@property(nonatomic, retain) IBOutlet MKMapView *mapView;


-(void) setNewLocation:(NSNotification *)aNotification;

-(IBAction) setCategoryHospitals: (id) sender;
-(IBAction) setCategoryPompiers: (id) sender;
-(IBAction) setCategoryUrgences: (id) sender;

-(void) drawPlaceMarks:(NSString *) category;
-(void) zoomToCurrentLocation:(CLLocationCoordinate2D) location delta:(Float32) delta;

@end
