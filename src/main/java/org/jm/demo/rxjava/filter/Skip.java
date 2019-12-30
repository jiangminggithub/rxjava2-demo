package org.jm.demo.rxjava.filter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * Skip: 忽略Observable发射的指定N项数据
 */
public class Skip {

    public static void main(String[] args) throws Exception {
        // 1. skip(long count)
        // 跳过前count项数据，保留后面的数据
        Observable.range(1, 10)
                .skip(5) // 过滤数据序列前5项数据
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept skip(1): " + t);
                    }
                });

        System.out.println("------------------------------------");
        // 2. skip(long time, TimeUnit unit)
        // 跳过开始的time时间段内的数据，保留后面的数据
        Observable.intervalRange(1, 5, 0, 1, TimeUnit.SECONDS)
                .skip(2, TimeUnit.SECONDS)    // 跳过前2秒的数据
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> accept skip(2): " + t);
                    }
                });

        System.out.println("------------------------------------");
        // 3. skipLast(int count)
        // 跳过数据后面的count个数据
        Observable.range(1, 10)
                .skipLast(5) // 跳过数据序列的后5项数据
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept skipLast(3): " + t);
                    }
                });

        System.out.println("------------------------------------");
        // 4. skipLast(long time, TimeUnit unit, [boolean delayError])
        // 丢弃在原始Observable的生命周期内最后time时间内发射的数据
        // 可选参数Scheduler: 指定调度线程 delayError：延迟异常通知
        Observable.intervalRange(1, 10, 0, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> DataSource: " + t);
                    }
                })
                .skipLast(2, TimeUnit.SECONDS)
           //   .skipLast(2, TimeUnit.SECONDS, Schedulers.trampoline()) // 通过scheduler指定工作线程
           //   .skipLast(2, TimeUnit.SECONDS, true)                    // 延迟Error的通知，多用于组合Observable的场景
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> accept skipLast(4): " + t);
                    }
                });

        System.in.read();
    }

}
