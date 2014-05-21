package de.mpg.molgen.buczek.g2048;

import de.mpg.molgen.buczek.g2048.CommandLineParser.OptionProcessor;



public class Sim implements CommandLineParser.OptionsCreator {
	
	public int MAX_DEPTH=6;
	public int maxThreads=0;
	
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

	private static void die(String message) {System.err.println(message);System.exit(1);}

	static final String USAGE="usage: [options] [random-seed]\n" +
			"  --max-depth n     : max game tree depth\n" +
			"  --max-threads n   : max threads\n";

	public void createOptions(OptionProcessor op) {
		maxThreads=op.getIntegerOption("max-threads",0);
		MAX_DEPTH=op.getIntegerOption("max-depth",4);
	}
	
	private void parseOptions(String []args) {
		CommandLineParser commandLineParser=new CommandLineParser();		

		if (!commandLineParser.parse(args,this)) {
			die(USAGE);
		}
		if (commandLineParser.getRemainingArgCount()>1) {
			die(USAGE);
		}		
		System.out.println("maxThreads="+maxThreads);
		System.out.println("MAX_DEPTH="+MAX_DEPTH);
		

		if (commandLineParser.getRemainingArgCount()==1) {
			int seed=new Integer(commandLineParser.getRemainingArgs()[0]);
			Board.initRandom(seed);
			System.out.println("RANDOM:      "+seed);
		} else {
			System.out.println("RANDOM:      random");
		}		
	}
	
	
	
	private void _main(String args[]) {

		parseOptions(args);
		
		board.setRandomPiece();	
		board.setRandomPiece();
		board.dump();

		System.out.println("run....");

		while (true) {
			GameTreeDir gameTree = new GameTreeDir(board);
			if (maxThreads>0)
				gameTree.setMaxThreads(maxThreads);
			
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

			
		Sim sim=new Sim();
		sim._main(args);
	}

	
}
