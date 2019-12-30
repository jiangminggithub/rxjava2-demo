package org.jm.demo.rxjava.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.subjects.ReplaySubject;

/**
 * @author Ming
 * <p>
 * SerializedObserver: 保证同时只有一个线程调用 onNext, onCompleted,
 * onError方法，并不是将所有emit的值放到一个线程上然后处理
 */
public class SerializedObserverBase {

    public static void main(String[] args) throws Exception {
        // 创建Subject
        ReplaySubject<String> subject = ReplaySubject.create();

        // 创建一个 SerializedObserver来进行串行化，保证线程安全
        // 注意：只保证同时只有一个线程调用 onNext, onCompleted, onError方法，并不是将所有emit的值放到一个线程上然后处理
        SerializedObserver<String> observer = new SerializedObserver<String>(new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe");
            }

            @Override
            public void onNext(String t) {
                System.out.println("--> onNext: " + t + ", ReceiverThreadID: " + Thread.currentThread().getId());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError");
            }

            @Override
            public void onComplete() {
                System.out.println("--> onComplete");
            }
        });

        // 订阅
        subject.subscribe(observer);

        // 多线程执行
        for (int i = 0; i < 10; i++) {
            final int value = i + 1;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    subject.onNext(value + "-SendThreadID: " + Thread.currentThread().getId());
//					if (value == 10) {
//						subject.onComplete();
//					}
                }
            }).start();
        }

        System.in.read();
    }

}
