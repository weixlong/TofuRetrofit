package com.pudding.tofu.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void v(@NonNull Boolean b){
        if(TofuConfig.isDebug()) {
            Log.v(TAG, b+"");
        }
    }

    public void d(@NonNull Boolean b){
        if(TofuConfig.isDebug()) {
            Log.d(TAG, b+"");
        }
    }

    public void i(@NonNull Boolean b){
        if(TofuConfig.isDebug()) {
            Log.i(TAG, b+"");
        }
    }

    public void e(@NonNull Boolean b){
        if(TofuConfig.isDebug()) {
            Log.e(TAG, b+"");
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
     * Object 转 json字符串打印
     * @param o
     */
    public void jov(@NonNull Object o) {
        if(TofuConfig.isDebug()) {
            Log.v(TAG, JSONObject.toJSONString(o));
        }
    }

    /**
     * Object 转 json字符串打印
     * @param o
     */
    public void jod(@NonNull Object o) {
        if(TofuConfig.isDebug()) {
            Log.d(TAG, JSONObject.toJSONString(o));
        }
    }

    /**
     * Object 转 json字符串打印
     * @param o
     */
    public void joi(@NonNull Object o) {
        if(TofuConfig.isDebug()) {
            Log.i(TAG, JSONObject.toJSONString(o));
        }
    }

    /**
     * Object 转 json字符串打印
     * @param o
     */
    public void joe(@NonNull Object o) {
        if(TofuConfig.isDebug()) {
            Log.e(TAG, JSONObject.toJSONString(o));
        }
    }


    /**
     * Array 转 json字符串打印
     * @param list
     */
    public void jav(@NonNull List list) {
        if(TofuConfig.isDebug()) {
            Log.v(TAG, JSONArray.toJSONString(list));
        }
    }

    /**
     * Array 转 json字符串打印
     * @param
     */
    public void jad(@NonNull List list) {
        if(TofuConfig.isDebug()) {
            Log.d(TAG, JSONArray.toJSONString(list));
        }
    }

    /**
     * Array 转 json字符串打印
     * @param
     */
    public void jai(@NonNull List list) {
        if(TofuConfig.isDebug()) {
            Log.i(TAG, JSONArray.toJSONString(list));
        }
    }

    /**
     * Array 转 json字符串打印
     * @param
     */
    public void jae(@NonNull List list) {
        if(TofuConfig.isDebug()) {
            Log.e(TAG, JSONArray.toJSONString(list));
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
            ps.append(msg);
            return this;
        }

        /**
         * Object 转 jsonString
         * @param o
         * @return
         */
        public pBuilder jop(@NonNull Object o){
            ps.append(JSONObject.toJSONString(o));
            return this;
        }

        /**
         * List 转 jsonString
         * @param o
         * @return
         */
        public pBuilder jap(@NonNull List o){
            ps.append(JSONArray.toJSONString(o));
            return this;
        }

        public pBuilder p(@NonNull Boolean msg){
            ps.append(msg);
            return this;
        }

        public pBuilder p(@NonNull int msg){
            ps.append(msg);
            return this;
        }

        public pBuilder p(@NonNull float msg){
            ps.append(msg);
            return this;
        }

        public pBuilder p(@NonNull long msg){
            ps.append(msg);
            return this;
        }

        public pBuilder p(@NonNull double msg){
            ps.append(msg);
            return this;
        }

        public pBuilder ps(@NonNull String msg){
            ps.append(msg+" ");
            return this;
        }

        /**
         * Object 转 jsonString
         * @param o
         * @return
         */
        public pBuilder jops(@NonNull Object o){
            ps.append(JSONObject.toJSONString(o)+" ");
            return this;
        }

        /**
         * List 转 jsonString
         * @param o
         * @return
         */
        public pBuilder japs(@NonNull List o){
            ps.append(JSONArray.toJSONString(o)+" ");
            return this;
        }

        public pBuilder ps(@NonNull int msg){
            ps.append(msg+" ");
            return this;
        }

        public pBuilder ps(@NonNull float msg){
            ps.append(msg+" ");
            return this;
        }

        public pBuilder ps(@NonNull long msg){
            ps.append(msg+" ");
            return this;
        }

        public pBuilder ps(@NonNull double msg){
            ps.append(msg+" ");
            return this;
        }

        public pBuilder ps(@NonNull Boolean msg){
            ps.append(msg+" ");
            return this;
        }

        public pBuilder ps(){
            ps.append(" ");
            return this;
        }

        public pBuilder pln(){
            ps.append("\n");
            return this;
        }

        public pBuilder pon(){
            ps.append(" : ");
            return this;
        }

        /**
         * Object 转 jsonString
         * @param o
         * @return
         */
        public pBuilder jopln(@NonNull Object o){
            ps.append(JSONObject.toJSONString(o)+" \n");
            return this;
        }

        /**
         * List 转 jsonString
         * @param o
         * @return
         */
        public pBuilder japln(@NonNull List o){
            ps.append(JSONArray.toJSONString(o)+" \n");
            return this;
        }

        public pBuilder pon(@NonNull String msg){
            ps.append(msg+" : ");
            return this;
        }

        public pBuilder pon(@NonNull int msg){
            ps.append(msg+" : ");
            return this;
        }

        public pBuilder pon(@NonNull float msg){
            ps.append(msg+" : ");
            return this;
        }

        public pBuilder pon(@NonNull long msg){
            ps.append(msg+" : ");
            return this;
        }

        public pBuilder pon(@NonNull double msg){
            ps.append(msg+" : ");
            return this;
        }

        public pBuilder pon(@NonNull Boolean msg){
            ps.append(msg+" : ");
            return this;
        }

        /**
         * Object 转 jsonString
         * @param o
         * @return
         */
        public pBuilder jopon(@NonNull Object o){
            ps.append(JSONObject.toJSONString(o)+" : ");
            return this;
        }

        /**
         * List 转 jsonString
         * @param o
         * @return
         */
        public pBuilder japon(@NonNull List o){
            ps.append(JSONArray.toJSONString(o)+" : ");
            return this;
        }

        public pBuilder pln(@NonNull String msg){
            ps.append(msg+" \n");
            return this;
        }

        public pBuilder pln(@NonNull Boolean msg){
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

        public pBuilder peq(@NonNull Boolean msg){
            ps.append(msg+" = ");
            return this;
        }

        /**
         * Object 转 jsonString
         * @param o
         * @return
         */
        public pBuilder jopeq(@NonNull Object o){
            ps.append(JSONObject.toJSONString(o)+" = ");
            return this;
        }

        /**
         * List 转 jsonString
         * @param o
         * @return
         */
        public pBuilder japeq(@NonNull List o){
            ps.append(JSONArray.toJSONString(o)+" = ");
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
