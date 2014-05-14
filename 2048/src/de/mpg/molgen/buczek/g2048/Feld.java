package de.mpg.molgen.buczek.g2048;
import java.util.Random;

public class Feld {
	static final String D_NAMES[] = {"UP","LEFT","RIGHT","DOWN"};
	static private Random random=new Random();
	static public void initRandom(long seed) {
		random=new Random(seed);
	}
	
	
	private int gitter[][]={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
	private int xfree=16;
	
	int free() { return xfree; }
	
	public Feld() {
	}
	
	public Feld(Feld f) {
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++) gitter[i][j]=f.gitter[i][j];
			// not faster: System.arraycopy(f.gitter[i],0,gitter[i],0,4);
		xfree=f.xfree;
	}

	
	public Boolean isEqual(Feld f) {
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++)
				if (gitter[i][j] != f.gitter[i][j])
					return false;
		return true;
		
	}
	
	public void dump() {
		for (int i=0;i<4;i++) {
			for (int j=0;j<4;j++) {
				System.out.printf("%4d ",gitter[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
	public Boolean zufall() {
		if (xfree==0)
			return false;
		int value=random.nextInt(4)>1 ? 2 : 4;
		int count=random.nextInt(xfree);
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++) 
				if (gitter[i][j]==0)
					if (count--==0) {
						gitter[i][j]=value;
						xfree--;
						return true;
					}
		// not reached
		System.err.println("internal error");
		System.exit(1);
		return false;
	}

	
	
	public  void shift(int array[]) {
		int read=0;
		for (int write=0;write<4;write++) {
			while (read<4 && array[read]==0) read++;
			array[write]=read<4 ? array[read++] : 0;
			while (read<4 && array[read]==0) read++;
			if (read<4 && array[read]==array[write]) {
				array[write]+=array[read++];
				xfree++;
			}
		}		
	}
	

	
	
	
	public Boolean left() {
		Feld save=new Feld(this);
		for (int i=0;i<4;i++) {
			int d[]=new int[4];
			for (int j=0;j<4;j++)
				d[j]=gitter[i][j];
			shift(d);
			for (int j=0;j<4;j++)
				gitter[i][j]=d[j];
		}	
		return !isEqual(save);
	}

	public Boolean right() {
		Feld save=new Feld(this);
		for (int i=0;i<4;i++) {
			int d[]=new int[4];
			for (int j=0;j<4;j++)
				d[3-j]=gitter[i][j];
			shift(d);
			for (int j=0;j<4;j++)
				gitter[i][j]=d[3-j];
		}	
		return !isEqual(save);
	}

	
	public Boolean up() {
		Feld save=new Feld(this);
		for (int i=0;i<4;i++) {
			int d[]=new int[4];
			for (int j=0;j<4;j++)
				d[j]=gitter[j][i];
			shift(d);
			for (int j=0;j<4;j++)
				gitter[j][i]=d[j];
		}	
		return !isEqual(save);
	}
	
	public Boolean down() {
		Feld save=new Feld(this);
		for (int i=0;i<4;i++) {
			int d[]=new int[4];
			for (int j=0;j<4;j++)
				d[3-j]=gitter[j][i];
			shift(d);
			for (int j=0;j<4;j++)
				gitter[j][i]=d[3-j];
		}	
		return !isEqual(save);
	}
	
	public int get(int i,int j) {
		try {
			return gitter[i][j];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}
	
	public void set(int i,int j,int value) {
		try {
			if (gitter[i][j]==0) xfree--;
			if (value==0) xfree++;
			gitter[i][j]=value;
		} catch (ArrayIndexOutOfBoundsException e) {
			;
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

	
}
	
	