package com.pudding.tofu.widget.JSBridge;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JSHolder {

    private JsWebView webView;

    private String jsId;

    private static JSHolder holder = new JSHolder();

    private ToJs tojs = new ToJs();

    private RJs rjs = new RJs();

    private int key = 0x0059595959;

    private int index = 0;

    private JSHolder() {
        //todo nothing
    }

    /**
     * 获得holder对象
     *
     * @return
     */
    public static JSHolder get() {
        synchronized (JSHolder.class) {
            return holder;
        }
    }


    /**
     * 绑定wiew
     *
     * @param web
     * @return
     */
    public JSHolder web(@NonNull JsWebView web) {
        this.webView = web;
        return this;
    }

    /**
     * js名字
     *
     * @param js
     * @return
     */
    public JSHolder jsPath(@NonNull String js) {
        this.jsId = js;
        return this;
    }

    /**
     * 加载一个url
     *
     * @param url
     */
    public JSHolder load(@NonNull String url) {
        checkWebAvailable();
        this.webView.loadUrl(url);
        return this;
    }


    /**
     * 发送数据到js
     *
     * @return
     */
    public ToJs toJs() {
        checkWebAvailable();
        return tojs;
    }

    /**
     * 注册js回调方法
     *
     * @return
     */
    public RJs rJs() {
        checkWebAvailable();
        return rjs;
    }


    public class RJs {

        private List<Target> queue = new ArrayList<>();

        private WebClientAdapter adapter;


        private RJs() {
            //todo nothing
        }

        public RJs setLoadCallback(WebClientAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        /**
         * 注册返回目标方法
         *
         * @param target
         * @return
         */
        public RJs add(@NonNull Object target, @NonNull String method) {
            if (webView.getTag(key) == null) {
                webView.setTag(key, ++index + System.currentTimeMillis());
                JsWebViewClient client = new JsWebViewClient(this);
                if(adapter == null){
                    adapter = new WebClientAdapter() ;
                }
                client.setWebClientCallback(adapter);
                webView.setWebViewClient(client);
                adapter = null;
            }
            if (target != null && !TextUtils.isEmpty(method)) {
                Target targetObj = new Target(target, method);
                if (!queue.contains(targetObj)) {
                    queue.add(targetObj);
                }
            }
            return this;
        }

        /**
         * 移除一个注册目标
         *
         * @param target
         * @return
         */
        public RJs remove(@NonNull Object target) {
            for (Target q : queue) {
                if (q.target.equals(target)) {
                    queue.remove(q);
                    q.target = null;
                }
            }
            return this;
        }

        /**
         * 清除所有注册对象
         */
        public void clear() {
            queue.clear();
        }


        /**
         * js回调方法
         *
         * @param data
         */
        private void dispatchQueue(String data) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                for (Target target : queue) {
                    Method[] declaredMethods = target.target.getClass().getDeclaredMethods();
                    if (declaredMethods != null && declaredMethods.length > 0) {
                        for (Method declaredMethod : declaredMethods) {
                            if (declaredMethod.getName().equals(jsonObject.getString("methodName"))) {
                                executeDispatch(target.target, declaredMethod, jsonObject.getString("data"));
                                return;
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        /**
         * 执行分发
         *
         * @param method
         * @param json
         */
        private void executeDispatch(Object target, Method method, String json) {
            try {
                method.setAccessible(true);
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes != null && parameterTypes.length == 1 &&
                        parameterTypes[0].isInstance("") && !TextUtils.isEmpty(json)) {
                    method.invoke(target, json);
                } else {
                    method.invoke(target);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }


        class Target {
            Object target;
            String method;

            private Target(Object target, String method) {
                this.target = target;
                this.method = method;
            }
        }
    }


    /**
     * 调用js方法
     */
    public class ToJs {

        private String method;

        private String data;

        private StringBuffer path = new StringBuffer();


        private ToJs() {
            //todo nothing
        }

        /**
         * js方法
         *
         * @param method
         * @return
         */
        public ToJs jsMethod(@NonNull String method) {
            this.method = method;
            return tojs;
        }

        /**
         * 参数
         *
         * @param json
         * @return
         */
        public ToJs toData(@NonNull String json) {
            this.data = json;
            return tojs;
        }




        /**
         * 加载
         */
        public void send() {
            checkWebAvailable();
            checkParamAvailable();
            path.delete(0, path.length());
            path.append("javascript:");
            if (!TextUtils.isEmpty(jsId)) {
                path.append(jsId);
                path.append("." + method + "(");
            } else {
                path.append(method + "(");
            }
            if (!TextUtils.isEmpty(data)) {
                path.append(data);
            }
            path.append(")");
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                ((Activity) webView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(path.toString());
                    }
                });
            } else {
                webView.loadUrl(path.toString());
            }
        }


        /**
         * 参数检查
         */
        private void checkParamAvailable() {
            if (TextUtils.isEmpty(method)) throw new NullPointerException("please set method !!! ");
        }

    }


    /**
     * 设置Web后onCreate中调用
     */
    public JSHolder onCreate() {
        checkWebAvailable();
        setConfigCallback((WindowManager) webView.getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        return this;
    }

    /**
     * 设置Web后onDestroy中调用
     */
    public void onDestroy() {
        setConfigCallback(null);
        if (webView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destorywebView
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    /**
     * 参数检查
     */
    private void checkWebAvailable() {
        if (webView == null) throw new NullPointerException("webView is null !!");
    }

    /**
     * 反射释放和设置web资源
     *
     * @param windowManager
     */
    private void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);
            if (null == configCallback) {
                return;
            }
            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
        }
    }


}
