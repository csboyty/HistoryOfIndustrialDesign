//
//  LocalSQL.m
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import "LocalSQL.h"

sqlite3 *dataBase;

@implementation LocalSQL
+ (BOOL)openDataBase
{
    NSString *path = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) lastObject];
    NSString *fileName = [path stringByAppendingPathComponent:@"Data.db"];
    if (sqlite3_open([fileName UTF8String], &dataBase) == SQLITE_OK)
        return YES;
    return NO;
}

+ (BOOL)createLocalTable
{
    NSString *sqlStr = [NSString stringWithFormat:@"create table if not exists myTable(idS char,name char,md5 char, timestamp char, url char)"];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(dataBase, [sqlStr UTF8String], -1, &stmt, 0) != SQLITE_OK)
    {
        sqlite3_finalize(stmt);
        [sqlStr release];
        return NO;
    }
    if (sqlite3_step(stmt) == SQLITE_DONE)
    {
        sqlite3_finalize(stmt);
        [sqlStr release];
        return YES;
    }
    sqlite3_finalize(stmt);
    [sqlStr release];
    return NO;

}


@end
