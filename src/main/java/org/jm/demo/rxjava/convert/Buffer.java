package org.jm.demo.rxjava.convert;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * Buffer 操作符将一个Observable变换为另一个，原来的Observable正常发射数据，变换产生
 * 的Observable发射这些数据的缓存集合
 */
public class Buffer {

    public static void main(String[] args) throws Exception {

        // 1. buffer(count)
        // 以列表(List)的形式发射非重叠的缓存，每一个缓存至多包含来自原始 Observable的count项数据（最后发射的列表数据可能少于count项）
        Observable.range(1, 10)
                .buffer(3)
                .subscribe(new Consumer<List<Integer>>() {

                    @Override
                    public void accept(List<Integer> t) throws Exception {
                        System.out.println("--> bufferr(1) accept: " + t);
                    }
                });

        System.out.println("-------------------------------------");
        // 2. buffer(boundary) 监视一个名叫boundary的Observable，
        // 开始创建一个List收集原始 Observable 数据，监视一个名叫boundary的Observable，
        // 每当这个Observable发射了一个值，它就创建一个新的List开始收集来自原始Observable的数据并发射原来已经收集数据的List,
        // 当boundary发送了完成通知，会将此时还未发送的 List 发送。
        // 所有发送的 List 可能没有收集到数据，此时数据的收集可能并不会完整收集所有原始 Observable 数据。
        Observable.range(1, 5000000)
                .buffer(Observable.timer(2000, TimeUnit.MILLISECONDS)) // 1毫秒后开始接受原始数据
                .subscribeOn(Schedulers.trampoline())
                .subscribe(new Consumer<List<Integer>>() {

                    @Override
                    public void accept(List<Integer> t) throws Exception {
                        System.out.println("--> accept(2): " + t.size());    // 每次收集的数据序列个数
                    }
                });


        System.out.println("-----------------------------------");
        // 3. buffer(int count, int skip)
        // 在指定的数据中移动指针来获取缓存数据：指针每次移动1个数据长度，每次缓存3个数据
        Observable.range(1, 5)
                .buffer(3, 1)
                .subscribe(new Consumer<List<Integer>>() {

                    @Override
                    public void accept(List<Integer> t) throws Exception {
                        System.out.println("--> bufferr(3) accept: " + t);
                    }
                });

        System.out.println("-----------------------------------");
        // 4. buffer(long timespan, TimeUnit unit)
        // 每隔timespan时间段以list的形式收集数据
        Observable.range(1, 50000)
                .buffer(1, TimeUnit.MILLISECONDS)    // 每隔1毫秒收集一次原始序列数据
                .subscribe(new Consumer<List<Integer>>() {

                    @Override
                    public void accept(List<Integer> t) throws Exception {
                        System.out.println("--> bufferr(4) accept: " + t.size());    // 每次收集的数据序列个数
                    }
                });


        System.out.println("-----------------------------------");
        // 5. buffer(long timespan, TimeUnit unit, int count)
        // 每隔1毫秒缓存50个数据
        Observable.range(1, 1000)
                .buffer(1, TimeUnit.MILLISECONDS, 50)    // 每隔1毫秒收集50个数据序列
                .subscribe(new Consumer<List<Integer>>() {

                    @Override
                    public void accept(List<Integer> t) throws Exception {
                        System.out.println("--> bufferr(5) accept: " + t.size());    // 每次收集的数据序列个数
                    }
                });

        System.out.println("-----------------------------------");
        // 6. buffer(long timespan, long timeskip, TimeUnit unit)
        // 在每一个timeskip时期内都创建一个新的 List,
        // 每个List都独立收集timespan时间段原始Observable发射的数据,
        // 如果 timespan 长于 timeskip，它发射的数据包将会重叠，因此不同List中可能包含重复的数据项
        Observable.range(1, 50000)
                .buffer(1, 1, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .subscribe(new Consumer<List<Integer>>() {

                    @Override
                    public void accept(List<Integer> t) throws Exception {
                        System.out.println("--> accept(6): " + t.size());    // 每次收集的数据序列个数
                    }
                });

        System.out.println("-----------------------------------");
        // 7. buffer(Callable<ObservableSource<T>> boundarySupplier)
        // 当它订阅原来的Observable时，开始将数据收集到一个List，然后它调用 bufferClosingSelector 生成第二个Observable，
        // 当第二个Observable 发射一个 TClosing 时，buffer 发射当前的 List ，
        // 然后重复这个过程：开始组装一个新的List，然后调用bufferClosingSelector创建一个新的Observable并监视它。
        // 它会一直这样做直到原来的Observable执行完成。会收集完整的原始 Observable 的数据
        Observable.range(1, 50000)
                .buffer(new Callable<Observable<Long>>() {

                    @Override
                    public Observable<Long> call() throws Exception {
                        return Observable.timer(1, TimeUnit.MILLISECONDS);
                    }
                }).subscribe(new Consumer<List<Integer>>() {

            @Override
            public void accept(List<Integer> t) throws Exception {
                System.out.println("--> accept(7): " + t.size());    // 每次收集的数据序列个数
            }
        });
        System.in.read();
    }

}
