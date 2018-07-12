package com.ucsmy.ucsroute.config;

import com.ucsmy.ucsroute.encrypt.IUcsRouteDecrypt;

/**
 * 配置
 * Created by Seven on 2017/5/10.
 */

public class UcsRouteConfig {

    private static UcsRouteConfig config;

    private UcsSchemeConfig ucsSchemeConfig;

    private IUcsRouteDecrypt decrypt;

    public static UcsRouteConfig getConfig() {
        if (null == config) {
            synchronized (UcsRouteConfig.class) {
                if (null == config) {
                    config = new UcsRouteConfig();
                }
            }
        }

        return config;
    }


    public void setUcsSchemeConfig(UcsSchemeConfig ucsSchemeConfig) {
        this.ucsSchemeConfig = ucsSchemeConfig;
    }

    public UcsSchemeConfig getUcsSchemeConfig() {
        return ucsSchemeConfig;
    }

    public void setEncrypt(IUcsRouteDecrypt decrypt) {
        this.decrypt = decrypt;
    }

    public IUcsRouteDecrypt getDecrypt() {
        return decrypt;
    }
}
