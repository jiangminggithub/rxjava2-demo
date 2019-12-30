package org.jm.demo.rxjava.condition;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * SkipWhile: 丢弃原始 Observable 发射的数据，直到一个特定的条件为假，然后发射原始 Observable 剩余的数据。
 */
public class SkipWhile {

    public static void main(String[] args) throws Exception {

        /**
         *  skipWhile(Predicate<? super T> predicate)
         *  丢弃原始 Observable 发射的数据，直到函数predicate的条件为假，然后发射原始Observable剩余的数据。
         */
        Observable.intervalRange(1, 10, 0, 500, TimeUnit.MILLISECONDS)
                .skipWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        if (aLong > 5) {
                            return false;       // 当原始数据大于5时，发射后面的剩余部分数据
                        }
                        return true;            // 丢弃原始数据项
                    }
                }).subscribe(new Observer<Long>() {
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
