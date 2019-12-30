package org.jm.demo.rxjava.create;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author Ming
 * <p>
 * From: 将其它种类的对象和数据类型转换为Observable，发射来自对应数据源数据类型的数据
 */
public class From {

    public static void main(String[] args) {
        Integer[] array = {1, 2, 3, 4, 5, 6};
        List<String> iterable = new ArrayList<String>();
        iterable.add("A");
        iterable.add("B");
        iterable.add("C");
        iterable.add("D");
        iterable.add("E");

        // 1. fromArray
        Observable.fromArray(array).subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer t) throws Exception {
                System.out.println("--> accept(1):fromArray: " + t);
            }
        });

        System.out.println("---------------------------------------");
        // 2. fromIterable
        Observable.fromIterable(iterable)
                .subscribe(new Consumer<String>() {

                    @Override
                    public void accept(String t) throws Exception {
                        System.out.println("--> accept(2) fromIterable: " + t);
                    }
                });

        System.out.println("---------------------------------------");
        // 3. fromCallable
        Observable.fromCallable(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return 1;
            }
        }).subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer t) throws Exception {
                System.out.println("--> accept(3): fromCallable: " + t);
            }
        });

        System.out.println("---------------------------------------");
        // 4. fromFuture
        Observable.fromFuture(new Future<String>() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public String get() throws InterruptedException, ExecutionException {
                System.out.println("--> fromFutrue: get()");
                return "hello";
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public String get(long timeout, TimeUnit unit)
                    throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(String t) throws Exception {
                System.out.println("--> accept(4): fromFuture： " + t);
            }
        });
    }

}
