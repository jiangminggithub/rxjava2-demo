package org.jm.demo.rxjava.connectable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * Share: 等价于 Observable.publish().refCount();
 * 将一个普通的Observable转换为一个具有connectableObservable特性的Observable。
 * <p>
 * 它的作用等价于对一个 Observable同时应用 publish 和 refCount 操作。
 */
public class Share {

    public static void main(String[] args) throws Exception {

        // share()
        // 通过share() 同时应用 publish 和 refCount 操作
        Observable<Long> share = Observable
                .intervalRange(1, 5, 0, 500, TimeUnit.MILLISECONDS)
          //    .publish().refCount()
                .share()  // 等价于上面的操作
                .subscribeOn(Schedulers.newThread())    // 指定订阅调度在子线程
                .observeOn(Schedulers.newThread());     // 指定观察者调度在子线程

        // 1. 第一个订阅者
        share.subscribe(new Observer<Long>() {
            private  Disposable disposable;
            private  int buff = 0;

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("----> onSubscribe(1): ");
                disposable = d;
            }

            @Override
            public void onNext(Long value) {
                if (buff == 3) {
                    disposable.dispose();   // 解除当前的订阅
                    System.out.println("----> Subscribe(1) is dispose! ");
                } else {
                    System.out.println("--> onNext(1): " + value);
                }
                buff++;
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(1): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onComplete(1): ");
            }
        });

        // 2. 第二个订阅者
        share.doOnSubscribe(new Consumer<Disposable>() {

                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        System.out.println("----> onSubscribe(2): ");
                    }
                })
                .delaySubscription(1, TimeUnit.SECONDS)    // 延迟1秒后订阅
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long value) throws Exception {
                        System.out.println("--> accept(2): " + value);
                    }
                });

        System.in.read();
    }
}
