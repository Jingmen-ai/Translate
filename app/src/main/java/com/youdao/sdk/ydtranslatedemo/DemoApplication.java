/**
 * @(#)DemoApplication.java, 2015年4月3日. Copyright 2012 Yodao, Inc. All rights
 *                           reserved. YODAO PROPRIETARY/CONFIDENTIAL. Use is
 *                           subject to license terms.
 */
package com.youdao.sdk.ydtranslatedemo;

import com.youdao.sdk.app.YouDaoApplication;

import android.app.Application;

/**
 * @author lukun
 */
public class DemoApplication extends Application {

    private static DemoApplication youAppction;

    @Override
    public void onCreate() {
        super.onCreate();
        if(YouDaoApplication.getApplicationContext() == null)
            YouDaoApplication.init(this, "334ec28c3127e90a");
        youAppction = this;
    }

    public static DemoApplication getInstance() {
        return youAppction;
    }

}
