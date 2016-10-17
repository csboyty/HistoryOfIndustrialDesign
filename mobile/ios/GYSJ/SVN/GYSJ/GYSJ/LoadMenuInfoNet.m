//
//  LoadMenuInfoNet.m
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//


#import "LoadMenuInfoNet.h"
#import "JSONKit.h"

@implementation LoadMenuInfoNet

@synthesize delegate;

- (void)loadMenuFromUrl:(NSString*)urlStr
{
    
    NSString *URLStr = [NSString stringWithFormat:@"http://lotusprize.com/travel/dataUpdate.json?lastUpdateDate=0"];

    NSLog(@"%@", URLStr);
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URLStr] cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:10.f];
    [request setHTTPMethod:@"GET"];

    NSURLConnection *connect = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    if (connect)
    {
        backData = [[NSMutableData data] retain];
        NSLog(@"theConnection Success");
    }
    else
    {
        backData = nil;
        NSLog(@"Connection Fail!!!!");
    }
    [request release];


}
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    NSLog(@"error:%@", [error description]);
    
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
  
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    [backData appendData:data];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    //  NSLog(@"receive:%d，%s", [backData length],[backData bytes]);
    
    
    //    NSString *path = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) lastObject];
    //    NSString *pathFile = [path stringByAppendingPathComponent:@"3.zip"];
    //
    //    NSFileManager *fileManager = [NSFileManager defaultManager];
    //    [fileManager createFileAtPath:pathFile contents:backData attributes:nil];
    //
    //    NSLog(@"==%@", [MFSP_MD5 file_md5:pathFile]);
    NSDictionary *backDict = [backData objectFromJSONDataWithParseOptions:JKParseOptionValidFlags error:nil];
    NSLog(@"%@", backDict);
    [delegate didReceiveData:backDict];
    
    
    
}

- (void)dealloc
{
    if (backData) 
        [backData release];
    [super dealloc];
}

@end
