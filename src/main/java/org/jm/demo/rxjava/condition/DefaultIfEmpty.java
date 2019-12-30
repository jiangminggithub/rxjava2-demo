package org.jm.demo.rxjava.condition;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author jiangming
 * <p>
 * DefaultEmpty: 发射来自原始Observable的值，如果原始 Observable 没有发射数据项，就发射一个默认值。
 */
public class DefaultIfEmpty {

    public static void main(String[] args) {
        /**
         *   defaultIfEmpty(@NotNull T defaultItem)
         *  如果原始Observable没有发射任何数据正常终止（以 onCompleted 的形式），
         *  DefaultIfEmpty 返回的Observable就发射一个你提供的默认值defaultItem。
         */
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onComplete();   // 不发射任何数据，直接发射完成通知
            }
        }).defaultIfEmpty("No Data emitter!!!")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("--> onNext: " + s);
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
