package de.mpg.molgen.buczek.g2048;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public abstract class GameTree extends RecursiveAction {

	protected Board				board;
	protected double			value;
	protected GameTree			children[];
	protected int 				depth;			// remaining depth
	
	public abstract void    init_children();
	public abstract double  computeValueFromChildren();

	public double getValue() {
		return value;
	}
		
	abstract protected void _run();		// called from Pool Thread

	static ForkJoinPool forkJoinPool=new ForkJoinPool();

	public void run (final int maxDepth) {					// called from User (Main) Thread
		depth=maxDepth;
		forkJoinPool.invoke(new RecursiveAction () {
			private static final long serialVersionUID = 1L;
			protected void compute() {
				_run();
			}		
		});
	}
	
	protected void run_purge() {		// called from Pool Thread - _run and release children when done
		_run();
		children=null;
	}

	protected void compute() {
		run_purge();
	}

	
}
