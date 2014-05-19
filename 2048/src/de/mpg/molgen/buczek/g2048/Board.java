package de.mpg.molgen.buczek.g2048;
import java.util.Random;

public class Board {
	static final String D_NAMES[] = {"UP","LEFT","RIGHT","DOWN"};
	static private Random random=new Random();
	static public void initRandom(long seed) {
		random=new Random(seed);
	}
	
	
	// 0 = 0 (!)
	// 1 = 2
	// 2 = 4
	// 3 = 8	
	private byte cells[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	private int  freeCellCount=16;
	private int  score=0;
	
	int getFreeCellCount() { return freeCellCount; }
	int getScore()         { return score; }
	
	public Board() { }
		
	public Board(Board board) {
		System.arraycopy(board.cells,0,this.cells,0,16);
		freeCellCount=board.freeCellCount;
		score=board.score;
	}

	
	public Boolean isEqual(Board f) {
		for (int i=0;i<16;i++)
				if (cells[i] != f.cells[i])
					return false;
		return true;
		
	}
	
	public void dump() {
		for (int i=0;i<4;i++) {
			for (int j=0;j<4;j++) {
				System.out.printf("%4d ",cells[i*4+j]==0 ? 0 : (1<<cells[i*4+j]));
			}
			System.out.println();
		}
		System.out.println("Score: "+score);
		System.out.println();
	}
	
	
	public Boolean setRandomPiece() {
		if (freeCellCount==0)
			return false;
		byte value=random.nextInt(10)>1 ? (byte)1 : (byte)2;
		int count=random.nextInt(freeCellCount);
		for (int i=0;i<16;i++)
				if (cells[i]==0)
					if (count--==0) {
						cells[i]=value;
						freeCellCount--;
						return true;
					}
		// not reached
		System.err.println("internal error");
		System.exit(1);
		return false;
	}

	



	private  void shift(byte array[]) {
		int read=0;
		for (int write=0;write<4;write++) {
			while (read<4 && array[read]==0) read++;
			array[write]=read<4 ? array[read++] : 0;
			while (read<4 && array[read]==0) read++;
			if (read<4 && array[read]==array[write]) {
				array[write]++;
				read++;
				freeCellCount++;
				score+=1<<array[write];
			}
		}		
	}
	

	private Boolean left() {
		Board save=new Board(this);
		for (int i=0;i<4;i++) {
			byte d[]=new byte[4];
			for (int j=0;j<4;j++)
				d[j]=cells[i*4+j];
			shift(d);
			for (int j=0;j<4;j++)
				cells[i*4+j]=d[j];
		}	
		return !isEqual(save);
	}

	private Boolean right() {
		Board save=new Board(this);
		for (int i=0;i<4;i++) {
			byte d[]=new byte[4];
			for (int j=0;j<4;j++)
				d[3-j]=cells[i*4+j];
			shift(d);
			for (int j=0;j<4;j++)
				cells[i*4+j]=d[3-j];
		}	
		return !isEqual(save);
	}

	
	private Boolean up() {
		Board save=new Board(this);
		for (int i=0;i<4;i++) {
			byte d[]=new byte[4];
			for (int j=0;j<4;j++)
				d[j]=cells[j*4+i];
			shift(d);
			for (int j=0;j<4;j++)
				cells[j*4+i]=d[j];
		}	
		return !isEqual(save);
	}
	
	private Boolean down() {
		Board save=new Board(this);
		for (int i=0;i<4;i++) {
			byte d[]=new byte[4];
			for (int j=0;j<4;j++)
				d[3-j]=cells[j*4+i];
			shift(d);
			for (int j=0;j<4;j++)
				cells[j*4+i]=d[3-j];
		}	
		return !isEqual(save);
	}
	
	public int get(int i,int j) {
		if (i>=0&&i<4&&j>=0&&j<4)
			return cells[i*4+j];
		else
			return 0;
	}
	
	public void set(int i,int j,int value) {
		if (i>=0&&i<4&&j>=0&&j<4) {
			if (cells[i*4+j]==0) freeCellCount--;
			if (value==0) freeCellCount++;
			cells[i*4+j]=(byte)value;
		}
	}

	public Boolean move(int direction) {
		switch(direction) {
		case 0 : return up();
		case 1 : return left();
		case 2 : return right();
		case 3 : return down();
		}
		return false;
	}

/*
	public native boolean move(int direction);
	static {
		System.loadLibrary("shift");
	}
*/


	
	
	
}

