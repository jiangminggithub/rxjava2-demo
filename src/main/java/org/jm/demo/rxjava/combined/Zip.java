package org.jm.demo.rxjava.combined;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangming
 * <p>
 * Zip:
 * 1. Zip 操作符与 Merge 的使用上基本一致，主要不同的是zip发射的数据取决于发射数据项最少的那个Observable并且按照严格的顺序去结合数据。
 * 2. 同样具备静态方法 zip 与对象方法zipWith，可以传递一个Observable列表List，数组，甚至是一个发射Observable序列的Observable。
 */
public class Zip {

    public static void main(String[] args) throws Exception {
        // 创建Observable
        Observable<Integer> observable1 = Observable.just(1, 2, 3);
        Observable<Integer> observable2 = Observable.just(1, 2, 3, 4, 5, 6);

        // 创建list
        List<Observable<Integer>> lists = new ArrayList<>();
        lists.add(observable1);
        lists.add(observable2);

        // 创建数组Array
        Observable<Integer>[] arrays = new Observable[2];
        arrays[0] = observable1;
        arrays[1] = observable2;

        // 创建发射Observable序列的Observable
        Observable<Observable<Integer>> sources = Observable.create(new ObservableOnSubscribe<Observable<Integer>>() {

            @Override
            public void subscribe(ObservableEmitter<Observable<Integer>> emitter) throws Exception {
                emitter.onNext(observable1);
                emitter.onNext(observable2);
                emitter.onComplete();
            }
        });

        // 1. zip（sources）
        // 可接受2-9个参数的Observable，对其进行顺序合并操作，最终合并的数据项取决于最少的数据项的Observable
        Observable.zip(observable1, observable2, new BiFunction<Integer, Integer, String>() {

            @Override
            public String apply(Integer t1, Integer t2) throws Exception {
                System.out.println("--> apply(1): t1 = " + t1 + ", t2 = " + t2);
                return t1 + t2 + "";
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                System.out.println("--> accept(1): " + s);  // 最终接受observable1全部数据项与observable2相同数量顺序部分数据
            }
        });

        System.out.println("------------------------------------------------------");
        // 2. zipIterable(Iterable<ObservableSource> sources, Function<Object[],R> zipper, boolean delayError, int bufferSize)
        // 可选参数, maxConcurrency: 最大的并发处理数, bufferSize: 缓存的数量（从每个内部观察资源预取的项数）
        // 接受一个Observable的列表List
        Observable.zip(lists, new Function<Object[], String>() {

            @Override
            public String apply(Object[] objects) throws Exception {
                Integer result = 0;
                for (Object object : objects) {
                    result = result + (Integer) object;
                }
                return String.valueOf(result);
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                System.out.println("--> accept(2): " + s);
            }
        });

        System.out.println("------------------------------------------------------");
        // 3. zipArray(Function<Object[],R> zipper, boolean delayError, int bufferSize, ObservableSource... sources)
        // delayError: 延迟Error通知, bufferSize: 缓存的数量（从每个内部观察资源预取的项数）
        // 接受一个Observable的数组Array
        Observable.zipArray(new Function<Object[], String>() {

            @Override
            public String apply(Object[] objects) throws Exception {
                Integer result = 0;
                for (Object object : objects) {
                    result = result + (Integer) object;
                }
                return String.valueOf(result);
            }
        }, true, 3, arrays)
                .subscribe(new Consumer<String>() {

                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("--> accept(3): " + s);
                    }
                });

        System.out.println("------------------------------------------------------");
        // 4. zip(ObservableSource<ObservableSource> sources, Function<Object[], R> zipper)
        // 接受一个发射Observable序列的Observable
        Observable.zip(sources, new Function<Object[], String>() {

            @Override
            public String apply(Object[] objects) throws Exception {
                Integer result = 0;
                for (Object object : objects) {
                    result = result + (Integer) object;
                }
                return String.valueOf(result);
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                System.out.println("--> accept(4)");
            }
        });

        System.out.println("------------------------------------------------------");
        // 5. zipWith
        // zip 是静态方法， zipWith 是对象方法: Observable.merge(observable1,observable2)
        // 等价于 observable1.mergeWith(observable2)
        observable1.zipWith(observable2, new BiFunction<Integer, Integer, String>() {

            @Override
            public String apply(Integer t1, Integer t2) throws Exception {
                String result = "";
                result = t1 + t2 + "";
                return result;
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                System.out.println("--> accept(5): " + s);
            }
        });

    }
}
