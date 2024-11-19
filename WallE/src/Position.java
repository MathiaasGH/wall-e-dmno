

public class Position extends Capteurs{
	private double x, y; 
	private double degres; 
	private char startSide; //b ou g pour blueSide ou greenSide
	
	public Position(int x, int y, char startSide) {
		super();
		this.x=x; 
		this.y=y;
		degres = 0;
		this.startSide=startSide; 
		System.out.println("Classe position instanciée");
	}
	/**
	 * Méthode qui renvoie la valeur de x
	 * @return x, la position en abscisse du robot
	 */
	public double getX() {
		return this.x;
	}
	/**
	 * Méthode qui renvoie la valeur de y
	 * @return y, la position en ordonnée du robot
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Méthode qui renvoie la valeur de degres
	 * @return degres, l'orientation en degrès du robot
	 */
	public double getDegres() {
		return this.degres;
	}
	
	/**
	 * Méthode qui permet de modifier la valeur de x qui représente la position en abscisse du robot
	 * @param a la nouvelle valeur de x
	 */
	public void setX (double a) {
		x = a;
	}
	
	/**
	 * Méthode qui permet de modifier la valeur de y qui représente la position en ordonnée du robot
	 * @param b la nouvelle valeur de y
	 */
	public void setY (double b) {
		y = b;
	}

	/**
	 * Méthode qui permet de modifier la valeur de degres qui représente l'orientation du robot
	 * @param d la nouvelle orientation en degrès du robot
	 */
	public void setDegres (double d) {
		this.degres=d ;
	}
	
	/**
	 * Méthode qui permet de mettre à jour l'orientation du robot en fonction de l'angle duquel il a tourné
	 * @param distanceTourne, l'angle que le robot a touré
	 */
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

	/**
	 * Méthode qui permet de mettre à jour la position du robot en fonction d'une distance parcours et de son orientation
	 * @param d, distance en double parcouru par le robot
	 */
	public void  updatePosition( double d ) {
		double a,o;
		double degrespos;
		if (degres>0 && degres<90) {
			degrespos=(-1*degres)+90;
		}
		else if (degres>90 && degres<=180) {
			degrespos=((degres-90)*-1);
		}
		else if (degres<0 && degres>-90) {
			degrespos=(degres*-1)+90;
		}
		else if (degres<-90 && degres>=-180) {
			degrespos=-1*((degres+180)+90);
		}
		else if (degres==0.0) {
			degrespos=90;
		}
		else if (degres==90) {
			degrespos=0;
		}
		else if (degres==-90) {
			degrespos=180;
		}
		else if (degres==180||degres==-180) {
			degrespos=90;
		}
		else degrespos=degres;
		double degresrad= degrespos*Math.PI/180;
		a= Math.cos(degresrad) * d ;
		o= Math.sin(degresrad) * d ;
		this.setX(a+x);
		this.setY(o+y);
	}

	/**
	 * Méthode qui renvoie l'angle duquel il faut tourner pour se retrouver face à la base adverse
	 * @return l'angle en degrès duquel il faut tourner pour être face à la base adverse
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
