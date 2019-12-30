package org.jm.demo.rxjava.connectable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * RefCount: 让一个可连接的Observable行为像普通的Observable一样的使用
 * 效果上: 将一个 ConnecableObservable 变成普通的Observable效果
 */
public class RefCount {

    public static void main(String[] args) throws Exception {

        /**
         * refCount(int subscriberCount, long timeout, TimeUnit unit, Scheduler scheduler)
         *
         * 具有以下可选参数：
         * subscriberCount： 指定需要连接到上游的订阅者数量。注意：当订阅者满足此数量后才会处理
         * timeout：         所有订阅用户退订后断开连接前的等待时间
         * unit：            时间单位
         * scheduler:        断开连接之前要等待的目标调度器
         */
        Observable<Long> refCountObservable = Observable
                .intervalRange(1, 5, 0, 1000, TimeUnit.MILLISECONDS)
                .publish()
                .refCount()
                .subscribeOn(Schedulers.newThread())    // 指定订阅调度在子线程
                .observeOn(Schedulers.newThread());     // 指定观察者调度在子线程
            //  .refCount(1, 500, TimeUnit.MILLISECONDS, Schedulers.newThread());

        // 第1个订阅者
        refCountObservable.subscribe(new Observer<Long>() {
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

        // 第2个订阅者
        refCountObservable.doOnSubscribe(new Consumer<Disposable>() {

                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        System.out.println("----> onSubscribe(2): ");
                    }
                })
                .delaySubscription(2, TimeUnit.SECONDS)   // 延迟2秒后订阅
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long value) throws Exception {
                        System.out.println("--> accept(2): " + value);
                    }
                });

        System.in.read();
    }
}
