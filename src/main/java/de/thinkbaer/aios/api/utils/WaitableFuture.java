package de.thinkbaer.aios.api.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class WaitableFuture<T> extends FutureTask<T> {
	private CountDownLatch countDownLatch = new CountDownLatch(1);

	public WaitableFuture(Callable<T> runner) {
		super(runner);
		this.countDownLatch = new CountDownLatch(1);
	}

	public WaitableFuture(Runnable runner, T result) {
		super(runner, result);
		this.countDownLatch = new CountDownLatch(1);
	}

	public void run() {
		super.run();		
		this.countDownLatch.countDown();
	}
	
	public void await(long count, TimeUnit unit){
		try {
			this.countDownLatch.await(count, unit);
		} catch (InterruptedException e) {
		}
	}
}