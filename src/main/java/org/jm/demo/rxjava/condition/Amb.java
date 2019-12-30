package org.jm.demo.rxjava.condition;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * Amb: 给定两个或多个Observables，它只发射首先发射数据或通知的那个Observable的所有数据。
 */
public class Amb {

    public static void main(String[] args) throws Exception {
        // 创建Observable
        Observable<Integer> delayObservable = Observable.range(1, 5)
                                            .delay(100, TimeUnit.MILLISECONDS); // 延迟100毫秒发射数据
        Observable<Integer> rangeObservable = Observable.range(6, 5);

        // 创建Observable的集合
        ArrayList<Observable<Integer>> list = new ArrayList<>();
        list.add(delayObservable);
        list.add(rangeObservable);

        // 创建Observable的数组
        Observable<Integer>[] array = new Observable[2];
        array[0] = delayObservable;
        array[1] = rangeObservable;

        /**
         *  1. ambWith(ObservableSource<? extends T> other)
         *  与另外一个Observable比较，只发射首先发射通知的Observable的数据
         */
        rangeObservable.ambWith(delayObservable)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(1): " + integer);
                    }
                });

        System.in.read();
        System.out.println("------------------------------------------------");
        /**
         *  2. amb(Iterable<? extends ObservableSource<? extends T>> sources)
         *  接受一个Observable类型的集合， 只发射集合中首先发射通知的Observable的数据
         */
        Observable.amb(list)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(2): " + integer);
                    }
                });

        System.in.read();
        System.out.println("------------------------------------------------");
        /**
         *  3. ambArray(ObservableSource<? extends T>... sources)
         *  接受一个Observable类型的数组， 只发射数组中首先发射通知的Observable的数据
         */
        Observable.ambArray(array)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(3): " + integer);
                    }
                });

        System.in.read();
    }
}
