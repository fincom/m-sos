//
//  SBJsonWriter.m
//  JSON
//
//  Created by Stig Brautaset on 16/03/2009.
//  Copyright 2009 Stig Brautaset. All rights reserved.
//

#import "SBJsonWriter.h"

@interface SBJsonWriter ()

- (BOOL)appendValue:(id)fragment into:(NSMutableString*)json;
- (BOOL)appendArray:(NSArray*)fragment into:(NSMutableString*)json;
- (BOOL)appendDictionary:(NSDictionary*)fragment into:(NSMutableString*)json;
- (BOOL)appendString:(NSString*)fragment into:(NSMutableString*)json;

- (NSString*)indent;

@end

@implementation SBJsonWriter

@synthesize sortKeys;
@synthesize humanReadable;


/**
 Returns a string containing JSON representation of the passed in value, or nil on error.
 If nil is returned and @p error is not NULL, @p *error can be interrogated to find the cause of the error.
 
 @param value any instance that can be represented as a JSON fragment
 @param allowScalar wether to return json fragments for scalar objects
 */
- (NSString*)stringWithObject:(id)value allowScalar:(BOOL)allowScalar {
    [self clearErrorTrace];
    depth = 0;
    NSMutableString *json = [NSMutableString stringWithCapacity:128];
    
    if (!allowScalar && ![value isKindOfClass:[NSDictionary class]] && ![value isKindOfClass:[NSArray class]]) {
        [self addErrorWithCode:EFRAGMENT description:@"Not valid type for JSON"];
        
    } else if ([self appendValue:value into:json]) {
        return json;
    }
    
    return nil;
}


- (NSString*)indent {
    return [@"\n" stringByPaddingToLength:1 + 2 * depth withString:@" " startingAtIndex:0];
}

- (BOOL)appendValue:(id)fragment into:(NSMutableString*)json {
    if ([fragment isKindOfClass:[NSDictionary class]]) {
        if (![self appendDictionary:fragment into:json])
            return NO;
        
    } else if ([fragment isKindOfClass:[NSArray class]]) {
        if (![self appendArray:fragment into:json])
            return NO;
        
    } else if ([fragment isKindOfClass:[NSString class]]) {
        if (![self appendString:fragment into:json])
            return NO;
        
    } else if ([fragment isKindOfClass:[NSNumber class]]) {
        if ('c' == *[fragment objCType])
            [json appendString:[fragment boolValue] ? @"true" : @"false"];
        else
            [json appendString:[fragment stringValue]];
        
    } else if ([fragment isKindOfClass:[NSNull class]]) {
        [json appendString:@"null"];
        
    } else {
        [self addErrorWithCode:EUNSUPPORTED description:[NSString stringWithFormat:@"JSON serialisation not supported for %@", [fragment class]]];
        return NO;
    }
    return YES;
}

- (BOOL)appendArray:(NSArray*)fragment into:(NSMutableString*)json {
    [json appendString:@"["];
    depth++;
    
    BOOL addComma = NO;    
    for (id value in fragment) {
        if (addComma)
            [json appendString:@","];
        else
            addComma = YES;
        
        if ([self humanReadable])
            [json appendString:[self indent]];
        
        if (![self appendValue:value into:json]) {
            return NO;
        }
    }
    
    depth--;
    if ([self humanReadable] && [fragment count])
        [json appendString:[self indent]];
    [json appendString:@"]"];
    return YES;
}

- (BOOL)appendDictionary:(NSDictionary*)fragment into:(NSMutableString*)json {
    [json appendString:@"{"];
    depth++;
    
    NSString *colon = [self humanReadable] ? @" : " : @":";
    BOOL addComma = NO;
    NSArray *keys = [fragment allKeys];
    if (self.sortKeys)
        keys = [keys sortedArrayUsingSelector:@selector(compare:)];
    
    for (id value in keys) {
		if (addComma)
            [json appendString:@","];
        else
            addComma = YES;
        
        if ([self humanReadable])
            [json appendString:[self indent]];
        
        if (![value isKindOfClass:[NSString class]]) {
            [self addErrorWithCode:EUNSUPPORTED description: @"JSON object key must be string"];
            return NO;
        }
        
        if (![self appendString:value into:json])
            return NO;
        
        [json appendString:colon];
        if (![self appendValue:[fragment objectForKey:value] into:json]) {
            [self addErrorWithCode:EUNSUPPORTED description:[NSString stringWithFormat:@"Unsupported value for key %@ in object", value]];
            return NO;
        }
    }
    
    depth--;
    if ([self humanReadable] && [fragment count])
        [json appendString:[self indent]];
    [json appendString:@"}"];
    return YES;    
}

- (BOOL)appendString:(NSString*)fragment into:(NSMutableString*)json {
    
    static NSMutableCharacterSet *kEscapeChars;
    if( ! kEscapeChars ) {
        kEscapeChars = [[NSMutableCharacterSet characterSetWithRange: NSMakeRange(0,32)] retain];
        [kEscapeChars addCharactersInString: @"\"\\"];
    }
    
    [json appendString:@"\""];
    
    NSRange esc = [fragment rangeOfCharacterFromSet:kEscapeChars];
    if ( !esc.length ) {
        // No special chars -- can just add the raw string:
        [json appendString:fragment];
        
    } else {
        NSUInteger length = [fragment length];
        for (NSUInteger i = 0; i < length; i++) {
            unichar uc = [fragment characterAtIndex:i];
            switch (uc) {
                case '"':   [json appendString:@"\\\""];       break;
                case '\\':  [json appendString:@"\\\\"];       break;
                case '\t':  [json appendString:@"\\t"];        break;
                case '\n':  [json appendString:@"\\n"];        break;
                case '\r':  [json appendString:@"\\r"];        break;
                case '\b':  [json appendString:@"\\b"];        break;
                case '\f':  [json appendString:@"\\f"];        break;
                default:    
                    if (uc < 0x20) {
                        [json appendFormat:@"\\u%04x", uc];
                    } else {
                        CFStringAppendCharacters((CFMutableStringRef)json, &uc, 1);
                    }
                    break;
                    
            }
        }
    }
    
    [json appendString:@"\""];
    return YES;
}


@end
