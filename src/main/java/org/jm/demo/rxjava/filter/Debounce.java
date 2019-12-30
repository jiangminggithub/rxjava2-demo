package org.jm.demo.rxjava.filter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * Debounce: 过滤发射速率较快的数据项，防抖操作
 * 与ThrottleWithTimeout相同
 */
public class Debounce {

    public static void main(String[] args) throws Exception {

        // 1. debounce(long timeout, TimeUnit unit)
        // 发送一个数据，如果在包含timeout时间内，没有第二个数据发射，那么就会发射此数据，否则丢弃此数据
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);    // 下一个数据到此数据发射,	30 < timeout	--> skip
                Thread.sleep(30);
                emitter.onNext(2);    // 下一个数据到此数据发射,	100 > timeout	--> deliver
                Thread.sleep(100);
                emitter.onNext(3);    // 下一个数据到此数据发射,	50 = timeout	--> skip:
                Thread.sleep(50);
                emitter.onNext(4);    // 下一个数据到此数据发射,	onCompleted		--> deliver
                emitter.onComplete();

            }
        }).debounce(50, TimeUnit.MILLISECONDS)    // 指定防抖丢弃时间段为50毫秒
            //  .debounce(50, TimeUnit.MILLISECONDS, Schedulers.trampoline())	// 指定调度为当前线程排队
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept debounce(1-1): " + t);
                    }
                });

        System.out.println("---------------------------------------------");
        // 2. debounce(debounceSelector)
        // 原始数据发射每一个序列的通过监听debounceSelector的数据通知，
        // 在debounceSelector数据发送前，如果有下一个数据，则丢弃当前项数据，继续监视下一个数据
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);        // skip		--> debounceSelector is no emitter(<2s)
                Thread.sleep(1000);
                emitter.onNext(2);        // skip		--> debounceSelector is no emitter(<2s)
                Thread.sleep(200);
                emitter.onNext(3);        // deliver	--> debounceSelector is emitter(>2s)
                Thread.sleep(2500);
                emitter.onNext(4);        // skip		--> debounceSelector is no emitter(=2s)
                Thread.sleep(2000);
                emitter.onNext(5);        // deliver	--> onComplete
                Thread.sleep(500);
                emitter.onComplete();
            }
        }).debounce(new Function<Integer, ObservableSource<Long>>() {

            @Override
            public ObservableSource<Long> apply(Integer t) throws Exception {
                System.out.println("--> apply(1-2): " + t);
                // 设置过滤延迟时间为2秒，此时返回的Observable从订阅到发送数据时间段即为timeout
                return Observable.timer(2, TimeUnit.SECONDS)
                        .doOnSubscribe(new Consumer<Disposable>() {

                            @Override
                            public void accept(Disposable t) throws Exception {
                                // 开始订阅，监听数据的发送来过滤数据
                                System.out.println("--> debounceSelector(1-2) is onSubscribe!");
                            }
                        }).doOnDispose(new Action() {

                            @Override
                            public void run() throws Exception {
                                // 发射数据后，丢弃当前的数据，解除当前绑定
                                System.out.println("--> debounceSelector(1-2) is unSubscribe!");
                            }
                        });
            }
        }).subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer t) throws Exception {
                System.out.println("----------> accept(1-2): " + t);
            }
        });


        System.in.read();
    }

}
