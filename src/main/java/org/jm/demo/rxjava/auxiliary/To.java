package org.jm.demo.rxjava.auxiliary;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author jiangming
 * <p>
 * To: 将Observable转换为另一个对象或数据结构。
 */
public class To {

    public static void main(String[] args) {

        Observable<Integer> range = Observable.range(1, 5);

        /**
         *  1. toList()
         *  让Observable将多项数据组合成一个List，然后调用一次onNext方法传递整个列表。
         */
        range.toList()
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        System.out.println("--> toList accept(1): " + integers);
                    }
                });

        System.out.println("------------------------------------------");
        /**
         *  2. toMap(Function<? super T, ? extends K> keySelector,Function<? super T, ? extends V> valueSelector)
         *   toMap收集原始Observable发射的所有数据项到一个Map（默认是HashMap）然后发射这个Map。
         *   你可以提供一个用于生成Map的Key的函数，还可以提供一个函数转换数据项到Map存储的值（默认数据项本身就是值）。
         */
        range.toMap(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "key" + integer;     // 返回一个Map的key
            }
        }, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer;             // 返回一个Map的value
            }
        }).subscribe(new SingleObserver<Map<String, Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(2)");
            }

            @Override
            public void onSuccess(Map<String, Integer> stringIntegerMap) {
                System.out.println("--> onSuccess(2): " + stringIntegerMap);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(2): " + e);
            }
        });

        System.out.println("------------------------------------------");
        /**
         *  3. toSortedList()
         *  它会对产生的列表排序，默认是自然升序，如果发射的数据项没有实现Comparable接口，会抛出一个异常。
         *  然而，你也可以传递一个函数作为用于比较两个数据项
         */
        Observable.just(5, 3, 8, 6, 9, 10)
                .toSortedList()
                .subscribe(new SingleObserver<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(3)");
                    }

                    @Override
                    public void onSuccess(List<Integer> integers) {
                        System.out.println("--> onSuccess(3): " + integers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(3): " + e);
                    }
                });

        System.out.println("------------------------------------------");
        /**
         *  4. toSortedList(Comparator comparator)
         *
         *  传递一个函数comparator作为用于比较两个数据项，它会对产生的列表排序
         */
        Observable.just(5, 3, 8, 6, 9, 10)
                .toSortedList(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        System.out.println("--> compare: o1 = " + o1 + ", o2 = " + o2);
                        return o1 - o2;     // 比较器的排序逻辑
                    }
                }).subscribe(new SingleObserver<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(4)");
                    }

                    @Override
                    public void onSuccess(List<Integer> integers) {
                        System.out.println("--> onSuccess(4): " + integers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(4): " + e);
                    }
                });

        System.out.println("------------------------------------------");
        /**
         *  5. toMultimap(Function<T, K> keySelector, Function<T, V> valueSelector)
         *  类似于 toMap ，不同的是，它生成的这个Map的value类型还是一个ArrayList
         */
        range.toMultimap(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "key" + integer;     // 返回一个Map的key
            }
        }, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer;             // 返回一个Map的value
            }
        }).subscribe(new SingleObserver<Map<String, Collection<Integer>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe(5)");
            }

            @Override
            public void onSuccess(Map<String, Collection<Integer>> stringCollectionMap) {
                System.out.println("--> onSuccess(5): " + stringCollectionMap);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError(5): " + e);
            }
        });

    }
}
