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
		if (move(f,direction)) {
			System.out.println(D_NAMES[direction]);
			f.dump();
			zufall();
			return true;			
		}
		return false;
	}

	static Boolean move(Feld f,int direction) {
		switch(direction) {
		case 0 : return f.up();
		case 1 : return f.left();
		case 2 : return f.right();
		case 3 : return f.down();
		}	
		return false;		
	}
	 
	static int bewertung(Feld f) {


		int v=f.free();
		
		
		for (int i=0;i<3;i++) {			
			for (int j=0;j<3;j++) {
				int d=f.get(i,j);
				if (f.get(i-1,j)*2==d) v++;
				if (f.get(i+1,j)==d) v++;
				if (f.get(i,j-1)*2==d) v++;
				if (f.get(1,j+1)==d) v++;				
			}
		}
		return v;
		//return f.free()*1024+v;
	 }

	
	
	void main() {
		System.out.println("init...");
		f.zufall();	
		f.zufall();
		f.dump();
		System.out.println("run....");

		while (true) {

			int  value[] = new int[4];			
			for (int d=0;d<4;d++) {
				Feld ff=new Feld(f);
				if (move(ff,d)) {
					value[d]=bewertung(ff);
				} else {
					value[d]=Integer.MIN_VALUE;
				}
				 System.out.println(" V : dir "+D_NAMES[d]+" V "+value[d]);
			}
			int best_dir=-1;
			int best_value=Integer.MIN_VALUE;
			for (int d=0;d<4;d++) {
				if (value[d]>best_value) {
					best_dir=d;
					best_value=value[d];
				}
			}

			// System.out.println("best dir "+best_dir+" value "+best_value);
			
			if (!move(best_dir)) {
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
