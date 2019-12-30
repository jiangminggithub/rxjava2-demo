package org.jm.demo.rxjava.auxiliary;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * Timestamp: 给Observable发射的数据项附加一个指定的时间戳信息
 */
public class Timestamp {

    public static void main(String[] args) throws Exception {

        /**
         *  1. timestamp(Scheduler scheduler)
         *  scheduler: 可选参数，指定线程调度器
         *
         *  给Observable发射的数据项附加一个时间戳信息
         */
        Observable.intervalRange(1, 5, 1, 100, TimeUnit.MILLISECONDS)
                .timestamp(Schedulers.newThread())      // 指定在子线程调度处理
                .subscribe(new Observer<Timed<Long>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(1)");
                    }

                    @Override
                    public void onNext(Timed<Long> longTimed) {
                        long time = longTimed.time();           // 连续数据间的间隔时间
                        TimeUnit unit = longTimed.unit();       // 连续数据间的时间间隔单位
                        Long value = longTimed.value();         // Observable发送的数据项
                        System.out.println("--> onNext(1): " + longTimed);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(1): " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onComplete(1)");
                    }
                });

        System.in.read();
        System.out.println("-------------------------------------------");
        /**
         *  2. timestamp(TimeUnit unit, Scheduler scheduler)
         *  scheduler: 可选参数，指定线程调度器
         *
         *  给Observable发射的数据项附加一个指定单位的时间戳信息
         */
        Observable.intervalRange(1, 5, 1, 1200, TimeUnit.MILLISECONDS)
                .timestamp(TimeUnit.SECONDS, Schedulers.newThread())    // 指定时间单位为秒，在子线程调度处理
                .subscribe(new Observer<Timed<Long>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(2)");
                    }

                    @Override
                    public void onNext(Timed<Long> longTimed) {
                        System.out.println("--> onNext(2): " + longTimed);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(2): " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onComplete(2)");
                    }
                });

        System.in.read();

    }
}
