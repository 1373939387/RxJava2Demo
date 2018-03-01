package com.haoruigang.observer.observerdemo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by 13739 on 2018/1/12.
 */

public interface API {

    @GET("api/{openapi}/{BaikeLemmaCardApi}")
    Call<Result> getUserInfoQueryMap(@Path("openapi") String openapi, @Path("BaikeLemmaCardApi") String BaikeLemmaCardApi, @QueryMap Map<String, String> params);

    @POST("api/openapi/BaikeLemmaCardApi")
    Observable<Result> savaUser(@Body Info info);

}
