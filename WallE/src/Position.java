
public class Position {
	private double x, y; 
	private double degres; 
	private char startSide; //b ou g pour blueSide ou greenSide

	//dhqethzs
	public Position(int x, int y, char startSide) {
		this.x=x; 
		this.y=y;
		degres = 0;
		this.startSide=startSide; 
	}
	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getDegres() {
		return this.degres;
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

	public void setX (double a) {
		x = a;
	}

	public void setY (double b) {
		y = b;
	}


	public void setDegres (double d) {
		this.degres=d ;
	}

	public void  updatePosition( double d ) {
		double a,o;
		double degresrad= degres*Math.PI/180;
		a= Math.cos(degresrad) * d ;
		o= Math.sin(degresrad) * d ;
		this.setX(a);
		this.setY(o);
	}
	/*public void MiseAjour(String couleur) {
	if (!(startSide)) {
		if (couleur== "Rouge") {
			this.x=150;
		}
		if (couleur== "Vert") {
			this.y=180;
		}
		if (couleur== "Bleue") {
			this.y=60;
		}
		if (couleur== "Jaune") {
			this.x=50;
		}
		if (couleur== "Blanc") {
			this.y=30;
		}
	}
	else {
		if (couleur== "Rouge") {
			this.x=50;
		}
		if (couleur== "Vert") {
			this.y=60;
		}
		if (couleur== "Bleue") {
			this.y=180;
		}
		if (couleur== "Jaune") {
			this.x=150;
		}
		if (couleur== "Blanc") {
			this.y=30;
		}
	}
}
*/
	
	
	public double degresAuCampAdverse() {
		if (degres!=0) {
			return -degres;
		}
		else {return degres;
		}
	}

	public static void main(String[] args) {
	}
}
