package net.penguincoders.aipainting.util;

import com.fasterxml.jackson.databind.JsonNode;

import net.penguincoders.aipainting.reponse.UserProfileResponse;
import net.penguincoders.aipainting.reponse.UserResultResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST(ServerDirectories.LOGIN_API)
    Call<ResponseBody> login(@Field("account") String account, @Field("password") String password);

    @FormUrlEncoded
    @PUT("/api/user/{userId}/userinfo") // 根据 userId 确定服务器目录
    Call<ResponseBody> updateUserInfo(
            @Path("userId") String userId,
            @Field("newNickname") String newNickname,
            @Field("newGender") String newGender
    );

//    @FormUrlEncoded
//    @POST(ServerDirectories.EXECUTE_API)
//    Call<JsonNode> sendInput(@Field("applicationId") String applicationId, @Field("text") String text);

    @POST(ServerDirectories.EXECUTE_API)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> sendInput(@Body RequestBody body);

    @FormUrlEncoded
    @POST(ServerDirectories.SEND_CODE_API)
    Call<ResponseBody> sendSmsCode(@Field("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST(ServerDirectories.SEND_CODE_REG_API)
    Call<ResponseBody> sendSmsCodeInRegister(@Field("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST(ServerDirectories.SMS_LOGIN_API)
    Call<ResponseBody> smsLogin(
            @Field("phoneNumber") String phoneNumber,
            @Field("smsCode") String smsCode
    );

    @FormUrlEncoded
    @POST(ServerDirectories.SMS_LOGIN_REG_API)
    Call<ResponseBody> smsLoginRegister(
            @Field("phoneNumber") String phoneNumber,
            @Field("password") String password,
            @Field("smsCode") String smsCode
    );

    @GET("/api/user/{userId}/get-profile")
    Call<ResponseBody> getProfile(@Path("userId") String userId);

    @Multipart
    @POST("/upload/files")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);

}

