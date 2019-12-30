package org.jm.demo.rxjava.convert;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * GroupBy : 对原始数据进行分组数据处理,数据处理成key-value结构，key即为分组的标志
 */
public class GroupBy {

    public static void main(String[] args) throws Exception {
        // 1. groupBy(keySelector)
        // 将原始数据处理后加上分组tag，通过GroupedObservable发射分组数据
        Observable.range(1, 10)
                .groupBy(new Function<Integer, String>() {

                    @Override
                    public String apply(Integer t) throws Exception {
                        // 不同的key将会产生不同分组的Observable
                        return t % 2 == 0 ? "Even" : "Odd"; // 将数据奇偶数进行分组,
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<GroupedObservable<String, Integer>>() {

                    @Override
                    public void accept(GroupedObservable<String, Integer> grouped) throws Exception {
                        // 得到每个分组数据的的Observable
                        grouped.subscribe(new Consumer<Integer>() {

                            @Override
                            public void accept(Integer t) throws Exception {
                                // 得到数据
                                System.out.println("--> accept groupBy(1):   groupKey: " + grouped.getKey() + ", value: " + t);
                            }
                        });
                    }
                });

        System.out.println("----------------------------------------------------------------------------");
        // 2. groupBy(Function(T,R)，Function(T,R))
        // 第一个func对原数据进行分组处理（仅仅分组添加key，不处理原始数据），第二个func对原始数据进行处理
        Observable.range(1, 10)
                .groupBy(new Function<Integer, String>() {

                    @Override
                    public String apply(Integer t) throws Exception {
                        // 对原始数据进行分组处理
                        return t % 2 == 0 ? "even" : "odd";
                    }
                }, new Function<Integer, String>() {

                    @Override
                    public String apply(Integer t) throws Exception {
                        // 对原始数据进行数据转换处理
                        return t + " is " + (t % 2 == 0 ? "even" : "odd");
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<GroupedObservable<String, String>>() {

                    @Override
                    public void accept(GroupedObservable<String, String> grouped) throws Exception {
                        grouped.subscribe(new Consumer<String>() {

                            @Override
                            public void accept(String t) throws Exception {
                                // 接受最终的分组处理以及原数据处理后的数据
                                System.out.println("--> accept groupBy(2):   groupKey = " + grouped.getKey()
                                        + ", value = " + t);
                            }
                        });
                    }
                });

        System.in.read();
    }

}
