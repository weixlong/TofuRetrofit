package com.tofu.retrofit;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pudding.tofu.TofuSupportActivity;
import com.pudding.tofu.model.AnimBuilder;
import com.pudding.tofu.model.Tofu;
import com.pudding.tofu.retention.post;
import com.pudding.tofu.retention.postError;
import com.pudding.tofu.retention.subscribe;
import com.tofu.retrofit.orm.Text;


public class MainActivity extends TofuSupportActivity {

    public static final String update_sex_url = "http://www.kangyu.com/api/user/modify_sex";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View viewById = findViewById(R.id.main_text);
        findViewById(R.id.main_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Text text = new Text();
////                text.insert();
                text.in("999", 132465);
//                text.save();
                Tofu.orm().table(Text.class).save(text);
                Tofu.post().result(String.class)
                        .label("789")
                        .asDialog(v.getContext())
                        .url(update_sex_url)
                        .put("user_id", "23")
                        .put("sex", "0")
                        .execute();

            }
        });

        Tofu.anim().playOn()
                .move(Tofu.anim().move().target(viewById).moveTo(300, 300))
                .cubic(Tofu.anim().cubic().target(viewById).duration(3000).begin(300, 300).end(100, 600).spin(50, 400, 400, 500))
                .together(Tofu.anim().together()
                        .quad(Tofu.anim().quad().target(viewById).duration(4000).end(100, 600).spin(50, 50))
                        .alpha(Tofu.anim().alpha().target(viewById).duration(6000).alphaValues(1, 0, 1)))
                .rotate(Tofu.anim().rotate().target(viewById).duration(4000).values(180, 0, 180))
                .alpha(Tofu.anim().alpha().target(viewById).duration(5000).alphaValues(1, 0, 1))
                .color(Tofu.anim().color().target(viewById).duration(2000).text(true).colorValues(0xFFAAFFFF, 0xff78c5f9))
                .start();
//        Tofu.anim().color().text(true).target(viewById).duration(5000).colorValues(0xFFFFFFFF, 0xff78c5f9).setRepeatCount(-1).start();
//        Tofu.anim().target(viewById).duration(2000).quad().end(300,300).spin(100,-100).start();
//        Tofu.anim().target(viewById).rotate().rotateX().setRepeatCount(-1).setRepeatMode(AnimBuilder.RESTART).values(180,0,90).start();
    }

    @subscribe("789")
    public void p(String m) {
        System.out.println(m);
    }

    @post("789")
    protected void post(String m) {
        System.out.println("post: " + m);
        Text o = Tofu.orm().table(Text.class).queryFirst();
        System.out.println(o.textId);
    }


    @postError("789")
    public void postE(String m) {
//        List<Text> users = SQLite.select().from(Text.class).queryList();// 查询所有记录

//        Text user = SQLite.select().from(Text.class).querySingle();//   查询第一条记录
//        Text t = (Text) Tofu.orm(this).setClass(Text.class).queryFirst();
        Text o = Tofu.orm().table(Text.class).queryFirst();
        System.out.println(o.textId);
    }
}
