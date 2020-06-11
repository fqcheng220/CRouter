package com.fqcheng220.crouterapi;

import android.net.Uri;
import android.os.Bundle;

import com.fqcheng220.crouterannotation.Path;
import com.fqcheng220.crouterannotation.Query;

import com.fqcheng220.crouterapi.exception.ExceptionConsts;
import io.reactivex.Observable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteMeta {
    public String mHost;
    public String mPath;
    public List<String> mParameterQueryList;
    public List<ParameterHandler> mParameterHandlerList;
    public Converter.Factory mCommontConverterFactory = new CommontConverterFactory();;
    public List<ParameterMeta> mParameterMetaList;
    public boolean mRtnObservable = false;
    public MethodInvoker mMethodInvoker;

    private Bundle mBundle;
    private Map<String,String> mPathQueryMap;
    private Object[] mObjArgs;

    public RouteMeta(String host,Method method) {
        mHost = host;
        parseMethod(method);
    }

    public RouteMeta(String host,List<ParameterMeta> parameterMetaList,boolean rtnObservable) {
        mHost = host;
        mParameterMetaList = parameterMetaList;
        mRtnObservable = rtnObservable;
    }

    private void parseMethod(Method method){
        mPath = method.getAnnotation(Path.class).value();
        //parseParameterQueryList(method);
        //parseParmaterHandlers(method);
        parseParameter(method);
        parseReturnValue(method);
    }

    public interface ParameterHandler<T>{
        T handle(String p);
    }

    private void parseParameter(Method method){
        Annotation[][] annotations = method.getParameterAnnotations();
        Class[] classes = method.getParameterTypes();
        if(mParameterMetaList == null){
            mParameterMetaList = new ArrayList<>();
        }
        for (int parameterIndex = 0; parameterIndex < annotations.length; parameterIndex++) {
            ParameterMeta parameterMeta = new ParameterMeta();
            mParameterMetaList.add(parameterMeta);
            //跳过PostCard参数类型 确保mParameterHandlerList与mParameterQueryList一一对应
            if(method.getParameterTypes()[parameterIndex] == PostCard.class){
                parameterMeta.mIsTypePostCard = true;
                continue;
            }
            Class clz = classes[parameterIndex];
            Annotation[] annotationArr1 = annotations[parameterIndex];
            int i = 0;
            for (; i < annotationArr1.length; i++) {
                //参数注解为@Query
                if (annotationArr1[i].annotationType() == Query.class) {
                    String value = (((Query) annotationArr1[i]).value());
                    parameterMeta.mQueryKey = value;
                    parameterMeta.mConverter = mCommontConverterFactory.requestConverter(clz);
                    break;
                }
            }
            if (i >= annotationArr1.length) {
                throw new IllegalArgumentException(ExceptionConsts.ARGS_MSG_SERVICE_API_PARAMS_LOST_ANNOTATION);
            }
        }
    }

    private void parseParameterQueryList(Method method) {
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int parameterIndex = 0; parameterIndex < annotations.length; parameterIndex++) {
            //跳过PostCard参数类型 确保mParameterHandlerList与mParameterQueryList一一对应
            if(method.getParameterTypes()[parameterIndex] == PostCard.class){
                continue;
            }
            Annotation[] annotationArr1 = annotations[parameterIndex];
            int i = 0;
            for (; i < annotationArr1.length; i++) {
                //参数注解为@Query
                if (annotationArr1[i].annotationType() == Query.class) {
                    String value = (((Query) annotationArr1[i]).value());
                    if (mParameterQueryList == null) {
                        mParameterQueryList = new ArrayList<>();
                    }
                    mParameterQueryList.add(value);
                    break;
                }
            }
            if (i >= annotationArr1.length) {
                throw new IllegalArgumentException(ExceptionConsts.ARGS_MSG_SERVICE_API_PARAMS_LOST_ANNOTATION);
            }
        }
    }

    private void parseParmaterHandlers(Method method){
        Class[] classes = method.getParameterTypes();
        for(Class clz:classes){
            //跳过PostCard参数类型 确保mParameterHandlerList与mParameterQueryList一一对应
            if(clz == PostCard.class) continue;
            ParameterHandler item;
            if(clz.isAssignableFrom(Integer.class) || clz.isAssignableFrom(int.class)){
                item = sParameterHandlerPrimitiveInteger;
            }else if(clz.isAssignableFrom(Short.class)){
                item = sParameterHandlerPrimitiveShort;
            }else{
                item = null;
            }
            if(mParameterHandlerList == null){
                mParameterHandlerList = new ArrayList<>();
            }
            mParameterHandlerList.add(item);
        }
    }

    private void parseReturnValue(Method method){
        Class clz = method.getReturnType();
        if(clz.isAssignableFrom(Observable.class)){
            mRtnObservable = true;
        }
    }

    private static ParameterHandlerPrimitiveInteger sParameterHandlerPrimitiveInteger = new ParameterHandlerPrimitiveInteger();
    private static ParameterHandlerPrimitiveShort sParameterHandlerPrimitiveShort = new ParameterHandlerPrimitiveShort();

    public static class ParameterHandlerPrimitiveInteger implements ParameterHandler<Integer>{
        @Override public Integer handle(String p) {
            return Integer.valueOf(p);
        }
    }

    public static class ParameterHandlerPrimitiveShort implements ParameterHandler<Short>{
        @Override public Short handle(String p) {
            return Short.valueOf(p);
        }
    }

    public interface MethodInvoker<T>{
        T invoke(Object... args);
    }

    //public boolean isPathMatched(String fullPath){
    //    return !TextUtils.isEmpty(fullPath) && !TextUtils.isEmpty(mPath) && fullPath.startsWith(mPath);
    //}

    public Observable<Response> routeWithObservable(PostCard postCard) {
        if (mRtnObservable) {
            ////针对需要返回数据的请求 如果不是FragmentActivity的context直接抛出异常
            //if (postCard.mCtx instanceof FragmentActivity) {
                analysisPostCard(postCard);
                return (Observable<Response>) mMethodInvoker.invoke(mObjArgs);
            //} else {
            //    return Observable.error(new IllegalStateException(ExceptionConsts.STATE_MSG_NOT_CONTEXT_FRAGMENT_ACTIVITY));
            //}
        } else {
            return Observable.error(new IllegalStateException(ExceptionConsts.STATE_MSG_ROUTEMETA_NOT_OBSERVABLE));
        }
    }

    public void routeWithoutObservable(PostCard postCard) {
        analysisPostCard(postCard);
        mMethodInvoker.invoke(mObjArgs);
    }

    private void analysisPostCard(PostCard postCard) {
        String fullPath = postCard.mUrl;
        Bundle bundle = postCard.mBundle;

        if (mPathQueryMap == null) {
            mPathQueryMap = new HashMap<>();
        }
        Uri uri = Uri.parse(fullPath);
        for(String key:uri.getQueryParameterNames()){
            mPathQueryMap.put(key,uri.getQueryParameter(key));
        }
        //if(!TextUtils.isEmpty(fullPath) && fullPath.indexOf("?") != -1){
        //    String subStr = fullPath.substring(fullPath.indexOf("?"));
        //    if (!TextUtils.isEmpty(subStr)) {
        //        String[] arr = subStr.split("&");
        //        int i = 0;
        //        while (i + 2 < arr.length) {
        //            if (!TextUtils.isEmpty(arr[i]) && !TextUtils.isEmpty(arr[i + 1])) {
        //                mPathQueryMap.put(arr[i], arr[i + 1]);
        //            }
        //            i += 2;
        //        }
        //    }
        //}
        //mObjArgs = new Object[mParameterQueryList.size()];
        //if(mParameterQueryList != null) {
        //    for (int i = 0; i < mParameterQueryList.size(); i++) {
        //        String key = mParameterQueryList.get(i);
        //        Object value = null;
        //        if (mPathQueryMap.containsKey(key)) {
        //            value = mPathQueryMap.get(key);
        //        } else if (bundle.containsKey(key)) {
        //            value = bundle.get(key);
        //        }
        //        if (value != null) {
        //            ParameterHandler parameterHandler = mParameterHandlerList.get(i);
        //            if (parameterHandler != null) {
        //                mObjArgs[i] = parameterHandler.handle((String)value);
        //            } else {
        //                mObjArgs[i] = mPathQueryMap.get(mParameterQueryList.get(i));
        //            }
        //        } else {
        //            mObjArgs[i] = null;
        //        }
        //    }
        //}
        mObjArgs = new Object[mParameterMetaList.size()];
        for (int i = 0; i < mParameterMetaList.size(); i++) {
            if(mParameterMetaList.get(i).mIsTypePostCard){
                mObjArgs[i] = postCard;
            }else{
                String key = mParameterMetaList.get(i).mQueryKey;
                Object value = null;
                if (mPathQueryMap.containsKey(key)) {
                    value = mPathQueryMap.get(key);
                } else if (bundle.containsKey(key)) {
                    value = bundle.get(key);
                }
                if (value != null) {
                    Converter converter = mParameterMetaList.get(i).mConverter;
                    if (converter != null) {
                        mObjArgs[i] = converter.convert((String) value);
                    } else {
                        mObjArgs[i] = mPathQueryMap.get(mParameterQueryList.get(i));
                    }
                } else {
                    mObjArgs[i] = null;
                }
            }
        }
    }
}
