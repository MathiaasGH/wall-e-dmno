import lejos.hardware.*;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import lejos.robotics.*;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot {

	private Orientation orientation;
	private Capteurs capteurs;
	private Position position;


	public Robot() {
		orientation = new Orientation(this);
		capteurs = new Capteurs(this);
		position = new Position (0,0,'b');
	}
	
	public Capteurs getCapteurs() {
		return capteurs;
	}

	/** Méthode qui fait tourner le robot sans avancer d’un angle
	* @param int angle en degré du tour que doit faire le robot
	*/
	public void tournerDe (int angle) {
		mouvements.tournerDe(angle, false);
	}
	
	public void avancer(int dist) {
		mouvements.avancer(dist);
	}
	
	public void avancerIsPressed() {
		mouvements.avancerWhileIsNotPressed();
	}
		
	/** Méthode qui fait avancer le robot en suivant un arc d’un radius donnée sur une distance donnée
	* @param int radius de l’arc, int distance parcouru
	*/
	public void arc (int radius, int distance){}

	/**public void recherche(int duration) {
		mouvements.recherche(duration);
	}*/
	
	public void rechercheA(int ang) {
		mouvements.rechercheAngle(ang);
	}
	
	public static void main(String[] args) {
		Robot r= new Robot();
		//r.rechercheA(360);
		//r.tournerDe(360);
		//r.avancer();
		//r.avancer(2000);
		r.avancerIsPressed();
	}

}
