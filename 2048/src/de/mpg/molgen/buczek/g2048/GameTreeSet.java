package de.mpg.molgen.buczek.g2048;

import java.util.concurrent.ForkJoinTask;

public class GameTreeSet extends GameTree {
		
	private static final long serialVersionUID = 1L;

	public void init_children() {
		int variants=board.getFreeCellCount()*2;		
		
		children=new GameTree[variants];
		for (int i=0;i<variants;i++) {
			GameTreeDir child=new GameTreeDir(board);
			child.depth=depth;
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

	private void computeValueFromChildren() {

		double sum=0;

		int c;
		for (c=0;c<children.length/2;c++) {
			sum+=children[c].value*9;
		}

		for (;c<children.length;c++) {
			sum+=children[c].value;
		}

		value=sum/(children.length/2)/10;
	}
	

	protected void _run () {


		if (board.getFreeCellCount()==0) {
			value=0;
			return;
		}

		init_children();
		if (depth>2) {
			ForkJoinTask.invokeAll(children);
		} else {
			for (int c=0;c<children.length;c++) {
				GameTreeDir child=(GameTreeDir)children[c];
				child.run_purge();		
			}
		}
		computeValueFromChildren();			
	}	
}
