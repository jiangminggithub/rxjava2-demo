package org.jm.demo.rxjava.combined;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangming
 * <p>
 * StartWith: 在数据序列的开头插入一条指定的项或者数据序列
 */
public class StartWith {

    public static void main(String[] args) {

        // 创建列表List
        List<Integer> lists = new ArrayList<>();
        lists.add(999);
        lists.add(9999);
        lists.add(99999);

        // 创建数组Array
        Integer[] arrays = new Integer[3];
        arrays[0] = 999;
        arrays[1] = 9999;
        arrays[2] = 9999;

        // 1. startWith(item)
        // 在Observable数据发射前发射item数据项
        Observable.just(1, 2, 3)
                .startWith(999)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(1): " + integer);
                    }
                });

        System.out.println("-----------------------------------------");
        // 2. startWith(Iterable items)
        // 在Observable数据发射前发射items列表中的数据序列
        Observable.just(1, 2, 3)
                .startWith(lists)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(2): " + integer);
                    }
                });

        System.out.println("-----------------------------------------");
        // 3. startWithArray(items)
        // 在Observable数据发射前发射items数组中的数据序列
        Observable.just(1, 2, 3)
                .startWithArray(arrays)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(3): " + integer);
                    }
                });

        System.out.println("-----------------------------------------");
        // 4. startWith(ObservableSource other)
        // 在Observable数据发射前发射other中的数据序列
        Observable.just(1, 2, 3)
                .startWith(Observable.just(999, 9999, 99999))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("--> accept(4): " + integer);
                    }
                });

    }
}
