package org.seven.khttp.api

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 通用的API接口
 * 使用url作为请求。不同于一般使用url注解
 * Created by Seven on 2018/7/17.
 */
interface KApiServise {
    @POST()
    @FormUrlEncoded
    fun post(@Url url: String?, @FieldMap map: Map<String, String>?): Observable<ResponseBody>?

    @POST()
    fun post(@Url url: String?, @Body any: Any?): Observable<ResponseBody>?

    @POST()
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun postJson(@Url url: String?, @Body jsonBody: RequestBody?): Observable<ResponseBody>?

    @GET()
    fun get(@Url url: String?, @QueryMap maps: Map<String, String>): Observable<ResponseBody>?

    @DELETE()
    fun delete(@Url url: String?, @QueryMap maps: Map<String, String>): Observable<ResponseBody>?

    @HTTP(method = "DELETE",/*path = "",*/hasBody = true)
    fun delete(@Url url: String?, @Body any: Any?): Observable<ResponseBody>?

    @Headers("Content-Type: application/json", "Accept: application/json")
    @HTTP(method = "DELETE",/*path = "",*/hasBody = true)
    fun deleteJson(@Url url: String, @Body jsonBody: RequestBody?): Observable<ResponseBody>?

    @PUT()
    fun put(@Url url: String, @QueryMap maps: Map<String, String>): Observable<ResponseBody>?

    @PUT()
    fun putBody(@Url url: String?, @Body any: Any?): Observable<ResponseBody>?

    @PUT()
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun putJson(@Url url: String?, @Body jsonBody: RequestBody?): Observable<ResponseBody>?

    @Multipart
    @POST()
    fun uploadFlie(@Url url: String?, @Part("description") description: RequestBody?, @Part("files") file: MultipartBody.Part?): Observable<ResponseBody>?

    @Multipart
    @POST()
    fun uploadFiles(@Url url: String?, @PartMap() maps: Map<String, RequestBody>?): Observable<ResponseBody>?

    @Multipart
    @POST()
    fun uploadFiles(@Url url: String?, @Part() parts: Collection<MultipartBody.Part>?): Observable<ResponseBody>?

    @Streaming
    @GET
    fun downloadFile(@Url url: String?): Observable<ResponseBody>
}