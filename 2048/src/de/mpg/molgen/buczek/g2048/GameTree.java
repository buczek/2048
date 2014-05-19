package de.mpg.molgen.buczek.g2048;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public abstract class GameTree {

	Board				board;
	double				value;
	GameTree			children[];
	int 				depth;			// remaining depth
	
	public abstract void    init_children();
	public abstract double  computeValueFromChildren();

	public double getValue() {
		return value;
	}
		
	abstract protected void _run(int maxDepth);		// called from Pool Thread

	static ForkJoinPool forkJoinPool=new ForkJoinPool();

	public void run (final int maxDepth) {					// called from User (Main) Thread
		forkJoinPool.invoke(new RecursiveAction () {
			private static final long serialVersionUID = 1L;
			protected void compute() {
				_run(maxDepth);
			}		
		});
	}
	
	protected void run_purge(int maxDepth) {		// called from Pool Thread - _run and release children when done
		_run(maxDepth);
		children=null;
	}

	
}
