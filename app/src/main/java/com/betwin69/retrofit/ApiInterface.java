package com.betwin69.retrofit;


import com.betwin69.utils.URLHelper;
import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


//import io.reactivex.Observable;


/**
 * Created by RR on 27-Dec-17.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(URLHelper.ADD_FCM)
//    Observable<Response<List<LoginResponseModel>>> userLogin(@Field("username") String userId, @Field("password") String password);
    Observable<Response<JsonElement>> addFcm(@Field("fcm_id") String fcm_id);





}


