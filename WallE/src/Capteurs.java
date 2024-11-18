import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;
import lejos.robotics.Color;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.hardware.ev3.LocalEV3; 
import lejos.hardware.port.Port;

public class Capteurs {
	private EV3UltrasonicSensor vue;
	private EV3TouchSensor touche;
	private EV3ColorSensor colorSensor;
	private SampleProvider spVue;
	private SampleProvider spTouche;
	private SampleProvider colorProvider;

	public Capteurs() {
		touche = new EV3TouchSensor (SensorPort.S3);
		vue = new EV3UltrasonicSensor(SensorPort.S4);
		colorSensor = new EV3ColorSensor(SensorPort.S2);
		spVue = vue.getDistanceMode();
		spTouche = touche.getTouchMode();
		System.out.println("Classe capteurs instanciée");
	}

	/**
	 * Méthode qui renvoie true si le TouchCapteur est enclenché. 
	 * @return true si le ToucheCapteur est enclenché
	 */
	public boolean isPressed()	{
		float[] sample = new float[1];
		touche.fetchSample(sample, 0);
		System.out.println(sample[0]!=0);
		return sample[0] != 0;
	}

	/**
	 * Méthode qui permet de renvoyer le codage RGB de la couleur
	 * @param tab un tableau de float
	 * @return le tableau de float contenant les valeur du capteur de couleur
	 */
	public void capteurDeCouleur() {
		colorProvider =  colorSensor.getRGBMode();
		float[] colorSample = new float[colorProvider.sampleSize()];

		while (Button.ESCAPE.isUp()){
			colorProvider.fetchSample(colorSample,0);
			System.out.println("R" + colorSample [0]);
			System.out.println("G" + colorSample[1]);
			System.out.println("B" + colorSample[2]);
			Delay.msDelay(5000);
		}
	}

	/**
	 * Méthode qui permet de récolter les données du capteur d'ultraSon dans un tableau de float
	 * @param tab un tableau de float
	 * @return le tableau de float contenant les valeur du capteur d'ultraSon
	 */
	
	public String Convertioncouleur(float [] tabCouleurs) {
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
		if ((r>0.15 && r<0.25) && (v>0.135 && v<0.145) && (b>0.155 && b<0.165)) {
				return "Blanc"; 
			}
		else return "Autre";
	}
	
	public float[] regarde(float[] tab) {
		//System.out.println("Je regarde de nouveau");
		float[] newTab = Arrays.copyOf(tab, tab.length+1);
		vue.fetchSample(newTab, newTab.length-1);
		return newTab;
	}

	/**
	 * Méthode qui arrête les capteur d'ultraSon
	 */
	public void fermeLesYeux() {
		vue.close(); 
	}


}

