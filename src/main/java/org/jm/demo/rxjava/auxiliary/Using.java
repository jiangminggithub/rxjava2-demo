package org.jm.demo.rxjava.auxiliary;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.util.concurrent.Callable;

/**
 * @author jiangming
 * <p>
 * Using： Using操作符让你可以指示Observable创建一个只在它的生命周期内存在的资源，当Observable终止时这个资源会被自动释放。
 */
public class Using {

    public static void main(String[] args) throws Exception {

        /**
         *  1. using(Callable resourceSupplier, Function sourceSupplier, Consumer disposer, boolean eager)
         *
         *  resourceSupplier:   // 一个用户创建一次性资源的工厂函数
         *  sourceSupplier:     // 一个用于创建Observable的工厂函数
         *  disposer:           // 一个用于释放资源的函数
         *  eager:              // 可选参数，如果为true的话，则第三个函数disposer的处理在Observable的结束前执行
         *
         *  当一个观察者订阅 using 返回的Observable时， using 将会使用Observable工厂函数创建观察者要观察的Observable，
         *  同时使用资源工厂函数创建一个你想要创建的资源。
         *  当观察者取消订阅这个Observable时，或者当观察者终止时（无论是正常终止还是因错误而终止），
         *  using 使用第三个函数释放它创建的资源。
         */
        Observable.using(
                // 一个用户创建一次性资源的工厂函数
                new Callable<MyResource>() {
                    @Override
                    public MyResource call() throws Exception {
                        System.out.println("----> resourceSupplier call");
                        return new MyResource("This is Observable resource!");
                    }
                },
                // 一个用于创建Observable的工厂函数，这个函数返回的Observable就是最终被观察的Observable
                new Function<MyResource, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(MyResource myResource) throws Exception {
                        System.out.println("----> sourceSupplier apply: " + myResource);
                        return Observable.rangeLong(1, 5);
                    }
                },
                // 一个用于释放资源的函数
                new Consumer<MyResource>() {
                    @Override
                    public void accept(MyResource myResource) throws Exception {
                        System.out.println("----> disposer accept: ");
                        myResource.releaseResource();
                    }
                },
                // 可选参数，如果为true的话，则在Observable的结束前执行释放资源的函数
                true).subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("--> onNext: " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onComplete");
                    }
                });

    }
}

/**
 * 用于在Observable的生命周期内存在的资源对象
 */
class MyResource {
    private String resource;

    public MyResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "MyResource{" +
                "resource='" + resource + '\'' +
                '}';
    }

    public void releaseResource() {
        System.out.println("----> MyResource resource is release. ");
        resource = null;
    }
}