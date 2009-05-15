//
//  FirstViewController.m
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/7/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "MapViewController.h"
#import "ServicePlaceMark.h";

@implementation MapViewController

@synthesize sharedLocation;
@synthesize alertManager;
@synthesize mapView;
@synthesize toolbar;

// The designated initializer. Override to perform setup that is required before the view is loaded.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        //Listener for new Location
		[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(setNewLocation:)
													 name:@"set_NewLocation" object:nil];
    }
    return self;
}


/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView {
}
*/


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	
	//mapView = [[MKMapView alloc] initWithFrame:self.view.frame];
	
	mapView.delegate=self;
	
	//[self.view addSubview:mapView];
	
	//Gestion de la localisation
	id appDelegate = [UIApplication sharedApplication].delegate;
	sharedLocation = [appDelegate sharedLocation];
	
	[self zoomToCurrentLocation: [sharedLocation getCurrentLocation] delta:.005];
	
	//Alert Manager
	alertManager = [appDelegate alertManager];
	
	//Initialisation des placemarks
	placemarks = nil;
	
	//Show PlaceMark for a current Alert, n'aura jamais lieu vu que l'iphone est mono Thread
	NSString *keyword = [alertManager getAlertTypeCategory:(NSInteger)nil];
	if(keyword != nil){
		[self drawPlaceMarks:keyword];
	}
}

-(IBAction) setCategoryHospitals: (id) sender{
	[self drawPlaceMarks:@"Hospitals"];
}

-(IBAction) setCategoryPompiers: (id) sender{
	[self drawPlaceMarks:@"Pompiers"];
}

-(IBAction) setCategoryUrgences: (id) sender{
    [self drawPlaceMarks:@"Urgences"];
}


- (void) setNewLocation:(NSNotification *)aNotification {
	
	CLLocation* newLocation = [aNotification object];
	
	[self zoomToCurrentLocation: newLocation.coordinate delta:.005];
}


-(void) zoomToCurrentLocation:(CLLocationCoordinate2D) location delta:(Float32) delta{
	
	MKCoordinateRegion region;
	region.center = location;
	
	//Set Zoom level using Span
	MKCoordinateSpan span;
	span.latitudeDelta=delta;
	span.longitudeDelta=delta;
	region.span=span;
	
	[mapView setRegion:region animated:TRUE];
	
}


//
-(void) drawPlaceMarks:(NSString *) category{
	NSLog(category);
	NSArray *results; 
	
	results = [sharedLocation getServicesForCategory:category];
	
	if([results count] == 0){
		results = [sharedLocation getServicesForKeyword:category];
	}
	
	// loop over all the stream objects and print their titles
	NSInteger ndx;
	
	if(placemarks != nil){
			[mapView removeAnnotations:placemarks];
	}
	
	placemarks = [NSMutableArray new];
	
	for (ndx = 0; ndx < results.count; ndx++) {
		NSArray *result = (NSArray *)[results objectAtIndex:ndx];
		NSLog(@"This is the title of a result: %@", [result valueForKey:@"title"]); 
		NSLog(@"This is the title of a result: %@", [result valueForKey:@"lat"]); 
		NSLog(@"This is the title of a result: %@", [result valueForKey:@"lng"]); 
		
		CLLocationCoordinate2D location;
		NSNumberFormatter *nf = [[NSNumberFormatter alloc] init];
		[nf setNumberStyle:NSNumberFormatterDecimalStyle];

		location.latitude = [[nf numberFromString:[result valueForKey:@"lat"]] floatValue]; 
		
		location.longitude = [[nf numberFromString:[result valueForKey:@"lng"]] floatValue];
		
		ServicePlaceMark *placemark=[[ServicePlaceMark alloc] initWithCoordinate:location];
		placemark.title = [result valueForKey:@"title"];
		
		NSArray *phoneNumbers = (NSArray *)[result valueForKey:@"phoneNumbers"];
		if(phoneNumbers.count > 0){
			NSArray *phoneNumber = (NSArray *)[phoneNumbers objectAtIndex:0];
			placemark.subtitle = [phoneNumber valueForKey:@"number"];
			
		}else{
			placemark.subtitle = [result valueForKey:@"title"];
		}
		
		[placemarks addObject:placemark];
		
	}
	
	[mapView addAnnotations:placemarks];
	[self zoomToCurrentLocation: [sharedLocation getCurrentLocation] delta:0.103];
	
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation{
	
	MKPinAnnotationView *retval = nil;
	
	if ([annotation isMemberOfClass:[ServicePlaceMark class]]) {
		retval = (MKPinAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:[annotation title]];
		if (retval == nil) {
			retval = [[[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:[annotation title]] autorelease];
		}
		
		if (retval) {
			[retval setPinColor:MKPinAnnotationColorGreen];
			retval.animatesDrop = YES;
			retval.canShowCallout = TRUE;
		}
	}
	
    return retval;
	
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}

@end
