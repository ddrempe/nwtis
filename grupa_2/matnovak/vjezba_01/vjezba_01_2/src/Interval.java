class Interval {
	private int odBroja;
	private int doBroja;
	
	Interval (int odBroja, int doBroja){
		this.odBroja = odBroja;
		this.doBroja = doBroja;
	}
	
	public long dajZbroj(){
		long zbroj = 0;
		
		for(int i=odBroja; i<doBroja;i++){
			zbroj += i;
		}
		
		return zbroj;
	}
}