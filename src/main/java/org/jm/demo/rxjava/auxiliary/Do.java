package org.jm.demo.rxjava.auxiliary;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * Do: 注册一个动作作为原始Observable生命周期事件的一种占位符
 * <p>
 * 你可以注册某个事件的回调，当Observable的某个事件发生时，Rx会在与Observable链关联的正常通知集合中调用它。
 * Rxjava中实现了多种操作符用于达到这个目的.
 */
public class Do {

    public static void main(String[] args) {
        /**
         *  1. doOnSubscribe(Consumer onSubscribe)
         *  一旦有观察者订阅了Observable，就会被调用
         */
        Observable.just(999).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> doOnSubscribe");
            }
        }).subscribe(new Observer<Integer>() {
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

        System.out.println("--------------------------------------------");
        /**
         *  2. doOnLifecycle(Consumer onSubscribe, Action onDispose)
         *  onSubscribe： 接受观察者订阅前的通知，可以在此通知中解除订阅
         *  onDispose：   接受观察者调用解除订阅通知
         *  在观察者订阅产生和解除时调用
         */
        Observable.just(999).doOnLifecycle(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                System.out.println("----> doOnLifecycle onSubscribe(2)");
                // disposable.dispose();  // 可以在观察者订阅前直接解除订阅
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("----> doOnLifecycle onDispose(2)");
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                System.out.println("--> onSubscribe(2)");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("--> onNext(2): " + integer);
                disposable.dispose(); // 手动解除订阅
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

        System.out.println("--------------------------------------------");
        /**
         *  3. doOnNext(Consumer onNext)
         *  在Observable每次发射数据前被调用
         */
        Observable.just(999).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("----> doOnNext(3): " + integer);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("--> accept(3): " + integer);
            }
        });

        System.out.println("--------------------------------------------");
        /**
         *  4. doOnEach(Observer observer)
         *  在Observable调用观察者的所有通知前被调用
         */
        Observable.just(999).doOnEach(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("----> doOnEach(4) onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("----> doOnEach(4) onNext: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("----> doOnEach(4) onError: " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("----> doOnEach(4) onComplete");
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

        System.out.println("--------------------------------------------");
        /**
         *  5. doAfterNext(Consumer onAfterNext)
         *  在Observable调用OnNext通知(数据发射通知)之后被调用
         */
        Observable.just(999).doAfterNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("----> doAfterNext(5): " + integer);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("--> onNext(5): " + integer);
            }
        });

        System.out.println("--------------------------------------------");
        /**
         *  6. doOnError(Consumer onError)
         *  注册一个动作，当它的Observable由于异常终止调用 onError 时会被调用
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onError(new Exception("Test Error!"));
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("----> doOnError(6): " + throwable);
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
                System.out.println("--> onComplete(6)");
            }
        });

        System.out.println("--------------------------------------------");
        /**
         *  7.
         *  doOnTerminate(Action onTerminate):  当Observable终止之前会被调用，无论是正常还是异常终止
         *  doAfterTerminate(Action onFinally): 当Observable终止之后会被调用，无论是正常还是异常终止
         */
        Observable.just(999).doOnTerminate(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("----> doOnTerminate(7)");
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("--> accept(7): " + integer);
            }
        });

        System.out.println("--------------------------------------------");
        /**
         *  8. doOnComplete(Action onComplete)
         *  Observable正常终止调用 onCompleted 时会被调用
         */
        Observable.just(999).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("----> doOnComplete(8)");
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(8)");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("--> onNext(8): " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(8): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onComplete(8)");
            }
        });

        System.out.println("--------------------------------------------");
        /**
         *  9. doFinally(Action onFinally)
         *  Observable终止之后会被调用，无论是正常还是异常终止，但是优先于doAfterTerminate
         */
        Observable.just(999).doFinally(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("----> doFinally(9)");
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(9)");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("--> onNext(9): " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(9): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onComplete(9)");
            }
        });

        System.out.println("--------------------------------------------");
        /**
         *  10. doOnDispose(Action onDispose)
         *  在观察者调用Disposable的dispose()方法时被调用
         */
        Observable.just(999).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("----> doOnDispose(10)");
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(10)");
                d.dispose();
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("--> onNext(10): " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(10): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onComplete(10)");
            }
        });

    }
}
