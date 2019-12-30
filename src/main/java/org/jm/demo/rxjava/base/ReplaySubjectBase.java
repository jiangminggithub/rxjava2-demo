package org.jm.demo.rxjava.base;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

/**
 * @author jiangming
 * <p>
 * ReplaySubject: 释放接收到的所有数据
 */
public class ReplaySubjectBase {

    public static void main(String[] args) {

        // 1. 接受收到的所有数据以及通知，对每隔Observer都执行相同的独立的操作
        ReplaySubject<Integer> subject = ReplaySubject.create();

        // 2. 指定内部缓存大小，此方法避免在内部缓冲区增长以容纳新缓冲区时过多的数组重分配
        // ReplaySubject<Integer> subject = ReplaySubject.create(5);

        // 3. createWithSize(count)
        // 指定保留订阅前数据项的个数的Subject，会发射订阅前count个数据和后续的数据
        // ReplaySubject<Integer> subject = ReplaySubject.createWithSize(1);

        // 4. createWithTime(maxAge, unit, scheduler)
        // 指定保留订阅前指定maxAge时间段内数据和后续的数据
        // ReplaySubject<Integer> subject = ReplaySubject.createWithTime(1, TimeUnit.MILLISECONDS, Schedulers.trampoline());

        // 创建Observer(观察者), 可以接受Observable所有通知
        Observer<Integer> observer = new Observer<Integer>() {

            public void onSubscribe(Disposable d) {
                System.out.println("----------------------------------");
                System.out.println("--> onSubscribe");
            }

            public void onNext(Integer t) {
                System.out.println("--> onNext = " + t);
            }

            public void onError(Throwable e) {
                System.out.println("--> onError: " + e);
            }

            public void onComplete() {
                System.out.println("--> onComplete");
            }
        };

        // 正常接受所有Observable的数据和通知
        subject.subscribe(observer);
        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);

        // 正常接受所有Observable的数据和通知
        subject.subscribe(observer);
        subject.onNext(4);
        // 如果有error，则发送error通知，不影响任何一个观察者数据与通知接受
        // subject.onError(new NullPointerException());
        subject.onNext(5);
        subject.onComplete();

        // 正常接受所有Observable的数据和通知
        subject.subscribe(observer);

    }

}
