package de.mpg.molgen.buczek.g2048;



public class GameTreeDir extends GameTree {

	int best_child=0;

	public GameTreeDir() {}

	public GameTreeDir(Feld feld) {
		this.feld=new Feld (feld);
	}

	int getBestDirection() {

		for (int d=0;d<4;d++) {
			if (children[d]!=null) {
				System.out.printf(" %-5s : %16.14f\n",Feld.D_NAMES[d],children[d].value);
			}
		}		
		System.out.println();
		return best_child;
	}


	public void init_children() {
		children=new GameTree[4];
		for (int i=0;i<4;i++) {
			Feld f=new Feld(feld);
			if (f.move(i)) {
				GameTreeSet child=new GameTreeSet();
				child.feld=f;
				child.value=f.free();
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

		if (max_free_count<2 || maxDepth<=0 || value>=8)
			return;

		for (int i=0;i<children.length;i++) {
			if (children[i]!=null) {
				children[i].run(maxDepth-1);
			}
		}

		value=computeValueFromChildren();			
	}
}