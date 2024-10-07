import java.util.Arrays;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class Capteurs {

	private EV3UltrasonicSensor vue;
	private SampleProvider sp;
	private Robot robot;
	
	public Capteurs(Robot r) {
		robot = r;
		vue = new EV3UltrasonicSensor(SensorPort.S4);
		sp = vue.getDistanceMode();
	}
	
	public float[] regarde(float[] tab) {
		float[] newTab = Arrays.copyOf(tab, tab.length+1);
		vue.fetchSample(newTab, newTab.length-1);
		return newTab;
	}
}
