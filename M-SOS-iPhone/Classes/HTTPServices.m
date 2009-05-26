//
//  HTTPServices.m
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "HTTPServices.h"
#import "JSON.h"


@implementation HTTPServices
@synthesize dataReply;
@synthesize response;
@synthesize error;

//Send HTTP Request to a given url, using given method and formData if available
-(HTTPServices *) sendHTTPRequest:(NSString *) url httpMethod:(NSString *) method requestString:(NSString *) rqStr{
	
	hasError = FALSE;
	id formData = nil;
	
	if(rqStr != nil){
		NSData *formData = [NSData dataWithBytes:[rqStr UTF8String] length:[rqStr lengthOfBytesUsingEncoding:NSUTF8StringEncoding]];
	}
	
	NSMutableURLRequest *request;
	
	request = [NSMutableURLRequest requestWithURL: [NSURL URLWithString:url]];
	
    [request setHTTPMethod: method];
	
	if([formData isKindOfClass:[NSData class]]){
		[request setHTTPBody:formData];
	}
	
	dataReply = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
	
	if (dataReply == nil)
	{
		hasError = TRUE;
	}
	
	//Lets log HTTPResponse, for fun :)
	NSHTTPURLResponse *httpResponse;
	httpResponse = (NSHTTPURLResponse *)response;
    NSLog(@"HTTP Response Headers %@", [httpResponse allHeaderFields]); 
	
	return self;
}

-(HTTPServices *) sendJSONRequest:(NSString *) url httpMethod:(NSString *) method service:(NSString *) service params:(NSMutableDictionary *) params{
	SBJSON *json = [SBJSON new];
	json.humanReadable = YES;
	
	//Pass it twice to escape quotes
	NSString *jsonString = [NSString stringWithFormat:@"%@", [params JSONFragment], nil];
	NSString *changeJSON = [NSString stringWithFormat:@"%@", [jsonString JSONFragment], nil];
	
	NSLog(jsonString);
	NSLog(changeJSON);
	
	
	NSString *requestString = [NSString stringWithFormat:@"{\"id\":1,\"method\":\"%@\",\"params\":%@}",service,jsonString,nil];
	NSLog(requestString);
	
	
	NSData *requestData = [NSData dataWithBytes: [requestString UTF8String] length: [requestString length]];
	
	
	NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL: [NSURL URLWithString: url]];
	
	NSString *postLength = [NSString stringWithFormat:@"%d", [requestData length]];
	[request setHTTPMethod: method];
	[request setValue:postLength forHTTPHeaderField:@"Content-Length"];
	[request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
	[request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
	[request setHTTPBody: requestData];
	
	//Data returned by WebService
	dataReply = [NSURLConnection sendSynchronousRequest: request returningResponse:&response error:&error ];
	NSString *returnString = [[NSString alloc] initWithData:dataReply encoding: NSUTF8StringEncoding];
	
	if (dataReply == nil)
	{
		hasError = TRUE;
	}
	
	//Lets log HTTPResponse, for fun :)
	NSHTTPURLResponse *httpResponse;
	httpResponse = (NSHTTPURLResponse *)response;
    NSLog(@"HTTP Response Headers %@", [httpResponse allHeaderFields]); 
	NSLog(returnString);
	
	return self;
}

//Handle HTTP Error
-(Boolean) hasError{
	
	//Handle Status HTTP Error
	if ([response respondsToSelector:@selector(statusCode)])
	{
		int statusCode = [((NSHTTPURLResponse *)response) statusCode];
		if (statusCode >= 400)
		{
			hasError = TRUE;
		}
	}
	
	return hasError;
}

@end
