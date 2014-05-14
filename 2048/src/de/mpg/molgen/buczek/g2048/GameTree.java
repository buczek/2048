package de.mpg.molgen.buczek.g2048;

public abstract class GameTree {

	Board				board;
	double				value;
	GameTree			children[];
	
	public abstract void init_children();
	public abstract double  computeValueFromChildren();

	public double getValue() {
		return value;
	}
		
	abstract public void run (int maxDepth);

}
