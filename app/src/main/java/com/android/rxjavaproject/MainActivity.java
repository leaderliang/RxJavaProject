package com.android.rxjavaproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

/**
 * 各个操作符执行的结果通过 log 日志来进行查看
 * 操作符
 * create、map、zip、Concat、flatMap、concatMap
 *
 * @author liang
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/*      RxUsage.commonUsage();
        RxUsage.commonUsageForChain();
        RxUsage.testCreate();
        RxUsage.testMap();
        RxUsage.testZip();
        RxUsage.testConcat();
        RxUsage.testFlatMap();
        RxUsage.testConcatMap();*/

    }


}
