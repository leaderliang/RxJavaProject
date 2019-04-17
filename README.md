# RxJava Project
## ObservableEmitter 和 Disposable 解释
### ObservableEmitter 
Emitter 是发射器的意思，那就很好猜了，这个就是用来发出事件的，它可以发出三种类型的事件，通过调用 emitter 的 onNext(T value)、onComplete() 和 onError(Throwable error) 就可以分别发出 next 事件、complete 事件和 error 事件。

注意，并不意味着可以随意发射乱七八糟的事件，需要满足一定的规则：

- 上游可以发送无限个 onNext, 下游也可以接收无限个 onNext.
- 当上游发送了一个 onComplete 后, 上游 onComplete 之后的事件将会继续发送, 而下游收到 onComplete 事件之后将不再继续接收事件.
- 当上游发送了一个 onError 后,  上游 onError 之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
- 上游可以不发送 onComplete 或 onError.
- 最为关键的是 onComplete 和 onError必须唯一并且互斥, 即不能发多个 onComplete, 也不能发多个 onError,  也不能先发一个 onComplete, 然后再发一个 onError, 反之亦然

> 注: 关于 onComplete 和 onError 唯一并且互斥这一点,  是需要自行在代码中进行控制, 如果你的代码逻辑中违背了这个规则, **并不一定会导致程序崩溃.** 比如发送多个onComplete是可以正常运行的, 依然是收到第一个 onComplete 就不再接收了, 但若是发送多个 onError, 则收到第二个 onError 事件会导致程序会崩溃， [代码仓库里](https://github.com/leaderliang/RxJavaProject/blob/master/app/src/main/java/com/android/rxjavaproject/RxUsage.java) 里都有写出。

### Disposable
单词的字面意思是一次性用品,用完即可丢弃的.  那么在RxJava中怎么去理解它呢, 对应于上面的水管的例子, 我们可以把它理解成两根管道之间的一个机关, 当调用它的 dispose() 方法时, 它就会将两根管道切断, 从而导致下游收不到事件.

> 注意: 调用 dispose() 并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.

### subscribe ()
subscribe 有好几个重载方法

```
public final Disposable subscribe() {}
public final Disposable subscribe(Consumer<? super T> onNext) {}
public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {} 
public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {}
public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {}
public final void subscribe(Observer<? super T> observer) {}
```
- 最后一个带有 Observer 参数的在 RxUsage 中的 commonUsageForChain（）已使用过；
- 不带任何参数的 subscribe() 表示下游不关心任何事件,你上游尽管发你的数据去吧, 下游可不关心你发什么；
- 带有一个 Consumer 参数的方法表示下游只关心 onNext 事件, 其他的事件我假装没看见, 因此我们如果只需要 onNext 事件可以这么写；
- 其他几个方法同理, 这里就不解释啦，下面示例已列出。

```
Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emitter 1");
                emitter.onNext(1);
                Log.d(TAG, "emitter 2");
                emitter.onNext(2);
                Log.d(TAG, "emitter 3");
                emitter.onNext(3);
                Log.d(TAG, "emitter complete");
                emitter.onComplete();
                Log.d(TAG, "emitter 4");
                emitter.onNext(4);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "onNext: " + integer);
            }
        }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        
           }
        }...);
```

## RxJava 强大的线程控制
### 上下游默认是在同一个线程工作
在主线程中分别创建上游和下游, 然后将他们连接在一起, 同时分别打印出它们所在的线程
```
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
            Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
            Log.d(TAG, "emit 1");
            emitter.onNext(1);
        }
    });

    Consumer<Integer> consumer = new Consumer<Integer>() {
        @Override
        public void accept(Integer integer) throws Exception {
            Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
            Log.d(TAG, "onNext: " + integer);
        }
    };

    observable.subscribe(consumer);
}
```
运行结果为:
```
TAG: Observable thread is : main
TAG: emit 1                     
TAG: Observer thread is :main   
TAG: onNext: 1
```








### 参考：
> https://www.jianshu.com/p/464fa025229e
