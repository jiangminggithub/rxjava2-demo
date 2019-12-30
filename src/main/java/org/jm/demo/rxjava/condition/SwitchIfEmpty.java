package org.jm.demo.rxjava.condition;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author jiangming
 * <p>
 * SwitchIfEmpty: 如果原始Observable没有发射数据时，发射切换一个指定的Observable继续发射数据
 */
public class SwitchIfEmpty {

    public static void main(String[] args) {

        /**
         *  switchIfEmpty(ObservableSource other)
         *  如果原始Observable没有发射数据时，发射切换指定的other继续发射数据
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onComplete();           // 不发射任何数据，直接发射完成通知
            }
        }).switchIfEmpty(Observable.just(888))  // 如果原始Observable没有发射数据项，默认发射备用的Observable，发射数据项888
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("--> onNext: " + integer);
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
