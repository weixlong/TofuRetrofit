package com.pudding.tofu.model;

import android.support.annotation.NonNull;

import com.pudding.tofu.widget.CollectUtil;

import net.tsz.afinal.FinalDb;

import java.util.List;

/**
 * Created by wxl on 2018/7/27 0027.
 * 邮箱：632716169@qq.com
 */

public class OrmBuilder implements UnBind {


    private Class<?> clazz;

    private FinalDb orm;

    /**
     * 设置数据库名
     *
     * @param
     * @return
     */
    protected OrmBuilder setOrm(FinalDb orm) {
        this.orm = orm;
        return this;
    }

    /**
     * 设置操作表
     *
     * @param clazz
     * @return
     */
    public <Bean> OrmBuilder table(@NonNull Class<Bean> clazz) {
        this.clazz = clazz;
        return this;
    }


    /**
     * 查询
     *
     * @param <Bean>
     * @return
     */
    public <Bean> List<Bean> query() {
        checkParamAvailable();
        List<Bean> list = (List<Bean>) orm.findAll(clazz);
        this.clazz = null;
        return list;
    }

    /**
     * 查询某一条数据
     * @param id
     * @param <Bean>
     * @return
     */
    public <Bean> Bean query(Object id){
        checkParamAvailable();
        Bean bean = (Bean) orm.findById(id, clazz);
        this.clazz = null;
        return bean;
    }

    /**
     * 查询第一条
     *
     * @param <Bean>
     * @return
     */
    public <Bean> Bean queryFirst() {
        checkParamAvailable();
        List<Bean> list = (List<Bean>) orm.findAll(clazz);
        if (CollectUtil.isEmpty(list)) {
            this.clazz = null;
            return null;
        }
        this.clazz = null;
        return list.get(0);
    }

    /**
     * 删除某条数据
     *
     * @param bean
     * @param <Bean>
     */
    public <Bean> void delete(Bean bean) {
        checkParamAvailable();
        orm.delete(bean);
        this.clazz = null;
    }

    /**
     * 条件删除
     *
     * @param where
     */
    public void deleteWhere(String where) {
        checkParamAvailable();
        orm.deleteByWhere(clazz, where);
        this.clazz = null;
    }

    /**
     * 条件删除
     * @param id
     */
    public void deleteById(Object id){
        checkParamAvailable();
        orm.deleteById(clazz,id);
        this.clazz = null;
    }


    /**
     * 清除全部
     */
    public void clear() {
        checkParamAvailable();
        orm.deleteAll(clazz);
        this.clazz = null;
    }

    /**
     * 保存
     *
     * @param bean
     * @param <Bean>
     */
    public <Bean> void save(Bean bean) {
        checkParamAvailable();
        orm.save(bean);
        this.clazz = null;
    }

    /**
     * 表中只保存这一条数据
     * @param bean
     * @param <Bean>
     */
    public <Bean> void saveOnly(Bean bean){
        checkParamAvailable();
        orm.deleteAll(clazz);
        save(bean);
    }

    /**
     * 更新
     * @param bean
     * @param <Bean>
     */
    public <Bean> void update(Bean bean) {
        checkParamAvailable();
        orm.update(bean);
        this.clazz = null;
    }

    /**
     * 条件更新
     * @param bean
     * @param where
     * @param <Bean>
     */
    public <Bean> void updateWhere(Bean bean,String where){
        checkParamAvailable();
        orm.update(bean,where);
        this.clazz = null;
    }

    /***
     * 删除表
     */
    public void dropTable(){
        checkParamAvailable();
        orm.dropTable(clazz);
        this.clazz = null;
    }


    /**
     * 参数检查
     */
    private void checkParamAvailable() {

        if(orm == null){
            throw new IllegalArgumentException("your need initialize tofu before initialize FinalDb ,please call TofuKnife.initialize(Context,String) !!");
        }

        if (clazz == null) {
            throw new IllegalArgumentException("your clazz table is null !!");
        }
    }

    protected OrmBuilder() {

    }

    @Override
    public void unbind() {
        clazz = null;
    }
}
