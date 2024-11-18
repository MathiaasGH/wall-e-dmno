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

