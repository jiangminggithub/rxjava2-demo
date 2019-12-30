package org.jm.demo.rxjava.convert;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * Scan: 连续地对【数据序列】的每一项应用一个函数，然后连续发射结果
 */
public class Scan {

    public static void main(String[] args) {
        // 1. scan(BiFunction(Integer sum, Integer t2))
        // 接受数据序列，从第二个数据开始，每次会将上次处理数据和现在接受的数据进行处理后发送
        Observable.range(1, 10)
                .scan(new BiFunction<Integer, Integer, Integer>() {

                    @Override
                    public Integer apply(Integer LastItem, Integer item) throws Exception {
                        System.out.println("--> apply: LastItem = " + LastItem + ", CurrentItem = " + item);
                        return LastItem + item; // 实现求和操作
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept scan(1): " + t);

                    }
                });

        System.out.println("-----------------------------------------------");
        // 2. scan(R,Func2)
        // 指定初始种子值，第一次发送种子值,后续发送原始数据序列以及累计处理数据
        Observable.range(1, 10)
                .scan(100, new BiFunction<Integer, Integer, Integer>() {    // 指定初始种子数据为100

                    @Override
                    public Integer apply(Integer lastValue, Integer item) throws Exception {
                        System.out.println("--> apply: lastValue = " + lastValue + ", item = " + item);
                        return lastValue + item;    // 指定初值的求和操作
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept scan(2) = " + t);
                    }
                });
    }

}
