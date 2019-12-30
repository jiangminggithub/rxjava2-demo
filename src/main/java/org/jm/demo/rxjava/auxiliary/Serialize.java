package org.jm.demo.rxjava.auxiliary;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author jiangming
 * <p>
 * Serialized: 强制一个Observable连续调用(同步)并保证行为正确
 */
public class Serialize {

    public static void main(String[] args) throws Exception {

        /**
         *  serialize()
         *  强制一个Observable连续调用(同步)并保证行为正确
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 多线程事件调用
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            emitter.onNext(i + 1);
                        }
                        emitter.onComplete();
                    }
                }).start();

                // 多线程事件调用
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 100; i < 110; i++) {
                            emitter.onNext(i + 1);
                        }
                        emitter.onComplete();
                    }
                }).start();
            }
        }).serialize() // 序列化,合法性操作
          .subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {
                    System.out.println("--> onSubscribe");
                }

                @Override
                public void onNext(Integer integer) {
                    System.out.println("--> onNext: " + integer);
                }

                @Override
                public void onError(Throwable e) {
                    System.out.println("--> onError: " + e);
                }

                @Override
                public void onComplete() {
                    System.out.println("--> onComplete");
                }
          });

        System.in.read();
    }
}
