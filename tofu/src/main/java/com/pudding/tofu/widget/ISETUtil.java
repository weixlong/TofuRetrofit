package com.pudding.tofu.widget;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;

import com.pudding.tofu.model.Tofu;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ISETUtil {

    private List<EM> ems = new ArrayList<>();


    private static class instance {
        private static final ISETUtil INSTANCE = new ISETUtil();
    }


    private ISETUtil() {
        ems.clear();
    }


    /**
     * 判断按添加先后顺序执行
     *
     * @return
     */
    public static ISETUtil get() {
        return instance.INSTANCE;
    }

    /**
     * 添加判断对象
     *
     * @param et
     * @param err
     * @return
     */
    public ISETUtil isNotEmpty(@NonNull EditText et, @NonNull String err) {
        ems.add(new EM(et, err, "isNotEmpty"));
        return this;
    }

    /**
     * 判断不为空
     *
     * @param em
     * @return
     */
    private boolean isNotEmpty(EM em) {
        return !TextUtils.isEmpty(em.et.getText());
    }


    /**
     * 是否为手机号
     *
     * @param et
     * @param err
     * @return
     */
    public ISETUtil isMobile(@NonNull EditText et, @NonNull String err) {
        ems.add(new EM(et, err, "isMobile"));
        return this;
    }

    /**
     * 是否为手机号
     *
     * @param em
     * @return
     */
    private boolean isMobile(EM em) {
        return ValidatorUtil.isMobile(em.et.getText().toString());
    }

    /**
     * 两个输入是否相同
     *
     * @param et1
     * @param et2
     * @param err
     * @return
     */
    public ISETUtil equals(@NonNull EditText et1, @NonNull EditText et2, @NonNull String err) {
        ems.add(new EM(et1, et2, err, "equals"));
        return this;
    }

    /**
     * 是否输入的相同
     *
     * @param em
     * @return
     */
    private boolean equals(EM em) {
        return TextUtils.equals(em.et.getText(), em.et1.getText());
    }


    /**
     * 是否为邮箱
     *
     * @param et
     * @param err
     * @return
     */
    public ISETUtil isEmail(@NonNull EditText et, @NonNull String err) {
        ems.add(new EM(et, err, "isEmail"));
        return this;
    }

    /**
     * 是否为邮箱
     *
     * @param em
     * @return
     */
    private boolean isEmail(EM em) {
        return ValidatorUtil.isEmail(em.et.getText().toString());
    }


    /**
     * 是否为身份证号
     *
     * @param et
     * @param err
     * @return
     */
    public ISETUtil isIDCard(@NonNull EditText et, @NonNull String err) {
        ems.add(new EM(et, err, "isIDCard"));
        return this;
    }


    /**
     * 是否为身份证号
     *
     * @param em
     * @return
     */
    private boolean isIDCard(EM em) {
        return ValidatorUtil.isIDCard(em.et.getText().toString());
    }


    /**
     * 是否为用户名
     *
     * @param et
     * @param err
     * @return
     */
    public ISETUtil isUserName(@NonNull EditText et, @NonNull String err) {
        ems.add(new EM(et, err, "isUserName"));
        return this;
    }


    /**
     * 是否为用户名
     *
     * @param em
     * @return
     */
    private boolean isUserName(EM em) {
        return ValidatorUtil.isUsername(em.et.getText().toString());
    }

    /**
     * 是否为数字
     * @param et
     * @param err
     * @param digit 位数
     * @return
     */
    public ISETUtil isNumber(@NonNull EditText et, @IntRange(from = 0) int digit, @NonNull String err){
        ems.add(new EM(et, err, "isNumber",digit));
        return this;
    }

    /**
     * 判断是否是符合位数的整数
     * @param em
     * @return
     */
    private boolean isNumber(EM em){
        String s = em.et.getText().toString();
        try {
            if(s.length() != em.digit){
                return false;
            }
            Long.parseLong(s);
        } catch (Exception e){
            return false;
        }
        return true;
    }


    /**
     * 判断以上添加是否全部符合条件，否则弹出第一个不符合的提示并返回
     *
     * @return
     */
    public boolean is() {
        try {
            for (EM em : ems) {
                Method declaredMethod = this.getClass().getDeclaredMethod(em.model, EM.class);
                declaredMethod.setAccessible(true);
                boolean r = (boolean) declaredMethod.invoke(this, em);
                if (!r) {
                    if(!TextUtils.isEmpty(em.msg)) {
                        Tofu.tu().tell(em.msg);
                    }
                    ems.clear();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ems.clear();
            return false;
        }
        ems.clear();
        return true;
    }


    private class EM {
        private EditText et;
        private String msg;
        private EditText et1;
        private String model;
        private int digit;

        public EM(EditText et, String msg, String model) {
            this.et = et;
            this.msg = msg;
            this.model = model;
        }

        public EM(EditText et, EditText et1, String msg, String model) {
            this.et = et;
            this.msg = msg;
            this.et1 = et1;
            this.model = model;
        }

        public EM(EditText et, String msg, String model, int digit) {
            this.et = et;
            this.msg = msg;
            this.model = model;
            this.digit = digit;
        }
    }
}
