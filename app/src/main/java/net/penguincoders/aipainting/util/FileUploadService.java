package net.penguincoders.aipainting.util;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {
    @Multipart
    @POST("/upload/files")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);

    @GET("/upload/ana_files")
    Call<ResponseBody> emptyGetRequest();
}
