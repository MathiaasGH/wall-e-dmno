import lejos.hardware.port.MotorPort;

public class Orientation {
	
	private final static String DEVANT = "devant";
	private final static String DERRIERE = "derriere";
	private final static String NEUTRE = "neutre";
	private final static String GAUCHE = "gauche";
	private final static String DROITE = "droite";

	private String face; //valeur possible : devant, derrière, neutre. 
	private String cote; //valeur possible : gauche, droite, neutre. 
	private DifferentialDrive dd;
	
	public Orientation() {
		face =NEUTRE;
		cote =NEUTRE;
		dd = new DifferentialDrive(MotorPort.A, MotorPort.B);
	}

	/** Méthode qui renvoie la nouvelle position d’un objet
	* @param int id l’identité de l’objet
	* @return retourne la nouvelle position de l’objet
	*/
	public void actualiser(){}

	public void avancer() {
		dd.forward();
	}
	
	public void tourneDr() {
		dd.rotateClockwise();
	}
	
	public void tournerGa() {
		dd.rotateCounterClockwise();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
