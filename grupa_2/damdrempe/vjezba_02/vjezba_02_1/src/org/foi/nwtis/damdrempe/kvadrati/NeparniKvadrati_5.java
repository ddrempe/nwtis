package org.foi.nwtis.damdrempe.kvadrati;

public class NeparniKvadrati_5{
	
	static Ispisivac_1 kreirajIspisivac_1(int odBroja, int doBroja) {
		long vrijeme = System.currentTimeMillis();
		int o = (int) (v % 3);
		
		switch(o){
			case 0:
				return new NeparniKvadrati_3(odBroja, doBroja);
			case 1:
				return new NeparniKvadrati_4(odBroja, doBroja);
			case 2:
				return new KolikoJeSati();
		}
		
		return null;
	}
	
}