package org.jm.demo.rxjava.combined;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Consumer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * SwitchOnNext:   订阅一个发射多个Observables的Observable。它每次观察那些Observables中的一个，
 * 发射的这个新Observable并取消订阅前一个发射数据的旧Observable，开始发射最新的Observable发射的数据。
 */
public class SwitchOnNext {

    public static void main(String[] args) throws Exception {
        // 创建Observable
        Observable<Long> observable1 = Observable.intervalRange(1, 5, 1, 500, TimeUnit.MILLISECONDS);
        Observable<Long> observable2 = Observable.intervalRange(10, 5, 1, 500, TimeUnit.MILLISECONDS);

        // 创建发射Observable序列的Observable
        Observable<Observable<Long>> sources = Observable.create(new ObservableOnSubscribe<Observable<Long>>() {

            @Override
            public void subscribe(ObservableEmitter<Observable<Long>> emitter) throws Exception {
                emitter.onNext(observable1);
                Thread.sleep(1000);
                // 此时发射一个新的observable2，将会取消订阅observable1
                emitter.onNext(observable2);
                emitter.onComplete();
            }
        });

        // 创建发射含有Error通知的Observable序列的Observable
        Observable<Observable<Long>> sourcesError = Observable.create(new ObservableOnSubscribe<Observable<Long>>() {

            @Override
            public void subscribe(ObservableEmitter<Observable<Long>> emitter) throws Exception {
                emitter.onNext(observable1);
                emitter.onNext(Observable.error(new Exception("Error Test1!"))); // 发射一个发射Error通知的Observable
                emitter.onNext(Observable.error(new Exception("Error Test2!"))); // 发射一个发射Error通知的Observable
                Thread.sleep(1000);
                // 此时发射一个新的observable2，将会取消订阅observable1
                emitter.onNext(observable2);
                emitter.onComplete();
            }
        });

        // 1. switchOnNext(ObservableSource<ObservableSource> sources, int bufferSize)
        // 可选参数 bufferSize： 缓存数据项大小
        // 接受一个发射Observable序列的Observable类型的sources，
        // 当sources发射一个新的Observable后，则会取消订阅前面的旧observable，直接开始接受新Observable的数据
        Observable.switchOnNext(sources)
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long integer) throws Exception {
                        System.out.println("--> accept(1): " + integer);
                    }
                });

        System.in.read();
        System.out.println("--------------------------------------------------------------------");
        // 2. switchOnNextDelayError(ObservableSource<ObservableSource> sources, int prefetch)
        // 可选参数 prefetch： 与读取数据项大小
        // 当sources发射一个新的Observable后，则会取消订阅前面的旧observable，直接开始接受新Observable的数据，
        // 保留onError通知直到合并后的Observable所有的数据发射完成，在那时它才会把onError传递给观察者
        Observable.switchOnNextDelayError(sourcesError)
                .subscribe(new Observer<Long>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(2)");
                    }

                    @Override
                    public void onNext(Long t) {
                        System.out.println("--> onNext(2): " + t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 判断是否是CompositeException对象（发生多个Observable出现Error时会发送的对象）
                        if (e instanceof CompositeException) {
                            CompositeException compositeException = (CompositeException) e;
                            List<Throwable> exceptions = compositeException.getExceptions();
                            System.out.println("--> onError(2): " + exceptions);
                        } else {
                            System.out.println("--> onError(2): " + e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onComplete(2)");
                    }
                });

        System.in.read();
    }
}
