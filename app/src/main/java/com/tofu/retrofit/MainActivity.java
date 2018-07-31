package com.tofu.retrofit;

import android.os.Bundle;
import android.view.View;

import com.pudding.tofu.BaseActivity;
import com.pudding.tofu.model.Tofu;
import com.pudding.tofu.retention.post;
import com.pudding.tofu.retention.postError;
import com.pudding.tofu.retention.subscribe;
import com.tofu.retrofit.orm.Text;


public class MainActivity extends BaseActivity {

    public static final String update_sex_url = "http://www.kangyu.com/api/user/modify_sex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Text text = new Text();
////                text.insert();
                text.in("999",132465);
//                text.save();
                Tofu.orm().table(Text.class).save(text);
                Tofu.post().result(String.class)
                        .label("789")
                        .asDialog(v.getContext())
                        .url(update_sex_url)
                        .put("user_id","23")
                        .put("sex","0")
                        .execute();

            }
        });

    }

    @subscribe("789")
    public void p(String m){
        System.out.println(m);
    }

    @post("789")
    protected void post(String m){
        System.out.println("post: "+m);
        Text o = Tofu.orm().table(Text.class).queryFirst();
        System.out.println(o.textId);
    }


    @postError("789")
    public void postE(String m){
//        List<Text> users = SQLite.select().from(Text.class).queryList();// 查询所有记录

//        Text user = SQLite.select().from(Text.class).querySingle();//   查询第一条记录
//        Text t = (Text) Tofu.orm(this).setClass(Text.class).queryFirst();
        Text o = Tofu.orm().table(Text.class).queryFirst();
        System.out.println(o.textId);
    }
}
