package com.pudding.tofu.widget.scale;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pudding.tofu.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by wxl on 2018/6/26 0026.
 * 邮箱：632716169@qq.com
 */

public class ShowBigView extends Activity {

    private BigView bigView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_big_view);
        bigView = findViewById(R.id.show_big_view);
        try {
            String path  = getIntent().getStringExtra("path");
            File file = new File(path);
            FileInputStream fileInputStream = openFileInput(file.getPath());
            bigView.setImage(fileInputStream);
        } catch (FileNotFoundException e) {
            System.err.println("Tofu : "+e);
        }
    }
}
