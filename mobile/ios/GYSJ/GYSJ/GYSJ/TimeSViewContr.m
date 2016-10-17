//
//  TimeSViewContr.m
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import "TimeSViewContr.h"
#import "TimeView.h"

#define LabelBgColor [UIColor colorWithRed:139.0/255 green:113.0/255 blue:42.0/255 alpha:1]
#define StartX 70
#define GapX 80

#define StartLowY 119
#define StartTopY 20
#define LabelHeigh 15

#define ScrollViewContentX 1500

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
    
    [_scrollView setContentSize:CGSizeMake(ScrollViewContentX, 100)];
    timeView.timeLabel = timeLabel;
    
    [self addUpLabel];
    [self addLowwerLabel];
    [self addTimeLabel];
    
}

- (void)addUpLabel
{
    for (int i = StartX; i < ScrollViewContentX ; i+= GapX)
    {
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(i, StartTopY, 1, LabelHeigh)];
        label.backgroundColor = LabelBgColor;
        [_scrollView addSubview:label];
        [label release];
    }
    UILabel *topLable = [[UILabel alloc] initWithFrame:CGRectMake(0, StartTopY, ScrollViewContentX, 1)];
    topLable.backgroundColor = LabelBgColor;
    [_scrollView addSubview:topLable];
    [topLable release];

}

- (void)addLowwerLabel
{
    for (int i = StartX; i < ScrollViewContentX ; i+= GapX)
    {
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(i, StartLowY, 1, LabelHeigh)];
        label.backgroundColor = LabelBgColor;
        [_scrollView addSubview:label];
        [label release];
    }
    UILabel *bottomLable = [[UILabel alloc] initWithFrame:CGRectMake(0, StartLowY+LabelHeigh, ScrollViewContentX, 1)];
    bottomLable.backgroundColor = LabelBgColor;
    [_scrollView addSubview:bottomLable];
    [bottomLable release];

}

- (void)addTimeLabel
{
    int startYear = 1870;
    for (int i = StartX; i < ScrollViewContentX ; i+= GapX)
    {
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(i-20, 68, 40, 20)];
        label.textColor = LabelBgColor;
        label.backgroundColor = [UIColor clearColor];
        label.text = [NSString stringWithFormat:@"%d", startYear];
        label.textAlignment = NSTextAlignmentCenter;
        
        [_scrollView addSubview:label];
        [label release];
        startYear += 10;
    }
    UILabel *bottomLable = [[UILabel alloc] initWithFrame:CGRectMake(0, StartLowY+LabelHeigh, ScrollViewContentX, 1)];
    bottomLable.backgroundColor = LabelBgColor;
    [_scrollView addSubview:bottomLable];
    [bottomLable release];
}


- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    float offX = _scrollView.contentOffset.x;
    if (offX > ScrollViewContentX - 1024) 
        [_scrollView setContentOffset:CGPointMake(ScrollViewContentX - 1024, 0)];
    if (offX < 0) 
         [_scrollView setContentOffset:CGPointZero];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
