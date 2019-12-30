package org.jm.demo.rxjava.base;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * @author jiangming
 * <p>
 * Maybe: 只能发射 0 或者 1 项数据，即使后续有多个数据，后面的数据也不会被处理
 */
public class MaybeBase {

    public static void main(String[] args) {
        // Maybe 只发送0个或者1个数据，后续数据将被忽略
        Maybe.create(new MaybeOnSubscribe<String>() {

            @Override
            public void subscribe(MaybeEmitter<String> emitter) throws Exception {
                // 如果先发送了，将会调用MaybeObserver的onCompleted方法，如果有数据发送或者调用onError，则不会去调用
                // emitter.onComplete();
                emitter.onSuccess("Hello"); // 如果发送了第一个数据后续数据将不会被处理
                emitter.onSuccess("World");
            }
        }).subscribe(new MaybeObserver<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe");
            }

            @Override
            public void onSuccess(String t) {
                System.out.println("--> onSuccess: " + t);
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
    }

}
