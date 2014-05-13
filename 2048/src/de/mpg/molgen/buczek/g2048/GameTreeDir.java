package de.mpg.molgen.buczek.g2048;

import java.util.ArrayList;

import de.mpg.molgen.buczek.g2048.GameTree.BewertungsHandler;


public class GameTreeDir extends GameTree {

	int best_child=0;

	public GameTreeDir() {}

	public GameTreeDir(Feld feld) {
		this.feld=new Feld (feld);
	}

	int getBestDirection() {
		return best_child;
	}


	public void init_children() {
		children=new GameTree[4];
		for (int i=0;i<4;i++) {
			Feld f=new Feld(feld);
			if (f.move(i)) {
				GameTreeSet child=new GameTreeSet();
				child.parent=this;
				child.feld=f;
				children[i]=child;							
			}
		}
	}


	public int computeValueFromChildren() {

		int max_value=Integer.MIN_VALUE;
		for (int i=0;i<children.length;i++) {
			if (children[i]!=null) {
				if (children[i].value>max_value) {
					best_child=i;
					max_value=children[i].value;
				}
			}
		}
		return max_value+feld.free()*1024;
	}		




	public void run (int maxDepth) {

		init_children();

		int	free[]=new int[4];

		for (int d=0;d<4;d++) {
			free[d]=children[d]!=null ? children[d].feld.free() : 0;
		}

		int max_free_count=0;
		for (int d=0;d<4;d++) {
			if (free[d]>value) {
				value=free[d];
				best_child=d;
				max_free_count=1;
			} else if (free[d]==value) {
				max_free_count++;
			} else {
				children[d]=null;
			}
		}

		if (max_free_count<2 || maxDepth<=0)
			return;


		for (int i=0;i<children.length;i++) {
			if (children[i]!=null) {
				children[i].run(maxDepth-1);
			}
		}

		value=computeValueFromChildren();			
	}
}
