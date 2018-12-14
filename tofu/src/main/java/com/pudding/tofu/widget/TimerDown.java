package com.pudding.tofu.widget;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import com.pudding.tofu.model.Tofu;

public class TimerDown {

    private static class instance{
        private static TimerDown INSTANCE = new TimerDown();
    }



    public static TimerDown get(){
        return instance.INSTANCE;
    }

    /**
     * 计时器
     */
    private CountDownTimer timer;

    /**
     * 倒计时时间间隔1s
     * @param time
     * @param onTickLabel
     * @param onFinishLabel
     */
    public synchronized void countdown(int time, @NonNull final String onTickLabel, @NonNull final String onFinishLabel) {
        if (time < 0) time = 0;
        final int countTime = time;
        stop();//停止前一次调用的倒计时
        // 注意：倒计时时间都是毫秒。倒计时总时间+间隔
        timer = new CountDownTimer(countTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Tofu.go().to(onTickLabel,millisUntilFinished);
            }
            @Override
            public void onFinish() {
                Tofu.go().to(onFinishLabel);
            }
        }.start();// 调用CountDownTimer对象的start()方法开始倒计时，也不涉及到线程处理
    }


    /**
     * 倒计时
     * @param time 总时间
     * @param interval 间隔时间
     * @param onTickLabel 过程回调
     * @param onFinishLabel 完成回调
     */
    public synchronized void countdown(long time , long interval, @NonNull final String onTickLabel, @NonNull final String onFinishLabel) {
        if (time < 0) time = 0;
        if (interval < 0) interval = 1000;
        final long countTime = time;
        stop();//停止前一次调用的倒计时
        // 注意：倒计时时间都是毫秒。倒计时总时间+间隔
        timer = new CountDownTimer(countTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                Tofu.go().to(onTickLabel,millisUntilFinished);
            }
            @Override
            public void onFinish() {
                Tofu.go().to(onFinishLabel);
            }
        }.start();// 调用CountDownTimer对象的start()方法开始倒计时，也不涉及到线程处理
    }


    public void stop(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
