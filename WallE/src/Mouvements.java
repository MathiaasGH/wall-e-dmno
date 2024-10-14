import lejos.robotics.navigation.MovePilot;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.*;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;


public class Mouvements {

	private final static int DELAY = 25;
	private final static String DEVANT = "devant";
	private final static String DERRIERE = "derriere";
	private final static String NEUTRE = "neutre";
	private final static String GAUCHE = "gauche";
	private final static String DROITE = "droite";	

	private Robot robot;
	private String face; //valeur possible : devant, derrière, neutre. 
	private String cote; //valeur possible : gauche, droite, neutre. 
	//private DifferentialDrive dd;

	private Wheel wheel1;
	private Wheel wheel2;
	private Chassis chassis;
	private MovePilot pilot;

	public Mouvements(Robot r) {
		robot = r;
		face =NEUTRE;
		cote =NEUTRE;
		//dd = new DifferentialDrive(MotorPort.A, MotorPort.B);

		wheel1 = WheeledChassis.modelWheel(Motor.A, 56).offset(-62);
		wheel2 = WheeledChassis.modelWheel(Motor.B, 56).offset(62);
		chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, WheeledChassis.TYPE_DIFFERENTIAL); 
		pilot = new MovePilot(chassis);

		System.out.println("Classe orientation instanciee");
	}

	/** Méthode qui renvoie la nouvelle position d’un objet
	 * @param int id l’identité de l’objet
	 * @return retourne la nouvelle position de l’objet
	 */
	public void actualiser(){}

	public void avancer(int dist) {
		pilot.travel(dist); // A VOIR
	}
	
	public void avancer() {
		pilot.forward(); // A VOIR
	}

	public void tourneDr() {
		pilot.rotateRight();
	}

	public void tournerGa() {
		pilot.rotateLeft();
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
		pilot.stop();
	}

	public boolean isMoving() {
		return pilot.isMoving();
	}

	public void tournerDe(int angle, boolean asynchrone) {
		pilot.rotate(angle, asynchrone);       
	}
	
	public void avancerWhileIsNotPressed() {
		Capteurs cpt = robot.getCapteurs();
		this.avancer();
		int i=0;
		while(isMoving() && !cpt.isPressed() && i<10000) {
			if(cpt.isPressed())
				System.out.println("oui");
			try {
	            Thread.sleep(50); // délai de 50 ms
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
			i++;
		}
		pilot.stop();
	}

	public void rechercheAngle(int angle) {
		//System.out.println(pilot.getAngularSpeed());
		pilot.setAngularSpeed(200);
		Capteurs cpt = robot.getCapteurs();
		tournerDe(angle, true);
		float[] valeurs = new float[0];
		int indice = 0;
		while(isMoving()) {
			System.out.println(indice);
			valeurs = cpt.regarde(valeurs);
			indice++;
		}
		float[] min = min(valeurs);
		int angleMin = ((int)min[1] * angle ) / (int)indice;
		System.out.println("Le minimum est : " + min[0] + " que j'ai a " + angleMin + " degres.");
		System.out.println("J'ai prit " + indice + " données");
		if(angleMin-angle>angle/2) {
			this.tournerDe(-(angle-angleMin + 4), false);
		}
		else this.tournerDe(angleMin-angle + 4,false);
		chercherpalet((int)(1000*min[0]) + 2);
		Delay.msDelay(10000);
		

	}

	public void chercherpalet(int d) {
		Capteurs cpt = robot.getCapteurs();
		float[] valeurs = new float[2];
		valeurs[0] = 1000000;
		valeurs[1] = 1000000;
		robot.avancer(d);
		while(isMoving() && (valeurs[valeurs.length-1]<= valeurs[valeurs.length-2]) && !cpt.isPressed()) {
			valeurs = cpt.regarde(valeurs);
			System.out.println(valeurs[valeurs.length-1]);	
		}
		pilot.stop();

	}

	/**public void recherche(int duration) {
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
<<<<<<< HEAD

	}*/

	public static void main(String[] args) {

		Mouvements o = new Mouvements(null);


	}

}
