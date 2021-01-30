package ru.geekbrains.utils;

import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@UtilityClass
public class RetrofitUtils {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new PrettyLogger());

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public Retrofit getRetrofit(){

        logging.setLevel(BODY);
        httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(ConfigUtils.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

    }
}
