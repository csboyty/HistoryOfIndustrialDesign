//
//  ViewController.m
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import "ViewController.h"
#import "ContentViewContr.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    timeSViewContr = [[TimeSViewContr alloc] init];
    [self.view addSubview:timeSViewContr.view];
    
    menuViewContr = [[MenuViewContr alloc] init];
    [menuView addSubview:menuViewContr.view];
    
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (IBAction)menuBtPress:(UIButton*)sender
{
    ContentViewContr *contentViewContr = [[ContentViewContr alloc] init];
    contentViewContr.modalPresentationStyle = UIModalPresentationFormSheet;
    [self presentViewController:contentViewContr animated:YES completion:nil];
    [contentViewContr release];
}
@end
