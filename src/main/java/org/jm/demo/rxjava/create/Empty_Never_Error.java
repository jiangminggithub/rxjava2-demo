package org.jm.demo.rxjava.create;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Ming
 * <p>
 * Empty: 创建一个不发射任何数据但是正常终止的Observable
 */
public class Empty_Never_Error {

    public static void main(String[] args) {
        System.out.println("--> 1 -----------------------------------");
        // 1.  创建一个不发射任何数据但是正常终止的Observable
        Observable.empty()
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(Object t) {
                        System.out.println("onNext: " + t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        System.out.println("--> 2 -----------------------------------");
        // 2.  创建一个不输出数据，并且不会终止的Observable
        Observable.never()
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(Object t) {
                        System.out.println("onNext: " + t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        System.out.println("--> 3 -----------------------------------");
        // 3.  创建一个不发射数据以一个错误终止的Observable
        Observable.error(new NullPointerException("error test"))
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(Object t) {
                        System.out.println("onNext: " + t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

    }

}
