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

		wheel1 = WheeledChassis.modelWheel(Motor.B, 56).offset(-62);
		wheel2 = WheeledChassis.modelWheel(Motor.C, 56).offset(62);
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
		pilot.travel(dist,true); // A VOIR
	}
	
	public void avancer(int dist,boolean b) {
		pilot.travel(dist,b); // A VOIR
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
		System.out.println("L'indice : " + indice);
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
	
	public boolean distanceDiminue(float[] tab) {
		if(tab.length>=2)
			return tab[tab.length-1]<=tab[tab.length-2];
		return true;
	}

	public void avancerWhileIsNotPressed(int dist) {
	    // Initialisation des capteurs et des distances
	    Capteurs cpt = robot.getCapteurs();
	    float[] distance = cpt.regarde(new float[0]);

	    // Avancer de manière asynchrone sur la distance spécifiée + 5 cm
	    this.avancer(dist + 100);
	    
	    // Boucle tant que le robot est en mouvement et que le capteur de toucher n'est pas pressé
	    while (isMoving() && !cpt.isPressed()) {
	        // Vérifie les distances uniquement si elles sont disponibles
	        distance = cpt.regarde(distance);
	        if (distance.length > 0) {
	            float derniereDistance = distance[distance.length - 1];

	            // Vérifie si la distance est inférieure à 20 cm pour ouvrir les bras
	            if (derniereDistance < 0.3) {
	                cpt.ouvreBrasAsynchrone();
	            }
	        }

	       
	    }

	    // Arrête le robot et ferme les bras une fois la boucle terminée
	    robot.fermeBras();
	    pilot.stop();
	}

	public void rechercheAngle(int angle) {
		pilot.setAngularSpeed(70);
		Capteurs cpt = robot.getCapteurs();
		//Je ferme les bras
		cpt.fermeBras();
		//Je tourne de angle de manière asynchrone
		tournerDe(angle, true);
		//J'initialise un tableau dans lequel on range les distances que l'on voit
		float[] valeurs = new float[0];
		int indice = 0;
		//Tant que le robot bouge...
		while(isMoving()) {
			//System.out.println(indice);
			//On remplit le tableau de distances
			valeurs = cpt.regarde(valeurs);
			indice++;
		}
		//Je récupère la plus petite distane ainsi que l'indice de cette distance dans le tableau
		float[] min = min(valeurs);
		System.out.println((int)min[1] + " " + angle + " " + indice);
		//Je déduis l'angle grâce à un produit en croix
		int angleMin = ((int)min[1] * angle ) / (int)indice;
		float dist = min[0];
		//System.out.println("Le minimum est : " + min[0] + " que j'ai a " + angleMin + " degres.");
		//System.out.println("J'ai prit " + indice + " données");
		//J'optimise l'angle à tourner pour que le robot s'aligne avec l'objet
		if(angleMin<angle/2) {
			this.tournerDe(angleMin, false);
		}
		else this.tournerDe(-(angle-angleMin),false);
		//chercherpalet((int)(1000*min[0]) + 2);

		//Je crée un tableau dans lequel je range la distance entre le robot et l'objet le plus proche de lui
		//après s'être ré-orienté
		float[] valeurApresOrientation = new float[0];
		valeurApresOrientation = cpt.regarde(valeurApresOrientation);
		//Si la distance est la même à +/- 10cm que ce que le robot voyait en tourant...
		if(valeurApresOrientation[0]>=dist-0.1 && valeurApresOrientation[0]<=dist+0.1) {
			//...le robot avance de la distance.
			avancerWhileIsNotPressed((int)(1000*dist));
		}
		//Sinon le robot re-recherche.
		else rechercheAngle(360);
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

	/*public void chercherpalet(int d) {
		Capteurs cpt = robot.getCapteurs();
		float[] valeurs = new float[2];
		valeurs[0] = 1000000;
		valeurs[1] = 1000000;
		robot.avancer(d);
		int indice = 0;
		while(isMoving() && (valeurs[valeurs.length-1]<= valeurs[valeurs.length-2]) && !robot.isPressed()) {
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
