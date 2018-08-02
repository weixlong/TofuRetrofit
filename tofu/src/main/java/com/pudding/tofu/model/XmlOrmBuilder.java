package com.pudding.tofu.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Created by wxl on 2018/7/12 0012.
 * 邮箱：632716169@qq.com
 */

public class XmlOrmBuilder implements UnBind {

    private Context context;

    private SharedPreferences toFuShare;

    private SharedPreferences.Editor editor;

    private XmlBuilder builder;

    protected XmlOrmBuilder() {
    }

    protected XmlOrmBuilder onBindContext(Context c){
        context = c;
        return this;
    }

    /**
     * 获取默认包名的xml存储
     * @return
     */
    public XmlBuilder getXml(){
        checkParamAvailable();
        toFuShare = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        editor = toFuShare.edit();
        if(builder == null){
            builder = new XmlBuilder(toFuShare,editor);
        } else {
            builder.setXmlEditor(editor);
            builder.setXmlShare(toFuShare);
        }
        return builder;
    }

    /**
     * 获取指定名字的xml存储
     * @param name
     * @return
     */
    public XmlBuilder getXml(@NonNull String name){
        checkParamAvailable();
        toFuShare = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        editor = toFuShare.edit();
        if(builder == null){
            builder = new XmlBuilder(toFuShare,editor);
        } else {
            builder.setXmlEditor(editor);
            builder.setXmlShare(toFuShare);
        }
        return builder;
    }



    private void checkParamAvailable(){
        if(context == null)throw new IllegalArgumentException("your context is null , please call TofuKnife initialize . ");
    }



    public class XmlBuilder {

        private SharedPreferences xmlShare;

        private SharedPreferences.Editor xmlEditor;

        private XmlBuilder() {
        }

        private XmlBuilder(SharedPreferences xmlShare, SharedPreferences.Editor xmlEditor) {
            this.xmlShare = xmlShare;
            this.xmlEditor = xmlEditor;
        }

        private void setXmlShare(SharedPreferences xmlShare) {
            this.xmlShare = xmlShare;
        }

        private void setXmlEditor(SharedPreferences.Editor xmlEditor) {
            this.xmlEditor = xmlEditor;
        }

        public XmlBuilder put(@NonNull String key, @NonNull boolean value){
            xmlEditor.putBoolean(key,value);
            xmlEditor.commit();
            return this;
        }

        public XmlBuilder put(@NonNull String key, @NonNull String value){
            xmlEditor.putString(key,value);
            xmlEditor.commit();
            return this;
        }

        public XmlBuilder put(@NonNull String key, @NonNull int value){
            xmlEditor.putInt(key,value);
            xmlEditor.commit();
            return this;
        }

        public XmlBuilder put(@NonNull String key, @NonNull Long value){
            xmlEditor.putLong(key,value);
            xmlEditor.commit();
            return this;
        }

        public XmlBuilder put(@NonNull String key, @NonNull float value){
            xmlEditor.putFloat(key,value);
            xmlEditor.commit();
            return this;
        }

        public XmlBuilder put(@NonNull String key, @NonNull Set<String> values){
            xmlEditor.putStringSet(key,values);
            xmlEditor.commit();
            return this;
        }

        public XmlBuilder remove(String key){
            xmlEditor.remove(key);
            xmlEditor.commit();
            return this;
        }

        public boolean getBoolean(String key){
            return xmlShare.getBoolean(key,false);
        }

        public String getString(String key){
            return xmlShare.getString(key,"");
        }

        public long getLong(String key){
            return xmlShare.getLong(key,0);
        }

        public float getFloat(String key){
            return xmlShare.getFloat(key,0);
        }

        public int getInt(String key){
            return xmlShare.getInt(key,0);
        }

        public Set<String> getSet(String key){
            return xmlShare.getStringSet(key,null);
        }

        public void clear(){
            editor.clear();
            editor.commit();
        }
    }


    @Override
    public void unbind() {
        editor = null;
        toFuShare = null;
        builder = null;
    }
}
