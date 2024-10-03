import java.util.Arrays;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;


public class tests {

	public static float min(float[] tab) {
		float min=0;
		for(int i=0;i<tab.length || tab[i]!=0;i++) {
			if(min>tab[i]) {
				
			}
			else
				min=tab[i];
		}
		return min;
	}
	
	public static void main(String[] args) {
		
		//Avancer de 37 cm
		//DifferentialPilot pilot = new DifferentialPilot(1.5, 1.5, Motor.A, Motor.B, true);
		//pilot.travel(10);
	
		/*EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S4);
		SampleProvider dp = us.getDistanceMode();
		float[] valeurs = new float[1];
		dp.fetchSample(valeurs, 0);
		System.out.println(valeurs[0]);
		try {
			Thread.sleep(5000);
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		*/
		
		DifferentialPilot pilot = new DifferentialPilot(1.5, 1.5, Motor.A, Motor.B, true);
		//pilot.setRotateSpeed(100);
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S4);
		SampleProvider dp = us.getDistanceMode();
		float[] valeurs = new float[1000000000];
		float dist=1;
		int i=0;
		pilot.rotate(3000,false);
		while(dist>0.40) {
			//pilot.rotateLeft();
			//System.out.println("Je tourne");
			dp.fetchSample(valeurs, i);
			dist=valeurs[i];
			i++;
		}
		System.out.println(min(valeurs));
		try {
			Thread.sleep(10000);
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		
	}

}
