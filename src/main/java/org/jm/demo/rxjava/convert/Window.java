package org.jm.demo.rxjava.convert;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * Window: 定期将来自原始Observable的数据分解为一个Observable窗口，发射这些窗口，而不是每次发射一项数据
 */
public class Window {

    public static void main(String[] args) throws Exception {
        // 1. window(Callable boundary)
        // 开启一个window，并订阅观察boundary返回的Observable发射了一个数据，
        // 则关闭此window，将收集的数据以Observable发送, 重新订阅boundary返回的Observable，开启新window
        Observable.intervalRange(1, 10, 0, 1, TimeUnit.SECONDS)
                .window(new Callable<Observable<Long>>() {

                    @Override
                    public Observable<Long> call() throws Exception {
                        System.out.println("--> call(1)");
                        return Observable.timer(2, TimeUnit.SECONDS); // 两秒后关闭当前窗口
                    }
                })
                .subscribe(new Consumer<Observable<Long>>() {

                    @Override
                    public void accept(Observable<Long> t) throws Exception {
                        // 接受每个window接受的数据的Observable
                        t.subscribe(new Consumer<Long>() {

                            @Override
                            public void accept(Long t) throws Exception {
                                System.out.println("--> accept(1): " + t);
                            }
                        });
                    }
                });

        System.out.println("----------------------------------------------------------------------");
        // 2. window(ObservableSource openingIndicator, Function<T, ObservableSource<R>> closingIndicator)
        // 当openingIndicator发射一个数据，就会打开一个window, 同时订阅closingIndicator返回的Observable，
        // 当这个Observable发射一个数据，就结束此window以及对应的closingIndicator,发送收集数据的 Observable。
        Observable<Long> openingIndicator = Observable.intervalRange(1, 5, 0, 1, TimeUnit.SECONDS)
                .doOnSubscribe(new Consumer<Disposable>() {

                    @Override
                    public void accept(Disposable t) throws Exception {
                        System.out.println("--> openingIndicator is subscribe!");
                    }
                })
                .doOnComplete(new Action() {

                    @Override
                    public void run() throws Exception {
                        System.out.println("--> openingIndicator is completed!");
                    }
                })
                .doOnNext(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> openingIndicator emitter: " + t);
                    }
                });

        Observable<Long> dataSource = Observable.intervalRange(1, 5, 0, 1, TimeUnit.SECONDS)
                .doOnSubscribe(new Consumer<Disposable>() {

                    @Override
                    public void accept(Disposable t) throws Exception {
                        System.out.println("--> DataSource is subscribe!");
                    }
                })
                .doOnNext(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("--> DataSource emitter: " + t);
                    }
                });

        dataSource.window(openingIndicator, new Function<Long, Observable<Long>>() {

            @Override
            public Observable<Long> apply(Long t) throws Exception {
                System.out.println("--> apply(2): " + t);
                return Observable.timer(2, TimeUnit.SECONDS).doOnSubscribe(new Consumer<Disposable>() {

                    @Override
                    public void accept(Disposable t) throws Exception {
                        System.out.println("--> closingIndicator is subscribe!");
                    }
                });
            }
        })
                .subscribe(new Consumer<Observable<Long>>() {

                    @Override
                    public void accept(Observable<Long> t) throws Exception {
                        System.out.println("-------------------> new window data");
                        t.subscribe(new Consumer<Long>() {

                            @Override
                            public void accept(Long t) throws Exception {
                                System.out.println("--> accept(2): " + t);
                            }
                        });
                    }
                });

        System.out.println("----------------------------------------------------------------------");
        // 3. window(count)
        // 以count为缓存大小收集的不重叠的Observables对象，接受的数据与原数据彼此对应
        Observable.range(1, 20)
                .window(5)    // 设置缓存大小为5
                .subscribe(new Consumer<Observable<Integer>>() {

                    @Override
                    public void accept(Observable<Integer> t) throws Exception {
                        System.out.println("--------------> new data window");
                        t.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer t) throws Exception {
                                System.out.println("--> accept window(3): " + t);
                            }
                        });
                    }
                });

        System.out.println("----------------------------------------------------------------------");
        // 4. window(count,skip)
        // window一开始打开一个window，每发射skip项数据就会打开一个window独立收集原始数据
        // 当window收集了count个数据就会关闭window，开启另外一个。
        // 当原始Observable发送了onError 或者 onCompleted 通知也会关闭当前窗口。
        // 4.1 skip = count: 会依次顺序接受原始数据，同window(count)
        Observable.range(1, 10)
                .window(3, 3)    // skip = count, 数据会依次顺序输出
                .subscribe(new Consumer<Observable<Integer>>() {

                    @Override
                    public void accept(Observable<Integer> t) throws Exception {

                        t.observeOn(Schedulers.newThread())
                                .subscribe(new Consumer<Integer>() {

                                    @Override
                                    public void accept(Integer t) throws Exception {
                                        System.out.println("--> accept window(4-1): " + t + " , ThreadID: " + Thread.currentThread().getId());
                                    }
                                });
                    }
                });

        // 4.2 skip > count: 两个窗口可能会有 skip-count 项数据丢失
        Observable.range(1, 10)
                .window(2, 3)    // skip > count, 数据会存在丢失
                .subscribe(new Consumer<Observable<Integer>>() {

                    @Override
                    public void accept(Observable<Integer> t) throws Exception {

                        t.observeOn(Schedulers.newThread())
                                .subscribe(new Consumer<Integer>() {

                                    @Override
                                    public void accept(Integer t) throws Exception {
                                        System.out.println("--> accept window(4-2): " + t + " , ThreadID: " + Thread.currentThread().getId());
                                    }
                                });
                    }
                });

        // 4.3 skip < count: 两个窗口可能会有 count-skip 项数据重叠
        Observable.range(1, 10)
                .window(3, 2)    // skip < count, 数据会重叠
                .subscribe(new Consumer<Observable<Integer>>() {

                    @Override
                    public void accept(Observable<Integer> t) throws Exception {

                        t.observeOn(Schedulers.newThread())
                                .subscribe(new Consumer<Integer>() {

                                    @Override
                                    public void accept(Integer t) throws Exception {
                                        System.out.println("--> accept window(4-3): " + t + " , ThreadID: " + Thread.currentThread().getId());
                                    }
                                });
                    }
                });


        System.out.println("----------------------------------------------------------------------");
        // 5. window(long timespan, TimeUnit unit)
        // 每当过了 timespan 的时间段，它就关闭当前窗口并打开另一个新window收集数据
        Observable.intervalRange(1, 10, 0, 1, TimeUnit.SECONDS)
                .window(2, TimeUnit.SECONDS)  				 // 间隔2秒关闭当前 window 并打开一个新 window 收集数据
             //	.window(2, TimeUnit.SECONDS, Schedulers.newThread()) // 指定在 newThread 线程中
                .subscribe(new Consumer<Observable<Long>>() {

                    @Override
                    public void accept(Observable<Long> t) throws Exception {
                        t.observeOn(Schedulers.newThread())
                                .subscribe(new Consumer<Long>() {

                                    @Override
                                    public void accept(Long t) throws Exception {
                                        System.out.println("--> accept window(5)： " + t + " , ThreadID: " + Thread.currentThread().getId());
                                    }
                                });
                    }
                });

        System.out.println("----------------------------------------------------------------------");
        // 6. window(long timespan, TimeUnit unit, long count)
        // 每当过了timespan的时间段或者当前窗口收到了count项数据，它就关闭当前window并打开另一个window收集数据
        Observable.intervalRange(1, 12, 0, 500, TimeUnit.MILLISECONDS)
                .window(2, TimeUnit.SECONDS, 5)    		// 每隔2秒关闭当前收集数据的window并开启一个window收集5项数据
             //	.window(2, TimeUnit.SECONDS,Schedulers.newThread(), 5 )	// 指定在 newThread 线程中
                .subscribe(new Consumer<Observable<Long>>() {

                    @Override
                    public void accept(Observable<Long> t) throws Exception {
                        t.observeOn(Schedulers.newThread())
                                .subscribe(new Consumer<Long>() {

                                    @Override
                                    public void accept(Long t) throws Exception {
                                        System.out.println("--> accept window(6)： " + t + " , ThreadID: " + Thread.currentThread().getId());
                                    }
                                });
                    }
                });

        System.out.println("----------------------------------------------------------------------");
        // 7. window(long timespan, long timeskip, TimeUnit unit)
        // 在每一个timeskip时期内都创建一个新的window,然后独立收集timespan时间段的原始Observable发射的每一项数据，
        // 如果timespan长于timeskip，它发射的数据包将会重叠，因此可能包含重复的数据项。
        // 7.1 skip = timespan: 会依次顺序接受原始数据，同window(count)
        Observable.intervalRange(1, 5, 0, 1000, TimeUnit.MILLISECONDS)
                .window(1, 1, TimeUnit.SECONDS)           // 设置每秒创建一个window，收集2秒的数据
             //	.window(2, 1, TimeUnit.SECONDS, Schedulers.newThread())		// 指定在 newThread 线程中
                .subscribe(new Consumer<Observable<Long>>() {

                    @Override
                    public void accept(Observable<Long> t) throws Exception {
                        t.observeOn(Schedulers.newThread())
                                .subscribe(new Consumer<Long>() {

                                    @Override
                                    public void accept(Long t) throws Exception {
                                        System.out.println("--> accept window(7-1): " + t + " , ThreadID: " + Thread.currentThread().getId());
                                    }
                                });
                    }
                });

        // 7.2 skip > timespan: 两个窗口可能会有 skip-timespan 项数据丢失
        Observable.intervalRange(1, 5, 0, 1000, TimeUnit.MILLISECONDS)
                .window(1, 2, TimeUnit.SECONDS)           // 设置每秒创建一个window，收集2秒的数据
             //	.window(2, 1, TimeUnit.SECONDS, Schedulers.newThread())		// 指定在 newThread 线程中
                .subscribe(new Consumer<Observable<Long>>() {

                    @Override
                    public void accept(Observable<Long> t) throws Exception {
                        t.observeOn(Schedulers.newThread())
                                .subscribe(new Consumer<Long>() {

                                    @Override
                                    public void accept(Long t) throws Exception {
                                        System.out.println("--> accept window(7-2): " + t + " , ThreadID: " + Thread.currentThread().getId());
                                    }
                                });
                    }
                });

        // 7.3 skip < timespan: 两个窗口可能会有 timespan-skip 项数据重叠
        Observable.intervalRange(1, 5, 0, 1000, TimeUnit.MILLISECONDS)
                .window(2, 1, TimeUnit.SECONDS)           // 设置每秒创建一个window，收集2秒的数据
            //	.window(2, 1, TimeUnit.SECONDS, Schedulers.newThread())		// 指定在 newThread 线程中
                .subscribe(new Consumer<Observable<Long>>() {

                    @Override
                    public void accept(Observable<Long> t) throws Exception {
                        t.observeOn(Schedulers.newThread())
                                .subscribe(new Consumer<Long>() {

                                    @Override
                                    public void accept(Long t) throws Exception {
                                        System.out.println("--> accept window(7-3): " + t + " , ThreadID: " + Thread.currentThread().getId());
                                    }
                                });
                    }
                });

        System.in.read();
    }

}
