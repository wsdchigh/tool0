package com.wsdc.g_a_0;

import android.content.res.Resources;

import dalvik.system.BaseDexClassLoader;

/*
 *  记录任何一个APK的配置
 *  <li>    ClassLoader
 *  <li>    Resources
 */
public interface APK {
    /*
     *  提供一个ClassLoader，加载apk中的数据
     */
    BaseDexClassLoader classLoader();

    /*
     *  提供一个资源管理者，用于管理apk中的资源
     */
    Resources resources();

    /*
     *  获取APK中携带的信息
     */
    XInfo info();

    /*
     *   获取所有的插件信息
     */
    //XInfoAll infoAll();
}
