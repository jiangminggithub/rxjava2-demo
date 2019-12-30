package org.jm.demo.rxjava.condition;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 * <p>
 * TakeUntil: 发射来自原始Observable的数据，直到第二个Observable发射了一个数据或一个通知
 */
public class TakeUntil {

    public static void main(String[] args) throws Exception {

        // 创建Observable，发送数字1～10，每间隔200毫秒发射一个数据
        Observable<Long> observable = Observable.intervalRange(1, 10, 0, 200, TimeUnit.MILLISECONDS);

        /**
         *  1. takeUntil(ObservableSource other)
         *  发射来自原始Observable的数据，直到other发射了一个数据或一个通知后停止发射原始Observable并终止。
         */
        observable.takeUntil(Observable.timer(1000, TimeUnit.MILLISECONDS)) // 1000毫秒后停止发射原始数据
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(1)");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("--> onNext(1): " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(1): " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onComplete(1)");
                    }
                });

        System.in.read();
        System.out.println("-----------------------------------------------");
        /**
         *  2. takeUntil(Predicate<? super T> stopPredicate)
         *  每次发射数据后，通过一个谓词函数stopPredicate来判定是否需要终止发射数据
         *  如果stopPredicate返回true怎表示停止发射后面的数据，否则继续发射后面的数据
         */
        observable.takeUntil(new Predicate<Long>() {
            @Override
            public boolean test(Long aLong) throws Exception {  // 函数返回false则为继续发射原始数据，true则停止发射原始数据
                if(aLong > 5){
                    return true;      // 满足条件后，停止发射数据
                }
                return false;         // 继续发射数据
            }
        }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(2)");
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("--> onNext(2): " + aLong);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(2): " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onComplete(2)");
            }
        });

        System.in.read();
    }
}
