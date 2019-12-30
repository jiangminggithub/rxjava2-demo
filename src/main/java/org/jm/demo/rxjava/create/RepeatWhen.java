package org.jm.demo.rxjava.create;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.io.IOException;

/**
 * @author Ming
 * <p>
 * RepeatWhen: 接收到终止通知后，决定是否重新订阅原来的Observable
 */
public class RepeatWhen {

    public static void main(String[] args) throws Exception {

        // repeatWhen(Func1())：接收到终止通知后，在函数中决定是否重新订阅原来的Observable
        // 需要注意的是repeatWhen的objectObservable处理（也可以单独自定义Observable返回），这里使用flathMap进行处理，
        // 让它延时发出onNext，这里onNext发出什么数据都不重要，它只是仅仅用来处理重订阅的通知，如果发出的是onComplete/onError，则不会触发重订阅
        Observable.range(1, 2)
                .doOnComplete(new Action() {

                    @Override
                    public void run() throws Exception {
                        System.out.println("-----------> 完成一次订阅");
                    }
                })
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    private int n = 0;

                    @Override
                    public ObservableSource<?> apply(Observable<Object> t) throws Exception {
                        // 接收到原始Observable的终止通知，决定是否重新订阅
                        System.out.println("--> apply repeat ");
                        return t.flatMap(new Function<Object, ObservableSource<?>>() {

                            @Override
                            public ObservableSource<?> apply(Object t) throws Exception {
                                if (n < 3) {    // 重新订阅3次
                                    n++;
                                    return Observable.just(0);
                                } else {
                                    return Observable.empty();
                                }
                            }
                        });
                        // return Observable.timer(1, TimeUnit.SECONDS);		// 间隔一秒后重新订阅一次
                        // return Observable.interval(1, TimeUnit.SECONDS);	// 每间隔一秒重新订阅一次
                    }
                })
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept: " + t);
                    }
                });

        System.in.read();

    }

}
