package org.jm.demo.rxjava.filter;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author jiangming
 * <p>
 * Distinct：去除原始发射数据中重复的部分
 */
public class Distinct {

    public static void main(String[] args) {
        // 1. distinct()
        // 去除全部数据中重复的数据
        Observable.just(1, 2, 3, 3, 4, 5, 6, 6)
                .distinct()
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept distinct(1): " + t);

                    }
                });

        System.out.println("-----------------------------------------");
        // 2. distinct(Function<T,K>)
        // 数根据原始Observable发射的数据项产生一个 Key，然后比较这些Key而不是数据本身，来判定两个数据是否是不同的（去除全部数据中重复的数据）
        Observable.just(1, 2, 3, 3, 4, 5, 6, 6)
                .distinct(new Function<Integer, String>() {

                    @Override
                    public String apply(Integer t) throws Exception {
                        // 根据奇数或偶数来判断数据序列的重复 key
                        return t % 2 == 0 ? "even" : "odd";
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept distinct(2): " + t);
                    }
                });

        System.out.println("-----------------------------------------");
        // 3. distinctUntilChanged()
        // 去除连续重复的数据
        Observable.just(1, 2, 3, 3, 4, 5, 6, 6, 3, 2)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept distinctUntilChanged(3): " + t);
                    }
                });

        System.out.println("-----------------------------------------");
        // 4. distinctUntilChanged(Function<T,K>)
        // 数根据原始Observable发射的数据项产生的 Key，去除连续重复的数据
        Observable.just(8, 2, 3, 5, 9, 5, 6, 6)
                .distinctUntilChanged(new Function<Integer, String>() {

                    @Override
                    public String apply(Integer t) throws Exception {
                        // 根据原始数据处理后添加key，依据这个key来判断是否重复（去除连续重复的数据）
                        return t % 2 == 0 ? "even" : "odd";
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept distinctUntilChanged(4): " + t);
                    }
                });
    }

}
