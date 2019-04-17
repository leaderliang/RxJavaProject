package com.android.rxjavaproject;

import android.widget.Toast;

import com.android.rxjavaproject.retrofit.GithubService;
import com.android.rxjavaproject.retrofit.Repo;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * TODO 创建一个Retrofit客户端
 *
 * @author dev.liang <a href="mailto:dev.liang@outlook.com">Contact me.</a>
 * @version 1.0
 * @since 2019/04/17 19:34
 */
public class RetrofitClient {


    public static Retrofit getInstance() {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        /*OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(9, TimeUnit.SECONDS);*/


        if (BuildConfig.DEBUG) {
            /*打印日志拦截器*/
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return new Retrofit.Builder().baseUrl("https://api.github.com")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /*
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    GithubService mGithubService = retrofit.create(GithubService.class);
    Call<List<Repo>> repos = mGithubService.getUserRepos();
        repos.enqueue(new Callback<List<Repo>>() {
        @Override
        public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
            System.out.println(response.code() + " " + new Gson().toJson(response.body()));
        }

        @Override
        public void onFailure(Call<List<Repo>> call, Throwable t) {
            Toast.makeText(MainActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
        }
    });
    */

}
