package exemple_prof;
import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.nxt.NXTCommand;
import lejos.remote.nxt.RemoteNXTPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Controller
{
    private final static int DELAY = 25;

    private final static int SKIP_FORWARD = 10;

    private final static float DELAYS_PER_DEG = 100.0f / 180;

    private final static int SMALL_ROT = 5;
    private final static int MED_ROT = 10;
    private final static int MAX_ROT = 30;

    private ColorSensor sensor;
    private DifferentialDrive drive;

    public Controller(Port sensor_port, Port left_port, Port right_port)
    {
        log("Initializing Controller");

       // sensor = new ColorSensor(sensor_port);
        drive = new DifferentialDrive(left_port, right_port);
    }

    public void run()
    {
        log("Running controller");

        do
        {
            move();
        }
        while (wide_seek());

        end();
    }

    private void move()
    {
        log("Forward");

        do
        {
            naive_move();
        }
        while (narrow_seek());

        log("Stop");
    }

    private void naive_move()
    {
        log("tutu");
        drive.forward();
        log("toto");
        System.out.println(sensor.onPath());
        //Button.ENTER.waitForPressAndRelease();
        
        while (sensor.onPath())
        {
            delay();
            System.out.println(sensor.onPath());
            //Button.ENTER.waitForPressAndRelease();
        }

        drive.stop();
    }

    private boolean narrow_seek()
    {
        log("Seeking Path in Narrow Arc.");

        return sweepClockwise(SMALL_ROT) || sweepCounterClockwise(2 * SMALL_ROT) || sweepClockwise(SMALL_ROT);
    }

    private void skip_forward(int duration)
    {
        drive.forward();

        for (int i = 0; i < duration; i++)
        {
            delay();
            
            //TEST ASYNCHRONIE
            
            System.out.print("GO");
            
            // FIN DU TEST
        }

        drive.stop();
    }

    private boolean wide_seek()
    {
        skip_forward(SKIP_FORWARD);

        return sweepClockwise(MED_ROT) ||
               sweepCounterClockwise(MED_ROT + MAX_ROT) ||
               sweepClockwise(2 * MAX_ROT) ||
               sweepCounterClockwise(MAX_ROT);
    }

    private boolean sweepClockwise(int rot_limit)
    {
        log("Sweeping clockwise. Limit: " + rot_limit + " degrees.");

        drive.rotateClockwise();

        return sweep(rot_limit);
    }

    private boolean sweepCounterClockwise(int rot_limit)
    {
        log("Sweeping counter-clockwise. Limit: " +  rot_limit + " degrees");

        drive.rotateCounterClockwise();

        return sweep(rot_limit);
    }

    private boolean sweep(int rot_limit)
    {

    	
        for (int i = 0; i < (rot_limit * DELAYS_PER_DEG); i++)
        {
            delay();

            System.out.println(sensor.onPath());
            //Button.ENTER.waitForPressAndRelease();
            
            if (sensor.onPath())
            {
                log("Path Detected");

                drive.stop();

                return true;
            }
        }

        log("Path not detected. Rotation limit exceeded.");

        drive.stop();

        return false;
    }

    private void end()
    {
        Sound.beepSequence();

        log("Program ends");
    }
    
   public static float min(float[] tab) {
	   float min=100000;
	   for(int i=0;i<tab.length;i++) {
		   if(tab[i]!=0 && tab[i]<min) {
			   min=tab[i];
		   }
	   }
	   return min;
   }
    
    private void cherchePlusProcheObj(int duration) {
    	
    	drive.rotateClockwise();
    	EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S4);
		SampleProvider dp = us.getDistanceMode();
		float[] valeurs = new float[125];
		for (int i = 0; i < duration; i++)
        {
            delay();
            System.out.println(i);
            if(i%2==0) {
            dp.fetchSample(valeurs, i);
            delay();
            }
            
            if(i==duration-1) {
            	us.close();
            }
        }
		System.out.println("Le minimum est : " + min(valeurs));
		//System.out.println(Arrays.toString(valeurs));
		Delay.msDelay(10000);
        drive.stop();
    }

    private void delay()
    {
        Delay.msDelay(DELAY);
    }

    private static void log(String msg)
    {
        System.out.println("log>\t" + msg);
    }
    
    public static void main(String[] args) {
		
    	Controller c = new Controller(SensorPort.S2, MotorPort.A, MotorPort.B);
    	//c.sweepClockwise(300);
 
    	c.cherchePlusProcheObj(DELAY*5);

	}
}
