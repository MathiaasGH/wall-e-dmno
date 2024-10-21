import java.util.Arrays;

import lejos.hardware.motor.Motor;
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
		touche = new EV3TouchSensor (SensorPort.S3);
		vue = new EV3UltrasonicSensor(SensorPort.S4);
		couleur = new EV3ColorSensor(SensorPort.S2);
		spVue = vue.getDistanceMode();
		spTouche = touche.getTouchMode();
		spCouleur = couleur.getColorIDMode();
	}
	
    public boolean isPressed()
    {
        float[] sample = new float[1];
        touche.fetchSample(sample, 0);
        System.out.println(sample[0]!=0);
        return sample[0] != 0;
    }
    
    public void ouvreBras() {
    	Motor.D.setSpeed(150000);
    	Motor.D.rotate(1500);
    }
	
    public void fermeBras() {
    	Motor.D.setSpeed(150000);
    	Motor.D.rotate(-1500);
    }
    
    public void ouvreBrasAsynchrone() {
    	System.out.println("J'ouvre les bras");
    	Motor.D.setSpeed(15000);
    	Motor.D.rotate(2000,true);
    }
	public float[] capteCouleur(float[] tab) {
		float[] newTab = Arrays.copyOf(tab, tab.length+1);
		couleur.fetchSample(newTab, newTab.length-1);
		return newTab; 
	}
	
	/* public float[] capteurDeCouleur() {
	    	colorSensor = new EV3ColorSensor(SensorPort.S2);
	    	average = new MeanFilter(colorSensor.getRGBMode(), 1);
			colorSensor.setFloodlight(Color.WHITE);
			path_color = new float[average.sampleSize()];
			average.fetchSample(path_color, 0);
			return path_color;
	    }
	*/
	
	/*	public String Convertioncouleur(float [] tabCouleurs) {
			double r = couleurs[0];
			double v = couleurs[1];
			double b = couleurs[2];
			if (r>230,v<25,b<25) {
				return "Rouge";
			}
			if (r<25,v>230,b<25) {
				return "Vert";
			}
			if (r<25,v<25,b>230) {
				return "Bleu"; 
			}
			if (r>230,v>230,b<25) {
				return "Jaune";
			}
				if (r>230,v>230,b>230) {
				return "Blanc"; 
			}
			
			else return "neutre";
		}
		*/
	
	public void fermeLesYeux() {
		vue.close();
	}
	public float[] regarde2(float[] tab) {
		float[] newTab = Arrays.copyOf(tab, tab.length+3);
		vue.fetchSample(newTab, newTab.length-3);
		vue.fetchSample(newTab, newTab.length-2);
		vue.fetchSample(newTab, newTab.length-1);
		return newTab;
	}
	public float[] regarde(float[] tab) {
		System.out.println("Je regarde de nouveau");
		float[] newTab = Arrays.copyOf(tab, tab.length+1);
		vue.fetchSample(newTab, newTab.length-1);
		return newTab;
	}
	
	
}
