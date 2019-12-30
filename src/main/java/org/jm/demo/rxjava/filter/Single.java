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
 * Single: 与first类似，但是如果原始Observable在完成之前不是正好发射一次数据，它会抛出一个NoSuchElementException
 */
public class Single {

	public static void main(String[] args) {
		// 1.singleElement()
		// 发射单例数据，超过一个就会NoSuchElementException	
		Observable.create(new ObservableOnSubscribe<Integer>() {

			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				emitter.onNext(1);
				emitter.onNext(2);
				emitter.onComplete();
			}
		}).singleElement() // 发送单个数据，大于1项数据就会有Error通知
		  .subscribe(new Consumer<Integer>() {
	
				@Override
				public void accept(Integer t) throws Exception {
					System.out.println("--> accept singleElement(1): " + t);
				}
		  },new Consumer<Throwable>() {

			@Override
			public void accept(Throwable t) throws Exception {
				System.out.println("--> OnError(1): " + t);
			}
		});
		
		System.out.println("--------------------------------");
		// 2. single(Integer defaultItem)
		// 发射单例数据，没有数据项发送指定默认defaultItem
		Observable.create(new ObservableOnSubscribe<Integer>() {

			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				emitter.onComplete();
			}
		}).single(999) // 没有接受到数据则发送默认数据999
		  .subscribe(new Consumer<Integer>() {
	
				@Override
				public void accept(Integer t) throws Exception {
					System.out.println("--> accept single(2): " + t);
				}
		  });
		
		System.out.println("--------------------------------");
		// 3.singleOrError()
		// 发射一个单例的数据，如果数据源 没有数据项，则发送一个NoSuchElementException异常通知
		Observable.create(new ObservableOnSubscribe<Integer>() {

			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				emitter.onComplete();
			}
		}).singleOrError() // 如果没有数据项发送，则发送一个NoSuchElementException异常通知
		  .subscribe(new SingleObserver<Integer>() {

				@Override
				public void onSubscribe(Disposable d) {
					System.out.println("--> onSubscribe(3): ");
				}

				@Override
				public void onSuccess(Integer t) {
					System.out.println("--> onSuccess(3): " + t);
				}

				@Override
				public void onError(Throwable e) {
					System.out.println("--> onError(3): " + e);
				}
			});
	}

}
