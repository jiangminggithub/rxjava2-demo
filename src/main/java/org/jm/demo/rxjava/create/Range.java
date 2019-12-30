package org.jm.demo.rxjava.create;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * Range 创建一个发射特定整数序列的Observable
 * <p>
 * 它接受两个参数，一个是范围的起始值，一个是范围的数据的数目。 如果你将第二个参数设为0，将导致Observable不发射任何数据（如果设置
 * 为负数，会抛异常）
 * <p>
 */
public class Range {

    public static void main(String[] args) {
        // 1. range(n,m) 发射从n开始的m个整数序列，序列区间[n,n+m-1)
        Observable.range(0, 5)
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("-- accept(range): " + t);
                    }
                });

        System.out.println("------------------------------");
        // 2. rangeLong(n,m) 发射从n开始的m个长整型序列，序列区间[n,n+m-1)
        Observable.rangeLong(1, 5)
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long t) throws Exception {
                        System.out.println("-- accept(rangeLong): " + t);
                    }
                });
    }

}
