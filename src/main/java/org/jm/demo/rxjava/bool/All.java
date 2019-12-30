package org.jm.demo.rxjava.bool;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * @author jiangming
 * <p>
 * All:传递一个谓词函数给 All 操作符，这个函数接受原始Observable发射的数据，根据计算返回一个布尔值。
 * All返回一个只发射一个单个布尔值的Observable，如果原始Observable正常终止并且每一项数据都满足条件，就返回true；
 * 如果原始Observable的任何一项数据不满足条件就返回false。
 */
public class All {

    public static void main(String[] args) {
        /**
         *  all(Predicate predicate)
         *  通过传入的谓语函数predicate进行判断所有数据项是否满足条件，然后返回一个判断结果发射给观察者
         */
        Observable.just(1, 2, 3, 4, 5)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 5; // 判断原始数据项中的所有数据项是否大于5
                    }
                })
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
