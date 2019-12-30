package org.jm.demo.rxjava.filter;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author jiangming
 * <p>
 * Last: 最后一项数据，或者满足某个条件的最后一项数据
 */
public class Last {

	public static void main(String[] args) {
		// 1. lastElement()
		// 接受最后一项数据
		Observable.create(new ObservableOnSubscribe<Integer>() {

			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				emitter.onNext(1);
				emitter.onNext(2);
				emitter.onNext(3);
				emitter.onComplete();
			}
		}).lastElement() // 存在数据发送的话，即发射最后一项数据，否则没有数据发射
		  .subscribe(new Consumer<Integer>() {

				@Override
				public void accept(Integer t) throws Exception {
					System.out.println("--> accept lastElement(1): " + t);
				}
			});

		System.out.println("--------------------------------");
		// 2. last(Integer defaultItem)
		// 接受最后一项数据,如果没有数据发送，发送默认数据：defaultItem
		Observable.range(0, 0)
			.last(999) // 接受最后一项数据,没有数据则发送默认数据999
			.subscribe(new Consumer<Integer>() {

				@Override
				public void accept(Integer t) throws Exception {
					System.out.println("--> accept: last(2): " + t);
				}
			});


		System.out.println("--------------------------------");
		// 3. lastOrError()
		// 接受最后一项数据,如果没有数据发送，抛出onError: NoSuchElementException
		Observable.range(0, 0)
			.lastOrError() // 接受最后一项数据,如果没有数据，则反射NoSuchElementException异常通知
			.subscribe(new SingleObserver<Integer>() {

				@Override
				public void onSubscribe(Disposable d) {
					System.out.println("--> onSubscribe: ");
				}

				@Override
				public void onSuccess(Integer t) {
					System.out.println("--> onSuccess(3)");
				}

				@Override
				public void onError(Throwable e) {
					System.out.println("--> onError(3): " + e);
				}
			});

	}

}
