import lejos.hardware.Button;

/**
* Cette classe permet de réaliser l'algorithme du robot.
* Cette classe hérite de la classe mouvements.
*
* @see Robot
* @author DEVILLIERS, MITTON, NDONG, OZTURK
*/
public class Robot extends Mouvements{

	/** L'entier pour savoir si le robot tournera à droite (45) apres avoir pris
	 * le premier palet ou alors si il tourner a gauche (-45).
	 */
	public static final int tourneDebut = 45;
	
	/**
	 * Constructeur pour la classe robot
	 * Permet d'initialiser la position initiale du robot ainsi
	 * que l'état de ses bras
	* @param x un entier prenant une valeur suivante {50,100,150}
	 * @param y un entier = 0
	 * @param etatBras un booléen représentant l'état des bras initial.
	 */
	public Robot(int x, int y, boolean etatBras) {
		super(x, y, etatBras);
		if(etatBras==true)
			fermeBras();
	}

	/**
	 * Permet de récupérer le premier palet, qui sera directement en face du robot au début de l'épreuve
	 */
	public void premierPalet() {
		ouvreBras();
		avancerDeRapide(650, false);
		fermeBras();
		tournerDeRapide(tourneDebut, false);
		avancerDeRapide(200, false);
		tournerDeRapide(-tourneDebut, false);
		avancerVigilantAllerAuCamp();
		reOrientationMur();
		ouvreBras();
		avancerDeRapide(-150, false);
		tournerDeRapide(135, false);
		fermeBras();
	}
	
	/**
	 * Correspond à l'algorithme principal du robot, permet d'effectuer une boucle 
	 * pour récupérer le premier palet puis de faire des recherches optimisées de palets
	 * pour les ramener dans le camps adverse.
	 */
	public void boucleRecherche() {
		System.out.println("Appuyez sur entrer");
		Button.ENTER.waitForPressAndRelease();
		
		premierPalet(); 
		int paletTrouvé = 1; 
		while(paletTrouvé<9) {
			if (recherche((int)angleDeRechercheOptimise())) {
				allerChezAdversaire();
				paletTrouvé+=1;
				continue;
			}
			if (recherche((int)angleDeRechercheOptimise())) {
				allerChezAdversaire();
				paletTrouvé+=1; 
				continue; 
			}
			allerAuCentre();
			continue; 
		}
	}

	public static void main(String[] args) {
		Robot r= new Robot(100,0,false);
		r.boucleRecherche();
	}
}