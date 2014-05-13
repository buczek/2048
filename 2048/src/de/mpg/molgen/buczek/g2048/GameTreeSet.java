package de.mpg.molgen.buczek.g2048;


public class GameTreeSet extends GameTree {
	
	public void init_children() {
		int variants=feld.free()*2;		
		
		children=new GameTree[variants];
		for (int i=0;i<variants;i++) {
			GameTreeDir child=new GameTreeDir();
			child.parent=this;
			child.feld=new Feld(feld);
			children[i]=child;			
		}
		
		int variant=0;
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++)
				if (feld.get(i,j)==0) {
					children[variant].feld.set(i,j,2);
					variant++;
				}

		for (int i=0;i<4;i++)
				for (int j=0;j<4;j++)
						if (feld.get(i,j)==0) {
								children[variant].feld.set(i,j,4);
									variant++;
						}

	}

	public int computeValueFromChildren() {

		int sum=0;
		int i;
		for (i=0;i<children.length/2;i++) {
			sum+=children[i].value*3;
		}

		for (;i<children.length;i++) {
			sum+=children[i].value;
		}

		return sum;
	}
	

	
	public void run (int maxDepth) {

		if (feld.free()==0) {
			value=Integer.MIN_VALUE;
			return;
		}

		init_children();

		for (int c=0;c<children.length;c++) {
			GameTreeDir child=(GameTreeDir)children[c];
			child.run(maxDepth);		
		}

		value=computeValueFromChildren();			
	}
	
}
