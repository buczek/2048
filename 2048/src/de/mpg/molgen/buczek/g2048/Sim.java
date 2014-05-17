package de.mpg.molgen.buczek.g2048;



public class Sim {

	public final int MAX_DEPTH=6;
	
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
		
		
		System.out.println("init...");
		board.setRandomPiece();	
		board.setRandomPiece();
		board.dump();
		System.out.println("run....");

		
		while (true) {

			GameTreeDir gameTree = new GameTreeDir(board);


			int freeCellCount=board.getFreeCellCount();
			int depth=freeCellCount>=8 ? MAX_DEPTH-2 : freeCellCount >=5 ? MAX_DEPTH-1 : MAX_DEPTH;

			
			gameTree.run(depth);
			
			int direction=gameTree.getBestDirection();
			
			if (!move(direction)) {
				System.out.println("no moves left");
				break;
			}
		}
	}
		

	
	
	public static void main(String[] args) {

		if (args.length>0) {
			
			Board.initRandom(new Integer(args[0]));
		}
		
		Sim sim=new Sim();
		sim.main();
	}
	
}
