package org.jm.demo.rxjava.create;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * Repeat: 创建一个发射特定数据，重复多次的Observable
 */
public class Repeat {

    public static void main(String[] args) {
        // 1. repeat(): 一直重复发射原始 Observable的数据序列
        Observable.range(1, 5)
                .repeat()
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept(1): " + t);
                    }
                });

        System.out.println("----------------------------------------");
        // 2. repeat(n): 重复执行5次
        Observable.range(1, 2)
                .repeat(3)
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept(2): " + t);
                    }
                });

    }

}
