
public class Position {
	private int x, y; 
	private double degres; 
	private char startSide; //b ou g pour blueSide ou greenSide
	
	
	public Position(int x, int y, char startSide) {
		this.x=x; 
		this.y=y;
		degres = 0;
		this.startSide=startSide; 
	}
	
	public void updateOrientation(double distanceTourne) {
		double distanceAbsolue= Math.abs(distanceTourne);
		if(distanceAbsolue>360) {
			while(distanceAbsolue>360) {
				distanceAbsolue=distanceAbsolue-360;
			}
		}
		if (degres+distanceTourne>180) {
			degres=-180+((Math.abs(degres+distanceTourne))-180);
		}
		else if (degres+distanceTourne<-180) {
			degres=180-((Math.abs(degres+distanceTourne))-180);
		}
		else {
			degres=degres+distanceTourne;
		}
	}
	
}
