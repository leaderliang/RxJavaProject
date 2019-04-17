package com.android.rxjavaproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.rxjavaproject.retrofit.GithubService;
import com.android.rxjavaproject.retrofit.Repo;
import com.android.rxjavaproject.utils.JsonFormat;
import com.android.rxjavaproject.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 各个操作符执行的结果通过 log 日志来进行查看
 * 操作符
 * create、map、zip、Concat、flatMap、concatMap
 *
 * @author liang
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RxUsage.testPrintThreadNameMoreChange();
/*      RxUsage.commonUsage();
        RxUsage.commonUsageForChain();
        RxUsage.testPrintThreadName();
        RxUsage.testPrintThreadNameMoreChange();
        RxUsage.testCreate();
        RxUsage.testMap();
        RxUsage.testZip();
        RxUsage.testConcat();
        RxUsage.testFlatMap();
        RxUsage.testConcatMap();*/



        getRepos();


    }

    /**
     * Retrofit 中使用 RxJava 线程调度
     */
    private void getRepos() {
        GithubService mGithubService = RetrofitClient.getInstance().create(GithubService.class);
        /* 传统方式调用*/
        Call<List<Repo>> repos = mGithubService.getUserRepos_();
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                Log.e(TAG, response.code() + " " + new Gson().toJson(response.body()));
                Log.e(TAG, response.code() + " " + JsonFormat.format(new Gson().toJson(response.body())));

            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Log.e(TAG, "onFailure");
                Toast.makeText(MainActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

        /* Rx 方式调用*/
        mGithubService.getUserRepos()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Repo>>() {
                    @Override
                    public void accept(List<Repo> repos) throws Exception {
                        ToastUtil.show(MainActivity.this, " " + new Gson().toJson(repos));
                    }
                });
//                .subscribe(new Observer<List<Repo>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Repo> repos) {
//                        Toast.makeText(MainActivity.this, " " + new Gson().toJson(repos), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }


}
