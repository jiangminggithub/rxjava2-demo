package org.jm.demo.rxjava.condition;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangming
 *
 * TakeWhile: 发射原始Observable的数据，直到一个特定的条件，然后跳过剩余的数据
 */
public class TakeWhile {

    public static void main(String[] args) throws Exception {
        // 创建Observable，发送数字1～10，每间隔200毫秒发射一个数据
        Observable<Long> observable = Observable.intervalRange(1, 10, 0, 200, TimeUnit.MILLISECONDS);

        /**
         *  takeWhile(Predicate predicate)
         *  发射原始Observable的数据，直到predicate的条件为false，然后跳过剩余的数据
         */
        observable.takeWhile(new Predicate<Long>() {
            @Override
            public boolean test(Long aLong) throws Exception {  // 函数返回值决定是否继续发射后续的数据
                if(aLong > 5){
                    return false;        // 满足条件后跳过后面的数据
                }
                return true;             // 继续发射数据
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
