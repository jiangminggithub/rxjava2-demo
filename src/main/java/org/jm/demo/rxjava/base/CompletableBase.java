package org.jm.demo.rxjava.base;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * Completable: 不会发射任何数据, 只有 onComplete 与 onError事件，
 * 同时没有Observable中的一些操作符，如 map，flatMap。通常与 andThen 操作符结合使用
 */
public class CompletableBase {

    public static void main(String[] args) {
        // 1. Completable：只发送complete 或 error 事件,不发送任何数据
        Completable.fromAction(new Action() {

            @Override
            public void run() throws Exception {
                System.out.println("Hello World! This is Completable.");
            }
        }).subscribe(new CompletableObserver() {

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError");
            }

            @Override
            public void onComplete() {
                System.out.println("--> onComplete");
            }
        });

        System.out.println("----------------------------------------------");
        // 2. 与 andThen 结合使用，当Completable执行完onCompleted后，执行andThen里的任务
        Completable.create(new CompletableOnSubscribe() {

            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                Thread.sleep(1000);
                System.out.println("--> completed");
                emitter.onComplete();
            }
        }).andThen(Observable.range(1, 5))
          .subscribe(new Consumer<Integer>() {

               @Override
               public void accept(Integer t) throws Exception {
                    System.out.println("--> accept: " + t);
               }
           });
    }
}
