package org.jm.demo.rxjava.auxiliary;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * Delay: 延迟一段指定的时间再发射来自Observable的发射物.
 * <p>
 * Delay 操作符让原始Observable在发射每项数据之前都暂停一段指定的时间段,
 * 效果是Observable发射的数据项在时间上向前整体平移了一个增量。
 */
public class Delay {

    public static void main(String[] args) throws Exception {
        // 创建Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            //  emitter.onError(new Exception("Test Error!"));
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onComplete();
            }
        });

        /**
         * 1. delay(long delay, TimeUnit unit,
         *  Scheduler scheduler: 可选参数，指定工作线程
         *  boolean delayError:  可选参数，延迟异常通知到最后
         *  )
         *  延迟指定时间段后发射原始Observable发射的数据序列，如果发生异常的话，会立即发射通知给观察者。
         */
        observable.doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("--> doOnNext(1): " + integer);
            }

        }).delay(1, TimeUnit.SECONDS, Schedulers.newThread(), false) // 在子线程中延迟1秒发射数据，不延迟异常通知
          .subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {
                    System.out.println("--> onSubscribe(1)");
                }

                @Override
                public void onNext(Integer integer) {
                    System.out.println("--> onNext(1): " + integer);
                }

                @Override
                public void onError(Throwable e) {
                    System.out.println("--> onError(1): " + e);
                }

                @Override
                public void onComplete() {
                    System.out.println("--> onCompleted(1)");
                }
          });

        System.in.read();
        System.out.println("-----------------------------------------------------");
        /**
         *  2. delay(Function<T, ObservableSource<U>> itemDelay)
         *   使用一个函数针对原始Observable的每一项数据返回一个Observable，它监视返回的这个Observable，
         *   当任何那样的 Observable 终止时，delay 返回的 Observable 就发射关联的那项数据。
         */
        observable.doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("--> doOnNext(2): " + integer);
            }

        }).delay(new Function<Integer, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Integer integer) throws Exception {
                System.out.println("--> ObservableSource(2)： " + integer);
                Observable<Long> timer = Observable.timer(integer, TimeUnit.SECONDS);
                return timer;
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
                System.out.println("--> onCompleted(2)");
            }
        });

        System.in.read();
        System.out.println("-----------------------------------------------------");
        /**
         *  3. delay(ObservableSource subscriptionDelay, Function<T, ObservableSource> itemDelay)
         *  延迟直到subscriptionDelay发射第一个数据项后开始订阅原始Observable，
         *  然后再使用一个函数针对原始Observable的每一项数据返回一个Observable，它监视返回的这个Observable，
         *  当任何那样的 Observable 终止时，delay 返回的 Observable 就发射关联的那项数据。
         */
        observable.doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("--> doOnNext(3): " + integer);
            }
           // 延迟3秒后开始订阅源Observable，然后对发射的每项数据进行function函数延迟
        }).delay(Observable.timer(3, TimeUnit.SECONDS), new Function<Integer, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Integer integer) throws Exception {
                System.out.println("--> apply(3): " + integer);
                return Observable.timer(integer, TimeUnit.SECONDS);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(3)");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("--> onNext(3): " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(3): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onCompleted(3)");
            }
        });

        System.in.read();
    }
}