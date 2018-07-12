package com.ucsmy.share.share;

import com.sina.weibo.sdk.share.WbShareCallback;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.ucsmy.share.config.INFO;

/**
 * 分享监听
 * Created by Seven on 2016/12/30.
 */

public abstract class ShareListener implements IUiListener {
    @Override
    public final void onComplete(Object o) {
        shareSuccess();
    }

    @Override
    public final void onError(UiError uiError) {
        shareFailure(
                new Exception(uiError == null ? INFO.DEFAULT_QQ_SHARE_ERROR : uiError.errorDetail));
    }

    @Override
    public final void onCancel() {
        shareCancel();
    }

//    @Override
//    public final void onResponse(BaseResponse baseResponse) {
//        switch (baseResponse.errCode) {
//            case WBConstants.ErrorCode.ERR_OK:
//                shareSuccess();
//                break;
//            case WBConstants.ErrorCode.ERR_FAIL:
//                shareFailure(new Exception(baseResponse.errMsg));
//                break;
//            case WBConstants.ErrorCode.ERR_CANCEL:
//                shareCancel();
//                break;
//            default:
//                shareFailure(new Exception(baseResponse.errMsg));
//        }
//    }

    public abstract void shareSuccess();

    public abstract void shareFailure(Exception e);

    public abstract void shareCancel();

    // 用于缓解用户焦虑
    public void shareRequest() {
    }
}
