package org.jm.demo.rxjava.filter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * TakeLast: 操作符修改原始Observable，你可以只发射Observable数据发射完成前的N项数据，忽略前面的数据。
 */
public class TakeLast {

    public static void main(String[] args) throws Exception {

        // 1. takeLast(int count)
        // 接受Observable数据发射完成前的Count项数据, 忽略前面的数据
        Observable.range(1, 10)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept(1): " + t);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("--> onCompleted(1): ");
                    }
                })
                .takeLast(5) // 发送数据发射完成前的5项数据
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept takeLast(1): " + t);
                    }
                });

        System.out.println("--------------------------------");
        // 2. takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize)
        // 可选参数 scheduler：指定工作调度器  delayError：延迟Error通知  bufferSize：指定缓存大小
        // 接受Observable数据发射完成前指定时间间隔发射的数据项
        Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> accept(2): " + t);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("--> onCompleted(2): ");
                    }
                })
                .takeLast(3, TimeUnit.SECONDS) // 发送数据发射完成前3秒时间段内的数据
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> accept takeLast(2): " + t);
                    }
                });

        System.out.println("--------------------------------");
        // 3. takeLast(long count, long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize)
        // 可选参数 scheduler：指定工作调度器  delayError：延迟Error通知  bufferSize：指定缓存大小
        // 接受Observable数据发射完成前time时间段内收集count项数据并发射
        Observable.intervalRange(1, 10, 1, 100, TimeUnit.MILLISECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> accept(3): " + t);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("--> onCompleted(3): ");
                    }
                })
                .takeLast(2, 500, TimeUnit.MILLISECONDS) // 在原数据发射完成前500毫秒内接受2项数据
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> accept takeLast(3): " + t);
                    }
                });

        System.in.read();

    }

}
