import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Orientation {

	private final static int DELAY = 25;
	private final static String DEVANT = "devant";
	private final static String DERRIERE = "derriere";
	private final static String NEUTRE = "neutre";
	private final static String GAUCHE = "gauche";
	private final static String DROITE = "droite";

	private Robot robot;
	private String face; //valeur possible : devant, derrière, neutre. 
	private String cote; //valeur possible : gauche, droite, neutre. 
	private DifferentialDrive dd;

	public Orientation(Robot r) {
		robot = r;
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
		dd.rotateCounterClockwise();
	}

	public void tournerGa() {
		dd.rotateClockwise();
	}

	private void delay(){
		Delay.msDelay(DELAY);
	}

	public static float[] min(float[] tab) {
		float min=100000;
		int indice=-1;
		for(int i=0;i<tab.length;i++) {
			if(tab[i]!=0 && tab[i]<min) {
				min=tab[i];
				indice=i;
			}
		}
		float[] tabR = new float[2];
		tabR[0] = min;
		tabR[1] = indice;
		return tabR;
	}

	private void stop() {
		dd.stop();
	}
	
	public boolean isMoving() {
		return dd.isMoving();
	}
	
	public void rechercheAngle(int angle) {
		Capteurs cpt = robot.getCapteurs();
		tournerDe(angle);
		float[] valeurs = new float[0];
		int indice = 0;
		while(isMoving()) {
			System.out.println(indice);
			valeurs = cpt.regarde(valeurs);
			indice++;
		}
		float[] min = min(valeurs);
		int angleMin = (int)min[1] * (int)angle / (int)indice;
		System.out.println("Le minimum est : " + min[0] + " que j'ai à " + angleMin + "°");
	}

	public void recherche(int duration) {
		Capteurs cpt = robot.getCapteurs();
		tourneDr();
		float[] valeurs = new float[0];
		valeurs = cpt.regarde(valeurs);
		for(int i=0;i<duration;i++) {
			delay();
			System.out.println(i);
			valeurs = cpt.regarde(valeurs);
			delay();
		}
		stop();
		cpt.fermeLesYeux();
		float[] min = min(valeurs);
		System.out.println("Le minimum est : " + min[0] + " que j'ai vu la " + min[1] + "ème fois sur " + duration);
		//System.out.println(Arrays.toString(valeurs));
		Delay.msDelay(10000);
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
