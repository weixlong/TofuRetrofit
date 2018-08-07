package com.pudding.tofu.model;

import android.text.TextUtils;

import com.pudding.tofu.retention.loadError;
import com.pudding.tofu.retention.loadFile;
import com.pudding.tofu.retention.loadProgress;
import com.pudding.tofu.retention.photoPick;
import com.pudding.tofu.retention.post;
import com.pudding.tofu.retention.postError;
import com.pudding.tofu.retention.subscribe;
import com.pudding.tofu.retention.upload;
import com.pudding.tofu.retention.uploadError;
import com.pudding.tofu.retention.uploadProgress;
import com.pudding.tofu.widget.CollectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wxl on 2018/6/22 0022.
 * 邮箱：632716169@qq.com
 */

public class TofuBus {

    private static volatile TofuBus tofuBus = new TofuBus();

    protected static TofuBus get() {
        synchronized (TofuBus.class) {
            return tofuBus;
        }
    }

    private TofuBus() {
    }

    /**
     * post方法站
     */
    private HashMap<String, List<TofuMethod>> cachePostMethods = new HashMap<>();

    /**
     * post错误返回方法站
     */
    private HashMap<String, List<TofuMethod>> postErrorMethods = new HashMap<>();

    /**
     * 下载文件
     */
    private HashMap<String, List<TofuMethod>> loadFileMethods = new HashMap<>();

    /**
     * 下载错误
     */
    private HashMap<String, List<TofuMethod>> loadFileErrorMethods = new HashMap<>();

    /**
     * 下载进度
     */
    private HashMap<String, List<TofuMethod>> loadFileProgressMethods = new HashMap<>();

    /**
     * 上传
     */
    private HashMap<String, List<TofuMethod>> upLoadFileMethods = new HashMap<>();

    /**
     * 上传错误
     */
    private HashMap<String, List<TofuMethod>> upLoadFileErrorMethods = new HashMap<>();

    /**
     * 上传进度
     */
    private HashMap<String, List<TofuMethod>> upLoadFileProgressMethods = new HashMap<>();

    /**
     * 普通注册
     */
    private HashMap<String, List<TofuMethod>> subscribeMethods = new HashMap<>();

    /**
     * 图片响应picker
     */
    private HashMap<String, List<TofuMethod>> pickerMethods = new HashMap<>();



    /**
     * 取target的介质集合
     */
    private List<String> keys = new ArrayList<>();


    private HashMap<String, HashMap<String, Method>> singleMethods = new HashMap<>();

    /**
     * 找post注解
     *
     * @param target
     */
    protected void findSubscribe(Object target) {
        String key = target.getClass().getName();
        keys.add(key);
        newTofuIfEmptyMethodList(key, cachePostMethods, postErrorMethods,
                loadFileMethods, loadFileErrorMethods, loadFileProgressMethods,
                upLoadFileMethods, upLoadFileErrorMethods,
                upLoadFileProgressMethods, subscribeMethods, pickerMethods);

        List<TofuMethod> postMethods = cachePostMethods.get(key);
        List<TofuMethod> tofuErrorMethods = postErrorMethods.get(key);
        List<TofuMethod> tofuLoadFileMethods = loadFileMethods.get(key);
        List<TofuMethod> tofuLoadFileErrorMethods = loadFileErrorMethods.get(key);
        List<TofuMethod> tofuLoadFileProgressMethods = loadFileProgressMethods.get(key);
        List<TofuMethod> tofuUpLoadFileMethods = upLoadFileMethods.get(key);
        List<TofuMethod> tofuUpLoadFileErrorMethods = upLoadFileErrorMethods.get(key);
        List<TofuMethod> tofuUpLoadFileProgressMethods = upLoadFileProgressMethods.get(key);
        List<TofuMethod> tofuSimpleMethods = subscribeMethods.get(key);
        List<TofuMethod> tofuPickerMethods = pickerMethods.get(key);


        Method[] declaredMethods = target.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            findPostSubscribe(postMethods, method, target);
            findPostErrorSubscribe(tofuErrorMethods, method, target);
            findLoadFileSubscribe(tofuLoadFileMethods, method, target);
            findLoadFileErrorSubscribe(tofuLoadFileErrorMethods, method, target);
            findLoadFileProgressSubscribe(tofuLoadFileProgressMethods, method, target);
            findUploadSubscribe(tofuUpLoadFileMethods, method, target);
            findUpLoadErrorSubscribe(tofuUpLoadFileErrorMethods, method, target);
            findUploadProgressSubscribe(tofuUpLoadFileProgressMethods, method, target);
            findSimpleSubscribe(tofuSimpleMethods, method, target);
            findPickerSubscribe(tofuPickerMethods, method, target);

        }
    }


    /**
     * 新建一个list
     *
     * @param methodMaps
     */
    private void newTofuIfEmptyMethodList(String key, HashMap<String, List<TofuMethod>>... methodMaps) {
        for (int i = 0; i < methodMaps.length; i++) {
            List<TofuMethod> tofuMethods = methodMaps[i].get(key);
            if (tofuMethods == null) {
                tofuMethods = new ArrayList<>();
            }
            methodMaps[i].put(key, tofuMethods);
        }
    }

    /**
     * simple站
     *
     * @param
     * @param simpleMethods
     * @param method
     * @param target
     */
    private void findSimpleSubscribe(List<TofuMethod> simpleMethods, Method method, Object target) {
        subscribe sub = method.getAnnotation(subscribe.class);
        if (null != sub) {
            String[] values = sub.value();
            fullyTofuMethod(values, simpleMethods, method, target);
        }
        subscribeMethods.put(target.getClass().getName(), simpleMethods);
    }


    /**
     * 图片选择站
     *
     * @param pickMethods
     * @param method
     * @param target
     */
    private void findPickerSubscribe(List<TofuMethod> pickMethods, Method method, Object target) {
        photoPick pick = method.getAnnotation(photoPick.class);
        if (null != pick) {
            String[] value = pick.value();
            fullyTofuMethod(value, pickMethods, method, target);
        }
        pickerMethods.put(target.getClass().getName(), pickMethods);
    }

    /**
     * post错误返回站
     *
     * @param method
     */
    private void findPostErrorSubscribe(List<TofuMethod> postMethods, Method method, Object target) {
        postError post = method.getAnnotation(postError.class);
        if (null != post) {
            String[] value = post.value();
            fullyTofuMethod(value, postMethods, method, target);
        }
        postErrorMethods.put(target.getClass().getName(), postMethods);
    }

    /**
     * 添加post请求站
     *
     * @param
     */
    private void findPostSubscribe(List<TofuMethod> postMethods, Method method, Object target) {
        post post = method.getAnnotation(com.pudding.tofu.retention.post.class);
        if (null != post) {
            String[] labels = post.value();
            fullyTofuMethod(labels, postMethods, method, target);
        }
        cachePostMethods.put(target.getClass().getName(), postMethods);
    }


    /**
     * 添加下载文件方法站
     *
     * @param
     * @param postMethods
     * @param method
     * @param target
     */
    private void findLoadFileSubscribe(List<TofuMethod> postMethods, Method method, Object target) {
        loadFile load = method.getAnnotation(loadFile.class);
        if (load != null) {
            String[] labels = load.value();
            fullyTofuMethod(labels, postMethods, method, target);
        }
        loadFileMethods.put(target.getClass().getName(), postMethods);
    }


    /**
     * 添加下载错误
     *
     * @param
     * @param loadFileErrorMethod
     * @param method
     * @param target
     */
    private void findLoadFileErrorSubscribe(List<TofuMethod> loadFileErrorMethod, Method method, Object target) {
        loadError error = method.getAnnotation(loadError.class);
        if (null != error) {
            String[] labels = error.value();
            fullyTofuMethod(labels, loadFileErrorMethod, method, target);
        }
        loadFileErrorMethods.put(target.getClass().getName(), loadFileErrorMethod);
    }

    /**
     * 添加下载进度
     *
     * @param
     * @param loadProgressMethods
     * @param method
     * @param target
     */
    private void findLoadFileProgressSubscribe(List<TofuMethod> loadProgressMethods, Method method, Object target) {
        loadProgress progress = method.getAnnotation(loadProgress.class);
        if (null != progress) {
            String[] labels = progress.value();
            fullyTofuMethod(labels, loadProgressMethods, method, target);
        }
        loadFileProgressMethods.put(target.getClass().getName(), loadProgressMethods);
    }


    /**
     * 添加上传站
     *
     * @param
     * @param upLoadMethod
     * @param method
     * @param target
     */
    private void findUploadSubscribe(List<TofuMethod> upLoadMethod, Method method, Object target) {
        upload up = method.getAnnotation(upload.class);
        if (null != up) {
            String[] labels = up.value();
            fullyTofuMethod(labels, upLoadMethod, method, target);
        }
        upLoadFileMethods.put(target.getClass().getName(), upLoadMethod);
    }

    /**
     * 添加上传错误
     *
     * @param
     * @param upLoadMethod
     * @param method
     * @param target
     */
    private void findUpLoadErrorSubscribe(List<TofuMethod> upLoadMethod, Method method, Object target) {
        uploadError error = method.getAnnotation(uploadError.class);
        if (null != error) {
            String[] labels = error.value();
            fullyTofuMethod(labels, upLoadMethod, method, target);
        }
        upLoadFileErrorMethods.put(target.getClass().getName(), upLoadMethod);
    }

    /**
     * @param
     * @param upLoadProgressMethod
     * @param method
     * @param target
     */
    private void findUploadProgressSubscribe(List<TofuMethod> upLoadProgressMethod, Method method, Object target) {
        uploadProgress progress = method.getAnnotation(uploadProgress.class);
        if (null != progress) {
            String[] labels = progress.value();
            fullyTofuMethod(labels, upLoadProgressMethod, method, target);
        }
        upLoadFileProgressMethods.put(target.getClass().getName(), upLoadProgressMethod);
    }

    /**
     * 填充数据
     *
     * @param labels
     * @param methods
     * @param method
     * @param target
     * @return
     */
    private List<TofuMethod> fullyTofuMethod(String[] labels, List<TofuMethod> methods, Method method, Object target) {
        for (String label : labels) {
            Class<?>[] parameters = method.getParameterTypes();
            method.setAccessible(true);
            TofuMethod tofuMethod = new TofuMethod(label, method, parameters, target);
            methods.add(tofuMethod);
        }
        return methods;
    }


    /**
     * 普通回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    protected <Result> void executeSimpleMethod(String label, Result... results) {
        executeSimple(label, subscribeMethods, results);
    }

    /**
     * 执行指定对象的方法
     *
     * @param target
     * @param label
     * @param results
     * @param <Result>
     */
    protected <Result> void executeTargetMethod(final Object target, final String label, final Result... results) {
        Observable.create(new ObservableOnSubscribe<SingleTarget>() {
            @Override
            public void subscribe(ObservableEmitter<SingleTarget> e) throws Exception {
                onFindTargetSingleMethod(e, target, label, results);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SingleTarget>() {
                    @Override
                    public void accept(SingleTarget singleTarget) throws Exception {
                        executeSingleTargetMethod(singleTarget.object, singleTarget.label, singleTarget.results);
                        singleTarget.object = null;
                    }
                });
    }


    /**
     * 找到注解方法
     *
     * @param e
     * @param target
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void onFindTargetSingleMethod(ObservableEmitter<SingleTarget> e, Object target, String label, Result... results) {
        String key = target.getClass().getName();
        HashMap<String, Method> mehtods = singleMethods.get(key);
        if (CollectUtil.isEmpty(mehtods)) {
            findTargetSingleMethod(target);
        }
        SingleTarget singleTarget = getSingleTarget(target, label, results);
        e.onNext(singleTarget);
    }


    /**
     * 实例化一个SingleTarget对象
     *
     * @param target
     * @param label
     * @param results
     * @param <Result>
     * @return
     */
    private <Result> SingleTarget getSingleTarget(Object target, String label, Result... results) {
        SingleTarget singleTarget = new SingleTarget();
        singleTarget.label = label;
        singleTarget.object = target;
        singleTarget.results = results;
        return singleTarget;
    }

    /**
     * 执行
     *
     * @param target
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void executeSingleTargetMethod(Object target, String label, Result... results) {
        try {
            HashMap<String, Method> methodHashMap = singleMethods.get(target.getClass().getName());
            Method method = methodHashMap.get(label);
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes == null || parameterTypes.length == 0) {
                method.invoke(target);
            } else {
                Object[] p = new Object[parameterTypes.length];
                findSimpleMethodWithParam(p, parameterTypes, results);
                method.invoke(target, p);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 找单个target下的注解方法
     *
     * @param target
     */
    private void findTargetSingleMethod(Object target) {
        HashMap<String, Method> targetMethods = new HashMap<>();
        Method[] methods = target.getClass().getDeclaredMethods();
        if (methods != null) {
            for (Method method : methods) {
                fullyTargetSingleMethod(targetMethods, method);
            }
        }
        singleMethods.put(target.getClass().getName(), targetMethods);
    }


    /**
     * 填方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullyTargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {

        fullyLoadTargetSingleMethod(targetMethods, method);

        fullyLoadETargetSingleMethod(targetMethods, method);

        fullyLoadGTargetSingleMethod(targetMethods, method);

        fullyPickTargetSingleMethod(targetMethods, method);

        fullyPostTargetSingleMethod(targetMethods, method);

        fullyPostETargetSingleMethod(targetMethods, method);

        fullySubTargetSingleMethod(targetMethods, method);

        fullyUpTargetSingleMethod(targetMethods, method);

        fullyUpETargetSingleMethod(targetMethods, method);

        fullyUpGTargetSingleMethod(targetMethods, method);

    }


    /**
     * 添加下载成功方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullyLoadTargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        loadFile load_File = method.getAnnotation(loadFile.class);
        if (load_File != null) {
            String[] value = load_File.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }

    /**
     * 添加下载错误方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullyLoadETargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        loadError load_Error = method.getAnnotation(loadError.class);
        if (load_Error != null) {
            String[] value = load_Error.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }

    /**
     * 添加下载进度方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullyLoadGTargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        loadProgress load_Progress = method.getAnnotation(loadProgress.class);
        if (load_Progress != null) {
            String[] value = load_Progress.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }

    /**
     * 添加图片选择方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullyPickTargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        photoPick pick = method.getAnnotation(photoPick.class);
        if (pick != null) {
            String[] value = pick.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }

    /**
     * 填充post 请求方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullyPostTargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        post p = method.getAnnotation(post.class);
        if (p != null) {
            String[] value = p.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }

    /**
     * 填充post请求错误方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullyPostETargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        postError p_e = method.getAnnotation(postError.class);
        if (p_e != null) {
            String[] value = p_e.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }

    /**
     * 填充描述方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullySubTargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        subscribe s = method.getAnnotation(subscribe.class);
        if (s != null) {
            String[] value = s.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }

    /**
     * 填充上传文件方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullyUpTargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        upload up = method.getAnnotation(upload.class);
        if (up != null) {
            String[] value = up.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }


    /**
     * 填充上传错误方法
     *
     * @param targetMethods
     * @param method
     */
    private void fullyUpETargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        uploadError up_e = method.getAnnotation(uploadError.class);
        if (up_e != null) {
            String[] value = up_e.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }

    /**
     * 填充上传进度
     *
     * @param targetMethods
     * @param method
     */
    private void fullyUpGTargetSingleMethod(HashMap<String, Method> targetMethods, Method method) {
        uploadProgress up_p = method.getAnnotation(uploadProgress.class);
        if (up_p != null) {
            String[] value = up_p.value();
            fullyTargetSingleMethod(value, targetMethods, method);
        }
    }


    /**
     * 存label方法
     *
     * @param value
     * @param targetMethods
     * @param method
     */
    private void fullyTargetSingleMethod(String[] value, HashMap<String, Method> targetMethods, Method method) {
        for (int i = 0; i < value.length; i++) {
            method.setAccessible(true);
            targetMethods.put(value[i], method);
        }
    }

    /**
     * 图片选择回调
     *
     * @param label
     * @param path
     */
    protected void executePickerMethod(String label, ArrayList<String> path) {
        executeSimple(label, pickerMethods, path);
    }


    /**
     * post执行的回调方法
     *
     * @param label
     */
    protected <Result> void executePostMethod(String label, Result... results) {
        execute(label, cachePostMethods, results);
    }


    /**
     * 执行post错误回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    protected <Result> void executePostErrorMethod(final String label, final Result... results) {
        execute(label, postErrorMethods, results);
    }



    /**
     * 执行下载文件
     *
     * @param label
     * @param results
     * @param <Result>
     */
    protected <Result> void executeLoadFileMethod(final String label, final Result... results) {
        execute(label, loadFileMethods, results);
    }


    /**
     * 下载错误返回
     *
     * @param label
     * @param results
     * @param <Result>
     */
    protected <Result> void executeLoadFileErrorMethod(final String label, final Result... results) {
        execute(label, loadFileErrorMethods, results);
    }

    /**
     * 下载进度
     *
     * @param label
     */
    protected <Result> void executeLoadProgressMethod(String label, Result... results) {
        execute(label, loadFileProgressMethods, results);
    }


    /**
     * 上传回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    protected <Result> void executeUploadMethod(String label, Result... results) {
        execute(label, upLoadFileMethods, results);
    }

    /**
     * 上传错误回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    protected <Result> void executeUploadErrorMethod(String label, Result... results) {
        execute(label, upLoadFileErrorMethods, results);
    }

    /**
     * 上传进度回调
     *
     * @param label
     * @param results
     * @param <Result>
     */
    protected <Result> void executeUploadProgressMethod(String label, Result... results) {
        execute(label, upLoadFileProgressMethods, results);
    }


    /**
     * 普通调用
     *
     * @param label
     * @param
     * @param results
     * @param <Result>
     */
    private <Result> void executeSimple(final String label, final HashMap<String, List<TofuMethod>> maps, final Result... results) {
        Observable.create(new ObservableOnSubscribe<Post>() {
            @Override
            public void subscribe(ObservableEmitter<Post> e) throws Exception {
                onExecuteSimpleInterceptorMethod(e, label, maps, results);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Post>() {
                    @Override
                    public void accept(Post post) throws Exception {
                        executeSimplePost(post);
                    }
                });
    }


    /**
     * 执行普通post方法
     *
     * @param post
     */
    private void executeSimplePost(Post post) {
        try {
            if (post.realParams == null || post.realParams.length == 0) {
                post.tofuMethod.getMethod().invoke(post.tofuMethod.getObject());
            } else {
                post.tofuMethod.getMethod().invoke(post.tofuMethod.getObject(), post.realParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行普通调用方法
     *
     * @param e
     * @param label
     * @param maps
     * @param results
     * @param <Result>
     */
    private <Result> void onExecuteSimpleInterceptorMethod(ObservableEmitter<Post> e, String label, HashMap<String, List<TofuMethod>> maps, Result... results) {
        for (int i = keys.size() - 1; i >= 0; i--) {
            List<TofuMethod> tofuMethods = maps.get(keys.get(i));
            if (CollectUtil.isEmpty(tofuMethods)) continue;
            for (TofuMethod tofuMethod : tofuMethods) {
                if (TextUtils.equals(label, tofuMethod.getlabel())) {
                    executeSimpleMethod(e, tofuMethod, results);
                    return;
                }
            }
        }
    }

    /**
     * 执行普通方法调用
     *
     * @param e
     * @param tofuMethod
     * @param results
     * @param <Result>
     */
    private <Result> void executeSimpleMethod(ObservableEmitter<Post> e, TofuMethod tofuMethod, Result... results) {
        Class[] paramterClass = tofuMethod.getParamterClass();
        if (paramterClass == null || paramterClass.length == 0) {
            executeParamsIsNone(e, tofuMethod);
        } else {
            executeSimpleParamNotNullMethod(e, paramterClass, tofuMethod, results);
        }
    }


    /**
     * 执行带参数的普通方法
     *
     * @param e
     * @param paramterClass
     * @param tofuMethod
     * @param results
     * @param <Result>
     */
    private <Result> void executeSimpleParamNotNullMethod(ObservableEmitter<Post> e, Class[] paramterClass, TofuMethod tofuMethod, Result... results) {
        Object[] realParams = new Object[paramterClass.length];
        findSimpleMethodWithParam(realParams, paramterClass, results);
        Post p = getSimplePost(realParams, tofuMethod);
        e.onNext(p);
    }


    /**
     * 找到并填充正式参数
     *
     * @param realParams
     * @param paramterClass
     * @param results
     * @param <Result>
     */
    private <Result> void findSimpleMethodWithParam(Object[] realParams, Class[] paramterClass, Result... results) {
        for (int j = 0; j < realParams.length; j++) {
            if (results.length <= realParams.length && j< results.length && paramterClass[j].isInstance(results[j])) {
                realParams[j] = results[j];
            } else {
                realParams[j] = null;
            }
        }
    }

    /**
     * 产生一个post对象
     *
     * @param realParams
     * @param tofuMethod
     * @return
     */
    private Post getSimplePost(Object[] realParams, TofuMethod tofuMethod) {
        Post p = new Post();
        p.realParams = realParams;
        p.tofuMethod = tofuMethod;
        return p;
    }


    /**
     * 执行方法
     *
     * @param label
     * @param
     */
    private <Result> void execute(final String label, final HashMap<String, List<TofuMethod>> maps, final Result... results) {
        Observable.create(new ObservableOnSubscribe<Post>() {
            @Override
            public void subscribe(ObservableEmitter<Post> e) throws Exception {
                executeInterceptorMethod(e, label, maps, results);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Post>() {
                    @Override
                    public void accept(Post post) throws Exception {
                        executeSimplePost(post);
                    }
                });
    }


    /**
     * 执行第一个label相同的方法
     *
     * @param e
     * @param label
     * @param maps
     * @param results
     * @param <Result>
     */
    private <Result> void executeInterceptorMethod(ObservableEmitter<Post> e, String label, HashMap<String, List<TofuMethod>> maps, Result... results) {
        for (int i = keys.size() - 1; i >= 0; i--) {
            List<TofuMethod> tofuMethods = maps.get(keys.get(i));

            if (CollectUtil.isEmpty(tofuMethods)) continue;
            for (TofuMethod tofuMethod : tofuMethods) {
                if (TextUtils.equals(label, tofuMethod.getlabel())) {
                    executeParamsMethod(e, tofuMethod, label, results);
                    return;
                }
            }
        }
    }


    /**
     * 执行参数方法
     *
     * @param e
     * @param tofuMethod
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void executeParamsMethod(ObservableEmitter<Post> e, TofuMethod tofuMethod, String label, Result... results) {
        Class[] paramterClass = tofuMethod.getParamterClass();
        if (paramterClass == null || paramterClass.length == 0) {
            executeParamsIsNone(e, tofuMethod);
        } else {
            executeParamsNotNullMethod(e, paramterClass, tofuMethod, label, results);
        }
    }


    /***
     * 参数不为空
     * @param e
     * @param paramterClass
     * @param tofuMethod
     * @param label
     * @param results
     * @param <Result>
     */
    private <Result> void executeParamsNotNullMethod(ObservableEmitter<Post> e, Class[] paramterClass, TofuMethod tofuMethod, String label, Result... results) {
        if (paramterClass[0].isInstance(label) && paramterClass.length > 1) {
            executeFirstIsLabel(e, label, paramterClass, tofuMethod, results);
        } else {
            simpleExecute(e, paramterClass, tofuMethod, results);
        }
    }

    /**
     * simpleExecute
     *
     * @param e
     * @param paramterClass
     * @param tofuMethod
     * @param results
     * @param <Result>
     */
    private <Result> void simpleExecute(ObservableEmitter<Post> e, Class[] paramterClass, TofuMethod tofuMethod, Result... results) {
        Object[] realParams = new Object[paramterClass.length];//真实的参数
        putParams(realParams, paramterClass, results);
        Post p = getSimplePost(realParams, tofuMethod);
        e.onNext(p);
    }

    /**
     * 如果参数是空
     *
     * @param e
     * @param tofuMethod
     */
    private void executeParamsIsNone(ObservableEmitter<Post> e, TofuMethod tofuMethod) {
        Post p = getSimplePost(null, tofuMethod);
        e.onNext(p);
    }


    /**
     * 如果第一条需要返回url
     *
     * @param e
     * @param label
     * @param paramterClass
     * @param tofuMethod
     * @param results
     * @param <Result>
     */
    private <Result> void executeFirstIsLabel(ObservableEmitter<Post> e, String label, Class[] paramterClass, TofuMethod tofuMethod, Result... results) {
        Object[] realParams = new Object[paramterClass.length];//真实的参数
        fullyFirstIsLabelParams(realParams, label, paramterClass, results);
        Post p = getSimplePost(realParams, tofuMethod);
        e.onNext(p);
    }


    /**
     * 参数填充
     *
     * @param realParams
     * @param label
     * @param paramterClass
     * @param results
     * @param <Result>
     */
    private <Result> void fullyFirstIsLabelParams(Object[] realParams, String label, Class[] paramterClass, Result... results) {
        realParams[0] = label;
        Object[] realtempParams = new Object[paramterClass.length - 1];
        Class[] tempParams = new Class[paramterClass.length - 1];

        for (int i = 0; i < tempParams.length; i++) {
            tempParams[i] = paramterClass[i + 1];
        }
        putParams(realtempParams, tempParams, results);
        for (int i = 1; i < realParams.length; i++) {
            realParams[i] = realtempParams[i - 1];
        }
    }

    /**
     * 放置参数
     *
     * @param realParams
     * @param paramterClass
     * @param results
     * @param <Result>
     */
    private <Result> void putParams(Object[] realParams, Class[] paramterClass, Result... results) {
        if (results == null || results.length == 0) {
            return;
        } else {
            for (int i = 0; i < realParams.length; i++) {
                realParams[i] = null;
                for (int j = 0; j < results.length; j++) {
                    if (i < results.length && paramterClass[i].isInstance(results[j])) {
                        realParams[i] = results[j];
                        break;
                    }
                }
            }
        }
    }

    /**
     * 解绑
     */
    protected void unBindTarget(Object target) {
        String name = target.getClass().getName();
        clear(singleMethods.remove(name));
        clear(cachePostMethods.remove(name));
        clear(postErrorMethods.remove(name));
        clear(loadFileMethods.remove(name));
        clear(loadFileErrorMethods.remove(name));
        clear(loadFileProgressMethods.remove(name));
        clear(subscribeMethods.remove(name));
        clear(pickerMethods.remove(name));
        clear(upLoadFileMethods.remove(name));
        clear(upLoadFileProgressMethods.remove(name));
        clear(upLoadFileErrorMethods.remove(name));
        keys.remove(name);
    }

    private void clear(Map map) {
        if (!CollectUtil.isEmpty(map)) {
            map.clear();
            map = null;
        }
    }

    private void clear(Collection list) {
        if (!CollectUtil.isEmpty(list)) {
            list.clear();
            list = null;
        }
    }

    class SingleTarget<Result> {
        //标记
        private String label;

        //对象
        private Object object;

        private Result[] results;
    }

    class Post {
        TofuMethod tofuMethod;
        Object[] realParams;
    }

    class TofuMethod {

        //标记
        private String label;
        //方法
        private Method method;
        //参数的类型
        private Class[] paramterClass;
        //对象
        private Object object;

        public TofuMethod(String label, Method method, Class[] paramterClass, Object object) {
            this.label = label;
            this.method = method;
            this.paramterClass = paramterClass;
            this.object = object;
        }

        public String getlabel() {
            return label;
        }

        public Method getMethod() {
            return method;
        }

        public Class[] getParamterClass() {
            return paramterClass;
        }

        public Object getObject() {
            return object;
        }
    }
}
