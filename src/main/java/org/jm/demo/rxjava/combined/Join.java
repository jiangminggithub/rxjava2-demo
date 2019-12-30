package org.jm.demo.rxjava.combined;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.util.concurrent.TimeUnit;


/**
 * @author jiangming
 *
 *  Join: 任何时候，只要在另一个Observable发射的数据定义的时间窗口内，这个Observable发射了一条数据，就结合两个Observable发射的数据。
 */
public class Join {

    public static void main(String[] args) throws Exception {

        // Observable的创建
        Observable<Long> sourceObservable = Observable.intervalRange(1, 5, 1, 500, TimeUnit.MILLISECONDS);
        Observable<Long> targetObservable = Observable.intervalRange(10, 5, 1, 1000, TimeUnit.MILLISECONDS);

        // 1. join(other, leftEnd, rightEnd, resultSelector)
        // other: 目标组合的Observable
        // leftEnd: 接收一个源数据项，返回一个Observable，这个Observable的生命周期就是源Observable发射数据的有效期
        // rightEnd: 接收一个源数据项，返回一个Observable，这个Observable的生命周期就是目标Observable发射数据的有效期
        // resultSelector： 接收源Observable和目标Observable发射的数据项， 处理后的数据返回给观察者对象
        sourceObservable.join(targetObservable, new Function<Long, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Long t) throws Exception {
                System.out.println("-----> t1 is emitter: " + t);
                return Observable.timer(1000, TimeUnit.MILLISECONDS);   // 源Observable发射数据的有效期为1000毫秒
            }
        }, new Function<Long, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Long t) throws Exception {
                System.out.println("-----> t2 is emitter: " + t);
                return Observable.timer(1000, TimeUnit.MILLISECONDS);   // 目标Observable发射数据的有效期为1000毫秒
            }
        }, new BiFunction<Long, Long, String>() {
            @Override
            public String apply(Long t1, Long t2) throws Exception {
                return "t1 = " + t1 + ", t2 = " + t2;                         // 对数据进行组合后返回和观察者
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String t) throws Exception {
                System.out.println("--> accept(1): " + t);
            }
        });

        System.out.println("-------------------------------------------------");
        // 2. groupJoin(other, leftEnd, rightEnd, resultSelector)
        // groupJoin操作符与join相同，只是参数传递有所区别。
        // resultSelector可以将原始数据转换为Observable类型的数据发送给观察者。
        sourceObservable.groupJoin(targetObservable, new Function<Long, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Long t) throws Exception {
                System.out.println("-----> t1 is emitter: " + t);
                return Observable.timer(1000, TimeUnit.MILLISECONDS);   // 源Observable发射数据的有效期为1000毫秒
            }
        }, new Function<Long, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Long t) throws Exception {
                System.out.println("-----> t2 is emitter: " + t);
                return Observable.timer(1000, TimeUnit.MILLISECONDS);   // 目标Observable发射数据的有效期为1000毫秒
            }
        }, new BiFunction<Long, Observable<Long>, Observable<String>>() {
            @Override
            public Observable<String> apply(Long t1, Observable<Long> t2) throws Exception {
                System.out.println("--> apply(2) combine: " + t1);            // 结合操作
                return t2.map(new Function<Long, String>() {
                    @Override
                    public String apply(Long t) throws Exception {
                        System.out.println("-----> apply(2) operation: " + t);
                        return "t1 = " + t1 + ", t2 = " + t;
                    }
                });
            }
        }).subscribe(new Consumer<Observable<String>>() {
            @Override
            public void accept(Observable<String> stringObservable) throws Exception {
                stringObservable.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String t) throws Exception {
                        System.out.println("--> accept(2): " + t);
                    }
                });
            }
        });

        System.in.read();
    }
}
