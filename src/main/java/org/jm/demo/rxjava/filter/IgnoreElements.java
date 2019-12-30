package org.jm.demo.rxjava.filter;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 *
 * @author jiangming
 *
 *         IgnoreElements: 不发射任何数据，只发射Observable的终止通知
 *         符抑制原始Observable发射的所有数据，只允许它的终止通知 （ onError 或 onCompleted ）通过
 *
 */
public class IgnoreElements {

    public static void main(String[] args) {
        // ignoreElements()
        // 只接受onError或onCompleted通知，拦截onNext事件（不关心发射的数据，只希望在成功或者失败的时候收到通知）
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                //	int i = 1/0;
                emitter.onComplete();
            }
        }).ignoreElements()
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe");
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
