package org.jm.demo.rxjava.combined;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * CombineLatest: 结合多个Observable结果，最后一个Observable的数据应用一个函数处理，返回所需的数据
 */
public class CombineLatest {

    public static void main(String[] args) throws Exception {
        // Observables 创建
        Observable<Long> observable1 = Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS);
        Observable<Long> observable2 = Observable.intervalRange(1, 5, 1, 2, TimeUnit.SECONDS);
        Observable<Long> observable3 = Observable.intervalRange(100, 5, 1, 1, TimeUnit.SECONDS);

        // 1. combineLatest(ObservableSource, ObservableSource [支持2-9个参数]...,  BiFunction)
        // 结合多个Observable, 当他们其中任意一个发射了数据时，使用函数结合他们最近发射的一项数据
        Observable.combineLatest(observable1, observable2, new BiFunction<Long, Long, String>() {

            @Override
            public String apply(Long t1, Long t2) throws Exception {
                System.out.println("--> apply(1) t1 = " + t1 + ", t2 = " + t2);
                if (t1 + t2 == 10) {
                    return "Success";   // 满足一定条件，返回指定的字符串
                }
                return t1 + t2 + "";    // 计算所有数据的和并转换为字符串
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(String t) throws Exception {
                System.out.println("----> accept combineLatest(1): " + t);
            }
        });

        System.out.println("--------------------------------------------------------");
        // 2. combineLatest(T1, T2, T3, Function)
        // Observables的结合
        Observable.combineLatest(observable1, observable2, observable3, new Function3<Long, Long, Long, String>() {
            @Override
            public String apply(Long t1, Long t2, Long t3) throws Exception {
                System.out.println("--> apply(2): t1 = " + t1 + ", t2 = " + t2 + ", t3 = " + t3);
                return t1 + t2 + t3 + "";   // 计算所有数据的和并转换为字符串
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String t) throws Exception {
                System.out.println("--> accept(2): " + t);
            }
        });

        System.in.read();
    }

}