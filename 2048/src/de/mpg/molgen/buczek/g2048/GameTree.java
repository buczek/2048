package de.mpg.molgen.buczek.g2048;

public abstract class GameTree {

	public final int VALUE_UNKNOWN=Integer.MIN_VALUE;
		
	Feld				feld=new Feld();
	int					value=VALUE_UNKNOWN;
	GameTree			parent;
	GameTree			children[];

	
	public abstract void init_children();
	public abstract int  computeValueFromChildren();

	public interface BewertungsHandler {
		int	bewerte(Feld feld);
	}

	public int getValue() {
		return value;
	}
		

	abstract public void run (int maxDepth);

}
