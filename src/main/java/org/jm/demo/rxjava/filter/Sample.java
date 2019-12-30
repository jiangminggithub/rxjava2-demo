package org.jm.demo.rxjava.filter;

import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jiangming
 * <p>
 * Sample: 定期发射Observable最近发射的数据项
 */
public class Sample {

	public static void main(String[] args) throws Exception {
		// 1. sample(long period, TimeUnit unit)/sample(long period, TimeUnit unit)
		// 将序列分为 period 的时间片段，从每片重取出最近的一个数据
		// 等同于throttleLast
		Observable.intervalRange(1, 5, 0, 1100, TimeUnit.MILLISECONDS)
			.doOnNext(new Consumer<Long>() {
	
				@Override
				public void accept(Long t) throws Exception {
					System.out.println("--> DataSource onNext: " + t);
				}
			}).sample(2, TimeUnit.SECONDS) 								// 每3秒时间段数据中取最近一个值
		//    .sample(2, TimeUnit.SECONDS, true) 						// 参数emitLast，设置是否忽略未采样的最后一个数据
		//	  .sample(2, TimeUnit.SECONDS, Schedulers.newThread())		// 指定调度器为newThread()
			  .subscribe(new Consumer<Long>() {
		
					@Override
					public void accept(Long t) throws Exception {
						System.out.println("--> accept(1): " + t);
					}
			  });
		
		// 2. sample(ObservableSource sampler)
		// 每当第二个 sampler 发射一个数据（或者当它终止）时就对原始 Observable进行采样
		Observable.intervalRange(1, 5, 0, 1020, TimeUnit.MILLISECONDS)
			.doOnNext(new Consumer<Long>() {

					@Override
					public void accept(Long t) throws Exception {
						System.out.println("--> DataSource onNext: " + t);
					}
			}).sample(Observable.interval(2, TimeUnit.SECONDS))	// 每隔2秒进行一次采样
			  .subscribe(new Consumer<Long>() {
	
					@Override
					public void accept(Long t) throws Exception {
						System.out.println("--> accept(2): " + t);
					}
			  });
		
		System.in.read();
	}

}
