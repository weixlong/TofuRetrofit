package com.pudding.tofu.widget.scale;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pudding.tofu.R;
import com.pudding.tofu.widget.CollectUtil;

import java.util.List;


public class ShowBigPic extends Activity implements ViewPager.OnPageChangeListener{
	ScaleViewPager iv_show_big_pic;
	List<String> paths;
	private TextView text;
	private int index;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.show_big_pic);
		text = (TextView) findViewById(R.id.iv_show_big_pic_position);
		iv_show_big_pic=(ScaleViewPager)findViewById(R.id.iv_show_big_pic_page);
		if(getIntent() != null) {
			paths = getIntent().getStringArrayListExtra("paths");
			index = getIntent().getIntExtra("position",0);
			initData();
		} else {
			finish();
		}
	}



	private void initData(){
		if(!CollectUtil.isEmpty(paths)){
			iv_show_big_pic.setAdapter(new ShowBigImageAdapter(this,paths));
			iv_show_big_pic.addOnPageChangeListener(this);
			iv_show_big_pic.setCurrentItem(index);
		} else {
			finish();
		}
		text.setText((index+1)+"/"+paths.size());
		iv_show_big_pic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		text.setText((position+1)+"/"+paths.size());
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
}
