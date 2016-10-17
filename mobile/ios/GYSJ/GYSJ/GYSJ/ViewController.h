//
//  ViewController.h
//  GYSJ
//
//  Created by sunyong on 13-7-23.
//  Copyright (c) 2013年 sunyong. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TimeSViewContr.h"
#import "MenuViewContr.h"

@interface ViewController : UIViewController
{
    IBOutlet UIView *menuView;
    
    TimeSViewContr *timeSViewContr;
    MenuViewContr *menuViewContr;
}

- (IBAction)menuBtPress:(UIButton*)sender;
@end
