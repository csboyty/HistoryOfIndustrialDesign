//
//  LoadMenuInfoNet.h
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NetworkDelegate.h"

@interface LoadMenuInfoNet : NSObject
{
    NSMutableData *backData;
}
@property(nonatomic, assign)id<NetworkDelegate>delegate;


- (void)loadMenuFromUrl:(NSString*)urlStr;

@end
