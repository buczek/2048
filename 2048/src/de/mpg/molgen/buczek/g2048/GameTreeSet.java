package de.mpg.molgen.buczek.g2048;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class GameTreeSet extends GameTree {
	
	static class MyRecursiveAction extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		GameTreeDir gameTree;
		int maxDepth;
		MyRecursiveAction(GameTreeDir gameTree,int maxDepth) {
			this.gameTree=gameTree;
			this.maxDepth=maxDepth;
		}
		protected void compute() {
			gameTree.run_purge(maxDepth);
		}
	}
	
	
	public void init_children() {
		int variants=board.getFreeCellCount()*2;		
		
		children=new GameTree[variants];
		for (int i=0;i<variants;i++) {
			GameTreeDir child=new GameTreeDir(board);
			children[i]=child;			
		}
		
		int variant=0;
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++)
				if (board.get(i,j)==0) {
					children[variant].board.set(i,j,2);
					variant++;
				}

		for (int i=0;i<4;i++)
				for (int j=0;j<4;j++)
						if (board.get(i,j)==0) {
								children[variant].board.set(i,j,4);
									variant++;
						}

	}

	public double computeValueFromChildren() {

		double sum=0;

		int i;
		for (i=0;i<children.length/2;i++) {
			sum+=children[i].value*9;
		}

		for (;i<children.length;i++) {
			sum+=children[i].value;
		}

		return sum/(children.length/2)/10;
	}
	

	protected void _run (int maxDepth) {

		if (board.getFreeCellCount()==0) {
			value=0;
			return;
		}

		init_children();
		//System.out.println("GameTreeSet.run at level "+maxDepth);
		if (maxDepth>2) {
			RecursiveAction actions[]=new RecursiveAction[children.length];
			for (int i=0;i<children.length;i++) {
					actions[i]=new MyRecursiveAction ((GameTreeDir)children[i],maxDepth);					
			}
			ForkJoinTask.invokeAll(actions);
		} else {
			for (int c=0;c<children.length;c++) {
				GameTreeDir child=(GameTreeDir)children[c];
				child.run_purge(maxDepth);		
			}
		}
		value=computeValueFromChildren();			
	}	
}
