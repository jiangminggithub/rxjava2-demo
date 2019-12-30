package org.jm.demo.rxjava.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.AsyncSubject;

/**
 * @author jiangming
 * <p>
 * AsyncSubject: 仅释放接收到的最后一个数据
 */
public class AsyncSubjectBase {

    public static void main(String[] args) {
        // 注意: 不要使用just(T)、from(T)、create(T)来使用Subject，因为会把Subject转换为Obserable
        // 无论订阅的时候AsyncSubject是否Completed，均可以收到最后一个值的回调
        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("emitter 1");
        asyncSubject.onNext("emitter 2");
        asyncSubject.onNext("emitter 3");
        asyncSubject.onNext("emitter 4");
        asyncSubject.onNext("emitter 5"); // 此时订阅后将近发送此项数据
        // asyncSubject.onNext(1/0 + ""); // 发生error时将不会有数据发射，仅发送error通知
        asyncSubject.onComplete();

        // 订阅后只会接收最后一个数据
        asyncSubject.subscribe(new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe");
            }

            @Override
            public void onNext(String t) {
                System.out.println("--> onNext = " + t);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--> onError = " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("--> onComplete");
            }
        });
    }

}
