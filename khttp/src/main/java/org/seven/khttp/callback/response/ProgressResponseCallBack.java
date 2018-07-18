package org.seven.khttp.callback.response;

/**
 * 上传进度回调接口
 * Created by Seven on 2018/7/17.
 */
public interface ProgressResponseCallBack {
    /**
     * 回调进度
     *
     * @param bytesWritten  当前读取响应体字节长度
     * @param contentLength 总长度
     * @param done          是否读取完成
     */
    void onResponseProgress(long bytesWritten, long contentLength, boolean done);
}
