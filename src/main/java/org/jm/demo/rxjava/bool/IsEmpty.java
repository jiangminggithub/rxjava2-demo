package org.jm.demo.rxjava.bool;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * @author jiangming
 * <p>
 * IsEmpty: 用于判断原始Observable是否未发射任何数据
 */
public class IsEmpty {

    public static void main(String[] args) {

        /**
         *  isEmpty()
         *  判断原始Observable是否发射了数据项，如果原始Observable发射了数据，将发射false，否则发射true。
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onComplete();       // 不发射任何数据，直接发射完成通知
            }
        }).isEmpty()
          .subscribe(new SingleObserver<Boolean>() {
              @Override
              public void onSubscribe(Disposable d) {
                  System.out.println("--> onSubscribe");
              }

              @Override
              public void onSuccess(Boolean aBoolean) {
                  System.out.println("--> onSuccess: " + aBoolean);
              }

              @Override
              public void onError(Throwable e) {
                  System.out.println("--> onError: " + e);
              }
          });

    }
}
