package com.tofu.retrofit.orm;


import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by wxl on 2018/7/27 0027.
 * 邮箱：632716169@qq.com
 */
@Table(name = "Text")
public class Text{


    public String textId;

    @Id
    public long id;

    public Text() {
    }

    public void in(String textId, long id) {
        this.textId = textId;
        this.id = id;
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
