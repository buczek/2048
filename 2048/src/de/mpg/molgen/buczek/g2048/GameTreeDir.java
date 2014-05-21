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

			
	int getBestDirection() {		
		if (children.length==0)	// no moves left
			return 0;
		for (int c=0;c<children.length;c++) {
			//double bonus=computeBonus(children[i].board);
			System.out.printf(" %-5s : %16.14f\n",Board.D_NAMES[childrenDirections[c]],children[c].value);
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
				//child.value=childBoards[d].getFreeCellCount();
				child.value=childBoards[d].getScore();
				children[childrenCount]=child;
				childrenDirections[childrenCount]=d;
				childrenCount++;
			}
		}				
	}

	private void computeValueFromChildren() {			// set value and best_child

		value=0;
		for (int c=0;c<children.length;c++) {
			if (children[c].value>value) {
				best_child=c;
				value=children[c].value;
			}
		}
	}
	
	protected void _run () {

		init_children();

		computeValueFromChildren();

		if (children.length<2 || depth<=0 ) {
			return;
		}

		if (depth>3 ) {
			ForkJoinTask.invokeAll(children);			
		} else {
			for (int c=0;c<children.length;c++) {
					children[c].run_purge();
			}			
		}			
		computeValueFromChildren();			
	}
}
