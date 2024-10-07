import java.util.Arrays;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class Capteurs {

	private EV3UltrasonicSensor vue;
	private EV3TouchSensor touche;
	private EV3ColorSensor couleur;
	private SampleProvider spVue;
	private SampleProvider spTouche;
	private SampleProvider spCouleur;
	private Robot robot;
	
	public Capteurs(Robot r) {
		robot = r;
		touche = new EV3TouchSensor (SensorPort.S1);
		vue = new EV3UltrasonicSensor(SensorPort.S4);
		couleur = new EV3ColorSensor(SensorPort.S2);
		spVue = vue.getDistanceMode();
		spTouche = touche.getTouchMode();
		spCouleur = couleur.getColorIDMode();
	}
	
	public float[] touche(float[] tab){
		float[] newTab = Arrays.copyOf(tab, tab.length+1);
		touche.fetchSample(newTab, newTab.length-1);
		return newTab;
	}
	
	public float[] capteCouleur(float[] tab) {
		float[] newTab = Arrays.copyOf(tab, tab.length+1);
		couleur.fetchSample(newTab, newTab.length-1);
		return newTab; 
	}
	
	public void fermeLesYeux() {
		vue.close();
	}
	public float[] regarde(float[] tab) {
		float[] newTab = Arrays.copyOf(tab, tab.length+1);
		vue.fetchSample(newTab, newTab.length-1);
		return newTab;
	}
}
