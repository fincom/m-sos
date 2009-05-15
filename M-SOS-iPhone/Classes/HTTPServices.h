//
//  HTTPServices.h
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface HTTPServices : NSObject {
	NSError *error;
    Boolean hasError;
	
	NSURLResponse *response;
    NSData *dataReply;
	
}

@property (nonatomic, retain) NSData *dataReply;
@property (nonatomic, retain) NSURLResponse *response;
@property (nonatomic, retain) NSError *error;

-(HTTPServices *) sendHTTPRequest:(NSString *) url httpMethod:(NSString *) method requestString:(NSString *) rqStr;
-(HTTPServices *) sendJSONRequest:(NSString *) url httpMethod:(NSString *) method service:(NSString *) service params:(NSDictionary *) params;

-(Boolean) hasError;
	
@end
