package de.thinkbaer.aios.api.utils;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class BlockingQueueWorker<X> implements Runnable {
	private static final Logger L = LogManager.getLogger(BlockingQueueWorker.class);
	
	private BlockingQueue<X> inputQueue;
	
	private volatile boolean isRunning = true;
	private final X POISON_PILL;
	private Consumer<X> consumer;
	
	public BlockingQueueWorker(BlockingQueue<X> inputQueue, Consumer<X> consumer, X poison) {
	    this.inputQueue = inputQueue;
	    this.consumer = consumer;
	    this.POISON_PILL = poison;
	    Objects.requireNonNull(this.consumer);
	}
	
	@Override
	public void run() {
	    while(isRunning) {
	        try {
	            X queueElement = inputQueue.take();
	            //L.debug("QUEUE: " + inputQueue.size());
	            if(queueElement == POISON_PILL) {
	            	break;
	            }
	            consumer.accept(queueElement);
	        } catch (InterruptedException e) {
	        	L.error("", e);	        	
	        } catch (Exception e) {
	        	L.error("", e);
	        }
	    }
	    isRunning = false;
	}

	public void finish() {
	    isRunning = false;
	    inputQueue.add(POISON_PILL);
	}
	
	public void finishAndWait() {
		finish();
		while(isRunning){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
	}
	
}
