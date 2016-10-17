//
//  TimeSViewContr.m
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import "TimeSViewContr.h"
#import "TimeView.h"

@interface TimeSViewContr ()

@end

@implementation TimeSViewContr

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [_scrollView setContentSize:CGSizeMake(1500, 100)];
    timeView.timeLabel = timeLabel;
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
