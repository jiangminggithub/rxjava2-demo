package org.jm.demo.rxjava.connectable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;

/**
 * @author jiangming
 * <p>
 * ConnectObservable: 可连接的Observable在被订阅时并不开始发射数据，只有在它的 connect() 被调用时才开始。
 * 用这种方法，你可以等所有的潜在订阅者都订阅了这个Observable之后才开始发射数据。
 */
public class ConnectableObservableDemo {

    public static void main(String[] args) throws Exception {

        // 1. publish()
        // 创建ConnectableObservable
        ConnectableObservable<Integer> connectableObservable = Observable.range(1, 5)
                .publish();    // publish操作将Observable转化为一个可连接的Observable

        // 创建普通的Observable
        Observable<Integer> range = Observable.range(1, 5);

        // 1.1 connectableObservable在被订阅时并不开始发射数据，只有在它的 connect() 被调用时才开始
        connectableObservable.subscribe(new Observer<Integer>() {

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
                System.out.println("--> onComplete(1)");
            }
        });

        // 1.2 connectableObservable在被订阅时并不开始发射数据，只有在它的 connect() 被调用时才开始
        connectableObservable.subscribe(new Observer<Integer>() {

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

        // 1.3 普通Observable在被订阅时就会发射数据
        range.subscribe(new Observer<Integer>() {

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
                System.out.println("--> onComplete(3)");
            }
        });

        System.out.println("----------------start connect------------------");
        // 可连接的Observable在被订阅时并不开始发射数据，只有在它的connect()被调用时才开始发射数据
        // connectableObservable.connect();

        // 可选参数Consumer，返回一个Disposable对象，可以获取订阅状态和取消当前的订阅
        connectableObservable.connect(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("--> connect accept: " + disposable.isDisposed());
                // disposable.dispose();
            }
        });

        System.in.read();
        System.out.println("----------------publish func(selector)------------------");
        // 2. publish(Function<Observable<T>, ObservableSource<R>> selector)
        // 接受原始Observable的数据，产生一个新的Observable，可以对这个Observable进行函数处理
        Observable<String> publish = Observable.range(1, 5)
                .publish(new Function<Observable<Integer>, ObservableSource<String>>() {

                    @Override
                    public ObservableSource<String> apply(Observable<Integer> integerObservable) throws Exception {
                        System.out.println("--> apply(4): " + integerObservable.toString());

                        Observable<String> map = integerObservable.map(new Function<Integer, String>() {

                            @Override
                            public String apply(Integer integer) throws Exception {
                                return "[this is map value]: " + integer * integer;
                            }
                        });
                        return map;
                    }
                });

        publish.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("--> accept(4): " + s);
            }
        });

    }
}
