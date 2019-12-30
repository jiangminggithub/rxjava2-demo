package org.jm.demo.rxjava.auxiliary;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * SubscribeOn: 指定Observable自身在哪个调度器上执行
 * <p>
 * 你可以使用 SubscribeOn 操作符指定Observable在一个特定的调度器上运转
 */
public class SubscribeOn {

    public static void main(String[] args) throws Exception {

        // 查看当前线程id
        System.out.println("----> main: threadID = " + Thread.currentThread().getId());

        /**
         *  subscribeOn(Scheduler scheduler)
         *  指定Observable在指定的scheduler上调度
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 查看Observable的工作线程id
                System.out.println("----> SubscribeOn: threadID = " + Thread.currentThread().getId());
                emitter.onNext(999);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())  // 指定Observable的工作线程在子线程
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept: " + integer);
                    }
                });

        System.in.read();
    }
}
