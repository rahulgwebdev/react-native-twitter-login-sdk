#import <React/RCTBridgeModule.h>
#import "TwitterLoginSdk-Bridging-Header.h"

@interface RCT_EXTERN_MODULE(TwitterLoginSdk, NSObject)

RCT_EXTERN_METHOD(initialize:(NSString *)consumerKey withB:(NSString *)consumerSecret                                                       withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(logIn:(RCTPromiseResolveBlock)resolve withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(logOut)

@end
