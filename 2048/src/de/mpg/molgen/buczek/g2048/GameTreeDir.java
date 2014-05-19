package de.mpg.molgen.buczek.g2048;

import java.util.concurrent.ForkJoinTask;

public class GameTreeDir extends GameTree {

	private static final long serialVersionUID = 1L;

	int best_child=0;
	int	childrenDirections[];
		
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
		if (children.length==0)	// no moves left
			return 0;
		for (int i=0;i<children.length;i++) {
			double bonus=computeBonus(children[i].board);
			System.out.printf(" %-5s : %16.14f bonus %5.3f\n",Board.D_NAMES[childrenDirections[i]],children[i].value,bonus);
			children[i].value+=bonus;
		}
		System.out.println();
		return childrenDirections[best_child];
	}


	public void init_children() {
		Board childBoards[] = new Board[4];
		int childrenCount=0;
		for (int d=0;d<4;d++) {
			Board childBoard=new Board(board);
			if (childBoard.move(d)) {
					childrenCount++;
					childBoards[d]=childBoard;
			}
		}
		children=new GameTreeSet[childrenCount];
		childrenDirections=new int[childrenCount];
		childrenCount=0;
		for (int d=0;d<4;d++) {
			if (childBoards[d]!=null) {
				GameTreeSet child=new GameTreeSet();
				child.depth=depth-1;
				child.board=childBoards[d];
				child.value=childBoards[d].getFreeCellCount();
				children[childrenCount]=child;
				childrenDirections[childrenCount]=d;
				childrenCount++;
			}
		}				
	}

	

	private void computeValueFromChildren() {			// set value and best_child

		value=0;
		for (int i=0;i<children.length;i++) {
			if (children[i].value>value) {
				best_child=i;
				value=children[i].value;
			}
		}
	}
	
	protected void _run () {

		init_children();
		// System.out.println("GameTreeSet.run at level "+maxDepth);

		computeValueFromChildren();

		if (children.length<2 || depth<=0 || value>=Sim.PRUNE_VALUE) {
			return;
		}

		if (depth>3 ) {
			ForkJoinTask.invokeAll(children);			
		} else {
			for (int i=0;i<children.length;i++) {
					children[i].run_purge();
			}			
		}			
		computeValueFromChildren();			
	}
}
