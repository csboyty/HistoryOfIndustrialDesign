//
//  TimeView.m
//  GYSJ
//
//  Created by sunyong on 13-7-26.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import "TimeView.h"

@implementation TimeView
@synthesize timeLabel;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
    }
    return self;
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    CGPoint point =  [[touches anyObject] locationInView:self];
    currentX = point.x;
}


- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = (UITouch*)[touches anyObject];
    CGPoint point =  [touch locationInView:[self superview]];
    float gapX = point.x - [touch previousLocationInView:[self superview]].x;
    NSLog(@"move:%f, %f", self.frame.origin.x + gapX, gapX);
    [self setFrame:CGRectMake(self.frame.origin.x + gapX, self.frame.origin.y, self.frame.size.width, self.frame.size.height)];
    currentX = point.x;
    
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    NSLog(@"end");
}

@end
