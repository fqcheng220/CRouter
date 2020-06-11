package com.fqcheng220.crouterapi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.fqcheng220.crouterannotation.entity.RouteMeta;
import com.fqcheng220.crouterapi.exception.ExceptionConsts;
import io.reactivex.Observable;
import java.util.HashMap;
import java.util.Map;

public class PostCard {
    public Context mCtx;
    public String mUrl;
    public Bundle mBundle;

    public void start(Context context){
        mCtx = context;
        RouteMeta routeMeta = CRouter.getInstance().matchRouteMeta(this);
        if(routeMeta != null){
            //routeMeta.routeWithoutObservable(this);
            routeWithoutObservable(routeMeta);
        }
    }

    public Observable<Response> startObservable(Context context){
        mCtx = context;
        RouteMeta routeMeta = CRouter.getInstance().matchRouteMeta(this);
        if(routeMeta != null){
            //return routeMeta.routeWithObservable(this);
            return routeWithObservable(routeMeta);
        }else{
            return Observable.error(new IllegalStateException(ExceptionConsts.STATE_MSG_NOT_ROUTEMETA_MATCHED));
        }
    }


    public Converter.Factory mCommontConverterFactory = new CommontConverterFactory();
    public Observable<Response> routeWithObservable(RouteMeta routeMeta) {
        ////针对需要返回数据的请求 如果不是FragmentActivity的context直接抛出异常
        //if (mCtx instanceof FragmentActivity) {
        Object[] objArgs = analysisPostCard(routeMeta);
        Object ret = routeMeta.mMethodInvoker.invoke(objArgs);
        //} else {
        //    return Observable.error(new IllegalStateException(ExceptionConsts.STATE_MSG_NOT_CONTEXT_FRAGMENT_ACTIVITY));
        //}
        if (routeMeta.mRtnObservable) {
            return (Observable<Response>)ret;
        } else {
            return Observable.error(new IllegalStateException(ExceptionConsts.STATE_MSG_ROUTEMETA_NOT_OBSERVABLE));
        }
    }

    public void routeWithoutObservable(RouteMeta routeMeta) {
        Object[] objArgs = analysisPostCard(routeMeta);
        routeMeta.mMethodInvoker.invoke(objArgs);
    }

    private Object[] analysisPostCard(RouteMeta routeMeta) {
        String fullPath = mUrl;
        Bundle bundle = mBundle;

        Map<String,String> pathQueryMap = new HashMap<>();
        Uri uri = Uri.parse(fullPath);
        for(String key:uri.getQueryParameterNames()){
            pathQueryMap.put(key,uri.getQueryParameter(key));
        }
        Object[] objArgs = new Object[routeMeta.mParameterMetaList.size()];
        for (int i = 0; i < routeMeta.mParameterMetaList.size(); i++) {
            if(routeMeta.mParameterMetaList.get(i).mIsTypePostCard){
                objArgs[i] = this;
            }else{
                String key = routeMeta.mParameterMetaList.get(i).mQueryKey;
                Object value = null;
                if (pathQueryMap.containsKey(key)) {
                    value = pathQueryMap.get(key);
                } else if (bundle.containsKey(key)) {
                    value = bundle.get(key);
                }
                if (value != null) {
                    Converter converter = mCommontConverterFactory.requestConverter(routeMeta.mParameterMetaList.get(i).mType);
                    if (converter != null) {
                        objArgs[i] = converter.convert((String) value);
                    } else {
                        //objArgs[i] = pathQueryMap.get(routeMeta.mParameterMetaList.get(i));
                        objArgs[i] = value;
                    }
                } else {
                    objArgs[i] = null;
                }
            }
        }
        return objArgs;
    }
}
