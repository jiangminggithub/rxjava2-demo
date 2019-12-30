package org.jm.demo.rxjava.base;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author jiangming
 * <p>
 * BehaviorSubject: 释放订阅前最后一个数据和订阅后接收到的所有数据
 */
public class BehaviorSubjectBase {

    public static void main(String[] args) throws Exception {
        // 创建无默认值的BehaviorSubject
        BehaviorSubject<Integer> subject = BehaviorSubject.create();
        // 创建有默认值的BehaviorSubject
        BehaviorSubject<Integer> subjectDefault = BehaviorSubject.createDefault(-1);

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

        // 1. 无数据发送的时候，发送默认值
        //	subjectDefault.subscribe(observer);

        // 2. 此时会发射所有订阅后正常发射的数据: 1, 2, 3, 4, error
        //	subject.subscribe(observer);
        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);

        // 3. 此时会发射订阅前的一个数据及后面正常发射的数据: 3, 4, error
        //	subject.subscribe(observer);
        subject.onNext(4);
//		subject.onError(new NullPointerException());

        // 4. 此时不会发射后续数据，仅发送Error通知
        //	subject.subscribe(observer);
        subject.onNext(5);
        subject.onComplete();

        // 5. 此时没有数据发射，如果有error存在的话，将会发送error
        subject.subscribe(observer);

    }

}
