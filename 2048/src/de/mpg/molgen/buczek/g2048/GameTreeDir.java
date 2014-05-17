package de.mpg.molgen.buczek.g2048;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameTreeDir extends GameTree {

	public final int PRUNE_VALUE=9;

	final static int MAX_THREADS=10;
	
	static ExecutorService executorService=Executors.newFixedThreadPool(MAX_THREADS);
	static class MyRunnable implements Runnable {
		GameTree gameTree;
		int maxDepth;
		MyRunnable(GameTree gameTree,int maxDepth) {
			this.gameTree=gameTree;
			this.maxDepth=maxDepth;
		}
		public void run() {
			gameTree.run(maxDepth);
		}
	}

	int best_child=0;

	public GameTreeDir() {}

	public GameTreeDir(Board board) {
		this.board=new Board (board);
	}


	
	private static double computeBonus(Board board) {
		int maxValue=0;
		boolean maxOnEdge=false;
		boolean maxInCorner=false;
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++) {
				int value=board.get(i,j);
				if (value>maxValue) {
					maxValue=value;
					maxOnEdge= (i==0||i==3||j==0||j==3);
					maxInCorner= (i==0||i==3) && (j==0||j==3);
				}
			}

		return maxInCorner ? 0.75 : maxOnEdge ? 0.25 : 0;
	}

	
	int getBestDirection() {		


		for (int i=0;i<4;i++)
			if (children[i]!=null) {
				double bonus=computeBonus(children[i].board);
				System.out.printf(" %-5s : %16.14f bonus %5.3f\n",Board.D_NAMES[i],children[i].value,bonus);
				children[i].value+=bonus;
			}
		value=computeValueFromChildren();		
		System.out.println();
		return best_child;
	}


	public void init_children() {
		children=new GameTree[4];
		for (int i=0;i<4;i++) {
			Board childBoard=new Board(board);
			if (childBoard.move(i)) {
				GameTreeSet child=new GameTreeSet();
				child.board=childBoard;
				child.value=childBoard.getFreeCellCount();
				children[i]=child;							
			}
		}
	}

	

	public double computeValueFromChildren() {

		double max_value=0;
		for (int i=0;i<children.length;i++) {
			if (children[i]!=null) {
				if (children[i].value>max_value) {
					best_child=i;
					max_value=children[i].value;
				}
			}
		}
		return max_value;
	}


	public void run_purge(int maxDepth) {
		run(maxDepth);
		children=null;
	}
		
	
	
	
	
	
	public void run (int maxDepth) {

		init_children();

		int max_free_count=0;
		for (int d=0;d<4;d++) {
			if (children[d]!=null) {
				double childValue=children[d].board.getFreeCellCount();
				if (childValue>value) {
					value=childValue;
					best_child=d;
					max_free_count=1;
				} else if (childValue==value) {
					max_free_count++;
				} else {
					// children[d]=null;
				}				
			}
		}

		if (max_free_count<2 || maxDepth<=0 || value>=PRUNE_VALUE) {
			return;
		}

		if (maxDepth>1) {		
			for (int i=0;i<children.length;i++) {
				if (children[i]!=null) {
					children[i].run(maxDepth-1);
				}
			}
		} else {

			Future<?>[] futures=new Future<?>[children.length];

			for (int i=0;i<children.length;i++) {
				if (children[i]!=null) {
					futures[i]=executorService.submit(new MyRunnable(children[i],maxDepth-1));
				}
			}
			for (int i=0;i<children.length;i++) {
				if (children[i]!=null) {
					try {
						futures[i].get();
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
			}

			value=computeValueFromChildren();			
		}

	}

}
