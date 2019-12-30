package org.jm.demo.rxjava.convert;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * Cast：将原始Observable发射的每一项数据都强制转换为一个指定的类型，然后再发射数据，它是 map 的一个特殊版本。转换失败会有Error通知。
 * 一般用于 数据类型的转换 和 数据实际类型的检查（多态）。
 */
public class Cast {

    public static void main(String[] args) {
        //	cast(clazz)
        // 1. 基本类型转换
        Observable.range(1, 5)
                .cast(Integer.class)
                .subscribe(new Consumer<Integer>() {

                    @Override
                    public void accept(Integer t) throws Exception {
                        System.out.println("-- accept cast(1): " + t);
                    }
                });

        // 2. 转换失败通知
        System.out.println("------------------------------------");
        Observable.just((byte) 1)
                .cast(Integer.class)
                .subscribe(new Observer<Integer>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("--> onSubscribe(2)");
                    }

                    @Override
                    public void onNext(Integer t) {
                        System.out.println("--> onNext(2) = " + t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--> onError(2) = " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("--> onComplete(2)");
                    }
                });

        System.out.println("------------------------------------");
        class Animal {
            public int id;
        }

        class Dog extends Animal {
            public String name;

            @Override
            public String toString() {
                return "Dog [name=" + name + ", id=" + id + "]";
            }
        }

        //  3. 多态转换，检查数据的实际类型
        Animal animal = new Dog();
        animal.id = 666;
        Observable.just(animal)
                .cast(Dog.class)
                .subscribe(new Consumer<Dog>() {

                    @Override
                    public void accept(Dog t) throws Exception {
                        System.out.println("--> accept cast(3): " + t);
                    }
                });

    }
}


