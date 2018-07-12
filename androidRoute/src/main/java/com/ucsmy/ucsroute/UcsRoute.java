package com.ucsmy.ucsroute;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ucsmy.ucsroute.config.UcsRouteConfig;
import com.ucsmy.ucsroute.config.UcsSchemeConfig;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 路由
 * Created by Seven on 2017/3/6.
 */

public final class UcsRoute<T extends UcsRoute.Builder> {

    private UcsSchemeConfig schemeConfig;

    private T builder;

    public UcsRoute(T builder) {
        schemeConfig = UcsRouteConfig.getConfig().getUcsSchemeConfig();
        this.builder = builder;
        parameter();
    }


    /**
     * 跳转
     */
    public void go() {
        if(TextUtils.isEmpty(getScheme())) {
            return;
        }

        if(TextUtils.isEmpty(getHost())) {
            return;
        }
        if(null != schemeConfig && null != schemeConfig.getPlotForm(builder.uri)) {
            schemeConfig.getPlotForm(builder.uri).go(builder);
        }

    }


    private String getScheme() {
        if(null == builder.uri) {
            return "";
        }
        return builder.uri.scheme();
    }

    private String getHost() {
        if(null == builder.uri) {
            return "";
        }

        return builder.uri.host();
    }

    /**
     * 解析参数
     */
    private void parameter() {
        if(null == builder.uri) {
            return;
        }
        if(TextUtils.isEmpty(getScheme())) {
            return;
        }
        Set<String> keys = builder.uri.queryParameterNames();
        if(null == keys || keys.size() == 0) {
            return;
        }

        Object value = null;
        for (String key : keys) {
            value = builder.uri.queryParameter(key);
            if(null != value) {
                if(null != builder.map) {
                    builder.map.put(key.trim(), value);
                }
            }
        }
        return;
    }


    public static class Builder<T extends Builder> {

        public Context mContext;
        public String tag = "tag";
        public String key = "key";
        public RouteUri uri;
        public Map<String, Object> map;
        public int requestCode = -1;


        public Builder(Context mContext) {
            this.mContext = mContext;
            map = new LinkedHashMap<>();
        }

        public T tag(String tag) {
            this.tag = tag;
            return (T) this;
        }

        public T uri(RouteUri uri) {
            this.uri = uri;
            return (T) this;
        }

        public T setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return (T) this;
        }

        public T param(String key, Object value) {
            if(null != map) {
                map.put(key, value);
            }

            return (T) this;
        }

        public T key(String key) {
            this.key = key;
            return (T) this;
        }


        public UcsRoute create() {
            UcsRoute route = new UcsRoute(this);
            return route;
        }
    }
}
