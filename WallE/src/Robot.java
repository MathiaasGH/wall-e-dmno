import lejos.hardware.*;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import lejos.robotics.*;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot {

	private Orientation orientation;
	private Touch touchS;
	private Motor d;
	private DifferentialPilot mcQueen; 
	private int vitesse; 

	/** Méthode qui fait avancer le robot 
	*/
	public void avancer () {}

	/** Méthode qui fait avancer le robot d’une distance d en cm
	* @param d une distance en cm
	*/
	public void avancerDe (int d) {}

	/** Méthode qui fait avancer le robot jusqu’à un évènements b 
	* @param b boolean état d’un évènements
	*/
	public void avancerJusqua (boolean b) {}

	/** Méthode qui fait reculer le robot 
	*/
	public void reculer () {}

	/** Méthode qui fait reculer le robot d’une distance de d cm
	* @param int d la distance à parcourir
	*/
	public void reculerDe (int d) {}

	/** Méthode qui permet d’ouvrir la pince du robot
	*/
	public void ouverture () {}

	/** Méthode qui permet de fermer la pince du robot
	*/
		public void fermeture () {}

		/** Méthode qui fait permet de modifier la vitesse de déplacement du robot
	* @param int v la nouvelle vitesse du robot
	*/
	public void setVitesse (int v) {}


	/** Méthode qui fait permet de renvoyer l’entier qui correspond à la couleur perçu par
	*le capteur
	* @return int le code correspondant à la couleur perçu
	*/
	public int capteurCouleur () {}

	/** Méthode qui fait renvoie true si un ennemi est en vue
	* @return boolean true si ennemi détecté
	*/
	 public boolean ennemi() {}

	/** Méthode qui fait renvoie true si le capteur tactile est enfoncé
	* @return boolean true si capteur tactile enfoncé
	*/
	public boolean tactileControle () {}


	/** Méthode qui fait tourner le robot sans avancer d’un angle
	* @param int angle en degré du tour que doit faire le robot
	*/
	public void tourner (int angle) {}
		
	/** Méthode qui fait avancer le robot en suivant un arc d’un radius donnée sur une distance donnée
	* @param int radius de l’arc, int distance parcouru
	*/
	public void arc (int radius, int distance){}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
