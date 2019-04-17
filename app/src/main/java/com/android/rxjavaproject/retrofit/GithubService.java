package com.android.rxjavaproject.retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * TODO 声明自己的网络请求接口
 *
 * @author dev.liang <a href="mailto:dev.liang@outlook.com">Contact me.</a>
 * @version 1.0
 * @since 2019/04/17 20:05
 */
public interface GithubService {

    @GET("/users/leaderliang/repos")
    Call<List<Repo>> getUserRepos_();

    @GET("/users/leaderliang/repos")
    Observable<List<Repo>> getUserRepos();



}
