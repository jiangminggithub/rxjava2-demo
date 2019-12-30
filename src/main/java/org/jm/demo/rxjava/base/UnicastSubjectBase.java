package org.jm.demo.rxjava.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.UnicastSubject;

/**
 * @author jiangming
 * <p>
 * UnicastSubject: 仅支持订阅一次的Subject
 * <p>
 * 仅支持订阅一次的Subject,如果多个订阅者试图订阅这个Subject，
 * 若该subject未terminate，将会受到IllegalStateException ，
 * 若已经terminate，那么只会执行onError或者onComplete方法。
 */
public class UnicastSubjectBase {

    public static void main(String[] args) {

        // 创建UnicastSubject，只能被订阅一次，不能再次被订阅
        UnicastSubject<Integer> subject = UnicastSubject.create();

        // 创建Observer(观察者), 可以接受Observable所有通知
        Observer<Integer> observer = new Observer<Integer>() {

            public void onSubscribe(Disposable d) {
                System.out.println("--------------------------------");
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
        // 订阅后，此subject将不可以再被订阅了
        subject.subscribe(observer);

        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);
        // 此时会有IllegalStateException，因为只能订阅一次，不能重复订阅
        subject.subscribe(observer);
        subject.onNext(4);
        subject.onNext(5);
        subject.onComplete();

        // 此时会有IllegalStateException，因为只能被订阅一次，不能重复订阅
        subject.subscribe(observer);

    }

}
