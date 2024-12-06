

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
			//System.out.println("angleOpti : "+ (int)angleDeRechercheOptimise());
			if (recherche((int)angleDeRechercheOptimise())) {
				allerChezAdversaire();
				paletTrouvé+=1;
				//System.out.println(getDegres());
				continue;
			}
			if (recherche((int)angleDeRechercheOptimise())) {
				allerChezAdversaire();
				paletTrouvé+=1; 
				continue; 
			}
			allerAuCentre(); // est ce que quand il va au centre il recupere le palet qu'il trouve au centre ? ou pas ?
			continue; 
		}
	}

	public static void main(String[] args) {
		Robot r= new Robot(50,0,'b',true);
		//r.fermeBras();
		r.boucleRecherche();
		//r.recherche(360);
	}
}