package com.example.my_app.MyApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstace {
    private static RetrofitInstace instace;
    private ApiInterface apiInterface;

    private RetrofitInstace() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000") // Updated for emulator
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static RetrofitInstace getInstance() {
        if (instace == null) {
            instace = new RetrofitInstace();
        }
        return instace;
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }
}