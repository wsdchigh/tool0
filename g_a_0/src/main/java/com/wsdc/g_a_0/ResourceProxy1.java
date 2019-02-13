package com.wsdc.g_a_0;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.wsdc.g_j_0.IContainer0;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/*
 *  考虑到有多个Resource
 *  <li>    不同的APK文件中有不同的资源
 *  <li>    但是他们会统一处理
 *  <li>    我们选择Activity返回这个  代理者  这里面会查询到第一个，然后返回
 *
 *  <li>    因为本地local中的资源非常少，所以不要讲local放在前面，减少异常的抛出
 *
 *
 *  <li>    针对任何一个APK文件创建一个 Resources
 *          <li>    不要对每一个Activity都去创建Resources
 */
public class ResourceProxy1 extends Resources{
    private List<Resources> proxyList = new LinkedList<>();

    /*
     *  任何一个Activity都有一个Resource，底层的Resource是唯一的
     *  <li>    顶层封装底层，所以不同的Activity的Resources是不一样的
     *
     *  <li>    如果功能自身无法实现，或者功能应该local来执行
     *          <li>    获取屏幕数据等等
     *          <li>    我们使用local来表达数据
     */
    private Resources local;

    public ResourceProxy1(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

    public ResourceProxy1(Resources local,AssetManager assets) {
        /*
         *   针对任何一个APK文件，创建一个
         */
        this(assets,new DisplayMetrics(),new Configuration());
        this.local = local;

        /*
         *  <li>    优先自己
         *  <li>    然后系统自带
         */
        proxyList.add(this);
        proxyList.add(local);
    }

    @Override
    public Drawable getDrawable(int id,Theme theme) throws NotFoundException {
        Drawable rtn = null;
        for (Resources resources : proxyList) {
            if(Build.VERSION.SDK_INT >= 21){
                try{
                    rtn = resources.getDrawable(id,theme);
                }catch (NotFoundException e){
                    e.printStackTrace();
                }
            }else{
                try{
                    rtn = resources.getDrawable(id);
                }catch (NotFoundException e){
                    e.printStackTrace();
                }
            }

            if(rtn != null){
                return rtn;
            }

        }
        throw new NotFoundException();
    }

    @Override
    public Drawable getDrawable(int id) throws NotFoundException {
        Drawable rtn = null ;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getDrawable(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public CharSequence getText(int id) throws NotFoundException {
        CharSequence rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getText(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }
        throw new NotFoundException();
    }

    @Override
    public Typeface getFont(int id) throws NotFoundException {
        Typeface tf = null;
        for (Resources resources : proxyList) {
            if (Build.VERSION.SDK_INT >= 26){
                try{
                    tf = resources.getFont(id);
                }catch (NotFoundException e){
                    e.printStackTrace();
                }
                if(tf != null){
                    return tf;
                }
            }

        }
        throw new NotFoundException();
    }

    @Override
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        CharSequence rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getQuantityText(id,quantity);
            }catch (NotFoundException e){
                e.printStackTrace();
            }

            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public String getString(int id) throws NotFoundException {
        String rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getString(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        String rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getString(id,formatArgs);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }



    @Override
    public String getQuantityString(int id, int quantity, Object... formatArgs) throws NotFoundException {
        String rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getQuantityString(id,quantity,formatArgs);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public String getQuantityString(int id, int quantity) throws NotFoundException {
        String rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getQuantityString(id,quantity);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public CharSequence getText(int id, CharSequence def) {
        CharSequence rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getText(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public CharSequence[] getTextArray(int id) throws NotFoundException {
        CharSequence[] rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getTextArray(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public String[] getStringArray(int id) throws NotFoundException {
        String[] rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getStringArray(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public int[] getIntArray(int id) throws NotFoundException {
        int[] rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getIntArray(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public TypedArray obtainTypedArray(int id) throws NotFoundException {
        TypedArray rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.obtainTypedArray(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public float getDimension(int id) throws NotFoundException {
        Float rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getDimension(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public int getDimensionPixelOffset(int id) throws NotFoundException {
        Integer rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getDimensionPixelOffset(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public int getDimensionPixelSize(int id) throws NotFoundException {
        Integer rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getDimensionPixelSize(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public float getFraction(int id, int base, int pbase) {
        Float rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getFraction(id,base,pbase);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {
        Drawable rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getDrawableForDensity(id,density);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public Drawable getDrawableForDensity(int id, int density, Theme theme) {
        Drawable rtn = null;
        for (Resources resources : proxyList) {
            try{
                if(Build.VERSION.SDK_INT >= 21){
                    rtn = resources.getDrawableForDensity(id,density,theme);
                }else{
                    rtn = resources.getDrawableForDensity(id,density);
                }

            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public Movie getMovie(int id) throws NotFoundException {
        Movie rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getMovie(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public int getColor(int id) throws NotFoundException {
        Integer rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getColor(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public int getColor(int id, Theme theme) throws NotFoundException {
        Integer rtn = null;
        for (Resources resources : proxyList) {
            try{
                if(Build.VERSION.SDK_INT >= 23){
                    rtn = resources.getColor(id,theme);
                }else{
                    rtn = resources.getColor(id);
                }

            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public ColorStateList getColorStateList(int id) throws NotFoundException {
        ColorStateList rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getColorStateList(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public ColorStateList getColorStateList(int id, Theme theme) throws NotFoundException {
        ColorStateList rtn = null;
        for (Resources resources : proxyList) {
            try{
                if(Build.VERSION.SDK_INT >= 23){
                    rtn = resources.getColorStateList(id,theme);
                }else{
                    rtn = resources.getColorStateList(id);
                }
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public boolean getBoolean(int id) throws NotFoundException {
        Boolean rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getBoolean(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public int getInteger(int id) throws NotFoundException {
        Integer rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getInteger(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public XmlResourceParser getLayout(int id) throws NotFoundException {
        XmlResourceParser rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getLayout(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public XmlResourceParser getAnimation(int id) throws NotFoundException {
        XmlResourceParser rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getAnimation(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public XmlResourceParser getXml(int id) throws NotFoundException {
        XmlResourceParser rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getXml(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public InputStream openRawResource(int id) throws NotFoundException {
        InputStream rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.openRawResource(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }


    @Override
    public InputStream openRawResource(int id, TypedValue value) throws NotFoundException {
        InputStream rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.openRawResource(id,value);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public AssetFileDescriptor openRawResourceFd(int id) throws NotFoundException {
        AssetFileDescriptor rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.openRawResourceFd(id);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public void getValue(int id, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        for (Resources resources : proxyList) {
            try{
                resources.getValue(id,outValue,resolveRefs);
                return;
            }catch (NotFoundException e){
                e.printStackTrace();
            }
        }

        throw new NotFoundException();
    }

    @Override
    public void getValueForDensity(int id, int density, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        for (Resources resources : proxyList) {
            try{
                resources.getValueForDensity(id,density,outValue,resolveRefs);
                return;
            }catch (NotFoundException e){
                e.printStackTrace();
            }
        }

        throw new NotFoundException();
    }

    @Override
    public void getValue(String name, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        for (Resources resources : proxyList) {
            try{
                resources.getValue(name,outValue,resolveRefs);
                return;
            }catch (NotFoundException e){
                e.printStackTrace();
            }
        }

        throw new NotFoundException();
    }

    //  TODO    这里需要谨慎考虑，到底使用自己还是 local
    @Override
    public TypedArray obtainAttributes(AttributeSet set, int[] attrs) {
        /*
        TypedArray rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.obtainAttributes(set,attrs);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
        */
        return local.obtainAttributes(set,attrs);
    }

    @Override
    public void updateConfiguration(Configuration config, DisplayMetrics metrics) {
        local.updateConfiguration(config,metrics);
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        return local.getDisplayMetrics();
    }

    @Override
    public Configuration getConfiguration() {
        return local.getConfiguration();
    }

    @Override
    public int getIdentifier(String name, String defType, String defPackage) {
        return local.getIdentifier(name,defType,defPackage);
    }

    @Override
    public String getResourceName(int resid) throws NotFoundException {
        String rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getResourceName(resid);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public String getResourcePackageName(int resid) throws NotFoundException {
        String rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getResourcePackageName(resid);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public String getResourceTypeName(int resid) throws NotFoundException {
        String rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getResourceTypeName(resid);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public String getResourceEntryName(int resid) throws NotFoundException {
        String rtn = null;
        for (Resources resources : proxyList) {
            try{
                rtn = resources.getResourceEntryName(resid);
            }catch (NotFoundException e){
                e.printStackTrace();
            }
            if(rtn != null){
                return rtn;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public void parseBundleExtras(XmlResourceParser parser, Bundle outBundle) throws IOException, XmlPullParserException {
        local.parseBundleExtras(parser,outBundle);
    }

    @Override
    public void parseBundleExtra(String tagName, AttributeSet attrs, Bundle outBundle) throws XmlPullParserException {
        local.parseBundleExtra(tagName, attrs, outBundle);
    }
}
