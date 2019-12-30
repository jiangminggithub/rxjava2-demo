package org.jm.demo.rxjava.base;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.AsyncSubject;

/**
 * @author jiangming
 * <p>
 * TestObserver: 主要用于断言Observable的行为
 */
public class TestObserverBase {

    public static void main(String[] args) {
        // Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(100);
                emitter.onError(new NullPointerException());
                emitter.onComplete();
            }
        });

        // 1. 创建TestObserver对象
        TestObserver<Integer> testObserver = TestObserver.create(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe:");
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
                System.out.println("--> onComplete:");
            }
        });

        observable.subscribe(testObserver);
        try {
            // 断言是否为收到订阅，但是没有事件发送
            testObserver.assertEmpty();
            // 断言是否收到onComplete()
            testObserver.assertComplete();
            // 断言没有数据100发送
            testObserver.assertNever(100);
            // 断言接收数据结果
            testObserver.assertResult(1, 2, 3);
            // 断言异常
            testObserver.assertError(NullPointerException.class);
        } catch (Error e) {
            System.out.println("Error: " + e);
        }

        System.out.println("-----------------------------------------------");
        // Subject
        AsyncSubject<Object> subject = AsyncSubject.create();

        // 2. 从Observable或者Subject中获取TestObserver对象
        TestObserver<Integer> test = observable.test();
        TestObserver<Object> test2 = subject.test();
        System.out.println(test.values()); // received onNext values
        try {
            // 断言是否为收到订阅，但是没有事件发送
            test.assertEmpty();
            test2.assertEmpty();
            // 断言是否收到onComplete()
            test.assertComplete();
            // 断言没有数据100发送
            test.assertNever(100);
            // 断言接收数据结果
            test.assertResult(1, 2, 3);
            // 断言异常
            test.assertError(NullPointerException.class);
        } catch (Error e) {
            System.out.println("Error: " + e);
        }

    }

}
