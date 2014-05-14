package de.mpg.molgen.buczek.g2048;

public class Sim {

	public final int MAX_DEPTH=4;
	
	Board board=new Board();

	void setRandomPiece() {
		System.out.println("set new random piece");
		board.setRandomPiece();
		board.dump();
	}
		
	Boolean move(int direction) {
		if (board.move(direction)) {
			System.out.println(Board.D_NAMES[direction]);
			board.dump();
			setRandomPiece();
			return true;			
		}
		return false;
	}

	 
	
	
	void main() {
		
		Board.initRandom(1234);
		
		System.out.println("init...");
		board.setRandomPiece();	
		board.setRandomPiece();
		board.dump();
		System.out.println("run....");

		
		while (true) {

			GameTreeDir gameTree = new GameTreeDir(board);

			// int depth=f.free()>8 ? 4 : 6;
			// int depth=f.free()>8 ? 4 : f.free()>4 ? 6 : 8;			
			// int depth=f.free()>4 ? 2 : f.free()>3 ? 3 : 4;
			// int depth=f.free()>6 ? 2 : 4;
			// int depth=3;
			int depth=MAX_DEPTH;
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
