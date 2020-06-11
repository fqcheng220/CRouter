package com.fqcheng220.crouterapi;

import android.os.Bundle;
import android.text.TextUtils;
import com.fqcheng220.crouterannotation.entity.RouteMeta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRouter {
    private static final String SEPERATOR_SCHEME = "://";
    private static final String SEPERATOR_PATH = "/";
    private static CRouter sInst;
    private static boolean sInit = false;
    public static CRouter getInstance(){
        //if(!sInit){
        //    throw new IllegalStateException("CRouter must be inited first！！！");
        //}
        if(sInst == null){
            sInst = new CRouter();
        }
        return sInst;
    }

    public static void init(String scheme){
        //if(sInit){
        //    throw new IllegalStateException("CRouter must only be inited once！！！");
        //}
        if(sInst == null){
            sInst = new CRouter(scheme,"");
        }
        sInit = true;
    }

    private CRouter(){
        mScheme = "dfcft";
        mHost = "fakehost";
    }

    private CRouter(String scheme,String host){
        mScheme = scheme;
        mHost = host;
    }

    private String mScheme;
    private String mHost;
    private Map<String, List<RouteMeta>> mMapHostMeta = new HashMap<>();

    public void initRouteMetas(String host, List<RouteMeta> list){
        //if(mMapHostMeta == null){
        //    mMapHostMeta = new HashMap<>();
        //}
        mMapHostMeta.put(host,list);
    }

    public RouteMeta matchRouteMeta(PostCard postCard){
        if(postCard != null && !TextUtils.isEmpty(postCard.mUrl)){
            List<RouteMeta> matchedList = null;
            String matchedHost = null;
            for(Map.Entry<String,List<RouteMeta>> entry:mMapHostMeta.entrySet()){
                if (postCard.mUrl.startsWith(mScheme + SEPERATOR_SCHEME  + entry.getKey())) {
                    matchedList = entry.getValue();
                    matchedHost = entry.getKey();
                    break;
                }
            }
            RouteMeta matchedRouteMeta = null;
            if(matchedList != null){
                for(RouteMeta routeMeta:matchedList){
                    if (postCard.mUrl.startsWith(mScheme + SEPERATOR_SCHEME + matchedHost + SEPERATOR_PATH + routeMeta.mPath)) {
                        matchedRouteMeta = routeMeta;
                        break;
                    }
                }
            }
            return matchedRouteMeta;
        }
        return null;
    }

    public static class Builder{
        private String mUrl;
        private Bundle mBundle;

        private Builder(String url){
            mUrl = url;
            mBundle = new Bundle();
        }

        public static Builder create(String url){
            return new Builder(url);
        }

        public Builder with(String key,String value){
            mBundle.putString(key,value);
            return this;
        }

        public PostCard build(){
            PostCard postCard = new PostCard();
            postCard.mUrl = mUrl;
            postCard.mBundle = mBundle;
            return postCard;
        }
    }
}
