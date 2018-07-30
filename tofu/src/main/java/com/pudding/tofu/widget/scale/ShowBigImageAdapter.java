package com.pudding.tofu.widget.scale;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
import com.pudding.tofu.R;

import java.util.List;

/**
 * Created by GPC on 2017/6/8.
 * wxl 2017/6/8
 */

public class ShowBigImageAdapter extends PagerAdapter {

    private Context context;

    private List<String> paths;

    public ShowBigImageAdapter(Context context, List<String> paths) {
        this.context = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getItemView(position);
        container.addView(view);
        return view;
    }

    private View getItemView(int position) {
        View itemView  = LayoutInflater.from(context).inflate(R.layout.show_big_pic_item_layout,null);
        final PhotoDraweeView photoView = (PhotoDraweeView) itemView.findViewById(R.id.show_big_image_item_image);
        if(UriUtil.isNetworkUri(Uri.parse(paths.get(position)))){
            photoView.setPhotoUri(Uri.parse(paths.get(position)));
        } else {
            Toast.makeText(context, "图片获取失败", Toast.LENGTH_SHORT).show();
        }
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                ((Activity)(context)).finish();
            }

        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
