package com.pudding.tofu;


import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by wxl on 2018/7/27 0027.
 * 邮箱：632716169@qq.com
 *
 * 参数必须有get和set方法
 */
@Table(name = "Text") //必须
public class OrmText {


    public String textId;

    @Id //必须
    public long id;

    public OrmText() {
    }



    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
