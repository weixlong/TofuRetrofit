package com.pudding.tofu.widget.loadApk;

public interface OnLoadApkFileListener {

    /**
     * 进度
     * @param progress
     */
     void onProgress(int progress);

    /**
     * 下载完成
     */
    void onComplete(String path);
}
