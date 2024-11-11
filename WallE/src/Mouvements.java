package wallHeredite;

import lejos.robotics.navigation.MovePilot;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.*;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class Mouvements extends Position{

	private final static int DELAY = 25;
	private Wheel wheel1;
	private Wheel wheel2;
	private Chassis chassis;
	private MovePilot pilot; 
	private boolean brasOuvert;

	public Mouvements(int x, int y, char StartSide, boolean etatBras) {
		super(x, y, StartSide);
		brasOuvert = etatBras;
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

	/**
	 * Méthode qui permet d'avancer de dist de manière asynchrone
	 * @param dist en cm
	 */
	public void avancer(int dist) {
		avancer(dist,true); // A VOIR
	}

	
	/**
	 * Méthode qui permet d'avancer de dist et de manière asynchrone ou non en fonction de b
	 * @param dist en cm
	 * @param b si true asynchrone sinon non asynchrone
	 */
	public void avancer(int dist,boolean b) { // A VOIR
		pilot.travel(dist,b);                 
		updatePosition(dist);
	}

	/**
	 * Permet de tourner de angle de manière asynchrone
	 * @param angle en degrés
	 */
	public void tournerDe(int angle) {
		tournerDe(angle, true);
	}

	/**
	 * Méthode qui permet de tourner de angle de manière asynchrone ou non en fonction de b
	 * @param angle en degrés 
	 * @param asynchrone si true asynchrone sinon non asynchrone
	 */
	public void tournerDe(int angle, boolean asynchrone) {
		pilot.rotate(angle, asynchrone); 
		updateOrientation(angle);
	}

	/**
	 * Méthode qui permet d'ouvrir les bras si ils ne sont pas déjà ouvert
	 */
	public void ouvreBras() {
		if (brasOuvert==true) {
			return;
		}
		Motor.D.setSpeed(150000);
		Motor.D.rotate(1500);
		brasOuvert=true;
	}

	/**
	 * Méthode qui permet d'ouvrir les bras si ils ne sont pas déjà ouvert de manière asynchrone
	 */
	public void ouvreBrasAsynchrone() {
		if (brasOuvert==true) {
			return;
		}
		Motor.D.setSpeed(15000);
		Motor.D.rotate(2000,true);
		brasOuvert=true;
	}

	/**
	 * Méthode qui permet de fermer les bras si ils ne sont pas déjà fermé
	 */
	public void fermeBras() {
		if (brasOuvert==false) {
			return;
		}
		Motor.D.setSpeed(150000);
		Motor.D.rotate(-1500);
		brasOuvert=false;
	}

	/**
	 * Méthode qui permet de savoir si le robot est en mouvement 
	 * @return boolean true si le robot bouge, false sinon
	 */
	public boolean isMoving() {
		return pilot.isMoving();
	}

	/**
	 * Méthode qui permet de faire stopper les mouvement du robot
	 */
	private void stop() {
		pilot.stop();
	}

	/**
	 * Méthode qui permet de créer un délaie
	 */
	private void delay(){
		Delay.msDelay(DELAY);
	}

	/**
	 * Méthode qui renvoie le minimum d'un tableau de float ainsi que l'indice auquel il à été trouvé
	 * @param tab tableau de float avec des distance et les indices auquel elles ont été trouvé
	 * @return tableau de float avec le minimum de tab et l'indice auquel le robot la trouvé
	 */
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

	/**
	 * Méthode qui renvoie un boolean qui indique si la distance à un objet diminue.
	 * @param tab tableau de float
	 * @return true si la distance diminue, false sinon
	 */
	public boolean distanceDiminue(float[] tab) {
		if(tab.length>=2)
			return tab[tab.length-1]<=tab[tab.length-2];
		return true;
	}

	/**
	 * Méthode qui recherche un palet et avance tant que le palet n'a pas toucher le capteur de toucher
	 * @param dist un int, la distance maximum à parcourir
	 */
	public void avancerWhileIsNotPressed(int dist) {
		// Creation d'un boolean pour ouvrir les bras une seule fois
		boolean dejaOuvert = false;
		// Initialisation des capteurs et des distances
		float[] distance = regarde(new float[0]);
		//System.out.println(pilot.getAngularSpeed());
		pilot.setAngularSpeed(70);
		// Avancer de manière asynchrone sur la distance spécifiée + 5 cm
		this.avancer(dist + 100);
		// Boucle tant que le robot est en mouvement et que le capteur de toucher n'est pas pressé
		while (isMoving() && !isPressed()) {
			// Vérifie les distances uniquement si elles sont disponibles
			distance = regarde(distance);
			if (distance.length > 0) {
				float derniereDistance = distance[distance.length - 1];

				// Vérifie si la distance est inférieure à 30 cm pour ouvrir les bras
				System.out.println(derniereDistance);
				if (derniereDistance < 0.35) {
					ouvreBrasAsynchrone();
					dejaOuvert=true;
				}
			}


		}

		// Arrête le robot et ferme les bras une fois la boucle terminée
		fermeBras();
		pilot.stop();
	}

	/**
	 * Méthode qui permet de rechercher un angle où il y aurait un palet, ce positionné en face et aller le toucher
	 * @param angle int en degrès, l'angle sur lequel le robot vas faire la recherche
	 */
	public void rechercheAngle(int angle) {
		fermeBras();
		pilot.setAngularSpeed(100);
		//Je tourne de angle de manière asynchrone
		tournerDe(angle, true);
		//J'initialise un tableau dans lequel on range les distances que l'on voit
		float[] valeurs = new float[0];
		int indice = 0;
		//Tant que le robot bouge...
		while(isMoving()) {
			//On remplit le tableau de distances
			valeurs = regarde(valeurs);
			indice++;
		}
		//Je récupère la plus petite distane ainsi que l'indice de cette distance dans le tableau
		float[] min = min(valeurs);
		System.out.println((int)min[1] + " " + angle + " " + indice);
		//Je déduis l'angle grâce à un produit en croix
		int angleMin = ((int)min[1] * angle ) / (int)indice;
		float dist = min[0];
		//J'optimise l'angle à tourner pour que le robot s'aligne avec l'objet
		if(angleMin<angle/2) {
			this.tournerDe(angleMin, false);
		}
		else this.tournerDe(-(angle-angleMin),false);
		//chercherpalet((int)(1000*min[0]) + 2);
		//Je crée un tableau dans lequel je range la distance entre le robot et l'objet le plus proche de lui
		//après s'être ré-orienté
		float[] valeurApresOrientation = new float[0];
		valeurApresOrientation = regarde(valeurApresOrientation);
		//Si la distance est la même à +/- 10cm que ce que le robot voyait en tourant...
		if(valeurApresOrientation[0]>=dist-0.1 && valeurApresOrientation[0]<=dist+0.1) {
			//...le robot avance de la distance.
			avancerWhileIsNotPressed((int)(1000*dist));
		}
		//Sinon le robot re-recherche.
		else rechercheAngle(360);
		Delay.msDelay(10000);
	}

	/**
	 * Méthode qui permet d'avancer vers un palet tant que le robot ne le touche pas en vérifiant que la distnace diminue
	 * @param d int en cm, distance maximum à parcourir
	 */
	public void chercherpalet(int d) {
		float[] valeurs = new float[2];
		valeurs[0] = 1000000;
		valeurs[1] = 1000000;
		avancer(d);
		while(isMoving() && (valeurs[valeurs.length-1]<= valeurs[valeurs.length-2]) && !isPressed()) {
			valeurs = regarde(valeurs);
		}
		pilot.stop();
	}
	/**
	 * Méthode qui permet de regarder le mur en face quand on pose un palet dans le camp adverse afin de retrouver l'angle 0 pour se 
	 * replacer bien en face. 
	 * Ce place tout droit devant le mur et met à jour l'orientation à 0 degrès. 
	 */
	public void reOrientationMur() {
		tournerDe(-45, false);
		pilot.setAngularSpeed(40);
		//Je tourne de angle de manière asynchrone
		tournerDe(90, true);
		//J'initialise un tableau dans lequel on range les distances que l'on voit
		float[] valeurs = new float[0];
		int indice = 0;
		//Tant que le robot bouge...
		while(isMoving()) {
			//On remplit le tableau de distances
			valeurs = regarde(valeurs);
			indice++;
		}
		//Je récupère la plus petite distane ainsi que l'indice de cette distance dans le tableau
		float[] min = min(valeurs);
		//Je déduis l'angle grâce à un produit en croix
		int angleMin = ((int)min[1] * 90 ) / (int)indice;
		tournerDe(angleMin-85,false);
		setDegres(0);
	}

	/**
	 * A TESTER
	 * Méthode qui fait tourner le robot vers le camp adverse puis qui avance jusqu'à la ligne blanche. Il lache le palet, recule
	 * de 5 cm puis fait un demi tour complet arpès avoir appelé reOrientationMur() pour update l'orientation. 
	 * 
	 */
	public void allerChezAdversaire() {
		tournerDe((int)degresAuCampAdverse(),false);
		//avancerWhileIsNotWhite(); ------------------------------> faut créer cette méthodes
		ouvreBras();
		reOrientationMur();
		avancer(-5);
		tournerDe(180,false);
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
	}*/

	public static void main(String[] args) {
	}

}
