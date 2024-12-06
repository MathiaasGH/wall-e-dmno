

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
		avancerWhileIsNotPressed(650);
		fermeBras();
		tournerDe(45, false);
		avancerDe(200, false);
		tournerDe(-45, false);
		avancerVigilantAllerAuCamp();
		reOrientationMur();
		ouvreBras();
		avancerDe(-150, false);
		tournerDe(180, false);
		fermeBras();
	}
	
	public void boucleRecherche() {
		premierPalet(); 
		int paletTrouvé = 1; 
		while(paletTrouvé<9) {
			if (recherche((int)angleDeRechercheOptimise())) {
				paletTrouvé+=1;
				continue;
			}
			if (recherche((int)angleDeRechercheOptimise())) {
				paletTrouvé+=1; 
				continue; 
			}
			allerAuCentre(); // est ce que quand il va au centre il recupere le palet qu'il trouve au centre ? ou pas ?
			continue; 
		}
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