package com.pudding.tofu.rx.tofu;

import android.text.TextUtils;

import com.pudding.tofu.rx.retion.loadMoreBefore;
import com.pudding.tofu.rx.retion.loadMoreFailed;
import com.pudding.tofu.rx.retion.loadMoreResult;
import com.pudding.tofu.rx.retion.refreshBefore;
import com.pudding.tofu.rx.retion.refreshFailed;
import com.pudding.tofu.rx.retion.refreshResult;
import com.pudding.tofu.widget.CollectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wxl on 2018/8/6 0006.
 * 邮箱：632716169@qq.com
 */

public class TofuBusRx {

    private volatile static TofuBusRx bus = new TofuBusRx();

    /**
     * 方法体
     */
    private class TofuMethod {
        Object target;
        Method method;
        String label;
        Class[] params;
        Object[] exe_params;
    }

    /**
     * 方法集合
     */
    private HashMap<String, HashMap<String, List<TofuMethod>>> busMethods = new HashMap<>();

    /**
     * 是否找到可以执行的方法
     */
    private boolean isFindAvailableMethod = false;

    /**
     * 目标集合
     */
    private List<String> keys = new ArrayList<>();

    private HashMap<String, Object> keyMap = new HashMap<>();

    private long position = 0;

    /**
     * 获取bus
     *
     * @return
     */
    public static TofuBusRx get() {
        synchronized (TofuBusRx.class) {
            return bus;
        }
    }

    /**
     * 绑定一个对象
     *
     * @param target
     */
    public synchronized void bindTarget(Object target) {
        String key = Long.toString(++position+System.currentTimeMillis());
        bindRetentionMethods(key,
                refreshResult.class.getName(),
                refreshFailed.class.getName(),
                refreshBefore.class.getName(),
                loadMoreBefore.class.getName(),
                loadMoreFailed.class.getName(),
                loadMoreResult.class.getName());
        keys.add(key);
        keyMap.put(key, target);
        findRetentionMethods(key, target);
    }

    /**
     * 初始化注解集合
     *
     * @param key
     * @param retentions
     */
    private void bindRetentionMethods(String key, String... retentions) {
        HashMap<String, List<TofuMethod>> map = busMethods.get(key);
        if (CollectUtil.isEmpty(map)) {
            map = new HashMap<>();
        }
        for (String retention : retentions) {
            List<TofuMethod> methodList = map.get(retention);
            if (CollectUtil.isEmpty(methodList)) {
                List<TofuMethod> methods = new ArrayList<>();
                map.put(retention, methods);
            }
        }
        busMethods.put(key, map);
    }


    /**
     * 保存注解方法
     *
     * @param target
     */
    private void findRetentionMethods(String key, Object target) {
        HashMap<String, List<TofuMethod>> map = busMethods.get(key);
        Method[] methods = target.getClass().getDeclaredMethods();
        if (methods != null) {
            for (Method method : methods) {
                findRefreshRetentionMethods(target, map, method);
                findRefreshFailedRetentionMethods(target, map, method);
                findRefreshBeforeRetentionMethods(target, map, method);
                findLoadBeforeRetentionMethods(target, map, method);
                findLoadRetentionMethods(target, map, method);
                findLoadFailedRetentionMethods(target, map, method);
            }
        }
    }

    /**
     * 找到注解方法
     *
     * @param map
     * @param method
     */
    private void findRefreshRetentionMethods(Object target, HashMap<String, List<TofuMethod>> map, Method method) {
        refreshResult r = method.getAnnotation(refreshResult.class);
        if (r != null) {
            String[] value = r.value();
            List<TofuMethod> methods = map.get(refreshResult.class.getName());
            if (CollectUtil.isEmpty(methods)) {
                methods = new ArrayList<>();
            }
            findRetentionLabelMethods(target, value, methods, method);
            map.put(refreshResult.class.getName(), methods);
        }
    }

    /**
     * 找到注解方法
     *
     * @param map
     * @param method
     */
    private void findRefreshFailedRetentionMethods(Object target, HashMap<String, List<TofuMethod>> map, Method method) {
        refreshFailed r = method.getAnnotation(refreshFailed.class);
        if (r != null) {
            String[] value = r.value();
            List<TofuMethod> methods = map.get(refreshFailed.class.getName());
            if (CollectUtil.isEmpty(methods)) {
                methods = new ArrayList<>();
            }
            findRetentionLabelMethods(target, value, methods, method);
            map.put(refreshFailed.class.getName(), methods);
        }
    }

    /**
     * 找到注解方法
     *
     * @param map
     * @param method
     */
    private void findRefreshBeforeRetentionMethods(Object target, HashMap<String, List<TofuMethod>> map, Method method) {
        refreshBefore r = method.getAnnotation(refreshBefore.class);
        if (r != null) {
            String[] value = r.value();
            List<TofuMethod> methods = map.get(refreshBefore.class.getName());
            if (CollectUtil.isEmpty(methods)) {
                methods = new ArrayList<>();
            }
            findRetentionLabelMethods(target, value, methods, method);
            map.put(refreshBefore.class.getName(), methods);
        }
    }


    /**
     * 找到注解方法
     *
     * @param map
     * @param method
     */
    private void findLoadBeforeRetentionMethods(Object target, HashMap<String, List<TofuMethod>> map, Method method) {
        loadMoreBefore r = method.getAnnotation(loadMoreBefore.class);
        if (r != null) {
            String[] value = r.value();
            List<TofuMethod> methods = map.get(loadMoreBefore.class.getName());
            if (CollectUtil.isEmpty(methods)) {
                methods = new ArrayList<>();
            }
            findRetentionLabelMethods(target, value, methods, method);
            map.put(loadMoreBefore.class.getName(), methods);
        }
    }

    /**
     * 找到注解方法
     *
     * @param map
     * @param method
     */
    private void findLoadRetentionMethods(Object target, HashMap<String, List<TofuMethod>> map, Method method) {
        loadMoreResult r = method.getAnnotation(loadMoreResult.class);
        if (r != null) {
            String[] value = r.value();
            List<TofuMethod> methods = map.get(loadMoreResult.class.getName());
            if (CollectUtil.isEmpty(methods)) {
                methods = new ArrayList<>();
            }
            findRetentionLabelMethods(target, value, methods, method);
            map.put(loadMoreResult.class.getName(), methods);
        }
    }


    /**
     * 找到注解方法
     *
     * @param map
     * @param method
     */
    private void findLoadFailedRetentionMethods(Object target, HashMap<String, List<TofuMethod>> map, Method method) {
        loadMoreFailed r = method.getAnnotation(loadMoreFailed.class);
        if (r != null) {
            String[] value = r.value();
            List<TofuMethod> methods = map.get(loadMoreFailed.class.getName());
            if (CollectUtil.isEmpty(methods)) {
                methods = new ArrayList<>();
            }
            findRetentionLabelMethods(target, value, methods, method);
            map.put(loadMoreFailed.class.getName(), methods);
        }
    }

    /**
     * 找到注解的方法
     *
     * @param labels
     * @param methods
     */
    private void findRetentionLabelMethods(Object target, String[] labels, List<TofuMethod> methods, Method method) {
        if (labels != null) {
            for (String label : labels) {
                TofuMethod tofuMethod = new TofuMethod();
                tofuMethod.target = target;
                tofuMethod.label = label;
                method.setAccessible(true);
                tofuMethod.method = method;
                tofuMethod.params = method.getParameterTypes();
                methods.add(tofuMethod);
            }
        }
    }


    /**
     * 执行刷新回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    public synchronized <Result> void executeLoadFailedRetention(String label, Result... results) {
        isFindAvailableMethod = false;
        for (int i = keys.size() - 1; i >= 0; i--) {
            if (!isFindAvailableMethod) {
                findAvailableLoadFailedRetentionMethods(keys.get(i), label, results);
            } else {
                break;
            }
        }
    }

    /**
     * 找到可执行的方法
     *
     * @param key
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void findAvailableLoadFailedRetentionMethods(String key, String label, Result... results) {
        HashMap<String, List<TofuMethod>> map = busMethods.get(key);
        if (!CollectUtil.isEmpty(map)) {
            List<TofuMethod> methods = map.get(loadMoreResult.class.getName());
            executeAvailableRetentionMethods(methods, label, results);
        }
    }

    /**
     * 执行刷新回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    public synchronized <Result> void executeLoadMoreRetention(String label, Result... results) {
        isFindAvailableMethod = false;
        for (int i = keys.size() - 1; i >= 0; i--) {
            if (!isFindAvailableMethod) {
                findAvailableLoadMoreRetentionMethods(keys.get(i), label, results);
            } else {
                break;
            }
        }
    }

    /**
     * 找到可执行的方法
     *
     * @param key
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void findAvailableLoadMoreRetentionMethods(String key, String label, Result... results) {
        HashMap<String, List<TofuMethod>> map = busMethods.get(key);
        if (!CollectUtil.isEmpty(map)) {
            List<TofuMethod> methods = map.get(loadMoreResult.class.getName());
            executeAvailableRetentionMethods(methods, label, results);
        }
    }


    /**
     * 执行刷新回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    public synchronized <Result> void executeLoadBeforeRetention(String label, Result... results) {
        isFindAvailableMethod = false;
        for (int i = keys.size() - 1; i >= 0; i--) {
            if (!isFindAvailableMethod) {
                findAvailableLoadBeforeRetentionMethods(keys.get(i), label, results);
            } else {
                break;
            }
        }
    }


    /**
     * 找到可执行的方法
     *
     * @param key
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void findAvailableLoadBeforeRetentionMethods(String key, String label, Result... results) {
        HashMap<String, List<TofuMethod>> map = busMethods.get(key);
        if (!CollectUtil.isEmpty(map)) {
            List<TofuMethod> methods = map.get(loadMoreBefore.class.getName());
            executeAvailableRetentionMethods(methods, label, results);
        }
    }

    /**
     * 执行刷新回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    public synchronized <Result> void executeRefreshBeforeRetention(String label, Result... results) {
        isFindAvailableMethod = false;
        for (int i = keys.size() - 1; i >= 0; i--) {
            if (!isFindAvailableMethod) {
                findAvailableRefreshBeforeRetentionMethods(keys.get(i), label, results);
            } else {
                break;
            }
        }
    }

    /**
     * 找到可执行的方法
     *
     * @param key
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void findAvailableRefreshBeforeRetentionMethods(String key, String label, Result... results) {
        HashMap<String, List<TofuMethod>> map = busMethods.get(key);
        if (!CollectUtil.isEmpty(map)) {
            List<TofuMethod> methods = map.get(refreshBefore.class.getName());
            executeAvailableRetentionMethods(methods, label, results);
        }
    }

    /**
     * 执行刷新回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    public synchronized <Result> void executeRefreshFailedRetention(String label, Result... results) {
        isFindAvailableMethod = false;
        for (int i = keys.size() - 1; i >= 0; i--) {
            if (!isFindAvailableMethod) {
                findAvailableRefreshFailedRetentionMethods(keys.get(i), label, results);
            } else {
                break;
            }
        }

    }

    /**
     * 找到可执行的方法
     *
     * @param key
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void findAvailableRefreshFailedRetentionMethods(String key, String label, Result... results) {
        HashMap<String, List<TofuMethod>> map = busMethods.get(key);
        if (!CollectUtil.isEmpty(map)) {
            List<TofuMethod> methods = map.get(refreshFailed.class.getName());
            executeAvailableRetentionMethods(methods, label, results);
        }
    }


    /**
     * 执行刷新回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    public synchronized <Result> void executeRefreshRetention(String label, Result... results) {
        isFindAvailableMethod = false;
        for (int i = keys.size() - 1; i >= 0; i--) {
            if (!isFindAvailableMethod) {
                findAvailableRefreshRetentionMethods(keys.get(i), label, results);
            } else {
                break;
            }
        }
    }


    /**
     * 找到可执行的方法
     *
     * @param key
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void findAvailableRefreshRetentionMethods(String key, String label, Result... results) {
        HashMap<String, List<TofuMethod>> map = busMethods.get(key);
        if (!CollectUtil.isEmpty(map)) {
            List<TofuMethod> methods = map.get(refreshResult.class.getName());
            executeAvailableRetentionMethods(methods, label, results);
        }
    }


    /**
     * 执行方法
     *
     * @param methods
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void executeAvailableRetentionMethods(List<TofuMethod> methods, String label, Result... results) {
        if (!CollectUtil.isEmpty(methods)) {
            for (int i = methods.size() - 1; i >= 0; i--) {
                if (TextUtils.equals(methods.get(i).label, label)) {
                    isFindAvailableMethod = true;
                    executeAvailableMethod(methods.get(i), results);
                    break;
                }
            }
        }
    }

    /**
     * 执行方法
     *
     * @param tofuMethod
     * @param results
     * @param <Result>
     */
    private <Result> void executeAvailableMethod(TofuMethod tofuMethod, Result... results) {
        if (tofuMethod.params == null || tofuMethod.params.length == 0) {
            executeAvailableNotParamsMethod(tofuMethod);
        } else {
            findAvailableMethodParams(tofuMethod, results);
            executeAvailableParamsMethod(tofuMethod);
        }
    }


    /**
     * 参数赋值
     *
     * @param tofuMethod
     * @param results
     * @param <Result>
     */
    private <Result> void findAvailableMethodParams(TofuMethod tofuMethod, Result... results) {
        Object[] p = new Object[tofuMethod.params.length];
        for (int i = 0; i < p.length; i++) {
            if (results != null && i < results.length) {
                p[i] = null;
                for (int i1 = 0; i1 < results.length; i1++) {
                    if (tofuMethod.params[i].isInstance(results[i1])) {
                        p[i] = results[i1];
                    }
                }
            } else {
                p[i] = null;
            }
        }
        tofuMethod.exe_params = p;
    }


    /**
     * 执行有参回调方法
     *
     * @param tofuMethod
     */
    private void executeAvailableParamsMethod(TofuMethod tofuMethod) {
        try {
            tofuMethod.method.invoke(tofuMethod.target, tofuMethod.exe_params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行没有参数的方法
     *
     * @param tofuMethod
     */
    private void executeAvailableNotParamsMethod(TofuMethod tofuMethod) {
        try {
            tofuMethod.method.invoke(tofuMethod.target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解绑
     *
     * @param target
     */
    public void unBindTarget(Object target) {
        if (target != null) {
            String key = findKeyByTarget(target.getClass().getName());
            if(TextUtils.isEmpty(key))return;
            HashMap<String, List<TofuMethod>> map = busMethods.get(key);
            keys.remove(key);
            keyMap.remove(key);
            if (CollectUtil.isEmpty(map)) return;
            map.clear();
        }
    }


    private String findKeyByTarget(String target) {
        for (int i = keys.size() - 1; i >= 0; i--) {
            Object o = keyMap.get(keys.get(i));
            if (o != null) {
                if (TextUtils.equals(target, o.getClass().getName())) {
                    return keys.get(i);
                }
            }
        }
        return null;
    }
}
