package com.pudding.tofu.rx.widget;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.pudding.tofu.model.Tofu;
import com.pudding.tofu.rx.callback.UnBindRx;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wxl on 2018/8/20 0020.
 * 邮箱：632716169@qq.com
 */

public class PayBuilder implements UnBindRx {

    private IWXAPI api;

    protected PayBuilder() {
    }


    /**
     * 支付结果回调与微信回调一致，将会回调到package#wxapi.WXPayEntryActivity#onResp(BaseResp baseResp)方法中
     * <p>
     * 如没有注册该activity将不会回调
     * </p>
     *
     * @param sign 支付宝支付参数签名结果
     */
    public void payAli(@NonNull final Activity activity, @NonNull final String sign, final boolean isFinishActivity) {
        Observable.create(new ObservableOnSubscribe<Map<String, String>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, String>> e) throws Exception {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(sign, true);
                e.onNext(result);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Map<String, String>>() {
                    @Override
                    public void accept(Map<String, String> result) throws Exception {
                        PayResult payResult = new PayResult(result);
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals("9000", resultStatus)) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            makeToPayResult(activity, 0,isFinishActivity);
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            makeToPayResult(activity, -1,isFinishActivity);
                        }
                    }
                });

    }


    /**
     * 调起微信支付
     *
     * @param resp
     * <p></p>
     * 如需要关闭当前activity需要手动关闭
     */
    public void payWX(@NonNull Activity activity, @NonNull String appId, @NonNull WXPayResp resp) {
        api = WXAPIFactory.createWXAPI(activity, appId, false);
        if (isPayWxSupported()) {
            api.registerApp(appId);
            onCallWXPay(resp);
        } else {
            Tofu.tu().tell("微信版本不支持,请安装最新版本的微信");
            makeToPayResult(activity, -1,false);
        }
    }


    /**
     * 调取微信支付
     *
     * @param resp
     */
    private void onCallWXPay(@NonNull WXPayResp resp) {
        PayReq req = new PayReq();
        req.appId = resp.appid;
        req.packageValue = "Sign=WXPay";
        req.partnerId = resp.partnerid;
        req.prepayId = resp.prepayid;
        req.nonceStr = resp.noncestr;
        req.timeStamp = resp.timestamp;
        req.sign = resp.sign;
        api.sendReq(req);
    }

    /**
     * 手动跳转到结果页
     *
     * @param activity
     * @param errorCode
     */
    private void makeToPayResult(@NonNull Activity activity, int errorCode,boolean isFinishActivity) {
        try {
            PayResp payResp = new PayResp();
            payResp.errCode = errorCode;
            String act = activity.getPackageName() + ".wxapi.WXPayEntryActivity";
            Class WXPayEntryActivity_class = Class.forName(act);
            Intent intent = new Intent(activity, WXPayEntryActivity_class);
            activity.startActivity(intent);
            IWXAPIEventHandler handler = (IWXAPIEventHandler) WXPayEntryActivity_class.newInstance();
            handler.onResp(payResp);
            if(isFinishActivity) {
                activity.finish();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }


    /**
     * 是否支持微信支付
     *
     * @return
     */
    public boolean isPayWxSupported() {
        return api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    @Override
    public void unBind() {

    }
}
