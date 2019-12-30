package org.jm.demo.rxjava.auxiliary;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * Timmeout: 对原始Observable的一个镜像，如果过了一个指定的时长仍没有发射数据，它会发一个错误通知。
 */
public class Timeout {

    public static void main(String[] args) throws Exception {

        /**
         *  1. timeout(long timeout, TimeUnit timeUnit)
         *  接受一个时长参数，如果在指定的时间段内没有数据项发射，将会发射一个Error通知，
         *  或者每当原始Observable发射了一项数据， timeout 就启动一个计时器，
         *  如果计时器超过了指定指定的时长而原始Observable没有发射另一项数据，
         *  就抛出 TimeoutException ，以一个错误通知终止Observable。
         */
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                //  Thread.sleep(2000);     // 延迟2秒后发射数据，此时会有TimeoutException
                emitter.onNext(1L);
                Thread.sleep(2000);     // 延迟2秒后发射数据，此时会有TimeoutException
                emitter.onNext(2L);
                emitter.onComplete();
            }
        }).timeout(1, TimeUnit.SECONDS)     // 指定超时时间段为1秒
          .subscribe(new Observer<Long>() {
                @Override
                public void onSubscribe(Disposable d) {
                    System.out.println("--> onSubscribe(1)");
                }

                @Override
                public void onNext(Long aLong) {
                    System.out.println("--> onNext(1): " + aLong);
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

        System.in.read();
        System.out.println("------------------------------------------------");

        /**
         *  2. timeout(long timeout, TimeUnit timeUnit,
         *  Scheduler scheduler,        // 可选参数，指定线程调度器
         *  ObservableSource other      // 可选参数，超时备用Observable
         *  )
         *
         *  在指定时间段后超时时会切换到使用一个你指定的备用的Observable，而不是发onError通知。
         */
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                //  Thread.sleep(2000);     // 延迟2秒后发射数据，此时会有TimeoutException
                emitter.onNext(1L);
                Thread.sleep(2000);     // 延迟2秒后发射数据，此时会有TimeoutException
                emitter.onNext(2L);
                emitter.onComplete();
            }
        }).timeout(1, TimeUnit.SECONDS,     // 指定超时时间段为1秒
                Schedulers.newThread(),             // 指定工作线程为子线程
                Observable.just(888L))              // 超时后默认发射的Observable
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(2)");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("--> onNext(2): " + aLong);
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

        System.in.read();
        System.out.println("------------------------------------------------");
        /**
         *  3. timeout(Function<T, ObservableSource> itemTimeoutIndicator
         *  ObservableSource other      // 可选参数，当超时后发射的备用Observable
         *  )
         *  对原始Observable的每一项返回一个Observable，
         *  如果当这个Observable终止时原始Observable还没有发射另一项数据，就会认为是超时了，
         *  如果没有指定超时备用的Observable，就抛出TimeoutException，以一个错误通知终止Observable，
         *  否则超时后发射备用的Observable。
         */
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                emitter.onNext(1L);
                Thread.sleep(3000);     // 延迟3秒后发射数据，此时会有TimeoutException
                emitter.onNext(2L);
                emitter.onComplete();
            }
        }).timeout(new Function<Long, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Long aLong) throws Exception {
                // 为每一个原始数据发射一个Observable来指示下一个数据发射的Timeout，这里指定1秒超时时间
                return Observable.timer(1, TimeUnit.SECONDS);
            }
        }, Observable.just(888L))  // 超时后默认发射的Observable
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(3)");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("--> onNext(3): " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(3): " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onComplete(3)");
                    }
                });

        System.in.read();
    }
}
