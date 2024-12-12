/**
* Cette classe permet de gérer les positions relatives au robot.
* Cette classe hérite de la classe capteurs.
*
* @see Position
* @author DEVILLIERS, MITTON, NDONG, OZTURK
*/

public class Position extends Capteurs{
	/** Les entiers correspondant aux coordonnées horizontales et verticales du robot*/
	private double x, y; 
	/** Le double représentant l'anglee relatif formé entre le robot et la perpendiculaire
	 * du mur de la table derrière la ligne adverse.
	 */
	private double degres; 
	
	/**
	 * Constructeur de la classe position.
	 * Permet d'initialiser la position du robot avant de démarrer la course.
	 * @param x un entier prenant une valeur suivante {50,100,150}
	 * @param y un entier = 0
	 */
	public Position(int x, int y) {
		super();
		this.x=x; 
		this.y=y;
		degres = 0;
	}
	
	/**
	 * Renvoie la valeur de l'abscisse du robot
	 * @return x, la position en abscisse du robot
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Renvoie la valeur de l'ordonnée du robot
	 * @return y, la position en ordonnée du robot
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Renvoie la valeur de degres
	 * @return degres, l'orientation en degrès du robot
	 */
	public double getDegres() {
		return this.degres;
	}
	
	/**
	 * Permet de modifier la valeur de x qui représente la position en abscisse du robot
	 * @param a la nouvelle valeur de x
	 */
	public void setX (double a) {
		x = a;
	}
	
	/**
	 * Permet de modifier la valeur de y qui représente la position en ordonnée du robot
	 * @param b la nouvelle valeur de y
	 */
	public void setY (double b) {
		y = b;
	}

	/**
	 * Permet de modifier la valeur de degres qui représente l'orientation du robot
	 * @param d la nouvelle orientation en degrès du robot
	 */
	public void setDegres (double d) {
		this.degres=d ;
	}
	
	/**
	 * Permet de mettre à jour l'orientation du robot en fonction de l'angle duquel il a tourné
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
	 * Permet de mettre à jour la position du robot en fonction d'une distance parcours et de son orientation
	 * @param d, distance en double parcouru par le robot
	 */
	public void  updatePosition( double d ) {
		double[] tab = calculerPositionPoint(d, degres);
		this.setX(tab[0]+x);
		this.setY(tab[1]+y);
	}
	
	/**
	 * Renvoie les coordonné en x et y d'un point en fonction d'un angle et d'une distance. 
	 * @param d en cm la distance parcourue
	 * @degres le degres sur le plateau
	 * @return un tableau de double qui possède en idx 0 la position en x
	 *  et en idx 1 la position en y 
	 */
	public double[] calculerPositionPoint(double d, double degres) {
		double[] tab = new double[2];
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
		tab[0]= Math.cos(degresrad) * d;
		tab[1]= Math.sin(degresrad) * d;
		return tab;
	}
	
	/**
	 * Permet de savoir si des coordonné sont proche à 2cm près du centre.
	 * @param tab, tableau de deux double, x et y
	 * @return un booleén : true si les coordonnées sont assez proches du centre (4cm de marge)
	 * false sinon 
	 */
	public boolean procheDuCentre(double[] tab) {
		if ((tab[0]<=102 && tab[0]>=98) && (tab[1]<=122 && tab[1]>=118)) {
			return true;
		}
		return false;
	}

	/**
	 * Renvoie l'angle duquel il faut tourner pour se retrouver face à la base adverse
	 * @return l'angle en degrés duquel il faut tourner pour être face au camps adverse
	 */
	public double degresAuCampAdverse() {
		if (degres!=0) {
			return -degres;
		}
		else {return degres;
		}
	}
	
	/**
	 * Permet de renvoyer un angle entre deux point grace à la position du robot
	 * @param Xb position en x du point B
	 * @param Yb position en y du point B
	 * @param Xc position en x du point C
	 * @param Yc position en y du point C
	 * @return un double correspondant à l'angle entre les point B et C en degrés.
	 */
	public double angleEntreDeuxPoint(double Xb, double Yb, double Xc, double Yc) {
		double Xu = Xb-x, Yu = Yb-y; 
		double Xv = Xc-x, Yv = Yc-y; 
		double produitScalaire = Xu*Xv+Yu*Yv;
		double AB = Math.sqrt(Math.pow(Xu, 2)+Math.pow(Yu, 2));
		double AC = Math.sqrt(Math.pow(Xv, 2)+Math.pow(Yv, 2));
		return (180/Math.PI)*Math.acos(produitScalaire/(AB*AC));
	}
	
	/**
	 * Renvoie l'angle dont il faut tourner pour se retrouver
	 * face au point qui forme le plus petit angle visuel entre le robot et ce point.
	 * @param Xb Xb position en x du point B
	 * @param Yb Yb position en y du point B
	 * @param Xc position en x du point C
	 * @param Yc Yc position en y du point C
	 * @return renvoie l'angle entre le robot est l'un des point, celui qui est le plus petit
	 */
	public double[] plusPetitAngleAuRobot(double Xb, double Yb, double Xc, double Yc) {
		double[] res = new double[2];
		double[] tab = calculerPositionPoint(10, getDegres());
		double angleAB = angleEntreDeuxPoint(tab[0], tab[1], Xb, Yb);
		double angleAC = angleEntreDeuxPoint(tab[0], tab[1], Xc, Yc);
		if (Math.abs(angleAB)>Math.abs(angleAC)) {
			res[0]=angleAC;
			res[1]=-1;
			return res; 
		}
		else {
			res[0]=angleAB;
			res[1]=1;
			return res; 
		}
	}

}
