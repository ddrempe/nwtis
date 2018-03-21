package org.foi.nwtis.matnovak.kvadrati;

public class NeparniKvadrati_4 extends NeparniKvadrati_2 
							   implements Ispisivac_2 {
	
	public NeparniKvadrati_4(int odBroja, int doBroja) {
		super(odBroja,doBroja);
	}
	
	public void ispisiPodatke() {
		ispis();
	}
	
	public void ispisiPodatkeLinijski() {
		this.odBroja = (this.odBroja % 2 == 1) 
						? this.odBroja : this.odBroja + 1;
		
		for(int i=this.odBroja;i <= this.doBroja;i=i+4) {
			System.out.print(i + " * " + i + " = " + i*i + ", ");
		}
	}
}