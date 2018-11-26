package com.pudding.tofu.widget;

import android.os.CountDownTimer;

import com.pudding.tofu.model.Tofu;

public class TimerDown {

    private static TimerDown down = new TimerDown();

    public static TimerDown get(){
        synchronized (TimerDown.class){
            return down;
        }
    }

    private CountDownTimer timer;

    public synchronized void countdown(int time, final String onTickLabel, final String onFinishLabel) {
        if (time < 0) time = 0;
        final int countTime = time;
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


    public void stop(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
