import java.util.Arrays;

import lejos.hardware.*;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import lejos.robotics.*;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot extends Mouvements{

	public Robot(int x, int y, char StartSide, boolean etatBras) {
		super(x, y, StartSide, etatBras);
	}
<<<<<<< HEAD

	public Capteurs getCapteurs() {
		return capteurs;
=======
	
	/**
	 * Méthode qui permet de récupérer le premier palet, qui sera directement en face du robot au début de l'épreuve
	 */
	public void premierPalet() {
		ouvreBras();
		avancerWhileIsNotPressed(65);
		fermeBras();
		tournerDe(20);
		avancer(10);
		tournerDe(-20);
		//avancerWhileIsNotWhite();
		reOrientationMur();
		ouvreBras();
		avancer(-10);
		tournerDe(180);
>>>>>>> branch 'main' of https://github.com/MathiaasGH/wall-e-dmno.git
	}

<<<<<<< HEAD
	/** Méthode qui fait tourner le robot sans avancer d’un angle
	 * @param int angle en degré du tour que doit faire le robot
	 */
	public void tournerDe (int angle) {
		mouvements.tournerDe(angle, false);
	}

	public void avancer(int dist) {
		mouvements.avancer(dist);
	}

	public void avancer(int dist, boolean b) {
		mouvements.avancer(dist,b);
	}

	public void avancerIsPressed() {
		float[] dist = new float[0];
		dist=capteurs.regarde(dist);
		mouvements.avancerWhileIsNotPressed((int)(1000*dist[0]));
	}

	/** Méthode qui fait avancer le robot en suivant un arc d’un radius donnée sur une distance donnée
	 * @param int radius de l’arc, int distance parcouru
	 */
	public void arc (int radius, int distance){}

	/**public void recherche(int duration) {
		mouvements.recherche(duration);
	}*/

	public void rechercheA(int ang) {
		//capteurs.fermeBras();
		mouvements.rechercheAngle(ang);
	}

	public void rechercheA2(int ang) {
		//capteurs.fermeBras();
		mouvements.rechercheAngle2(ang);
	}

	public boolean isPressed() {
		return capteurs.isPressed();
	}

	public void fermeBras() {
		capteurs.fermeBras();
	}

	public void ouvreBras() {

		capteurs.ouvreBras();
	}

	public void regarde() {
		float[] regard = new float[0];
		regard= capteurs.regarde(regard);
		System.out.println(Arrays.toString(regard));
	}

=======
>>>>>>> branch 'main' of https://github.com/MathiaasGH/wall-e-dmno.git
	public static void main(String[] args) {
<<<<<<< HEAD
		Robot r= new Robot();
		//for(int i=0;i<3;i++) {
		//r.fermeBras();
		//r.ouvreBras();
		//}
		//r.avancer(10);

		//r.regarde();
=======
		Robot r= new Robot(0,0,'b', false);
>>>>>>> branch 'main' of https://github.com/MathiaasGH/wall-e-dmno.git
		//r.avancerIsPressed();
<<<<<<< HEAD
		r.rechercheA2(360);
		//r.rechercheA(360);
=======
		//.rechercheAngle(360);
>>>>>>> branch 'main' of https://github.com/MathiaasGH/wall-e-dmno.git
		//r.tournerDe(360);
		//r.avancer();
		//r.avancer(100, false);
		//r.avancerIsPressed();
		//System.out.println(r.isPressed());
<<<<<<< HEAD
		Delay.msDelay(20000);
=======
		//Delay.msDelay(5000);
		//r.reOrientationMur();
		r.capteurDeCouleur(); 
>>>>>>> branch 'main' of https://github.com/MathiaasGH/wall-e-dmno.git
	}

}

