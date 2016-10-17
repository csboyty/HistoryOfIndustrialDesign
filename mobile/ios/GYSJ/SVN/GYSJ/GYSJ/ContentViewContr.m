//
//  ContentViewContr.m
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import "ContentViewContr.h"
#import "ZipArchive.h"

@interface ContentViewContr ()

@end

@implementation ContentViewContr

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (void)viewDidLoad
{
    activeView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    [activeView setCenter:CGPointMake(273, 300)];
    [self.view addSubview:activeView];
    [activeView startAnimating];
    
    [super viewDidLoad];
    NSString *doctPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)  lastObject];
    NSLog(@"%@", doctPath);
    NSString *filePath = [doctPath stringByAppendingPathComponent:@"1/doc/main.phone.html"];
    NSLog(@"%@", filePath);
    
    NSString *baseUrl = [doctPath stringByAppendingPathComponent:@"1/doc"];
    NSURL *baseURL = [[NSURL alloc] initFileURLWithPath:baseUrl isDirectory:YES];
    [_webView loadHTMLString:[NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil] baseURL:baseURL];
    [baseURL release];
    
//    NSString *path = [NSHomeDirectory() stringByAppendingPathComponent:@"Documents"];
//    
//    NSString *filePath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"%d.zip", 2]];
//    NSString *unZipPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"%d", 2]];
//    
//    ZipArchive *zip = [[ZipArchive alloc] init];
//    
//    
//    BOOL result;
//    
//    if ([zip UnzipOpenFile:filePath]) {
//        result = [zip UnzipFileTo:unZipPath overWrite:YES];
//        if (!result) {
//            NSLog(@"解压失败");
//        }
//        else
//        {
//            NSLog(@"解压成功");
//        }
//        [zip UnzipCloseFile];
//    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    return YES;
}

- (void)webViewDidStartLoad:(UIWebView *)webView
{
    
}

- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    [activeView stopAnimating];
}

- (void)dealloc
{
    [activeView release];
    [super dealloc];
}

@end
