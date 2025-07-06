package com.lee.aidiancan;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DeepseekApi {
    @POST("chat/completions")
    Call<DeepseekResponse> sendMessage(
        @Header("Authorization") String authorization,
        @Body DeepseekRequest request
    );
} 