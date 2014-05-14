package de.mpg.molgen.buczek.g2048;


public class Sim {

	Feld f=new Feld();

	void zufall() {
		System.out.println("set");
		f.zufall();
		f.dump();
	}
	
	
	Boolean down() {
		if (f.down()) {
			System.out.println("DOWN");
			f.dump();
			zufall();
			return true;
		}
		return false;
	}
	
	Boolean up() {
		if (f.up()) {
			System.out.println("UP");
			f.dump();
			zufall();
			return true;
		}
		return false;
	}
	
	Boolean left() {
		if (f.left()) {
			System.out.println("LEFT");
			f.dump();
			zufall();
			return true;
		}
		return false;
	}
	
	Boolean right() {
		if (f.right()) {
			System.out.println("RIGHT");
			f.dump();
			zufall();
			return true;
		}
		return false;
	}

	static private String D_NAMES[] = {"UP","LEFT","RIGHT","DOWN"};
		
	Boolean move(int direction) {
		if (f.move(direction)) {
			System.out.println(D_NAMES[direction]);
			f.dump();
			zufall();
			return true;			
		}
		return false;
	}

	 
	
	
	void main() {
		
		Feld.initRandom(123);
		
		
		System.out.println("init...");
		f.zufall();	
		f.zufall();
		f.dump();
		System.out.println("run....");

		
		while (true) {

			GameTreeDir gameTree = new GameTreeDir(f);

			//int depth=f.free()>8 ? 4 : 6;
			//int depth=f.free()>8 ? 4 : f.free()>4 ? 6 : 8;

			
			// int depth=f.free()>4 ? 2 : f.free()>3 ? 3 : 4;
			// int depth=f.free()>6 ? 2 : 4;
			// int depth=3;
			int depth=4;
			// System.out.println("depth "+depth);
			gameTree.run(depth);
			
			int direction=gameTree.getBestDirection();
			
			if (!move(direction)) {
				System.out.println("no moves left");
				break;
			}
			
		}
			
	}
		

	
	
	public static void main(String[] args) {
		Sim sim=new Sim();
		sim.main();
	}
	
}
