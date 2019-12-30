package org.jm.demo.rxjava.create;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;

/**
 * @author Ming
 * <p>
 * RepeatUntil: 根据条件（BooleanSupplier）判断是否需要继续订阅,	false:继续订阅; true:取消订阅
 */
public class RepeatUntil {

    public static void main(String[] args) {
        // repeatUntil 根据条件（BooleanSupplier）判断是否需要继续订阅
        Observable.range(1, 2)
                .doOnComplete(new Action() {

                    @Override
                    public void run() throws Exception {
                        System.out.println("-----------> 完成一次订阅");
                    }
                })
                .repeatUntil(new BooleanSupplier() {

                    private int n = 0;

                    @Override
                    public boolean getAsBoolean() throws Exception {
                        System.out.println("getAsBoolean = " + (n < 3 ? false : true));
                        // 是否需要终止
                        if (n < 3) {
                            n++;
                            return false;    // 继续重新订阅
                        }
                        return true;        // 终止重新订阅
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept: " + t);
                    }
                });
    }

}
