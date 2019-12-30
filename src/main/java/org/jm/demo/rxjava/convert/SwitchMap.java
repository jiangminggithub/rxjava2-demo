package org.jm.demo.rxjava.convert;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * SwitchMap： 有选择的订阅Observable，
 * 当原始Observable发射一个数据，通过 witchMap 返回一个Observable，
 * 当原始Observable发射一个新的数据时，它将取消订阅并停止监视产生执之前的Observable，监视当前新的Observable
 * 每个Observable任务都在子线程中有效，如果在一个线程中就会依次顺序执行。
 * 常用于网络请求中
 */
public class SwitchMap {

    public static void main(String[] args) throws Exception {

        // 1. witchMap(Function(T,R))
        // 同一个线程执行
        Observable.range(1, 3)
                .switchMap(new Function<Integer, ObservableSource<? extends Integer>>() {

                    @Override
                    public ObservableSource<? extends Integer> apply(Integer t) throws Exception {
                        System.out.println("--> apply(1): " + t);
                        return Observable.range(1, 3);    // 每个任务指定在同一个线程执行
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept switchMap(1): " + t);
                    }
                });

        System.out.println("---------------------------------------");
        // 2. witchMap(Function(T,R))
        // 不同线程执行
        Observable.range(1, 3)
                .switchMap(new Function<Integer, ObservableSource<? extends Integer>>() {

                    @Override
                    public ObservableSource<? extends Integer> apply(Integer t) throws Exception {
                        System.out.println("--> apply(2): " + t);
                        return Observable.range(1, 3)
                                .subscribeOn(Schedulers.newThread());    // 每个任务指定在子线程执行
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept switchMap(2): " + t);
                    }
                });


        System.out.println("---------------------------------------");
        // 3. switchMap(mapper, bufferSize)
        // bufferSize 参数是从当前活动的Observable中预读数据的大小
        Observable.range(1, 3)
                .switchMap(new Function<Integer, ObservableSource<? extends Integer>>() {

                    @Override
                    public ObservableSource<? extends Integer> apply(Integer t) throws Exception {
                        System.out.println("--> apply(3): " + t);
                        return Observable.range(1, 10).subscribeOn(Schedulers.newThread());
                    }
                }, 3)
                .subscribe(new Consumer<Integer>() {    // 指定缓存大小为3

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept switchMap(3): " + t);
                    }
                });

        System.in.read();

    }

}
