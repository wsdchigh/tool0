package com.wsdc.g_a_0.base;

import android.app.Application;

import com.wsdc.g_a_0.Starter;
import com.wsdc.g_a_0.plugin.IPlugin;
import com.wsdc.g_a_0.router.IRouter;

/*
 *  可以继承这个Application
 *  <li>    可以直接使用
 *  <li>    可以复制里面的代码到自己的Application之中
 *
 *
 */
public class BaseApplication extends Application {
    Starter starter;
    IRouter router;

    @Override
    public void onCreate() {
        super.onCreate();
        Starter.install(this);
        starter = Starter.getInstance();
        router = starter.getRouter();
        router.go("/test/guide/guide0",IPlugin.START_NOT_STACK | (IPlugin.START_NOT_STACK >> 2));

    }
}
