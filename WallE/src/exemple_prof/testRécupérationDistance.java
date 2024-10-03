package exemple_prof;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class testRécupérationDistance {

	public static void main(String[] args) {
		
		DifferentialDrive dd = new DifferentialDrive(MotorPort.A, MotorPort.B);
		//EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S4);
		//SampleProvider dp = us.getDistanceMode();
		
		for(int i=0;i<150;i++)
		dd.rotateClockwise();
		dd.stop();
	    
		}
	
	
	
}
