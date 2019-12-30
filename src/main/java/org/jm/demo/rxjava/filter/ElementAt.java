package org.jm.demo.rxjava.filter;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * ElementAt: 指定发射索引的数据数据项
 */
public class ElementAt {

    public static void main(String[] args) {
        // 1. elementAt(long index)
        // 指定发射第N项数据（从0开始计数）,如果数据不存在，会IndexOutOfBoundsException异常
        Observable.range(1, 10)
                .elementAt(5) // 发射数据序列中索引为5的数据项，索引从0开始
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept ElementAt(1): " + t);
                    }
                });

        System.out.println("----------------------------------------");
        // 2. elementAt(long index, Integer defaultItem)
        // 指定发射第N项数据（从0开始计数）,如果数据不存在,发送默认defaultItem
        Observable.range(1, 10)
                .elementAt(20, 0) // 发射索引第20项数据，不存在此项数据时，发送默认数据0
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("--> accept elementAt(2): " + t);
                    }
                });

        System.out.println("----------------------------------------");
        // 3. elementAtOrError(long index)
        // 如果指定发射的数据不存在，会抛出NoSuchElementException
        Observable.range(1, 10)
                .elementAtOrError(50) // 发射索引为50的数据，不存在则发送NoSuchElementException异常通知
                .subscribe(new SingleObserver<Integer>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(3): ");
                    }

                    @Override
                    public void onSuccess(Integer t) {
                        System.out.println("--> onSuccess(3): " + t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(3): " + e);
                    }
                });

    }

}
