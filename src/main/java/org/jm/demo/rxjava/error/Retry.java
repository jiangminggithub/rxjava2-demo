package org.jm.demo.rxjava.error;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.*;

import java.util.concurrent.TimeUnit;

public class Retry {
    // flag for emitted onError times
    public static int temp = 0;

    public static void main(String[] args) throws Exception {

        // 创建可以发送Error通知的Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                if (temp <= 2) {
                    emitter.onError(new Exception("Test Error!"));
                    temp++;
                }
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            }
        });

        /**
         * 1. retry()
         *  无论收到多少次onError通知, 都会去继续订阅并发射原始Observable。
         */
        observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> doOnSubscribe(1)");
            }
        }).retry().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("--> accept(1): " + integer);
            }
        });

        System.out.println("---------------------------------------------");
        temp = 0;
        /**
         * 2. retry(long times)
         *  遇到异常后，最多重新订阅源Observable times次
         */
        observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> doOnSubscribe(2)");
            }
        }).retry(1) // 遇到异常后，重复订阅的1次
          .subscribe(new Observer<Integer>() {
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

        System.out.println("---------------------------------------------");
        temp = 0;
        /**
         * 3. retry(long times, Predicate<Throwable> predicate)
         *  遇到异常后最多重新订阅times次,每次重新订阅经过函数predicate最终判断是否继续重新订阅
         *  如果times到达上限或者predicate返回false中任意一个最先满足条件,都会终止重新订阅
         */
        observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> doOnSubscribe(3)");
            }
        }).retry(2, new Predicate<Throwable>() {
            @Override
            public boolean test(Throwable throwable) throws Exception {
                System.out.println("--> test(3)");
                if(throwable instanceof Exception) {
                    return true;    // 遇到异常通知后是否继续继续订阅
                }
                return false;
            }
        }).subscribe(new Observer<Integer>() {
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

        System.out.println("---------------------------------------------");
        temp = 0;
        /**
         * 4. retry(Predicate<Throwable> predicate)
         *  遇到异常时，通过函数predicate判断是否重新订阅源Observable
         */
        observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> doOnSubscribe(4)");
            }
        }).retry(new Predicate<Throwable>() {
            @Override
            public boolean test(Throwable throwable) throws Exception {
                if (throwable instanceof Exception) {
                    return true;    // 遇到异常通知后是否继续继续订阅
                }
                return false;
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

        System.out.println("---------------------------------------------");
        temp = 0;
        /**
         * 5. retry(BiPredicate<Integer, Throwable> predicate)
         *   遇到异常时，通过函数predicate判断是否重新订阅源Observable,并且通过参数integer传递给predicate重新订阅的次数
         */
        observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> doOnSubscribe(5)");
            }
        }).retry(new BiPredicate<Integer, Throwable>() {
            @Override
            public boolean test(Integer integer, Throwable throwable) throws Exception {
                System.out.println("--> test(5): " + integer);
                if (throwable instanceof Exception) {
                    return true;    // 遇到异常通知后是否继续继续订阅
                }
                return false;
            }
        }).subscribe(new Observer<Integer>() {
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

        System.out.println("---------------------------------------------");
        temp = 0;
        /**
         * 6. retryUntil(BooleanSupplier stop)
         * 重试重新订阅，直到给定的停止函数stop返回true
         */
        observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> doOnSubscribe(6)");
            }
        }).retryUntil(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() throws Exception {
                System.out.println("--> getAsBoolean(6)");
                if(temp == 1){  // 满足条件，停止重新订阅
                    return true;
                }
                return false;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(6)");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("--> onNext(6): " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(6): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onCompleted(6)");
            }
        });


        System.out.println("---------------------------------------------");
        temp = 0;
        /**
         * 7. retryWhen(Function<Observable<Throwable>, ObservableSource> handler)
         *  将onError中的Throwable传递给一个函数handler，这个函数产生另一个Observable，
         *  retryWhen观察它的结果再决定是不是要重新订阅原始的Observable。
         *  如果这个Observable发射了一项数据，它就重新订阅，
         *  如果这个Observable发射的是onError通知，它就将这个通知传递给观察者然后终止。
         */
        observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> doOnSubscribe(7)");
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                System.out.println("--> apply(7)");
                // 根据产生的Error的Observable是否正常发射数据来进行重新订阅，如果发射Error通知，则直接传递给观察者后终止
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if (temp == 1) {
                            return Observable.error(throwable); // 满足条件后，传递这个Error，终止重新订阅
                        }
                        return Observable.timer(1, TimeUnit.MILLISECONDS);  // 正常发射数据，可以重新订阅
                    }
                });
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(7)");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("--> onNext(7): " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(7): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onCompleted(7)");
            }
        });

        System.in.read();
    }
}
