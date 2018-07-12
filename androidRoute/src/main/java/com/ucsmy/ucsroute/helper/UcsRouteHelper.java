package com.ucsmy.ucsroute.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * helper
 * Created by Seven on 2017/5/10.
 */

public class UcsRouteHelper {

    /**
     * 过滤符合条件的页面
     *
     * @param mContext
     * @param code
     * @return
     */
    public static String activityName(Context mContext, String code, String tag) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
            ActivityInfo[] activities = info.activities;

            for (ActivityInfo activityInfo : activities) {
                if(null != activityInfo.metaData) {
                    if(null != activityInfo.metaData.get(tag)) {
                        String tagData = activityInfo.metaData.get(tag).toString();
                        //过滤多个页面数据。可以一个页面配置多个tab
                        if(tagData.contains(",")) {
                            String[] tagDatas = tagData.split(",");
                            for (String data : tagDatas) {
                                if(code.equalsIgnoreCase(data.trim())) {
                                    return activityInfo.name;
                                }
                            }
                        }

                        if(code.equalsIgnoreCase(tagData.trim())) {
                            return activityInfo.name;
                        }

                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * 获得主动页面的intent
     *
     * @param mContext
     * @return
     */
    public static Intent getUcsIntent(Context mContext, String className) {

        Intent intent = null;
        if(!TextUtils.isEmpty(className)) {
            intent = new Intent();
            ComponentName componentName = new ComponentName(mContext.getPackageName(), className);
            intent.setComponent(componentName);
        }
        return intent;
    }
}
