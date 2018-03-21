package org.foi.nwtis.matnovak.kvadrati;

public class NeparniKvadrati_1 extends Kvadrati{
	
	public NeparniKvadrati_1(int odBroja, int doBroja) {
		super(odBroja,doBroja);
	}
	
	public void ispis() {
		this.odBroja = (this.odBroja % 2 == 1) 
						? this.odBroja : this.odBroja + 1;
		
		for(int i=this.odBroja;i <= this.doBroja;i=i+2) {
	//			System.out.println(i + " * " + i + " = " + i*i);
				System.out.printf("%3d * %3d = %3d\n", i, i, i*i);
		}
	}
}