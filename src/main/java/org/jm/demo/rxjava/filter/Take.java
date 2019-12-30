package org.jm.demo.rxjava.filter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * Take: 使用Take操作符让你可以修改Observable的行为，只返回前面的N项数据，然后发射完成通知，忽略剩余的数据
 */
public class Take {

    public static void main(String[] args) throws InterruptedException, Exception {

        // 1. take(long count)
        // 返回前count项数据
        Observable.range(1, 100)
                .take(5) // 返回前5项数据
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept take(1): " + t);
                    }
                });

        System.out.println("--------------------------------");
        // 2. take(long time, TimeUnit unit,[Scheduler] scheduler)
        //  取一定时间间隔内的数据，可选参数scheduler指定线程调度器
        Observable.intervalRange(1, 10, 1, 1, TimeUnit.SECONDS)
                .take(5, TimeUnit.SECONDS) // 返回前5秒的数据项
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> accept take(2): " + t);
                    }
                });

        System.in.read();

    }

}
