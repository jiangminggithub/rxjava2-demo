package org.jm.demo.rxjava.create;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;

import java.util.concurrent.Callable;

/**
 * @author jiangming
 * <p>
 * Defer 直到有观察者订阅时才创建Observable，并且为每个观察者都创建一个新的Observable
 */
public class Defer {

    public static void main(String[] args) {
        // 创建一个defer的Observable
        Observable<Integer> deferObservable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            public ObservableSource<? extends Integer> call() throws Exception {

                Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {

                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onNext(4);
                        emitter.onNext(5);
                        emitter.onComplete();
                    }
                });
                return observable;
            }
        });

        // 创建第一个观察者并订阅defer Observable
        deferObservable.subscribe(new Consumer<Integer>() {

            public void accept(Integer t) throws Exception {
                System.out.println("No.1 --> accept = " + t);
            }
        });

        // 创建第二个观察者并订阅defer Observable
        deferObservable.subscribe(new Consumer<Integer>() {

            public void accept(Integer t) throws Exception {
                System.out.println("No.2 --> accept = " + t);
            }
        });

        // 创建第三个观察者并订阅defer Observable
        deferObservable.subscribe(new Consumer<Integer>() {

            public void accept(Integer t) throws Exception {
                System.out.println("No.3 --> accept = " + t);
            }
        });

    }

}
