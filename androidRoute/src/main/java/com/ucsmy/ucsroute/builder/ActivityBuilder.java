package com.ucsmy.ucsroute.builder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ucsmy.ucsroute.UcsRoute;
import com.ucsmy.ucsroute.helper.UcsRouteHelper;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 构建跳转的builder
 * Created by Seven on 2018/7/12.
 */
public final class ActivityBuilder extends UcsRoute.Builder {

    public ActivityBuilder(Context mContext) {
        super(mContext);
    }

    /**
     * 对应的activity
     *
     * @return
     */
    protected String activityName() {
        return UcsRouteHelper.activityName(mContext, uri.host(), tag);
    }

    /**
     * 构建意图
     *
     * @return
     */
    protected Intent routeIntent() {
        if(TextUtils.isEmpty(activityName())) {
            return null;
        }
        return UcsRouteHelper.getUcsIntent(mContext, activityName());
    }

    /**
     * 构建参数
     */
    protected void IntentParam(Intent intent) {
        if(null == intent) {
            return;
        }
        if(null != map && !map.isEmpty()) {
            Set<Map.Entry<String, Object>> entry = map.entrySet();
            for (Map.Entry e : entry) {
                Object o = e.getValue();
                if(null != o) {
                    if(o instanceof Integer) {
                        intent.putExtra(e.getKey().toString(), Integer.valueOf(o.toString()));
                    } else if(o instanceof Float) {
                        intent.putExtra(e.getKey().toString(), Float.valueOf(o.toString()));
                    } else if(o instanceof Double) {
                        intent.putExtra(e.getKey().toString(), Double.valueOf(o.toString()));
                    } else if(o instanceof String) {
                        intent.putExtra(e.getKey().toString(), o.toString());
                    } else if(o instanceof Boolean) {
                        intent.putExtra(e.getKey().toString(), Boolean.valueOf(o.toString()));
                    } else if(o instanceof Serializable) {
                        intent.putExtra(e.getKey().toString(), (Serializable) o);
                    }
                }
            }
        }
    }

    /**
     * 启动
     * activity
     */
    public void startActivity() {
        Intent intent = routeIntent();
        if(null == intent) {
            return;
        }
        IntentParam(intent);

        if(requestCode != -1) {
            ((Activity) mContext).startActivityForResult(intent, requestCode);
        } else {
            mContext.startActivity(intent);
        }
    }
}
