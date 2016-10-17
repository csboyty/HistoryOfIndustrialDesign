//
//  LocalSQL.h
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>



@interface LocalSQL : NSObject


+ (BOOL)openDataBase;
+ (BOOL)closeDataBase;
+ (BOOL)createLocalTable;
+ (NSString*)getTitleFromId:(NSString*)idStr;
+ (NSString*)getTimeStampFromId:(NSString*)idStr;
+ (NSArray*)getAllID;
+ (NSArray*)getName;
@end
