package org.jm.demo.rxjava.convert;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * FlatMap: 对原始数据进行转换操作发送至订阅者
 */
public class FlatMap {

    public static void main(String[] args) throws Exception {

        // 	1. flatMap(Function)
        // 	对原始Observable发射的每一项数据执行变换操作，这个函数返回一个本身也发射数据的Observable，
        // 	然后FlatMap合并这些Observables发射的数据，最后将合并后的结果当做它自己的数据序列发射
        Observable.range(1, 5)
                .flatMap(new Function<Integer, ObservableSource<? extends Integer>>() {

                    @Override
                    public ObservableSource<? extends Integer> apply(Integer t) throws Exception {
                        System.out.println("--> apply(1): " + t);                            // 原始数据
                        return Observable.range(1, t).subscribeOn(Schedulers.newThread());    // 处理后数据
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept flatMap(1): " + t);                    // 接受的所有数据
                    }
                });

        System.out.println("--------------------------------------");
        // 2. flatMap(Function, maxConcurrency)
        // maxConcurrency 这个参数设置 flatMap 从原来的Observable映射Observables的最大同时订阅数。
        // 当达到这个限制时，它会等待其中一个终止然后再订阅另一个
        Observable.range(1, 5)
                .flatMap(new Function<Integer, ObservableSource<? extends Integer>>() {

                    @Override
                    public ObservableSource<? extends Integer> apply(Integer t) throws Exception {
                        System.out.println("--> apply(2): " + t);
                        return Observable.range(1, t).subscribeOn(Schedulers.newThread());
                    }
                    // 指定最大订阅数为1，此时等待上一个订阅的Observable结束，在进行下一个Observable订阅
                }, 1)
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept flatMap(2): " + t);
                    }
                });

        System.out.println("--------------------------------------");
        // 3. flatMap(Function, delayErrors)
        // delayErrors 这个参数指定是否延迟发生Error的Observable通知
        // 当true 时延迟发生Error的这个订阅的Observable通知，不中断当前的订阅操作，
        // 继续下一个Observable的订阅，在所有订阅的Observable全部结束后发送Error这个Observable的通知
        Observable.range(1, 5)
                .flatMap(new Function<Integer, ObservableSource<? extends Integer>>() {

                    @Override
                    public ObservableSource<? extends Integer> apply(Integer t) throws Exception {
                        System.out.println("--> apply(3): " + t);

                        return Observable.create(new ObservableOnSubscribe<Integer>() {

                            @Override
                            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                if (t == 3) {
                                    throw new NullPointerException("delayErrors test!");    // 测试Error
                                }
                                for (int i = 1; i <= t; i++) {
                                    emitter.onNext(i);
                                }
                                emitter.onComplete();
                            }
                        });
                    }
                    // 设置延迟 Error 通知到最后
                }, true)
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept flatMap(3): " + t);
                    }
                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable t) throws Exception {
                        System.out.println("--> accept Error(3): " + t);
                    }
                });


        System.out.println("--------------------------------------------");
        //	4. flatMapIterable(Function(T，R))
        // 	对数据进行处理转换成Iterable来发射数据
        Observable.range(1, 5)
                .flatMapIterable(new Function<Integer, Iterable<? extends Integer>>() {

                    @Override
                    public Iterable<? extends Integer> apply(Integer t) throws Exception {
                        System.out.println("--> apply: " + t);
                        ArrayList<Integer> list = new ArrayList<Integer>();
                        list.add(888);
                        list.add(999);
                        return list;    // 将原始数据转换为两个数字发送
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept flatMapIterable(4)： " + t);
                    }
                });

        System.out.println("--------------------------------------------");
        //	5. flatMapIterable(Function（T，R）,Function(T,T,R))
        // 	第一个func接受原始数据，转换数据，第二个func同时接受原始和处理的数据，进行二次转换处理
        Observable.range(1, 3)
                .flatMapIterable(new Function<Integer, Iterable<? extends Integer>>() {

                    @Override
                    public Iterable<? extends Integer> apply(Integer t) throws Exception {
                        ArrayList<Integer> list = new ArrayList<Integer>();
                        list.add(888);
                        list.add(999);
                        return list; // 将原始数据转换为两个数字发送
                    }
                }, new BiFunction<Integer, Integer, Integer>() {

                    @Override
                    public Integer apply(Integer t1, Integer t2) throws Exception {
                        System.out.println("--> apply(5): t1 = " + t1 + ", t2 = " + t2);
                        return t1 + t2;    // 将原始数据和处理过的数据组合进行二次处理发送
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept flatMapIterable(5)： " + t);
                    }
                });
    }

}
