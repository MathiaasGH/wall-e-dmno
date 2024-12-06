

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
		Robot r= new Robot(100,0,'b',false);
		
		//r.ouvreBras();
		
		//r.avancerIsPressed();
		//r.fermeBras();
		//r.avancerDe(100,false);
		//r.avancerWhileIsNotPressed(300);
		//r.avancerDe(1200,false);	
		//r.tournerDe(360,false);
		//r.avancerWhileIsNotPressed(200);
		r.recherche(90);
		//r.MiseAjourPos();
		//System.out.println(r.angleTheorique(30));
		//r.testTempsRotation(360);
	/*	r.tournerDe(360,true);
		while(r.isMoving()) {
			
		}
			*/
		//r.tournerDe(1,true);
		//r.decoupeValeurs(360);
		//r.trouve();	
		//r.recherche(360);
		//r.rechercheAngle2(360);
		//r.rechercheA(360);
		//r.tournerDe(360);
		//r.avancer();
		//r.avancer(100, false);
		//r.avancerIsPressed();
		//System.out.println(r.isPressed());
		//Delay.msDelay(5000);
		//r.reOrientationMur();
		//r.capteurDeCouleur(); 
		//r.avancerJusquaCouleur("Blanc");
		//r.avancerVigilantAllerAuCamp();
	}
}