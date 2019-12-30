package org.jm.demo.rxjava.condition;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * SkipUntil: 丢弃原始Observable发射的数据，直到第二个Observable发射了一个数据，然后发射原始Observable的剩余数据。
 */
public class SkipUntil {

    public static void main(String[] args) throws Exception {

        /**
         *  skipUntil(ObservableSource other)
         *  丢弃原始Observable发射的数据，直到other发射了一个数据，然后发射原始Observable的剩余数据。
         */
        Observable.intervalRange(1, 10, 0, 500, TimeUnit.MILLISECONDS)
                // 丢弃2000毫秒的原始Observable发射的数据，接受后面的剩余部分数据
                .skipUntil(Observable.timer(2000, TimeUnit.MILLISECONDS))
                .subscribe(new Observer<Long>() {
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

        System.in.read();
    }
}
