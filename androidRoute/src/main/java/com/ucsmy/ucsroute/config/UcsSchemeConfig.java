package com.ucsmy.ucsroute.config;

import com.ucsmy.ucsroute.RouteUri;
import com.ucsmy.ucsroute.platform.IUcsPlatForm;

/**
 * 配置工具
 * Created by Seven on 2017/5/10.
 */

public interface UcsSchemeConfig {

    IUcsPlatForm getPlotForm(RouteUri uri);
}
