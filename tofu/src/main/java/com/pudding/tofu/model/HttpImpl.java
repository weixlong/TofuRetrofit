package com.pudding.tofu.model;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.pudding.tofu.R;
import com.pudding.tofu.callback.PostInterface;
import com.pudding.tofu.callback.PostResultCallback;
import com.pudding.tofu.widget.LoadDialog;

import java.util.List;

import okhttp3.Cookie;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class HttpImpl<Result> implements PostInterface, PostResultCallback {


    /**
     * 参数集合
     */
    private HttpParams params;

    /**
     * 请求链接
     */
    private String url;

    /**
     * 请求返回结果
     */
    private Class<Result> resultClass;


    /**
     * 进度条
     */
    private LoadDialog dialog;

    /**
     * 自定义对话框
     */
    private AlertDialog diyDialog;

    /**
     * 实现
     */
    private PostImpl impl;

    private String runtimeName;

    private String label;

    private HttpHeaders headers;

    private List<Cookie> cookies;

    protected HttpImpl() {
    }

    protected HttpImpl(List<Cookie> cookies,HttpHeaders headers,HttpParams params, String url, Class<Result> resultClass,String label) {
        this.params = params;
        this.url = url;
        this.resultClass = resultClass;
        this.label = label;
        this.headers = headers;
        this.cookies  = cookies;
    }

    protected void setHttpImpl(List<Cookie> cookies,HttpHeaders headers,HttpParams params, String url, Class<Result> resultClass,String label) {
        this.params = params;
        this.url = url;
        this.resultClass = resultClass;
        this.label = label;
        this.headers = headers;
        this.cookies = cookies;
    }

    @Override
    public void execute() {
        OkGo.getInstance().setConnectTimeout(TofuConfig.getConnectTimeout());
        if (impl == null) {
            impl = new PostImpl();
        }
        impl.post(resultClass, url, cookies,params,headers, this);
        if (TofuConfig.isDebug()) {
            print();
        }
    }

    @Override
    public void unBind() {
        closeDialog();
        if(impl != null) {
            impl.cancelPost(url);
            impl = null;
        }
    }

    protected void stopPost(){
        closeDialog();
        if(impl != null) {
            impl.cancelPost(url);
        }
    }

    @Override
    public <Result> void onSuccess(String url, Result result) {
        if(TextUtils.isEmpty(label)) {
            TofuBus.get().executePostMethod(url, result);
        } else {
            TofuBus.get().executePostMethod(label, result);
        }
        closeDialog();
    }

    @Override
    public void onFailed(String response,boolean isOutTime) {
        if(TextUtils.isEmpty(label)) {
            TofuBus.get().executePostErrorMethod(response, response,isOutTime);
        } else {
            TofuBus.get().executePostErrorMethod(label, response,isOutTime);
        }
        closeDialog();
        if(TofuConfig.isDebug()) {
            System.err.println("Tofu : post is failed !");
        }
    }


    @Override
    public void showDialog(Context context) {
        String runningActivityName = getRunningActivityName(context);
        if(runtimeName == null){
            runtimeName = runningActivityName;
            dialog = new LoadDialog(context, R.style.dialog);
        } else {
            if(!TextUtils.equals(runtimeName,runningActivityName)){
                runtimeName = runningActivityName;
                dialog = new LoadDialog(context, R.style.dialog);
            }
        }
        if(dialog != null) {
            dialog.showDialog();
        }
    }

    /**
     * 显示自定义对话框
     * @param dialog
     */
    public void showDialog(AlertDialog dialog){
        if(dialog != null && !dialog.isShowing()) {
            this.diyDialog = dialog;
            dialog.show();
        }
    }

    /**
     * 获取当前窗口名
     * @param context
     * @return
     */
    private String getRunningActivityName(Context context) {
        String contextString = context.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }

    @Override
    public void closeDialog() {
        if (dialog != null) {
            dialog.closeDialog();
        }
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * debug打印路径
     */
    private void print() {
        String l = null;
        String p = params.toString();
        if (p.contains("[")) {
            l = p.replace("[", "").replace("]", "");
        }
        if (TextUtils.isEmpty(l)) {
            System.out.println("Tofu : " + url);
        } else {
            System.out.println("Tofu : " + url + "?" + l);
        }
    }
}
