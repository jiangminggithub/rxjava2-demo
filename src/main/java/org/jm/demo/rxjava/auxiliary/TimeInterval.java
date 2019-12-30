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
 * TimeInterval:
 */
public class TimeInterval {

    public static void main(String[] args) throws Exception {

        /**
         * 1. timeInterval(Scheduler scheduler)
         *  scheduler: 可选参数，指定调度线程
         *  接收原始数据项，发射射表示相邻发射物时间间隔的对象
         */
        Observable.intervalRange(1, 10, 100, 100, TimeUnit.MILLISECONDS)
                .timeInterval()
             // .timeInterval(Schedulers.newThread())       // 指定工作线程
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
                        System.out.println("--> onNext(1): " + longTimed.toString());
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
        System.out.println("-------------------------------------------------");
        /**
         *  2. timeInterval(TimeUnit unit, Scheduler scheduler)
         *  指定时间间隔单位和指定工作线程，接收原始数据项，发射射表示相邻发射物时间间隔的对象
         */
        Observable.intervalRange(1, 10, 1000, 1200, TimeUnit.MILLISECONDS)
            //  .timeInterval(TimeUnit.SECONDS)                             // 指定时间间隔单位
                .timeInterval(TimeUnit.SECONDS, Schedulers.newThread())     // 指定时间间隔单位和指定工作线程
                .subscribe(new Observer<Timed<Long>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(2)");
                    }

                    @Override
                    public void onNext(Timed<Long> longTimed) {
                        System.out.println("--> onNext(2): " + longTimed.toString());
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
