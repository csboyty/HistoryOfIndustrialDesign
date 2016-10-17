//
//  TimeView.h
//  GYSJ
//
//  Created by sunyong on 13-7-26.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TimeView : UIView
{
    float beforeX;
    float currentX;
}
@property(nonatomic, assign)UILabel *timeLabel;
@end
