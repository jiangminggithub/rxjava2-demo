package org.jm.demo.rxjava.base;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.BiConsumer;

/**
 * @author jiangming
 * <p>
 * Single: 只发送 onSuccess or onError 通知,并且只会发送一次, 第一次发送数据后的都不会在处理
 */
public class SingleBase {

    public static void main(String[] args) {
        // Single: 只发送 onSuccess or onError 通知,并且只会发送一次, 第一次发送数据后的都不会在处理
        Single.create(new SingleOnSubscribe<String>() {

            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {
                emitter.onSuccess("Success");        // 发送success通知
                emitter.onSuccess("Success2");        // 只能发送一次通知,后续不在处理
            }
        }).subscribe(new BiConsumer<String, Throwable>() {

            @Override
            public void accept(String t1, Throwable t2) throws Exception {
                System.out.println("--> accept: t1 = " + t1 + ",  t2 = " + t2);
            }
        });
    }

}
