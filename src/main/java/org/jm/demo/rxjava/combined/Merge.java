package org.jm.demo.rxjava.combined;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangming
 * Merge: 合并多个Observables的发射物
 */
public class Merge {
    public static void main(String[] args) throws Exception {

        // 创建Observable对象
        Observable<Integer> odd = Observable.just(1, 3, 5);
        Observable<Integer> even = Observable.just(2, 4, 6);
        Observable<Integer> big = Observable.just(188888, 688888, 888888);

        // 创建list对象
        List<Observable<Integer>> list = new ArrayList<>();
        list.add(odd);
        list.add(even);
        list.add(big);

        // 创建Array对象
        Observable<Integer>[] observables = new Observable[3];
        observables[0] = odd;
        observables[1] = even;
        observables[2] = big;

        // 创建发射Observable序列的Observable
        Observable<ObservableSource<Integer>> sources = Observable.create(new ObservableOnSubscribe<ObservableSource<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<ObservableSource<Integer>> emitter) throws Exception {
                emitter.onNext(Observable.just(1));
                emitter.onNext(Observable.just(1, 2));
                emitter.onNext(Observable.just(1, 2, 3));
                emitter.onNext(Observable.just(1, 2, 3, 4));
                emitter.onNext(Observable.just(1, 2, 3, 4, 5));
                emitter.onComplete();
            }
        });

        // 创建有Error的Observable序列的Observable
        Observable<ObservableSource<Integer>> DelayErrorObservable = Observable.create(new ObservableOnSubscribe<ObservableSource<Integer>>() {

            @Override
            public void subscribe(ObservableEmitter<ObservableSource<Integer>> emitter) throws Exception {
                emitter.onNext(Observable.just(1));
                emitter.onNext(Observable.error(new Exception("Error Test1"))); // 发射一个Error的通知的Observable
                emitter.onNext(Observable.just(2, 3));
                emitter.onNext(Observable.error(new Exception("Error Test2"))); // 发射一个Error的通知的Observable
                emitter.onNext(Observable.just(4, 5, 6));
                emitter.onComplete();
            }
        });

        // 1. merge(ObservableSource source1, ObservableSource source2, ..., ObservableSource source4)
        // 可接受 2-4 个Observable对象进行merge
        Observable.merge(odd, even)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(1): " + integer);
                    }
                });

        System.out.println("-----------------------------------------------");
        // 2. merge(Iterable<? extends ObservableSource<? extends T>> sources, int maxConcurrency, int bufferSize)
        // 可选参数, maxConcurrency: 最大的并发处理数, bufferSize: 缓存的数量（从每个内部观察资源预取的项数）
        // 接受一个Observable的列表List
        Observable.merge(list)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(2): " + integer);
                    }
                });

        System.out.println("-----------------------------------------------");
        // 3. mergeArray(int maxConcurrency, int bufferSize, ObservableSource<? extends T>... sources)
        // 可选参数, maxConcurrency: 最大的并发处理数, bufferSize: 缓存的数量（从每个内部观察资源预取的项数）
        // 接受一个Observable的数组Array
        Observable.mergeArray(observables)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(3): " + integer);
                    }
                });

        System.out.println("-----------------------------------------------");
        // 4. merge(ObservableSource<? extends ObservableSource<? extends T>> sources, int maxConcurrency)
        // 可选参数, maxConcurrency: 最大的并发处理数
        // 接受一个发射Observable序列的Observable
        Observable.merge(sources)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(4): " + integer);
                    }
                });

        System.out.println("-----------------------------------------------");
        // 5. mergeWith(other)
        // merge 是静态方法， mergeWith 是对象方法: Observable.merge(odd,even) 等价于 odds.mergeWith(even)
        odd.mergeWith(even)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(5): " + integer);
                    }
                });

        System.out.println("-----------------------------------------------");
        // 6. mergeDelayError
        // 保留onError通知直到合并后的Observable所有的数据发射完成，在那时它才会把onError传递给观察者
        Observable.mergeDelayError(DelayErrorObservable)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(6)");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("--> onNext(6): " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 判断是否是CompositeException对象（发生多个Observable出现Error时会发送的对象）
                        if (e instanceof CompositeException) {
                            CompositeException compositeException = (CompositeException) e;
                            List<Throwable> exceptions = compositeException.getExceptions();
                            System.out.println("--> onError(6): " + exceptions);
                        } else {
                            System.out.println("--> onError(6): " + e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onComplete(6)");
                    }
                });

    }
}
