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

+ (BOOL)closeDataBase
{
    if (sqlite3_close(dataBase) == SQLITE_OK)
        return YES;
    return NO;
}

+ (BOOL)createLocalTable
{
    NSString *sqlStr = [NSString stringWithFormat:@"create table if not exists mytable( id char primary key,name char,md5 char, timestamp char, url char)"];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(dataBase, [sqlStr UTF8String], -1, &stmt, 0) != SQLITE_OK)
    {
        sqlite3_finalize(stmt);
        return NO;
    }
    if (sqlite3_step(stmt) == SQLITE_DONE)
    {
        sqlite3_finalize(stmt);
        return YES;
    }
    sqlite3_finalize(stmt);
    return NO;
}

+ (NSString*)getTitleFromId:(NSString*)idStr
{
    NSString *sqlStr = [NSString stringWithFormat:@"select *from mytable where id = '%@'", idStr];
    NSString *nameStr = nil;
    sqlite3_stmt *stmt; 
    if (sqlite3_prepare_v2(dataBase, [sqlStr UTF8String], -1, &stmt, 0) == SQLITE_OK)
    {
        while (sqlite3_step(stmt) == SQLITE_ROW)
        {
            NSString *idStr   = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 0)];
            nameStr = [NSString stringWithCString:(const char*)sqlite3_column_text(stmt, 1) encoding:NSUTF8StringEncoding];
            NSString *md5Str = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 2)];
            NSString *timestampStr = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 3)];
            NSString *urlStr = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 4)];
            NSLog(@"%@, %@, %@, %@, %@", idStr, nameStr, md5Str, timestampStr, urlStr);
        }
        sqlite3_finalize(stmt);
        return nameStr;
    }
    return nil;
}

+ (NSString*)getTimeStampFromId:(NSString*)idStr
{
    NSString *sqlStr = [NSString stringWithFormat:@"select *from mytable where id = '%@'", idStr];
    NSString *timeStampStr = nil;
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(dataBase, [sqlStr UTF8String], -1, &stmt, 0) == SQLITE_OK)
    {
        while (sqlite3_step(stmt) == SQLITE_ROW)
        {
            NSString *idStr   = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 0)];
            NSString *nameStr = [NSString stringWithCString:(const char*)sqlite3_column_text(stmt, 1) encoding:NSUTF8StringEncoding];
            NSString *md5Str = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 2)];
            timeStampStr = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 3)];
            NSString *urlStr = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 4)];
            NSLog(@"%@, %@, %@, %@, %@", idStr, nameStr, md5Str, timeStampStr, urlStr);
        }
        sqlite3_finalize(stmt);
        return timeStampStr;
    }
    return nil;
}

+ (NSArray*)getAllID
{
    NSMutableArray *arry = [NSMutableArray array];
    NSString *sqlStr = [NSString stringWithFormat:@"select *from mytable"];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(dataBase, [sqlStr UTF8String], -1, &stmt, 0) == SQLITE_OK)
    {
        while (sqlite3_step(stmt) == SQLITE_ROW)
        {
            NSString *idStr   = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 0)];
//            NSString *nameStr = [NSString stringWithCString:(const char*)sqlite3_column_text(stmt, 1) encoding:NSUTF8StringEncoding];
//            NSString *md5Str = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 2)];
//            NSString *timeStampStr = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 3)];
//            NSString *urlStr = [NSString stringWithFormat:@"%s", sqlite3_column_text(stmt, 4)];
           // NSLog(@"%@, %@, %@, %@, %@", idStr, nameStr, md5Str, timeStampStr, urlStr);
            [arry addObject:idStr];
        }
        sqlite3_finalize(stmt);
        NSLog(@"%@", arry);
        return arry;
    }
    return nil;
}
+ (NSArray*)getAllName
{
    NSMutableArray *arry = [NSMutableArray array];
    NSString *sqlStr = [NSString stringWithFormat:@"select *from mytable"];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(dataBase, [sqlStr UTF8String], -1, &stmt, 0) == SQLITE_OK)
    {
        while (sqlite3_step(stmt) == SQLITE_ROW)
        {
            NSString *nameStr = [NSString stringWithCString:(const char*)sqlite3_column_text(stmt, 1) encoding:NSUTF8StringEncoding];
            [arry addObject:nameStr];
        }
        sqlite3_finalize(stmt);
        NSLog(@"%@", arry);
        return arry;
    }
    return nil;
}

+ (BOOL)insertData:(NSDictionary*)infoDict
{
    if ([[infoDict objectForKey:@"op"] isEqual:@"d"]) // 删除操作
    {
        return [LocalSQL deleteData:[infoDict objectForKey:@"id"]];
    }
    NSString *idStr     = [infoDict objectForKey:@"id"];
    NSString *nameStr   = [infoDict objectForKey:@"name"];
    NSString *md5Str    = [infoDict objectForKey:@"md5"];
    NSString *timestamp = [infoDict objectForKey:@"timestamp"];
    NSString *urlStr    = [infoDict objectForKey:@"url"];
    
    NSString *sqlStr = [NSString stringWithFormat:@"insert into mytable values('%@','%@','%@','%@','%@')", idStr,nameStr, md5Str, timestamp, urlStr];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(dataBase, [sqlStr UTF8String], -1, &stmt, 0) != SQLITE_OK)
    {
        sqlite3_finalize(stmt);
        return NO;
    }
    if (sqlite3_step(stmt) == SQLITE_DONE)
    {
        sqlite3_finalize(stmt);
        return YES;
    }
    sqlite3_finalize(stmt);
    return NO;
}

+ (BOOL)deleteData:(NSString*)idStr
{
    NSString *sqlStr = [NSString stringWithFormat:@"delete from mytable where id = '%@'", idStr];
    sqlite3_stmt *stmt;
    if (sqlite3_prepare_v2(dataBase, [sqlStr UTF8String], -1, &stmt, 0) != SQLITE_OK)
    {
        sqlite3_finalize(stmt);
        return NO;
    }
    if (sqlite3_step(stmt) == SQLITE_DONE)
    {
        sqlite3_finalize(stmt);
        return YES;
    }
    sqlite3_finalize(stmt);
    return NO;
}

+ (BOOL)updateData:(NSDictionary*)infoDict
{
    NSString *sqlStr = [NSString stringWithFormat:@"update mytable set name='%@',md5='%@',timestamp='%@',url='%@' where id='%@'", @"1", @"2", @"3", @"4", @"5"];
    NSLog(@"%@", sqlStr);
    return YES; 
}

@end
