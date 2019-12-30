package org.jm.demo.rxjava.filter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 
 * @author jiangming
 * 
 * 	throttleFirst: 指定时间内取第一项数据, 只会执行一次
 *	throttleLast:  指定间隔时间内取最后一项数据，只要数据源发送数据没有结束，将一直间隔执行
 */
public class Throttle {

	public static void main(String[] args) throws Exception {
		// 1. throttleFirst(long windowDuration, TimeUnit unit)
		// 指定每个指定时间内取第一项数据, 直到原始数据序列全部发送结束
		Observable.intervalRange(1, 10, 0, 1, TimeUnit.SECONDS)
			.doOnNext(new Consumer<Long>() {
	
				@Override
				public void accept(Long t) throws Exception {
					System.out.println("--> DataSource doOnNext : " + t);
				}
			}).throttleFirst(2, TimeUnit.SECONDS)			// 获取每隔2秒之内收集的第一项数据
		 //   .throttleFirst(2, TimeUnit.SECONDS, Schedulers.newThread())	// 指定调度线程为newThread()
			  .subscribe(new Observer<Long>() {

					@Override
					public void onSubscribe(Disposable d) {
						System.out.println("--> throttleFirst onSubscribe");
					}

					@Override
					public void onNext(Long t) {
						System.out.println("-------------> throttleFirst onNext: " + t);
					}

					@Override
					public void onError(Throwable e) {
						System.out.println("--> throttleFirst onError: " + e);
					}

					@Override
					public void onComplete() {
						System.out.println("--> throttleFirst onComplete");
					}
				});
		
		System.out.println("------------------------------------");
		// 2. throttleLast(long intervalDuration, TimeUnit unit)
		// 指定间隔时间内取最后一项数据，直到原始数据序列全部发送结束
		Observable.intervalRange(1, 10, 0, 1050, TimeUnit.MILLISECONDS)
			.doOnNext(new Consumer<Long>() {

				@Override
				public void accept(Long t) throws Exception {
					System.out.println("--> DataSource doOnNext : " + t);
				}
			}).throttleLast(2, TimeUnit.SECONDS)				// 获取每隔2秒之内收集的最后一项数据
	     //   .throttleLast(2, TimeUnit.SECONDS, Schedulers.newThread())	// 指定调度线程为newThread()
			  .subscribe(new Observer<Long>() {

					@Override
					public void onSubscribe(Disposable d) {
						System.out.println("--> throttleLast onSubscribe");
					}

					@Override
					public void onNext(Long t) {
						System.out.println("-------------> throttleLast onNext: " + t);
					}

					@Override
					public void onError(Throwable e) {
						System.out.println("--> throttleLast onError: " + e);
					}

					@Override
					public void onComplete() {
						System.out.println("--> throttleLast onComplete");
					}
				});

		System.in.read();
	}

}
