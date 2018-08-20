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
        if(TofuConfig.isDebug()) {
            Log.v(TAG, msg);
        }
    }

    public void d(@NonNull String msg) {
        if(TofuConfig.isDebug()) {
            Log.d(TAG, msg);
        }
    }

    public void i(@NonNull String msg) {
        if(TofuConfig.isDebug()) {
            Log.i(TAG, msg);
        }
    }

    public void e(@NonNull String msg) {
        if(TofuConfig.isDebug()) {
            Log.e(TAG, msg);
        }
    }

    public void v(@NonNull int msg) {
        if(TofuConfig.isDebug()) {
            Log.v(TAG, String.valueOf(msg));
        }
    }

    public void d(@NonNull int msg) {
        if(TofuConfig.isDebug()) {
            Log.d(TAG, String.valueOf(msg));
        }
    }

    public void i(@NonNull int msg) {
        if(TofuConfig.isDebug()) {
            Log.i(TAG, String.valueOf(msg));
        }
    }

    public void e(@NonNull int msg) {
        if(TofuConfig.isDebug()) {
            Log.e(TAG, String.valueOf(msg));
        }
    }

    public void v(@NonNull long msg) {
        if(TofuConfig.isDebug()) {
            Log.v(TAG, String.valueOf(msg));
        }
    }

    public void d(@NonNull long msg) {
        if(TofuConfig.isDebug()) {
            Log.d(TAG, String.valueOf(msg));
        }
    }

    public void i(@NonNull long msg) {
        if(TofuConfig.isDebug()) {
            Log.i(TAG, String.valueOf(msg));
        }
    }

    public void e(@NonNull long msg) {
        if(TofuConfig.isDebug()) {
            Log.e(TAG, String.valueOf(msg));
        }
    }

    public void v(@NonNull float msg) {
        if(TofuConfig.isDebug()) {
            Log.v(TAG, String.valueOf(msg));
        }
    }

    public void d(@NonNull float msg) {
        if(TofuConfig.isDebug()) {
            Log.d(TAG, String.valueOf(msg));
        }
    }

    public void i(@NonNull float msg) {
        if(TofuConfig.isDebug()) {
            Log.i(TAG, String.valueOf(msg));
        }
    }

    public void e(@NonNull float msg) {
        if(TofuConfig.isDebug()) {
            Log.e(TAG, String.valueOf(msg));
        }
    }

    public void v(@NonNull double msg) {
        if(TofuConfig.isDebug()) {
            Log.v(TAG, String.valueOf(msg));
        }
    }

    public void d(@NonNull double msg) {
        if(TofuConfig.isDebug()) {
            Log.d(TAG, String.valueOf(msg));
        }
    }

    public void i(@NonNull double msg) {
        if(TofuConfig.isDebug()) {
            Log.i(TAG, String.valueOf(msg));
        }
    }

    public void e(@NonNull double msg) {
        if(TofuConfig.isDebug()) {
            Log.e(TAG, String.valueOf(msg));
        }
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

        public pBuilder pln(@NonNull String msg){
            ps.append(msg+" \n");
            return this;
        }

        public pBuilder pln(@NonNull int msg){
            ps.append(msg+" \n");
            return this;
        }

        public pBuilder pln(@NonNull float msg){
            ps.append(msg+" \n");
            return this;
        }

        public pBuilder pln(@NonNull long msg){
            ps.append(msg+" \n");
            return this;
        }

        public pBuilder pln(@NonNull double msg){
            ps.append(msg+" \n");
            return this;
        }

        public pBuilder peq(@NonNull String msg){
            ps.append(msg+" = ");
            return this;
        }

        public pBuilder peq(@NonNull int msg){
            ps.append(msg+" = ");
            return this;
        }

        public pBuilder peq(@NonNull float msg){
            ps.append(msg+" = ");
            return this;
        }

        public pBuilder peq(@NonNull long msg){
            ps.append(msg+" = ");
            return this;
        }

        public pBuilder peq(@NonNull double msg){
            ps.append(msg+" = ");
            return this;
        }

        public void v(){
            if(TofuConfig.isDebug()) {
                Log.v(TAG, ps.toString());
            }
        }

        public void d(){
            if(TofuConfig.isDebug()) {
                Log.d(TAG, ps.toString());
            }
        }

        public void i(){
            if(TofuConfig.isDebug()) {
                Log.i(TAG, ps.toString());
            }
        }

        public void e(){
            if(TofuConfig.isDebug()) {
                Log.e(TAG, ps.toString());
            }
        }
    }

    @Override
    public void unbind() {

    }
}
