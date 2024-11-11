import lejos.robotics.navigation.MovePilot;

import java.util.Arrays;

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
		pilot.setAngularSpeed(100);
		pilot.rotate(angle, asynchrone);       
	}

	public boolean distanceDiminue(float[] tab) {
		if(tab.length>=2)
			return tab[tab.length-1]<=tab[tab.length-2];
		return true;
	}

	public void avancerWhileIsNotPressed(int dist) {
		// Creation d'un boolean pour ouvrir les bras une seule fois
		boolean dejaOuvert = false;
		// Initialisation des capteurs et des distances
		Capteurs cpt = robot.getCapteurs();
		float[] distance = cpt.regarde(new float[0]);
		//System.out.println(pilot.getAngularSpeed());
		pilot.setAngularSpeed(70);
		// Avancer de manière asynchrone sur la distance spécifiée + 5 cm
		this.avancer(dist + 100);
		// Boucle tant que le robot est en mouvement et que le capteur de toucher n'est pas pressé
		while (isMoving() && !cpt.isPressed()) {
			// Vérifie les distances uniquement si elles sont disponibles
			distance = cpt.regarde(distance);
			if (distance.length > 0) {
				float derniereDistance = distance[distance.length - 1];

				// Vérifie si la distance est inférieure à 30 cm pour ouvrir les bras
				System.out.println(derniereDistance);
				if (derniereDistance < 0.35 && !dejaOuvert) {
					cpt.ouvreBrasAsynchrone();
					dejaOuvert=true;
				}
			}       
		}
		// Arrête le robot et ferme les bras une fois la boucle terminée
		robot.fermeBras();
		pilot.stop();
	}

	public void rechercheAngle(int angle) {
		Capteurs cpt = robot.getCapteurs();
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
		System.out.println("Nombre de regard : " + valeurs.length);
		/**
		//Je récupère la plus petite distance ainsi que l'indice de cette distance dans le tableau
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
		 */

	}

	public static boolean isDifferent(float v1, float v2) {
		//10 cm
		return Math.abs(v1-v2)>=10;
	}

	public static int minAngle(int[] tab) {
		int min = tab[0];
		for(int i=1;i<tab.length;i++) {
			if(tab[i]<min)
				min=tab[i];
		}
		return min;
	}

	public static int[] minDist(float[][] tab) {
		int minDist = (int)tab[0][1];
		int indx=(int)tab[0][0];
		for(int i=1;i<tab.length;i++) {
			System.out.println("Je regarde : " + tab[i][1]);
			if(tab[i][1]<minDist && tab[i][1]<400 && tab[i][1]>0) {
				indx=(int)tab[i][0];
			}
		}
		System.out.print("dist min : " + minDist + " d'angle : " + indx);
		return (new int[] {indx,minDist});
	}

	public void rechercheAngle2(int angle) {
		Capteurs cpt = robot.getCapteurs();
		tournerDe(angle, true);
		float[] valeurs = new float[0];
		while(isMoving()) {
			//System.out.println(indice);
			//On remplit le tableau de distances
			valeurs = cpt.regarde(valeurs);
		}		
		float[][] potentielsPalets = new float[0][];
		for(int i=0;i<valeurs.length;i++) {
			//On arrondit et on met en CM
			valeurs[i]=(int)(100*valeurs[i]);
		}

		System.out.println("Le nombre de valeurs : " + valeurs.length);

		float actuelleValeur = valeurs[0];

		for(int j=1; j<valeurs.length;j++) {
			if(isDifferent(actuelleValeur, valeurs[j])) {
				//System.out.println(actuelleValeur + " est different de " + valeurs[j]);
				int compteur=1;
				boolean flag=true;
				float lastDist=0;
				for(int m=j+1;m<valeurs.length && flag;m++) {
					if(!isDifferent(valeurs[m],valeurs[m-1]))
						compteur++;
					else {
						flag=false;
						lastDist=valeurs[j+(int)compteur/2];
					}
				}
				//NOUS ADMETTONS QUE C'EST UN PALET SI NOUS LE VOYONS SUR 10° MAX. NOUS DEVONS REVOIR CE "10°" EN 
				//PLACANT UN PALET AU PLUS PROCHE DU ROBOT (DE MANIERE A CE QUIL LE VOIT QD MM) ET MESURER L'ANGLE
				int angleThisPalet = compteur * angle / valeurs.length;
				if(angleThisPalet<=10) {
					//C UN PALET
					//System.out.println("J'AI TROUVE UN PALET");
					int milieu = j+compteur/2;
					int anglePalet = milieu * angle / valeurs.length;
					potentielsPalets = Arrays.copyOf(potentielsPalets, potentielsPalets.length+1);
					potentielsPalets[potentielsPalets.length-1]= new float[]{anglePalet,lastDist};
				}
				j=j+compteur;
			}
			if(j<valeurs.length)
				actuelleValeur=(int)valeurs[j];
		}
		System.out.println("Nombre de palets trouvés : " + potentielsPalets.length);

		//J'AI MON TABLEAU DES PALETS : potentielsPalets
		int[] min= minDist(potentielsPalets);
		float angleMin =min[0];
		float dist =min[1];
		this.tournerDe((int)angleMin, false);

		System.out.println(Arrays.deepToString(potentielsPalets));


		float[] valeurApresOrientation = new float[0];
		valeurApresOrientation = cpt.regarde(valeurApresOrientation);
		//Si la distance est la même à +/- 10cm que ce que le robot voyait en tourant...
		if((int)(100)*valeurApresOrientation[0]>=dist-5 && (int)(100)*valeurApresOrientation[0]<=dist+5) {
			//...le robot avance de la distance.
			//avancerWhileIsNotPressed((int)(1000*dist));
		}
		//Sinon le robot re-recherche.
		else rechercheAngle2(360);
		//Delay.msDelay(10000);


	}


	public static void main(String[] args) {

		Mouvements o = new Mouvements(null);

	}

}
