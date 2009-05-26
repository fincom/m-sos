//
//  DeviceManager.h
//  SOS-Iphone
//
//  Created by Rabii MOUALI on 5/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface DeviceManager : NSObject {
	
}

-(NSString *)getUniqueID;
-(NSString *)getPhoneNumber;
-(NSString *)getCurrentLanguage;

@end
