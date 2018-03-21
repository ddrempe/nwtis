class Interval {
	private int odBroja;
	private int doBroja;
	
	Interval (int odP, int doP){
		odBroja = odP;
		doBroja = doP;
	}
	
	public long dajZbroj(){
		long zbroj = 0;
		for(int i=odBroja; i<doBroja; i++){
			zbroj += i;
		}
		
		return zbroj;
	}
}