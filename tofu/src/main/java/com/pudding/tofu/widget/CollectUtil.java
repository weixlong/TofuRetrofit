package com.pudding.tofu.widget;

import java.util.Collection;
import java.util.Map;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class CollectUtil {

    public static boolean isEmpty(Collection list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Object[] args) {
        if (args == null || args.length == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Collection list) {
        return !isEmpty(list);
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(Object[] args) {
        return !isEmpty(args);
    }
}
