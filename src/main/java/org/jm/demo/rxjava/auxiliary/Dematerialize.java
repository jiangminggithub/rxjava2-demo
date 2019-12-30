package org.jm.demo.rxjava.auxiliary;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author jiangming
 * <p>
 * Dematerialize: 将原始Observable发射的 Notification 对象还原成Observable的通知。
 */
public class Dematerialize {

    public static void main(String[] args) {

        /**
         *  dematerialize()
         *  过时的方法，在Rxjava：2.2.4中已经被dematerialize(Function<T, Notification<R>> selector)替代
         *  将原始Observable发射的 Notification 对象还原成Observable的通知。
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Exception("Test Error!"));
                emitter.onComplete();
            }
        }).materialize()
          .dematerialize()  // 将Notification 对象还原成Observable的通知
          .subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable d) {
                    System.out.println("--> onSubscribe(1)");
                }

                @Override
                public void onNext(Object o) {
                    System.out.println("--> onNext(1): " + o);
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

        System.out.println("------------------------------------------------");
        /**
         *  dematerialize(Function<T, Notification<R>> selector)
         *  将原始Observable发射的 Notification 对象经过一个selector函数处理后，发射一个新的Notification，
         *  还原成Observable的通知。
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Exception("Test Error!"));
                emitter.onComplete();
            }
        }).materialize()
          .dematerialize(new Function<Notification<Integer>, Notification<Integer>>() {
                    @Override
                    public Notification<Integer> apply(Notification<Integer> integerNotification) throws Exception {
                        System.out.println("--> apply(2): " + integerNotification);
                        return integerNotification;
                    }
          }).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {
                    System.out.println("--> onSubscribe(2)");
                }

                @Override
                public void onNext(Integer integer) {
                    System.out.println("--> onNext(2): " + integer);
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

    }
}
