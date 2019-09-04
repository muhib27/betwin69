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
   // String phTaskSubmitId = CMTaskSubmitFragment.phTaskSubmitId;

//    @FormUrlEncoded
//    @POST(URLHelper.URL_LOGIN)
////    Observable<Response<List<LoginResponseModel>>> userLogin(@Field("username") String userId, @Field("password") String password);
//    Observable<Response<LoginApiModel>> userLogin(@Field("username") String userId, @Field("password") String password, @Field("fcm_id") String gcm_id);
//
//    @FormUrlEncoded
//    @POST(URLHelper.URL_PROFILE)
////    Observable<Response<List<LoginResponseModel>>> userLogin(@Field("username") String userId, @Field("password") String password);
//    Observable<Response<ProfileResponseModel>> userProfile(@Field("api_key") String api_key);

    @FormUrlEncoded
    @POST(URLHelper.ADD_FCM)
//    Observable<Response<List<LoginResponseModel>>> userLogin(@Field("username") String userId, @Field("password") String password);
    Observable<Response<JsonElement>> addFcm(@Field("fcm_id") String fcm_id);

//    @FormUrlEncoded
//    @POST(URLHelper.GET_MENU)
//    Observable<Response<MenuApiResponse>> getMenu(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_LOGOUT)
//    Observable<Response<JsonElement>> getLogout(@Field("api_key") String api_key, @Field("fcm_id") String fcm_id);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_PIGEONHOLE_TASK_LIST)
//    Observable<Response<PHTaskListResponse>> getPigeonholeTaskList(@Field("api_key") String api_key);
//
//
//    @POST(URLHelper.GET_PIGEONHOLE_TASK_ADD)
//      //Observable<Response<JsonElement>> getTaskAssign(@Body RequestBody file);
//    Observable<Response<JsonElement>> getTaskAssign(@Body MultipartBody file);
//
//    @POST(".")
//        //Observable<Response<JsonElement>> getTaskAssign(@Body RequestBody file);
//    Observable<Response<JsonElement>> getTaskEdit(@Body MultipartBody file);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_PIGEONHOLE_TASK_DELETE)
//    Observable<Response<JsonElement>> pigeonholeDelete(@Field("id") String id, @Field("api_key") String api_key);
//
//      //Observable<Response<JsonElement>> getTaskAssign(@Part("api_key") RequestBody api_key,@Part List<MultipartBody.Part> file, @Part("users[]") List<String> selectedList, @Part("course[]") List<String> courseList, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("due_date") RequestBody due_date);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_PIGEONHOLE_GET_COURSES)
//    Observable<Response<PigeonholeGetCourseApiResponse>> getPigeonholeCourses(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_PIGEONHOLE_DETAILS + "/{id}")
//    Observable<Response<PHTaskViewResponse>> getSinglePHDetails(@Field("api_key") String api_key, @Path("id") String id);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_PIGEONHOLE_TASK_VIEW_SUBITTED_TASK + "/{id}")
//    Observable<Response<PHTaskSubmitResponse>> getPHTaskViewSubmitTask(@Field("api_key") String api_key, @Path("id") String id);
//
//    @POST(".")
//        //Observable<Response<JsonElement>> getTaskAssign(@Body RequestBody file);
//    Observable<Response<JsonElement>> getPHTaskSubmitTask(@Body MultipartBody file);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_CM_BOX_SUBMITTED_LIST)
//    Observable<Response<CMBoxSubmittedTaskResponse>> getCMBoxList(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_CM_BOX_DETAILS + "/{id}")
//    Observable<Response<CMBoxSubmittedTaskResponse>> getCMBoxDetails(@Field("api_key") String api_key, @Path("id") String id);
//
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_PROGRAM_WHITE_ROUTINE_LIST)
//    Observable<Response<RoutineResponseModel>> getWhiteRoutine(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_PROGRAM_YELLOW_ROUTINE_LIST)
//    Observable<Response<RoutineResponseModel>> getYellowRoutine(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_PROGRAM_BLUE_ROUTINE_LIST)
//    Observable<Response<RoutineResponseModel>> getBlueRoutine(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_EVENTS_LIST)
//    Observable<Response<EventsResponseModel>> getEvents(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_NOTICE_LIST)
//    Observable<Response<NoticeResponseModel>> getNoticeList(@Field("api_key") String api_key);
//
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_NOTICE_DETAILS + "/{id}")
//    Observable<Response<SingleNoticeResponseModel>>  getSingleNoticeDetails(@Field("api_key") String api_key, @Path("id") String id);
//
//    @POST(URLHelper.GET_NOTICE_ADD)
//        //Observable<Response<JsonElement>> getTaskAssign(@Body RequestBody file);
//    Observable<Response<JsonElement>> getNoticeAdd(@Body MultipartBody file);
//
//    @POST(".")
//        //Observable<Response<JsonElement>> getTaskAssign(@Body RequestBody file);
//    Observable<Response<JsonElement>> getNoticeEdit(@Body MultipartBody file);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_NOTICE_DELETE)
//    Observable<Response<JsonElement>> noticeDelete(@Field("id") String id, @Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_RESEARCH_WING_LIST)
//    Observable<Response<ResearchWingResponseModel>> getResearchWingList(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_RESEARCH_TOPIC_LIST + "/{id}")
//    Observable<Response<ResearchTopicResponseModel>> getResearchTopicList(@Field("api_key") String api_key, @Path("id") String id);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_RESEARCH_CM_LIST)
//    Observable<Response<ResearchTopicResponseModel>> getCMResearch(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_INSTRUCTOR_LIST)
//    Observable<Response<InstructorResponseModel>> getInstructorList(@Field("api_key") String api_key);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_READING_LIST)
//    Observable<Response<RPResponseModel>> getReadingList(@Field("api_key") String api_key, @Field("parent_id") String parent_id);
//
//    @FormUrlEncoded
//    @POST(URLHelper.GET_READING_CONTENT)
//    Observable<Response<RMResponseModel>> getReadingContent(@Field("api_key") String api_key, @Field("reading_content_id") String reading_content_id);
//
//
//




}


