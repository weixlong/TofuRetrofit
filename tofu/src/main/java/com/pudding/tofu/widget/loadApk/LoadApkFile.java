package com.pudding.tofu.widget.loadApk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.BIND_AUTO_CREATE;

public class LoadApkFile {

    private DownloadService.DownloadBinder mDownloadBinder;

    private String url;

    private Intent intent;

    private Context context;

    private Disposable mDisposable;

    private OnLoadApkFileListener onLoadApkFileListener;

    private String apk_name;

    private boolean isInstall,isRoot;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadBinder = (DownloadService.DownloadBinder) service;
            if (mDownloadBinder != null) {
                mDownloadBinder.setInstallMode(isRoot);
                long downloadId = mDownloadBinder.startDownload(url,apk_name,isInstall,onLoadApkFileListener);
                startCheckProgress(downloadId);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownloadBinder = null;
        }
    };


    /**
     * 开始下载
     */
    public void start() {
        intent = new Intent(context, DownloadService.class);
        context.startService(intent);
        context.bindService(intent, mConnection, BIND_AUTO_CREATE);//绑定服务
    }

    /**
     * 停止下载
     */
    public void stop() {
        if (mDisposable != null) {
            //取消监听
            mDisposable.dispose();
        }
        if(context != null) {
            context.unbindService(mConnection);
            context.stopService(intent);
        }
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public LoadApkFile setOnLoadApkFileListener(OnLoadApkFileListener listener) {
        this.onLoadApkFileListener = listener;
        return this;
    }

    /**
     * 是否需要root模式安装
     * @param isRoot
     * @return
     */
    public LoadApkFile isRootInstall(boolean isRoot){
        this.isRoot = isRoot;
        return this;
    }

    /**
     * 有参构造
     *
     * @param url
     * @param context
     */
    public LoadApkFile(String url, String apk_name,boolean isInstall,Context context) {
        this.url = url;
        this.context = context;
        this.apk_name = apk_name;
        this.isInstall = isInstall;
    }

    /**
     * 开始监听进度
     *
     * @param downloadId
     */
    private void startCheckProgress(final long downloadId) {
        Observable.interval(100, 200, TimeUnit.MILLISECONDS, Schedulers.io())//无限轮询,准备查询进度,在io线程执行
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) {
                        return mDownloadBinder != null;
                    }
                }).map(new Function<Long, Integer>() {
            @Override
            public Integer apply(Long aLong) {
                return mDownloadBinder.getProgress(downloadId);
            }
        }).takeUntil(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer >= 100;
            }
        }).distinct()//去重复
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressObserver());
    }


    //观察者
    private class ProgressObserver implements Observer<Integer> {

        @Override
        public void onSubscribe(Disposable d) {
            mDisposable = d;
        }

        @Override
        public void onNext(Integer progress) {
            if (onLoadApkFileListener != null) {
                onLoadApkFileListener.onProgress(progress);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            if (context != null) {
                Toast.makeText(context, "下载出错", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onComplete() {
            stop();
        }

    }
}
