package org.jm.demo.rxjava.base;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * @author jiangming
 * <p>
 * SerializedSubject: 串行话处理，保证线程安全
 */
public class SerializedSubjectBase {

    public static void main(String[] args) throws Exception {

        // 创建Subject
        ReplaySubject<String> subject = ReplaySubject.create();

        // 通过toSerialized()进行串行化
        Subject<String> serialized = subject.toSerialized();

        // 订阅
        serialized.subscribe(new Consumer<String>() {

            @Override
            public void accept(String t) throws Exception {
                System.out.println("--> accept: " + t + ", ReceiverThreadID: " + Thread.currentThread().getId());
            }
        });

        // 多线程执行
        for (int i = 0; i < 10; i++) {
            final int value = i + 1;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    serialized.onNext(value + "-SendThreadID: " + Thread.currentThread().getId());
                }
            }).start();
        }

        System.in.read();
    }

}
