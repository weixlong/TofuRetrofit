package com.pudding.tofu.callback;

import java.io.File;

/**
 * Created by wxl on 2018/6/24 0024.
 * 邮箱：632716169@qq.com
 */

public interface LoadFileCallback {
    void inProgress(long currentSize, long totalSize, float progress, long networkSpeed);
    void onError(String url,boolean isOutTime);
    void onResponse(File file);
}
