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
 * First：只发射第一项（或者满足某个条件的第一项）数据
 */
public class Frist {

	public static void main(String[] args) {
		// 1. firstElement()
		// 只发射第一个数据
		Observable.range(1, 10)
			.firstElement()
			.subscribe(new Consumer<Integer>() {

				@Override
				public void accept(Integer t) throws Exception {
					System.out.println("--> accept firstElement(1): "  + t);
				}
			});

		System.out.println("----------------------------------");
		// 2. first(Integer defaultItem)
		// 发射第一个数据项，如果没有数据项，发送默认的defaultItem
		Observable.create(new ObservableOnSubscribe<Integer>() {

			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				emitter.onComplete();
			}
		}).first(999) // 没有数据发送时，发送默认值999
		  .subscribe(new Consumer<Integer>() {

			  @Override
			  public void accept(Integer t) throws Exception {
				  System.out.println("--> accept first(2): " + t);
			  }
		  });

		System.out.println("----------------------------------");
		// 3. firstOrError()
		// 发射第一个数据项,如果没有数据项,会有Error: NoSuchElementException
		Observable.create(new ObservableOnSubscribe<Integer>() {

			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				emitter.onComplete();
			}
		}).firstOrError() // 没有数据发送时，将会发送NoSuchElementException通知
		  .subscribe(new SingleObserver<Integer>() {

				@Override
				public void onSubscribe(Disposable d) {
					System.out.println("--> onSubscribe: ");
				}

				@Override
				public void onSuccess(Integer t) {
					System.out.println("--> accept onSuccess(3): " + t);
				}

				@Override
				public void onError(Throwable e) {
					System.out.println("--> acctpt onError(3): " + e);
				}
		  });

	}
}
