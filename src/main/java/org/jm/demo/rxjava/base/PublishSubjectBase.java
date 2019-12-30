package org.jm.demo.rxjava.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author jiangming
 * <p>
 * PublishSubject: 释放订阅后接收到的数据
 */
public class PublishSubjectBase {

    public static void main(String[] args) {
        // 释放订阅后接收到正常发射的数据，有error将不会发射任何数据
        PublishSubject<Integer> subject = PublishSubject.create();
        // 观察者对象
        Observer<Integer> observer = new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--------------------------------");
                System.out.println("--> onSubscribe");
            }

            @Override
            public void onNext(Integer t) {
                System.out.println("--> onNext: " + t);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError: " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onComplete");
            }
        };

        // 1. 此时订阅将释放后续正常发射的数据： 1，2, 3, 4, error
        // subject.subscribe(observer);
        subject.onNext(1);
        subject.onNext(2);

        // 2. 此时订阅，发射后续正常发射的数据：3, 4, error
        // subject.subscribe(observer);
        subject.onNext(3);
        subject.onNext(4);

        // 此时将不会发送任何数据，直接发送error
        subject.onError(new NullPointerException());
        subject.onNext(5);
        subject.onComplete();

        // 3. 此时订阅如果有error，仅发送error，否则无数据发射
        subject.subscribe(observer);

    }

}
