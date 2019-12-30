package org.jm.demo.rxjava.base;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * Observable: 被观察者，用于向观察者对象发送数据或者事件
 */
public class ObserverableBase {

    public static void main(String[] args) {
        // 创建Observable(被观察者)
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {

            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("Hello");
                //	emitter.onError(new NullPointerException());
                //	emitter.tryOnError(new Throwable());
                emitter.onComplete();
            }
        });

        // 创建Observer(观察者), 可以接受Observable所有通知
        Observer<String> observer = new Observer<String>() {

            public void onSubscribe(Disposable d) {
                System.out.println("--> onSubscribe");
            }

            public void onNext(String t) {
                System.out.println("--> onNext = " + t);
            }

            public void onError(Throwable e) {
                System.out.println("--> onError");
            }

            public void onComplete() {
                System.out.println("--> onComplete");
            }
        };

        // 创建只接受 onNext(item) 通知的Consumer(观察者)
        Consumer<String> nextConsumer = new Consumer<String>() {

            @Override
            public void accept(String t) throws Exception {
                System.out.println("--> accept nextConsumer: " + t);
            }
        };

        // 创建只接受 onError(Throwable) 通知的Consumer(观察者)
        Consumer<Throwable> errorConsumer = new Consumer<Throwable>() {

            @Override
            public void accept(Throwable t) throws Exception {
                System.out.println("-- accept errorConsumer: " + t);
            }
        };

        // 创建只接受 onComplete() 通知的Action(观察者)
        Action completedAction = new Action() {

            @Override
            public void run() throws Exception {
                System.out.println("--> run completedAction");
            }
        };

        // 创建只接受 onSubscribe 通知的Consumer(观察者)
        Consumer<Disposable> onSubscribeComsumer = new Consumer<Disposable>() {

            @Override
            public void accept(Disposable t) throws Exception {
                System.out.println("--> accept onSubscribeComsumer ");
            }
        };

        // 1. 进行订阅，subscribe(Observer)
        observable.subscribe(observer);

        System.out.println("---------------------------------------------");
        // 2. 进行订阅，subscribe(Consumer onNext)
        observable.subscribe(nextConsumer);

        System.out.println("---------------------------------------------");
        // 3. 进行订阅，subscribe(Consumer onNext, Consumer onError)
        observable.subscribe(nextConsumer, errorConsumer);

        System.out.println("---------------------------------------------");
        // 4. 进行订阅，subscribe(Consumer onNext, Consumer onError, Action onCompleted)
        observable.subscribe(nextConsumer, errorConsumer, completedAction);

        System.out.println("---------------------------------------------");
        // 5. 进行订阅，subscribe(Consumer onNext, Consumer onError, Action onCompleted,
        // Consumer onSubscribe)
        observable.subscribe(nextConsumer, errorConsumer, completedAction, onSubscribeComsumer);

    }

}
