package de.mpg.molgen.buczek.g2048;

import java.util.Vector;

public class ThreadScheduler {

	
	final static int MAX_THREADS=10;
	
	
	static class MyThread extends Thread {
		GameTree gameTree;
		int maxDepth;
		MyThread(GameTree gameTree,int maxDepth) {
			this.gameTree=gameTree;
			this.maxDepth=maxDepth;
		}
		public void run() {
			// System.out.println(Thread.currentThread()+" : running gameTree");
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
	
	public static void run (GameTree gameTree, int maxDepth) {
		
		Boolean useThread;
		synchronized(availableThreads) {
				if (availableThreads>0) {
					availableThreads--;
					useThread=true;
				} else {
					useThread=false;
				}
		}
		if (useThread) {
			Thread thread=new MyThread(gameTree,maxDepth);
			threadLocalData.get().threads.add(thread);
			// System.out.println("Start thread "+thread);
			thread.start();			
		} else {
			// System.out.println(Thread.currentThread()+" : no free threads / running gameTree myself");
			gameTree.run(maxDepth);
		}
	}	
	
	
	public static void waitAll() {
		Vector<Thread> threads=threadLocalData.get().threads;
		while(threads.size()>0) {
				try {
					Thread thread=threads.remove(0);
					// System.out.println(Thread.currentThread()+" : join thread "+thread);
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
