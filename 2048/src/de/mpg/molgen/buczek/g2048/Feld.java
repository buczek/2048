package de.mpg.molgen.buczek.g2048;
import java.util.Random;

public class Feld {

	static private Random random=new Random();

	int gitter[][]={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
	

	int free() {
		int count=0;
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++) 
				if (gitter[i][j]==0)
					count++;
		return count;
	}
	
	
	
	public Feld() {
	}
	
	public Feld(Feld f) {
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++)
				gitter[i][j]=f.gitter[i][j];
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
		int count=0;
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++) 
				if (gitter[i][j]==0)
					count++;
		int value=random.nextInt(4)>1 ? 2 : 4;
		if (count==0)
			return false;
		count=random.nextInt(count);
		for (int i=0;i<4;i++)
			for (int j=0;j<4;j++) 
				if (gitter[i][j]==0)
					if (count--==0) {
						gitter[i][j]=value;
						return true;
					}
		// not reached
		System.err.println("internal error");
		System.exit(1);
		return false;
	}

	
	public  int[] shift(int in[]) {
		int out[]={0,0,0,0};
		
		int read=0;
		for (int write=0;write<4;write++) {
			while (read<4 && in[read]==0) read++;
			out[write]=read<4 ? in[read++] : 0;
			while (read<4 && in[read]==0) read++;
			if (read<4 && in[read]==out[write])
				out[write]+=in[read++];
		}		
		return out;
	}
	

	
	
	
	public Boolean left() {
		Feld save=new Feld(this);
		for (int i=0;i<4;i++) {
			int d[]=new int[4];
			for (int j=0;j<4;j++)
				d[j]=gitter[i][j];
			d=shift(d);
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
			d=shift(d);
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
			d=shift(d);
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
			d=shift(d);
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

	
	
}
	
	