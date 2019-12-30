package org.jm.demo.rxjava.create;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * Timer 定时任务，延迟任务 , timer 返回一个Observable，它在延迟一段给定的时间后发射一个简单的数字0。
 */
public class Timer {

    public static void main(String[] args) {
        // timer(long delay, TimeUnit unit, Scheduler scheduler)
        // 定时delay时间 单位后发送数字0，指定可选参数Schedule调度器为trampoline(当前线程排队执行)
        Observable.timer(1, TimeUnit.SECONDS, Schedulers.trampoline())
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> accept: " + t);
                    }
                });
    }

}
