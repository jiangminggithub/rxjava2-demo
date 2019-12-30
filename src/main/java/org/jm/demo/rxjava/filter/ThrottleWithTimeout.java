package org.jm.demo.rxjava.filter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * ThrottleWithTimeout: 过滤发射速率较快的数据项，防抖操作
 * <p>
 * 与Debounce相同
 */
public class ThrottleWithTimeout {
	
	public static void main(String[] args) throws Exception {
		// 1. throttleWithTimeout(long timeout, TimeUnit unit)
		// 发送一个数据，如果在包含timeout时间内，没有第二个数据发射，那么就会发射此数据，否则丢弃此数据
		Observable.create(new ObservableOnSubscribe<Integer>() {

			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				// TODO Auto-generated method stub
				emitter.onNext(1);	// 下一个数据到此数据发射,	--> skip: 		30 < timeout
				Thread.sleep(30);
				emitter.onNext(2);	// 下一个数据到此数据发射,	--> skip: 		50 = timeout
				Thread.sleep(50);
				emitter.onNext(3);	// 下一个数据到此数据发射,	--> deliver: 	60 > timeout
				Thread.sleep(60);
				emitter.onNext(4);	// onComplete			--> deliver:	onComplete
				emitter.onComplete();
			}
		}).throttleWithTimeout(50, TimeUnit.MILLISECONDS) // 指定防抖丢弃时间段为50毫秒
	 //   .throttleWithTimeout(50, TimeUnit.MILLISECONDS, Schedulers.newThread()) // 指定调度线程为newThread()
		  .subscribe(new Consumer<Integer>() {

				@Override
				public void accept(Integer t) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("--> accept throttleWithTimeout(1): " + t);
				}
			});

		System.in.read();
		
	}

}
