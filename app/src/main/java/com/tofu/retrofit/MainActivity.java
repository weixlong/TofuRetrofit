package com.tofu.retrofit;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.pudding.tofu.TofuSupportActivity;
import com.pudding.tofu.model.AnimBuilder;
import com.pudding.tofu.model.Tofu;
import com.pudding.tofu.retention.pierce;
import com.pudding.tofu.retention.post;
import com.pudding.tofu.retention.postError;
import com.pudding.tofu.retention.subscribe;
import com.pudding.tofu.retention.tio;
import com.tofu.retrofit.orm.Text;


public class MainActivity extends TofuSupportActivity {

    public static final String update_sex_url = "http://www.kangyu.com/api/user/modify_sex";

    View viewById, viewById1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewById = findViewById(R.id.main_text);
        viewById1 = findViewById(R.id.main_text1);

        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Text text = new Text();
//////                text.insert();
//                text.in("999", 132465);
////                text.save();
//                Tofu.orm().table(Text.class).save(text);
//                Tofu.post().result(String.class)
//                        .label("789")
//                        .asDialog(v.getContext())
//                        .url(update_sex_url)
//                        .put("user_id", "23")
//                        .put("sex", "0")
//                        .execute();
//                Tofu.tio().io().to("789");
//                Tofu.go().pierceTo("a", "ccc");
//                Tofu.anim().playOn().setAnimRunBack(new AnimBuilder.AnimRunAdapter() {
//                    @Override
//                    public void onPlayOnAnimEnd(AnimBuilder.PlayOnBuilder builder) {
//                        Tofu.log().ping().p("onPlayOnAnimEnd").v();
//                    }
//                })
//                        // .move(Tofu.anim().move(viewById).duration(4000).moveTo(viewById.getX(),viewById.getY()).moveTo(300, 300).moveTo(200,400))
//                        .move(Tofu.anim().move(viewById1).duration(4000).moveTo(viewById1.getX(), viewById1.getY()).moveTo(500, 300).delay(2000))
//                        .cubic(Tofu.anim().cubic(viewById).duration(3000).begin(300, 300).end(100, 600).spin(50, 400, 400, 500))
//                        .color(Tofu.anim().color(viewById).duration(2000).text(true).colorValues(0xFFAAFFFF, 0xff78c5f9))
//                        .together(Tofu.anim().together().setAnimRunBack(new AnimBuilder.AnimRunAdapter() {
//                                    @Override
//                                    public void onTogetherAnimStart(AnimBuilder.TogetherBuilder builder) {
//                                        Tofu.log().ping().p("onTogetherAnimStart").v();
//                                    }
//
//                                    @Override
//                                    public void onTogetherAnimEnd(AnimBuilder.TogetherBuilder builder) {
//                                        Tofu.log().ping().p("onTogetherAnimEnd").v();
//
//                                    }
//                                }).color(Tofu.anim().color(viewById1).duration(2000).text(true).colorValues(0xFFAA0077, 0xff2536AA))
//                                        .move(Tofu.anim().move(viewById1).duration(4000).moveTo(500, 500))
//                                        .move(Tofu.anim().move(viewById).duration(4000).moveTo(viewById.getX(), viewById.getY()).moveTo(500, 300).delay(2000))
//                                        .alpha(Tofu.anim().alpha(viewById).duration(4000).alphaValues(1, 0, 1))
//                                //.move(Tofu.anim().move(viewById).duration(5000).moveTo(200,300))
//                        )
//                        .rotate(Tofu.anim().rotate(viewById).duration(4000).values(180, 0, 180))
//                        .alpha(Tofu.anim().alpha(viewById).duration(5000).alphaValues(1, 0, 1))
//                        .color(Tofu.anim().color(viewById).duration(2000).text(true).colorValues(0xFFAAFFFF, 0xff78c5f9))
//                        .start();

            }
        });

//        LoadDialog dialog = new LoadDialog(this,"加载中...");
//        dialog.showDialog();

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

    @tio("789")
    private void tio(){
        System.out.println("execute method in io or thread .");
    }


    @postError("789")
    public void postE(String m) {
//        List<Text> users = SQLite.select().from(Text.class).queryList();// 查询所有记录
//        Text user = SQLite.select().from(Text.class).querySingle();//   查询第一条记录
//        Text t = (Text) Tofu.orm(this).setClass(Text.class).queryFirst();
        Text o = Tofu.orm().table(Text.class).queryFirst();
        System.out.println(o.textId);
    }

    @pierce("a")
    private void a(String c) {
        Tofu.log().d("a(c)");
    }

    @pierce("a")
    private void b(String b) {
        Tofu.log().d("b()");
    }


}
