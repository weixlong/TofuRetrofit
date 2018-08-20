package com.pudding.tofu.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by wxl on 2018/8/20 0020.
 * 邮箱：632716169@qq.com
 */

public class LogBuilder implements UnBind {

    private String TAG = "Tofu";

    private pBuilder p;

    protected LogBuilder() {

    }

    /**
     * Tag
     *
     * @param tag
     * @return
     */
    public LogBuilder tag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            TAG = tag;
        }
        return this;
    }

    public void v(@NonNull String msg) {
        Log.v(TAG, msg);
    }

    public void d(@NonNull String msg) {
        Log.d(TAG, msg);
    }

    public void i(@NonNull String msg) {
        Log.i(TAG, msg);
    }

    public void e(@NonNull String msg) {
        Log.e(TAG, msg);
    }

    public void v(@NonNull int msg) {
        Log.v(TAG, String.valueOf(msg));
    }

    public void d(@NonNull int msg) {
        Log.d(TAG, String.valueOf(msg));
    }

    public void i(@NonNull int msg) {
        Log.i(TAG, String.valueOf(msg));
    }

    public void e(@NonNull int msg) {
        Log.e(TAG, String.valueOf(msg));
    }

    public void v(@NonNull long msg) {
        Log.v(TAG, String.valueOf(msg));
    }

    public void d(@NonNull long msg) {
        Log.d(TAG, String.valueOf(msg));
    }

    public void i(@NonNull long msg) {
        Log.i(TAG, String.valueOf(msg));
    }

    public void e(@NonNull long msg) {
        Log.e(TAG, String.valueOf(msg));
    }

    public void v(@NonNull float msg) {
        Log.v(TAG, String.valueOf(msg));
    }

    public void d(@NonNull float msg) {
        Log.d(TAG, String.valueOf(msg));
    }

    public void i(@NonNull float msg) {
        Log.i(TAG, String.valueOf(msg));
    }

    public void e(@NonNull float msg) {
        Log.e(TAG, String.valueOf(msg));
    }

    public void v(@NonNull double msg) {
        Log.v(TAG, String.valueOf(msg));
    }

    public void d(@NonNull double msg) {
        Log.d(TAG, String.valueOf(msg));
    }

    public void i(@NonNull double msg) {
        Log.i(TAG, String.valueOf(msg));
    }

    public void e(@NonNull double msg) {
        Log.e(TAG, String.valueOf(msg));
    }


    /**
     * 拼接打印
     * @return
     */
    public pBuilder ping(){
        if(p == null){
            p = new pBuilder();
        }
        p.clear();
        return p;
    }


    public class pBuilder{

        private StringBuffer ps = new StringBuffer();

        private pBuilder() {
        }

        private void clear(){
            ps.delete(0,ps.length());
        }

        public pBuilder p(@NonNull String msg){
            ps.append(msg+" ");
            return this;
        }

        public pBuilder p(@NonNull int msg){
            ps.append(msg+" ");
            return this;
        }

        public pBuilder p(@NonNull float msg){
            ps.append(msg+" ");
            return this;
        }

        public pBuilder p(@NonNull long msg){
            ps.append(msg+" ");
            return this;
        }

        public pBuilder p(@NonNull double msg){
            ps.append(msg+" ");
            return this;
        }

        public void v(){
            Log.v(TAG,ps.toString());
        }

        public void d(){
            Log.d(TAG,ps.toString());
        }

        public void i(){
            Log.i(TAG,ps.toString());
        }

        public void e(){
            Log.e(TAG,ps.toString());
        }
    }

    @Override
    public void unbind() {

    }
}
