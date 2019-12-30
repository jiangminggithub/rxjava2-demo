package org.jm.demo.rxjava.error;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author jiangming
 * Catch: 从onError通知中通过指定的方式恢复发射数据。
 */
public class Catch {

    public static void main(String[] args) {

        // 创建一个可以发射异常的Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(1 / 0);  // 产生一个异常
                emitter.onNext(3);
                emitter.onNext(4);
            }
        });

        /** 1. onErrorReturnItem(T item)
         * 让Observable遇到错误时发射一个指定的项（item）并且正常终止
         */
        observable.onErrorReturnItem(888)   // 源Observable发生异常时发射指定的888数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(1)");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("--> onNext(1): " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(1): " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onCompleted(1)");
                    }
                });

        System.out.println("-----------------------------------------------");
        /**
         * 2. onErrorReturn(Function<Throwable, T> valueSupplier)
         * 让Observable遇到错误时通过一个函数Function来接受Error参数并进行判断返回指定的类型数据，并且正常终止
         */
        observable.onErrorReturn(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) throws Exception {
                System.out.println("--> apply(1): e = " + throwable);
                return 888; // 源Observable发生异常时发射指定的888数据
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(2)");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("--> onNext(2): " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(2): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onCompleted(2)");
            }
        });

        System.out.println("-----------------------------------------------");
        /**
         * 3. onErrorResumeNext(ObservableSource next)
         * 让Observable在遇到错误时开始发射第二个指定的Observable的数据序列
         */
        observable.onErrorResumeNext(Observable.just(888))  // 当发生异常的时候继续发射此项Observable
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(3)");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("--> onNext(3): " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(3): " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onCompleted(3)");
                    }
                });

        System.out.println("-----------------------------------------------");
        /**
         * 4. onErrorResumeNext(Function<Throwable, ObservableSource<T>> resumeFunction)
         * 让Observable在遇到错误时通过一个函数Function来接受Error参数并进行判断返回指定的第二个Observable的数据序列
         */
        observable.onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                System.out.println("--> apply(4): " + throwable);
                return Observable.just(888);    // 当发生异常的时候继续发射此项Observable
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(4)");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("--> onNext(4): " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(4): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onCompleted(4)");
            }
        });

        System.out.println("-----------------------------------------------");
        // 创建一个可以发射异常的Observable
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
            //  emitter.onError(new Throwable("This is Throwable!"));  // Throwable类型异常,直接通知观察者
            //  emitter.onError(new Error("This is Error!"));          // Error类型异常,直接通知观察者
                emitter.onError(new Exception("This is Exception!"));  // Exception类型异常,进行处理,发送备用的Observable数据
            //    emitter.onNext(1 / 0);  // 会产生一个ArithmeticException异常,异常会被处理,发送备用的Observable数据
                emitter.onNext(3);
                emitter.onNext(4);
            }
        });
        /**
         * 5. onExceptionResumeNext(ObservableSource next)
         *  如果onError收到的Throwable不是一个Exception,它会将错误传递给观察者的onError方法,不会使用备用的Observable
         *  只对Exception类型的异常通知进行备用Observable处理
         */
        observable1.onExceptionResumeNext(Observable.just(888))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(5)");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("--> onNext(5): " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(5): " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onCompleted(5)");
                    }
                });

    }
}
