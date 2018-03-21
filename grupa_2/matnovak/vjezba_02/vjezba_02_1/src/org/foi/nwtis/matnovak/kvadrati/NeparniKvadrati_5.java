package org.foi.nwtis.matnovak.kvadrati;

public class NeparniKvadrati_5 {
	
	public static Ispisivac_1 kreirajIspisivac_1(int odBroja, int doBroja) {
		long v = System.currentTimeMillis();
		int o = (int) (v % 3);
		
		System.out.println(v);
		System.out.println(o);
		
		switch(o) {
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