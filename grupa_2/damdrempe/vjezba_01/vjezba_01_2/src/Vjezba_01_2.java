public class Vjezba_01_2 {
	public static void main (String args[]){
		if (args.length!=2){
			System.out.println("Unesite dva argumenta");
			return;
		}
		
		int odBroja = Integer.parseInt(args[0]);
		int doBroja = Integer.parseInt(args[1]);
		Interval interval = new Interval(odBroja, doBroja);
		
		long zbroj = interval.dajZbroj();
		System.out.println(zbroj);
	}
}