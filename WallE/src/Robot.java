
import java.util.Arrays;



import lejos.hardware.*;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import lejos.robotics.*;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot extends Mouvements{

	public Robot(int x, int y, char StartSide, boolean etatBras) {
		super(x, y, StartSide, etatBras);
		if(etatBras==true)
			fermeBras();
	}
	/**
	 * Méthode qui permet de récupérer le premier palet, qui sera directement en face du robot au début de l'épreuve
	 */
	public void premierPalet() {
		ouvreBras();
		avancerWhileIsNotPressed(65);		
		fermeBras();
		tournerDe(20);
		avancerDe(10);
		tournerDe(-20);
		//avancerWhileIsNotWhite();
		reOrientationMur();	
		ouvreBras();
		avancerDe(-10);
		tournerDe(180);
	}

	public static void main(String[] args) {
		Robot r= new Robot(0,0,'b',false);
		//r.fermeBras();
		//r.avancerIsPressed();
		//r.fermeBras();
		//r.avancerDe(100,false);
		r.recherche(360);
		//System.out.println(r.angleTheorique(30));
		//r.testTempsRotation(360);
	/**	r.tournerDe(360,true);
		while(r.isMoving()) {
			
		}
			*/
		//r.tournerDe(1,true);
		//r.decoupeValeurs(360);
		//r.trouve();	
		//r.recherche(360);
		//r.rechercheAngle2(360);
		//r.rechercheA(360);
		//.rechercheAngle(360);
		//r.tournerDe(360);
		//r.avancer();
		//r.avancer(100, false);
		//r.avancerIsPressed();
		//System.out.println(r.isPressed());
		//Delay.msDelay(5000);
		//r.reOrientationMur();
		//r.capteurDeCouleur(); 
	}

}

