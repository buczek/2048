package de.mpg.molgen.buczek.g2048;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadScheduler {

	final static int MAX_THREADS=2;

	static ExecutorService executorService=Executors.newFixedThreadPool(MAX_THREADS);
			
		
	static class MyRunnable implements Runnable {
		GameTree gameTree;
		int maxDepth;
		MyRunnable(GameTree gameTree,int maxDepth) {
			this.gameTree=gameTree;
			this.maxDepth=maxDepth;
		}
		public void run() {
			System.out.println(Thread.currentThread()+" : running gameTree");
			gameTree.run(maxDepth);
		}
	}
	
	static class LocalData {
		Vector<Thread>	threads=new Vector<Thread>();
	}
	
	static final ThreadLocal<LocalData> threadLocalData=new ThreadLocal<LocalData>() {
		protected LocalData initialValue() {
			return new LocalData();
		}
	};
	
	
	static Integer availableThreads=MAX_THREADS;
	
	public static Future<?> run (GameTree gameTree, int maxDepth) {
		
		return executorService.submit(new MyRunnable(gameTree,maxDepth));
	}
	
	public static void waitAll() {
		Vector<Thread> threads=threadLocalData.get().threads;
		while(threads.size()>0) {
				try {
					Thread thread=threads.remove(0);
					System.out.println(Thread.currentThread()+" : join thread "+thread);
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
				synchronized(availableThreads) {
					availableThreads++;
				}
		}
				
	}
}
