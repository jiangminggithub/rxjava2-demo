package org.jm.demo.rxjava.connectable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * Replay: 保证所有的观察者收到相同的数据序列，即使它们在Observable开始发射数据之后才订阅。
 * <p>
 * 如果在将一个Observable转换为可连接的Observable之前对它使用 Replay 操作符，
 * 产生的这个可连接Observable将总是发射完整的数据序列给任何未来的观察者，
 * 即使那些观察者在这 个Observable开始给其它观察者发射数据之后才订阅。
 */
public class Replay {

    public static void main(String[] args) throws Exception {
        // 创建发射数据的Observable
        Observable<Long> observable = Observable
                .intervalRange(1,
                        10,
                        1,
                        500,
                        TimeUnit.MILLISECONDS,
                        Schedulers.newThread());

        /**
         * 1.1 replay(Scheduler scheduler)
         * 可选参数：scheduler, 指定线程调度器
         * 接受原始数据的所有数据
         */
    //  ConnectableObservable<Long> replay1 = observable.replay();

        /**
         * 1.2 replay(int bufferSize, Scheduler scheduler)
         * 可选参数：scheduler, 指定线程调度器
         * 只缓存 bufferSize 个最近的原始数据
         */
    //  ConnectableObservable<Long> replay1 = observable.replay(1); // 设置缓存大小为1, 从原数据中缓存最近的1个数据

        /**
         * 1.3 replay(int bufferSize, long time, TimeUnit unit, Scheduler scheduler)
         * 可选参数：scheduler, 指定线程调度器
         * 在订阅前指定的时间段内缓存 bufferSize 个数据, 注意计时开始是原始数据发射第1个数据项之后开始
         */
    //  ConnectableObservable<Long> replay1 = observable.replay(5, 1000, TimeUnit.MILLISECONDS);

        /**
         * 1.4 replay(long time, TimeUnit unit, Scheduler scheduler)
         * 可选参数：scheduler, 指定线程调度器
         * 在订阅前指定的时间段内缓存数据, 注意计时开始是原始数据发射第1个数据项之后开始
         */
       ConnectableObservable<Long> replay1 = observable.replay( 1000, TimeUnit.MILLISECONDS);

        // 进行 connect 操作
        replay1.connect();

        // 第一个观察者
        replay1.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> onSubScribe(1-1)");
            }
        }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("--> accept(1-1): " + aLong);
            }
        });

        // 第二个观察者（延迟1秒后订阅）
        replay1.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> onSubScribe(1-2)");
            }
        }).delaySubscription(1, TimeUnit.SECONDS)
          .subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    System.out.println("--> accept(1-2): " + aLong);
                }
          });

        // 第三个观察者（延迟2秒后订阅）
        replay1.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> onSubScribe(1-3)");
            }
        }).delaySubscription(2, TimeUnit.SECONDS)
           .subscribe(new Consumer<Long>() {
               @Override
               public void accept(Long aLong) throws Exception {
                   System.out.println("--> accept(1-3): " + aLong);
               }
           });

        System.in.read();
        System.out.println("----------------------------------------------------------");
        /**
         * 2. replay(Function<Observable<T>, ObservableSource<R>> selector,
         * int bufferSize,              可选参数： 指定从元数据序列数据的缓存大小
         * long time, TimeUnit unit,    可选参数： 指定缓存指定时间段的数据序列
         * Scheduler scheduler)         可选参数： 指定线程调度器
         *
         * 接受一个变换函数 function 为参数，这个函数接受原始Observable发射的数据项为参数
         * 通过指定的函数处理后，返回一个处理后的Observable
         */
        Observable<String> replayObservable = observable.replay(new Function<Observable<Long>, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Observable<Long> longObservable) throws Exception {
                // 对原始数据进行处理
                Observable<String> map = longObservable.map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return aLong + "² = " + aLong * aLong;  // 将原始数据进行平方处理，并转换为字符串数据类型
                    }
                });

                return map;
            }
        }, 1, Schedulers.newThread());

        replayObservable.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread());

        // 第一个观察者
        replayObservable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("--> onSubScribe(2-1)");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("--> accept(2-1)： " + s);
            }
        });

        // 订阅第二个观察者 （延迟2秒后订阅）
        replayObservable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("--> onSubScribe(2-2)");
            }
        }).delaySubscription(2, TimeUnit.SECONDS)
          .subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    System.out.println("--> accept(2-2): " + s);
                }
           });

        System.in.read();
    }
}
