package com.android.rxjavaproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.rxjavaproject.retrofit.GithubService;
import com.android.rxjavaproject.retrofit.Repo;
import com.android.rxjavaproject.utils.JsonFormat;
import com.android.rxjavaproject.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.reactivestreams.Subscriber;

import java.io.IOException;
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
 *
 * 各个操作符的简单使用及执行的结果通过 log 日志来进行查看
 *
 * 操作符
 *  create：
 *  map：
 *  zip：
 *  Concat：
 *  flatMap:
 *  concatMap:
 *
 * @author liang
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private boolean isDefaultAddress;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        RxUsage.testZip();



/*      RxUsage.commonUsage();

        RxUsage.commonUsageForChain();

        RxUsage.testPrintThreadName();

        RxUsage.testPrintThreadNameMoreChange();

        RxUsage.testCreate();

        RxUsage.testMap();

        RxUsage.testZip();

        RxUsage.testConcat();

        RxUsage.testFlatMap();

        RxUsage.testConcatMap();

        getRepos();
        */



    }

    /**
     * Retrofit 中使用 RxJava 线程调度
     *
     * repos.enqueue 异步请求
     *
     * call.execute(); 代码会阻塞线程，因此你不能在安卓的主线程中调用，不然会面临 NetworkOnMainThreadException。如果你想调用execute方法，请在开启子线程执行。
     *
     * repos.cancel();
     */
    private void getRepos() {
        GithubService mGithubService = RetrofitClient.getInstance().create(GithubService.class);
        /* 传统方式调用*/
        Call<List<Repo>> repos = mGithubService.getUserRepos_();

        /*同步请求 报错*/
        /*try {
            Response<List<Repo>> repo  = repos.execute();
            Log.e(TAG, repo.code() + " " + new Gson().toJson(repo.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

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
//                        Toast.makeText(MainActivity.this, new Gson().toJson(repos), Toast.LENGTH_SHORT).show();
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
