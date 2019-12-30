package org.jm.demo.rxjava.auxiliary;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * ObserveOn: 指定一个观察者在哪个调度器上观察这个Observable.
 * <p>
 * 使用调度器Scheduler来管理多线程环境中Observable的转场。
 * 你可以使用ObserveOn操作符指定Observable在一个特定的调度器上发送通知给观察者 (调用观察者的onNext, onCompleted, onError方法)。
 */
public class ObserveOn {

    public static void main(String[] args) throws Exception {

        // 查看当前线程id
        System.out.println("----> main: threadID = " + Thread.currentThread().getId());

        /**
         *  observeOn(Scheduler scheduler,
         *  boolean delayError,     // 可选参数是否延迟异常
         *  int bufferSize          // 指定缓存大小
         *  )
         *  指定观察者在指定的scheduler线程中调度
         */
        Observable.just(999).observeOn(Schedulers.newThread(), true, 3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        // 查看观察者的线程id
                        System.out.println("--> accept ThreadID: " + Thread.currentThread().getId());
                        System.out.println("--> accept: " + integer);
                    }
                });

        System.in.read();

    }
}
