import lejos.hardware.*;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import lejos.robotics.*;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot {

	private Orientation orientation;
	private Capteurs capteurs;

	public Robot() {
		orientation = new Orientation(this);
		capteurs = new Capteurs(this);
	}
	
	public Capteurs getCapteurs() {
		return capteurs;
	}

	/** Méthode qui fait tourner le robot sans avancer d’un angle
	* @param int angle en degré du tour que doit faire le robot
	*/
	public void tournerDe (int angle) {
		orientation.tournerDe(angle);
	}
	
	public void avancer() {
		orientation.avancer();
	}
		
	/** Méthode qui fait avancer le robot en suivant un arc d’un radius donnée sur une distance donnée
	* @param int radius de l’arc, int distance parcouru
	*/
	public void arc (int radius, int distance){}

	/**public void recherche(int duration) {
		orientation.recherche(duration);
	}*/
	
	public void rechercheA(int ang) {
		orientation.rechercheAngle(ang);
	}
	
	public static void main(String[] args) {
		Robot r= new Robot();
		//r.rechercheA(330);
		//r.tournerDe(180);
		r.avancer();
	}

}
