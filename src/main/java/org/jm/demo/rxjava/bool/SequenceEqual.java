package org.jm.demo.rxjava.bool;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;

/**
 * @author jiangming
 * <p>
 * SequenceEqual: 判断两个Observables发射的序列是否相等
 */
public class SequenceEqual {

    public static void main(String[] args) {
        // 创建Observable
        Observable<Integer> observable1 = Observable.range(1, 10);
        Observable<Integer> observable2 = Observable.range(1, 10);

        /**
         *  1. sequenceEqual(ObservableSource source1, ObservableSource source2)
         *  比较两个Observable的数据项是否完全相同（相同数据，顺序，相同终止状态），相同则发射true，否则发射false
         */
        Observable.sequenceEqual(observable1, observable2)
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(1)");
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        System.out.println("--> onSuccess(1): " + aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(1): " + e);
                    }
                });

        System.out.println("----------------------------------------");
        /**
         *  2. sequenceEqual(ObservableSource source1, ObservableSource source2, BiPredicate isEqual, int bufferSize)
         *  isEqual:            // 可选参数，定义两个Observable的数据项比较规则
         *  bufferSize：        //  从第一个和第二个源ObservableSource预取的项数
         *  通过指定的比较函数isEqual比较两个Observable的数据项是否完全相同（相同数据，顺序，相同终止状态），
         *  相同则发射true，否则发射false。还可以通过bufferSize指定一个缓存大小。
         */
        Observable.sequenceEqual(observable1, observable2, new BiPredicate<Integer, Integer>() {
            @Override
            public boolean test(Integer t1, Integer t2) throws Exception {
                System.out.println("--> test(2): t1 = " + t1 + ", t2 = " + t2);
                return t1 == t2;    // 比较两个Observable的数据序列数据是否相等
            }
        }, 3).subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(2)");
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                System.out.println("--> onSuccess(2): " + aBoolean);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(2): " + e);
            }
        });

    }
}
