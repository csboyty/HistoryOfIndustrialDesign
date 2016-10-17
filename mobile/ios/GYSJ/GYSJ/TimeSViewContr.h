//
//  TimeSViewContr.h
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import <UIKit/UIKit.h>
@class TimeView;
@interface TimeSViewContr : UIViewController
{
    IBOutlet UIScrollView *_scrollView;
    IBOutlet UILabel *timeLabel;
    IBOutlet TimeView *timeView;
}
@end
