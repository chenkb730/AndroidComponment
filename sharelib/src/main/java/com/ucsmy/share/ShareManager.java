package com.ucsmy.share;

import com.ucsmy.share.config.ShareConfig;

/**
 * 分享管理
 * Created by Seven on 2016/12/30.
 */

public class ShareManager {


    public static ShareConfig CONFIG;

    public static void init(ShareConfig config) {
        CONFIG = config;
    }
}
