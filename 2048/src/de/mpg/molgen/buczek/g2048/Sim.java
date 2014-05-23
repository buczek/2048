package de.mpg.molgen.buczek.g2048;

public class Sim  {
	
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

		
	private void _main() {

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

	
	
	static final String USAGE="usage: [options] [random-seed]\n" +
			"  --max-depth n     : max game tree depth\n" +
			"  --max-threads n   : max threads\n";

	
	public static void main(String[] args) {

		final Sim sim=new Sim();

		CommandLineParser commandLineParser=new CommandLineParser();		

		if (!commandLineParser.parse(args,new CommandLineParser.OptionsCreator() {
			public void createOptions(CommandLineParser.OptionProcessor op) {
				sim.maxThreads=op.getIntegerOption("max-threads",0);
				sim.MAX_DEPTH=op.getIntegerOption("max-depth",4);
			}
		})) {
			die(USAGE);
		}
		if (commandLineParser.getRemainingArgCount()==1) {
			int seed=new Integer(commandLineParser.getRemainingArgs()[0]);
			Board.initRandom(seed);
			System.out.println("RANDOM:      "+seed);
		} else {
			System.out.println("RANDOM:      random");
		}		

		System.out.println("maxThreads="+sim.maxThreads);
		System.out.println("MAX_DEPTH="+sim.MAX_DEPTH);

		if (commandLineParser.getRemainingArgCount()==1) {
			int seed=new Integer(commandLineParser.getRemainingArgs()[0]);
			Board.initRandom(seed);
			System.out.println("RANDOM:      "+seed);
		} else {
			System.out.println("RANDOM:      random");
		}		


		if (commandLineParser.getRemainingArgCount()>1) {
			die(USAGE);
		}		

		sim._main();
	}

}

