package com.lee.aidiancan;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    private EditText etUserInput;
    private RecyclerView rvMessages;
    private ChatAdapter chatAdapter;
    private DeepseekApi deepseekApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ImageButton btnBack = findViewById(R.id.btn_back);
        etUserInput = findViewById(R.id.et_user_input);
        rvMessages = findViewById(R.id.rv_messages);
        ImageButton btnSubmit = findViewById(R.id.btn_submit);

        setupRecyclerView();
        setupDeepseekApi();

        btnBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            String userInput = etUserInput.getText().toString();
            if (!userInput.isEmpty()) {
                // 添加用户消息
                chatAdapter.addMessage(new ChatMessage(userInput, ChatMessage.TYPE_USER));
                // 清空输入框
                etUserInput.setText("");
                // 发送到API
                sendToDeepseek(userInput);
                // 滚动到底部
                rvMessages.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });
    }

    private void setupRecyclerView() {
        chatAdapter = new ChatAdapter(new ArrayList<>());
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(chatAdapter);
    }

    private void setupDeepseekApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        deepseekApi = retrofit.create(DeepseekApi.class);
    }

    private void sendToDeepseek(String message) {
        // 添加"正在思考中..."消息
        int thinkingPosition = chatAdapter.getItemCount();
        runOnUiThread(() -> {
            chatAdapter.addMessage(new ChatMessage("正在思考中...", ChatMessage.TYPE_AI));
            rvMessages.smoothScrollToPosition(thinkingPosition);
        });

        String authorization = "Bearer " + ApiConfig.API_KEY;
        Call<DeepseekResponse> call = deepseekApi.sendMessage(authorization, new DeepseekRequest(message));
        
        call.enqueue(new Callback<DeepseekResponse>() {
            @Override
            public void onResponse(Call<DeepseekResponse> call, Response<DeepseekResponse> response) {
                runOnUiThread(() -> {
                    // 移除"正在思考中..."消息
                    ((ArrayList<ChatMessage>)chatAdapter.getMessages()).remove(thinkingPosition);
                    chatAdapter.notifyItemRemoved(thinkingPosition);

                    if (response.isSuccessful() && response.body() != null) {
                        String aiResponse = response.body().getResponse();
                        if (aiResponse != null && !aiResponse.isEmpty()) {
                            chatAdapter.addMessage(new ChatMessage(aiResponse, ChatMessage.TYPE_AI));
                            rvMessages.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                        } else {
                            Toast.makeText(ChatActivity.this, "AI响应为空", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String errorMessage = "请求失败";
                        try {
                            if (response.errorBody() != null) {
                                errorMessage = response.errorBody().string();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<DeepseekResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    // 移除"正在思考中..."消息
                    ((ArrayList<ChatMessage>)chatAdapter.getMessages()).remove(thinkingPosition);
                    chatAdapter.notifyItemRemoved(thinkingPosition);
                    Toast.makeText(ChatActivity.this, 
                        "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
} 