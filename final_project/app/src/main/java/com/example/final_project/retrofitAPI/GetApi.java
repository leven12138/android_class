package com.example.final_project.retrofitAPI;

import com.example.final_project.model.DownloadResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetApi {

    @GET("video")
    Call<DownloadResponse> getVideo (@Query("student_id") String studentId,
                                     @Query("extra_value") String extraValue,
                                     @Header("token") String token);
}
