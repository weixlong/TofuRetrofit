package com.pudding.tofu.rx.tofu;

import android.support.annotation.NonNull;

import com.pudding.tofu.model.Tofu;
import com.pudding.tofu.rx.callback.UnBindRx;
import com.pudding.tofu.rx.widget.PayBuilder;
import com.pudding.tofu.rx.widget.PayFactory;
import com.pudding.tofu.rx.widget.RefreshBuilder;
import com.pudding.tofu.rx.widget.RefreshFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 */

public class TofuRx {

    /**
     * unbinds
     */
    private static HashMap<String,UnBindRx> targets = new HashMap<>();


    /**
     * 绑定对象
     * @param target
     */
    public static void bind(@NonNull Object target){
        TofuBusRx.get().bindTarget(target);
        Tofu.bind(target);
    }


    /**
     * 刷新管理
     * @param
     * @return
     */
    public static RefreshBuilder smart(){
        RefreshFactory factory = (RefreshFactory) targets.get("smart");
        if(factory == null){
            factory = RefreshFactory.get();
            targets.put("smart",factory);
        }
        return factory.builder();
    }

    /**
     * 支付
     * @return
     */
    public static PayBuilder pay(){
        PayFactory factory = (PayFactory) targets.get("pay");
        if(factory == null){
            factory = PayFactory.get();
            targets.put("pay",factory);
        }
        return factory.build();
    }





    /**
     * 解绑
     * @param target
     */
    public static void unBind(@NonNull Object target){
        Iterator<Map.Entry<String, UnBindRx>> iterator = targets.entrySet().iterator();
        while (iterator.hasNext()){
            iterator.next().getValue().unBind();
        }
        targets.clear();
        if(target != null) {
            TofuBusRx.get().unBindTarget(target);
            Tofu.unBind(target);
        }
    }
}
