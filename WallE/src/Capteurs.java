import java.util.Arrays;

import lejos.hardware.*;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
* Cette classe permet de gérer les capteurs du robot.
*
* @see Position
* @author DEVILLIERS, MITTON, NDONG, OZTURK
*/
public class Capteurs {

	/** L'EV3UltrasonicSensor correspondant au capteur visuel.*/
	private EV3UltrasonicSensor vue;
	/** L'EV3TouchSensor correspondant au capteur tactile.*/
	private EV3TouchSensor touche;
	/** L'EV3ColorSensor correspondant au capteur de colorimétrie.*/
	private EV3ColorSensor colorSensor;
	/** Le SampleProvider correspondant à la sortie des données captées par 
	 * le capteur visuel.*/
	public SampleProvider spVue;
	/** Le SampleProvider correspondant à la sortie des données captées par 
	 * le capteur de colorimétrie.*/
	private SampleProvider colorProvider;

	/**
	 * Constructeur de la classe Capteurs.
	 * Permet d'initialiser tous les capteurs du robots aux ports correspondants.
	 */
	public Capteurs() {
		touche = new EV3TouchSensor (SensorPort.S3);
		vue = new EV3UltrasonicSensor(SensorPort.S4);
		colorSensor = new EV3ColorSensor(SensorPort.S2);
		spVue = vue.getDistanceMode();
	}

	/**
	 * Renvoie true si le TouchCapteur est enclenché. 
	 * @return true si le ToucheCapteur est enclenché
	 */
	public boolean isPressed()	{
		float[] sample = new float[1];
		touche.fetchSample(sample, 0);
		return sample[0] != 0;
	}

	
	

	/**
	 * Permet de convertir un tableau de données d'ultrasons en chaine de caractère
	 * correspondant à une couleur.
	 * @param tab un tableau de float
	 * @return la couleur correspondant au tableau de données fourni en paramètres.
	 */
	public String ConvertionCouleur(float [] tabCouleurs) {
		double r = tabCouleurs[0];
		double v = tabCouleurs[1];
		double b = tabCouleurs[2];
		if ((r>0.03 && r<0.04) && (v>0.06 && v<0.07) && (b>0.03 && b<0.04)) {
			return "Vert";
		}
		if ((r>0.08 && r<0.09) && (v>0.015 && v<0.025) && (b>0.015 && b<0.025)) {
			return "Rouge";
		}
		if ((r>0.01 && r<0.02) && (v>0.02 && v<0.03) && (b>0.045 && b<0.055)) {
			return "Bleu"; 
		}
		if ((r>0.055 && r<0.065) && (v>0.045 && v<0.055) && (b>0.06 && b<0.07)) {
			return "Gris";
		}
		if ((r>0.14 && r<0.17) && (v>0.1 && v<0.15) && (b>0.035 && b<0.045)) {
			return "Jaune"; 
		}
		if ((r>0.0075 && r<0.0085) && (v>0.0065 && v<0.0075) && (b>0.0075 && b<0.0085)) {
			return "Noir"; 
		}
		if ((r>0.15 && r<0.25) && (v>0.10 && v<0.20) && (b>0.10 && b<0.20)) {
			return "Blanc"; 
		}
		else return "Autre";
	}

	/**
	 * Permet de renvoyer le codage RGB de la couleur vue.
	 * @param tab un tableau de float
	 * @return le tableau de float contenant les valeur du capteur de couleur
	 */
	public String capteurDeCouleur() {
		colorProvider =  colorSensor.getRGBMode();
		float[] colorSample = new float[colorProvider.sampleSize()];
		while (Button.ESCAPE.isUp()){
			colorProvider.fetchSample(colorSample,0);
			String couleur = ConvertionCouleur(colorSample);
			return couleur;
		}
		return "rien";
		}

	/**
	 * Permet de regarder une valeur devant le robot et de la ranger dans le tableau
	 * fourni en paramètres en l'aggrandissant d'une case.
	 * @param tab le tableau ayant déjà (ou pas) des données.
	 * @return un tableau de float ayant tous les éléments du tableau d'origine 
	 * avec la distance lue devant le robot en dernier élément.
	 */
	public float[] regarde(float[] tab) {
		float[] newTab = Arrays.copyOf(tab, tab.length+1);
		vue.fetchSample(newTab, newTab.length-1);
		Delay.msDelay(25);
		return newTab;
	}

	/**
	 * Arrête les capteur d'ultraSon
	 */
	public void fermeLesYeux() {
		vue.close(); 
	}


}

