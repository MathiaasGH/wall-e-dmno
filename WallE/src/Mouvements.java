import lejos.robotics.navigation.MovePilot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.*;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;


public class Mouvements extends Position {

	private final static double vitesse=20/1338.55;
	private final static double tailleDuPalet=6;
	private final static int angleAjustement = 40;
	private final static int timeToWait = 4000;
	private final static int DELAY = 25;
	private Wheel wheel1;
	private Wheel wheel2;
	private Chassis chassis;
	private MovePilot pilot; 
	private boolean brasOuvert;

	public Mouvements(int x, int y, char StartSide, boolean etatBras) {
		super(x, y, StartSide);
		brasOuvert = etatBras;
		wheel1 = WheeledChassis.modelWheel(Motor.B, 56).offset(-62);
		wheel2 = WheeledChassis.modelWheel(Motor.C, 56).offset(62);
		chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, WheeledChassis.TYPE_DIFFERENTIAL); 
		pilot = new MovePilot(chassis);
		System.out.println("Classe mouvement instanciee");
	}

	/** Méthode qui renvoie la nouvelle position d’un objet
	 * @param int id l’identité de l’objet
	 * @return retourne la nouvelle position de l’objet
	 */
	public void actualiser(){

	}

	public void avanceChronometre() {
		for(int i=0;i<10;i++) {
			long tempsAvant= System.currentTimeMillis();
			avancerDe(200,false);
			long tempsApres = System.currentTimeMillis();
			System.out.println(tempsApres-tempsAvant);
		}

	}

	/**
	 * Méthode qui permet d'avancer de dist de manière asynchrone
	 * @param dist en cm
	 */
	public long avancerDe(int dist) {
		return avancerDe(dist,true); // A VOIR
	}


	/**
	 * Méthode qui permet d'avancer de dist et de manière asynchrone ou non en fonction de b
	 * @param dist en cm
	 * @param b si true asynchrone sinon non asynchrone
	 */
	public long avancerDe(int dist,boolean b) { // A VOIR
		pilot.setAngularSpeed(200);
		pilot.setLinearSpeed(200);
		long tempsAvant= System.currentTimeMillis();
		pilot.travel(dist,b);                 
		updatePosition(dist/10);
		return tempsAvant;
	}

	public float calculeDistRestante(long tempsAvantDeRouler, int distanceAavancer) {
		long tempsMaintenant= System.currentTimeMillis();
		long diffTemp = tempsMaintenant-tempsAvantDeRouler;
		System.out.println("temps avant " + tempsAvantDeRouler + " tempsApres : " + tempsMaintenant);
		int distParcouru = (int)(diffTemp*vitesse);
		System.out.println("" + distanceAavancer + " " + distParcouru + " " + (distanceAavancer-distParcouru));
		return distanceAavancer-distParcouru;
	}

	/**
	 * Permet de tourner de angle de manière asynchrone
	 * @param angle en degrés
	 */
	public void tournerDe(int angle) {
		tournerDe(angle, true);
	}

	/**
	 * Méthode qui permet de tourner de angle de manière asynchrone ou non en fonction de b
	 * @param angle en degrés 
	 * @param asynchrone si true asynchrone sinon non asynchrone
	 */
	public void tournerDe(int angle, boolean asynchrone) {
		//40
		pilot.setAngularSpeed(40);
		pilot.setLinearSpeed(40);
		pilot.rotate(angle, asynchrone); 
		updateOrientation(angle);
	}

	public void tournerDeRapide(int angle, boolean asynchrone) {
		pilot.setAngularSpeed(500);
		pilot.setLinearSpeed(500);
		pilot.rotate(angle, asynchrone); 
		updateOrientation(angle);
	}
	
	
	/**
	 * Méthode qui permet de fermer les bras si ils ne sont pas déjà fermé
	 */
	public void fermeBras() {
		if (brasOuvert==false) {
			return;
		}
		Motor.D.setSpeed(150000);
		Motor.D.rotate(-1200);
		brasOuvert=false;
	}

	/**
	 * Méthode qui permet d'ouvrir les bras si ils ne sont pas déjà ouvert
	 */
	public void ouvreBras() {
		if (brasOuvert==true) {
			return;
		}
		Motor.D.setSpeed(150000);
		Motor.D.rotate(1200);
		brasOuvert=true;
	}

	/**
	 * Méthode qui permet d'ouvrir les bras si ils ne sont pas déjà ouvert de manière asynchrone
	 */
	public void ouvreBrasAsynchrone() {
		//System.out.println("je rentre pour ouvrir les bras asynchrone et " + brasOuvert);
		if (brasOuvert==true) {
			return;
		}
		Motor.D.setSpeed(15000);
		Motor.D.rotate(2000,true);
		brasOuvert=true;
	}

	/**
	 * Méthode qui permet de savoir si le robot est en mouvement 
	 * @return boolean true si le robot bouge, false sinon
	 */
	public boolean isMoving() {
		return pilot.isMoving();
	}

	/**
	 * Méthode qui permet de faire stopper les mouvement du robot
	 */
	private void stop() {
		pilot.stop(); 
	}

	/**
	 * Méthode qui permet de créer un délaie
	 */
	private void delay(){
		Delay.msDelay(DELAY);
	}

	/**
	 * Méthode qui renvoie le minimum d'un tableau de float ainsi que l'indice auquel il à été trouvé
	 * @param tab tableau de float avec des distance et les indices auquel elles ont été trouvé
	 * @return tableau de float avec le minimum de tab et l'indice auquel le robot la trouvé
	 */
	private static float[] min(float[] tab) {
		float min=100000;
		int indice=-1;
		for(int i=0;i<tab.length;i++) {
			if(tab[i]!=0 && tab[i]<min) {
				min=tab[i];
				indice=i;
			}
		}
		float[] tabR = new float[2];
		tabR[0] = min;
		//System.out.println("L'indice : " + indice);
		tabR[1] = indice;
		return tabR;
	}

	/**
	 * Méthode qui renvoie un boolean qui indique si la distance à un objet diminue.
	 * @param tab tableau de float
	 * @return true si la distance diminue, false sinon
	 */

	public boolean distanceDiminue(float[] tab) {
		if(tab.length>=2)
			return Math.abs((int)(100*tab[tab.length-1])-(int)(100*tab[tab.length-2]))<=5;
		return true;
	}

	public void avancerVigilantAllerAuCamp() {
		float[] distance = regarde(new float[0]);
		String couleur="";
		//System.out.println("je suis une grosse merde");
		boolean flag=true;
		this.avancerDe(3000);
		while (isMoving() && !(couleur=="Blanc")) {
			couleur = capteurDeCouleur();
			distance = regarde(distance);
			//System.out.println(Arrays.toString(distance));
			if (distance.length > 0) {
				float derniereDistance = distance[distance.length - 1];
				if(derniereDistance<0.2) {
					//System.out.println("Alled 1");
					flag = false;
					avancerDe(-1,false);
					Delay.msDelay(timeToWait);
					distance = regarde(distance);
					derniereDistance = distance[distance.length - 1];
					if (derniereDistance<0.2 && !flag) {
						//System.out.println("alled 2");
						if (getX()<100) {
							tournerDe(90, false);
							avancerDe(100, false);
							tournerDe(-90, false);
							avancerJusquaCouleur("Blanc");
						}
						else {
							tournerDe(-90, false);
							avancerDe(100, false);
							tournerDe(90, false);
							avancerJusquaCouleur("Blanc");
						}
					}
					else {
						avancerJusquaCouleur("Blanc");
					}
				}

			}
		}
		setY(240); 
	}

	public void avancerJusquaCouleur(String couleur1) {
		avancerDe(3000,true);
		String couleur = capteurDeCouleur();
		if (couleur1==couleur) {
			setY(240);
			avancerDe(-1,false);
		}

	}

	/**
	 * Méthode qui recherche un palet et avance tant que le palet n'a pas toucher le capteur de toucher
	 * @param dist un int, la distance maximum à parcourir
	 */
	public boolean avancerWhileIsNotPressed(int dist) {
		// Initialisation des capteurs et des distances
		//System.out.println("Je rentre dans avancerWHile");
		float[] distance = regarde(new float[0]);
		//System.out.println(pilot.getAngularSpeed());
	//	pilot.setAngularSpeed(100);
		// Avancer de manière asynchrone sur la distance spécifiée + 5 cm
		ouvreBras();
		long currentTime = this.avancerDe(dist + 50);
		// Boucle tant que le robot est en mouvement et que le capteur de toucher n'est pas pressé

		boolean flag = true;
		while (isMoving() && !isPressed()){ //&& distanceDiminue(distance)) {
			// Vérifie les distances uniquement si elles sont disponibles
			distance = regarde(distance);
			//System.out.println(Arrays.toString(distance));
			if (distance.length > 0) {
				float derniereDistance = distance[distance.length - 1];
			//	System.out.println("DD : " + derniereDistance);
				if(derniereDistance<0.2) {
					flag = false;
					avancerDe(-1,false);
					Delay.msDelay(timeToWait);
					distance = regarde(distance);
					derniereDistance = distance[distance.length - 1];
					if (derniereDistance<0.2 && !flag) {
						
						tournerDeRapide(180,false);
						recherche(360);
						
					}
					else {
				//		

						int distanceRestante = (int)calculeDistRestante(currentTime,dist);
						return avancerWhileIsNotPressed(distanceRestante);
						
					}
				}
			}       
		}
		// Arrête le robot et ferme les bras une fois la boucle terminée
		while(isMoving()) {
		}

		fermeBras();

		Delay.msDelay(2000);
		

		return isPressed();


	}


	public static boolean isNotDifferent(double v1, double v2) {
		//10dg
		return Math.abs((int)v1-(int)v2)<=10;
	}



	public static boolean isDifferent(float v1, float v2) {
		//10 cm
		return Math.abs(v1-v2)>=10;
	}

	/**
	 * Méthode qui permet d'avancer vers un palet tant que le robot ne le touche pas en vérifiant que la distnace diminue
	 * @param d int en cm, distance maximum à parcourir
	 */
	public void chercherpalet(int d) {
		float[] valeurs = new float[2];
		valeurs[0] = 1000000;
		valeurs[1] = 1000000;
		avancerDe(d);
		while(isMoving() && (valeurs[valeurs.length-1]<= valeurs[valeurs.length-2]) && !isPressed()) {
			valeurs = regarde(valeurs);
		}
		pilot.stop();
	}
	/**
	 * Méthode qui permet de regarder le mur en face quand on pose un palet dans le camp adverse afin de retrouver l'angle 0 pour se 
	 * replacer bien en face. 
	 * Ce place tout droit devant le mur et met à jour l'orientation à 0 degrès. 
	 */
	public void reOrientationMur() {
		tournerDe(-45, false);
		pilot.setAngularSpeed(40);
		//Je tourne de angle de manière asynchrone
		tournerDe(90, true);
		//J'initialise un tableau dans lequel on range les distances que l'on voit
		float[] valeurs = new float[0];
		int indice = 0;
		//Tant que le robot bouge...
		while(isMoving()) {
			//On remplit le tableau de distances
			valeurs = regarde(valeurs);
			indice++;
		}
		//Je récupère la plus petite distane ainsi que l'indice de cette distance dans le tableau
		float[] min = min(valeurs);
		//Je déduis l'angle grâce à un produit en croix
		int angleMin = ((int)min[1] * 90 ) / (int)indice;
		tournerDe(angleMin-85,false);
		setDegres(0);
	}

	/**
	 * A TESTER
	 * Méthode qui fait tourner le robot vers le camp adverse puis qui avance jusqu'à la ligne blanche. Il lache le palet, recule
	 * de 5 cm puis fait un demi tour complet arpès avoir appelé reOrientationMur() pour update l'orientation. 
	 * 
	 */
	public void allerChezAdversaire() {
		tournerDe((int)degresAuCampAdverse(),false);
		avancerVigilantAllerAuCamp();
		ouvreBras();
		reOrientationMur();
		avancerDe(-150);
		tournerDe(180,false);
		tournerDe(90,false);
	}



	public float[] supprime(float[] tab, int indiceAsupp) {
		float[] nouveauTab = new float[tab.length - 1];
		for (int i = 0, j = 0; i < tab.length; i++) {
			if (i != indiceAsupp) {
				nouveauTab[j++] = tab[i];
			}
		}
		return nouveauTab;
	}

	public float[] supprimerPremieresValeurs(float[] tab) {
		int taille = tab.length;
		while (tab.length < taille && tab.length>1 && tab[0] == tab[1]) {
			tab = supprime(tab, tab.length - 1);
		}
		return tab;
	}

	public float[] supprimerDernieresValeurs(float[] tab) {
		while (tab.length > 1 && tab[tab.length - 1] == tab[tab.length - 2]) {
			tab = supprime(tab, tab.length - 1);
		}
		return tab;
	}

	public float[] nettoyageDesValeursAbsurdes(float[] valeurs) {
		for (int i = 0; i < valeurs.length; i++) {
			//|| valeurs[i] > 300
			if (valeurs[i] == 0 ) {
				valeurs = supprime(valeurs, i);
				i--; // Revenir en arrière pour réévaluer après suppression
			}
		}
		return valeurs;
	}


	public void testTempsRotation(int angle) {
		// Mesurer le temps avant de faire tourner le robot
		long debutTemps = System.currentTimeMillis();  // ou System.nanoTime() pour une plus grande précision

		// Faire tourner le robot de 360° (par exemple)
		tournerDe(angle, false);

		// Mesurer le temps après avoir terminé la rotation
		long finTemps = System.currentTimeMillis();  // ou System.nanoTime()

		// Calculer le temps écoulé
		long tempsEcoule = finTemps - debutTemps;

		// Afficher ou retourner le temps écoulé
		//System.out.println("Temps de rotation pour " + angle + "dg : "+ tempsEcoule + " ms");


	}



	public float[] arronditAuPlusHaut(float[] tab) {
		if (tab == null) {
			return null; // Retourne null si le tableau d'entrée est null
		}

		float[] result = new float[tab.length]; // Créer un tableau pour les résultats

		for (int i = 0; i < tab.length; i++) {
			float valeur = tab[i];

			// Limite la valeur à 0 si elle est en dessous
			if (valeur < 0) {
				result[i] = 0;
			} else if (valeur > 300) {
				// Limite la valeur à 300 si elle dépasse
				result[i] = 300;
			} else {
				// Arrondir au multiple de 5 supérieur
				result[i] = (float) (Math.ceil(valeur / 5) * 5);
			}
		}

		return result;
	}

	public static float[] supprimerDoublonsSuccessifs(float[] tab) {
		if (tab == null || tab.length == 0) {
			return new float[0]; // Gérer le cas où le tableau est null ou vide
		}

		// Liste pour stocker les valeurs sans doublons successifs
		ArrayList<Float> resultList = new ArrayList<>();

		// Ajouter le premier élément
		resultList.add(tab[0]);

		// Parcourir le tableau pour vérifier les doublons successifs
		for (int i = 1; i < tab.length; i++) {
			if (tab[i] != tab[i - 1]) {
				resultList.add(tab[i]);
			}
		}

		// Convertir la liste en tableau float[]
		float[] result = new float[resultList.size()];
		for (int i = 0; i < resultList.size(); i++) {
			result[i] = resultList.get(i);
		}

		return result;
	}

	/**
	 * Méthode qui renvoie renvoit un tableau de toutes les distances autour de lui
	 * @param angle l'angle à tourner  
	 * @return un tableau de float contenant les distances
	 */
	public float[] rechercheDistances(int angle) {
		tournerDe(angle, true);

		float[] valeurs = new float[0];
		while(isMoving()) {
			valeurs = regarde(valeurs);
		}	
		/*
		float[] valeurs = new float[0];

		long tempsParDegré = 3500/360; // Temps obtenu avec testTemmpsRotation sur un test sur 360dg
		long tempsDerniereMesure = System.currentTimeMillis();

		tournerDe(angle, true);

		while(isMoving()) {
			//System.out.println(System.currentTimeMillis() + " " + tempsDerniereMesure);
			// Vérifie si suffisamment de temps est passé depuis la dernière mesure (pour chaque degré)
			if (System.currentTimeMillis() - tempsDerniereMesure >= tempsParDegré) {
				valeurs = regarde(valeurs);  // Prendre la mesure
				tempsDerniereMesure = System.currentTimeMillis();  // Met à jour le temps de la dernière mesure
			}
		}
		 */



		//valeurs = supprimerDernieresValeurs(valeurs);
		//valeurs = supprimerPremieresValeurs(valeurs);
		for(int i=0;i<valeurs.length;i++) {
			//On arrondit et on met en CM
			valeurs[i]=(100*valeurs[i]);
		}
		//valeurs = nettoyageDesValeursAbsurdes(valeurs);
		//System.out.println(Arrays.toString(valeurs));
		//valeurs = arronditAuPlusHaut(valeurs);
		valeurs = supprimerDoublonsSuccessifs(valeurs);
		return valeurs;
	}

	/**
	 * Méthoe pour supprimer un objet à un indice précis dans un tableau
	 * @param tab le tableau dans lequel supprimer un objet
	 * @param indiceAsupp l'indice de l'objet à supprimer
	 * @return le tableau avec l'objet en moins
	 */
	public int[][] supprime(int[][] tab, int indiceAsupp) {
		if (indiceAsupp < 0 || indiceAsupp >= tab.length) {
			throw new IllegalArgumentException("Indice à supprimer hors des limites du tableau");
		}
		int[][] nouveauTab = new int[tab.length - 1][];
		for (int i = 0, j = 0; i < tab.length; i++) {
			if (i != indiceAsupp) {
				nouveauTab[j++] = tab[i];
			}
		}
		return nouveauTab;
	}

	/**
	 * Méthode permettant au robot de tourner d'un certain angle et d'avancer jusqu'à l'objet passé en paramètre
	 * @param angle l'angle total duquel le robot a tourné pour trouver l'objet
	 * @param angleTrouve l'anle vers lequel l'objet doit s'orienter
	 * @param objet le tableau représentant l'objet à attraper
	 */
	public void tourneEtAvanceVersLobjet(int angle, int angleTrouve, int[] objet) {
		if(angleTrouve<angle/2) {
			this.tournerDe(angleTrouve,false);
		}
		else	this.tournerDe(-(angle-angleTrouve), false);
		float[] valeurApresOrientation = new float[0];
		valeurApresOrientation = regarde(valeurApresOrientation);
		int dist = objet[2];
		//if((int)(100*valeurApresOrientation[0])>=dist-10 && (int)(100*valeurApresOrientation[0])<=dist+10) {
		avancerWhileIsNotPressed((int)(10*dist));
		//}
		//else recherche(360);
		//Delay.msDelay(10000);
	}



	public boolean recherche(int angle) {
		double angleDeBase = getDegres();
		ArrayList<ArrayList<Float>> valeurs = decoupeValeursStricte(angle);
		// true = le premier est la suite du dernier
		boolean premierNestPasUneDisc = premierNestPasUneDisc(valeurs);
		return plusProcheDiscontinuite(valeurs, premierNestPasUneDisc, angle, (int)angleDeBase);

		//	System.out.println(valeurs);
	}

	private double angleTheorique(float dist) {
		return 2*Math.atan(tailleDuPalet/(2*dist)) * 180/Math.PI;
	}


	/**
	 * Méthode qui renvoie true si le palet est accessible (en dehors des camp)
	 * @param tab int[] qui contient la distance au palet en [0] et l'angle où on la vue en [1]
	 * 		  orientaitonAvantDeTourner renvoie l'angle auquel était le robot avant de chercher
	 * @return boolean true si le palet n'est pas dans un camp adverse false sinon 
	 */
	public boolean paletValide(double[] tab, double orientationAvantDeTourner) {
		double degres = tab[1]+ orientationAvantDeTourner;
		double degresrad= degres*Math.PI/180;
		double posY = (Math.cos(degresrad) * tab[0])+getY() ;
		//System.out.println(posY);
		if (posY<240 && posY>0) {
			return true;
		}
		return false;
	}

	/**
	 * Méthode qui permet au robot d'aller au centre du terrain
	 */
	public void allerAuCentre() { 
		double[] tab = calculerPositionPoint(10, getDegres());
		double m1;
		double m2;
		if (tab[1]==getY()) {
			m1=0;
		}
		else { m1 = (tab[0]-getX())/(tab[1]-getY());
		}
		if (getY()==120) {
			m2=0;
		}
		else { m2 = (100-getX())/(120-getY());
		}
		double angle =(Math.atan(Math.abs((m1-m2)/(1+m1*m2))));
		angle = angle * (180/Math.PI);
		double distAParcourir = Math.sqrt(Math.pow(100-getX(), 2)+Math.pow(120-getY(), 2));
		if (procheDuCentre(calculerPositionPoint(distAParcourir, (180-angle+getDegres())))) {
			angle = 180-angle;
		}
		else if (procheDuCentre(calculerPositionPoint(distAParcourir, (-angle+getDegres())))) {
			angle = -angle;
		}
		else if (procheDuCentre(calculerPositionPoint(distAParcourir, (-(180-angle)+getDegres())))) {
			angle =-(180-angle);
		}
		else if (procheDuCentre(calculerPositionPoint(distAParcourir, (angle+getDegres())))) {
		}
		tournerDe((int)angle, false);
		avancerDe((int)distAParcourir*10, false);
		updateOrientation(angle);
		updatePosition(distAParcourir);
	}

	public static int[] minAngle(int[] tab) {
		int[] min = new int[] {tab[0],0};
		for(int i=1;i<tab.length;i++) {
			if(tab[i]<min[0])
				min=new int[]{tab[i],i};
		}
		return min;
	}

	private double[] max(List<Double> tab) {
		double max=-1;
		int idx=0;
		int idxTrouve=idx;
		ListIterator i = tab.listIterator();
		while(i.hasNext()) {
			double current = (double)i.next();
			if(current > max && current<10) {
				max=current;
				idxTrouve=idx;
			}
			idx++;
		}
		return new double[] {max,idxTrouve};
	}

	private int getTotalIndiceJusqua(ArrayList<ArrayList<Float>> list,int idx) {
		int taille=0;
		ListIterator i = list.listIterator();
		int indice = 0;
		while(i.hasNext() && indice<idx) {
			taille=taille+((ArrayList<Float>)(i.next())).size();
		}
		//System.out.println(taille);
		return taille;
	}

	public static int indicePlusProche(List<Double> list1, List<Double> list2) {
		if (list1 == null || list2 == null || list1.size() != list2.size() || list1.isEmpty()) {
			throw new IllegalArgumentException("Les listes doivent être non nulles, de même taille et non vides.");
		}

		int indicePlusProche = -1;
		float ecartMinimal = Float.MAX_VALUE;

		for (int i = 0; i < list1.size(); i++) {
			double ecart = Math.abs(list1.get(i) - list2.get(i));
			if (ecart < ecartMinimal) {
				ecartMinimal = (float)ecart;
				indicePlusProche = i;
			}
		}

		return indicePlusProche;
	}

	private int totalOccurence(ArrayList<ArrayList<Float>> tab, boolean premierNestPasUneDisc) {
		int idx = (premierNestPasUneDisc)?1:0;

		int occurenceCumule= 0;
		if(idx==1) {
		}
		else
			occurenceCumule=tab.get(0).size()/2;
		//(idx)
		ListIterator i = tab.listIterator();

		for(; i.hasNext() ;) {
			ArrayList<Float> sousTab = (ArrayList<Float>)i.next();
			if(i.nextIndex()-1 == tab.size()-1) {
				if(premierNestPasUneDisc) {
					int occurence=sousTab.size()/2;
					occurenceCumule+=occurence/2;
				}
			}
			try {
				int occurence = sousTab.size();
				occurenceCumule+=occurence;
			}
			catch(Exception e) {

			}
		}
		//System.out.println("Ici " + occurenceCumule);
		return occurenceCumule;
	}

	private boolean plusProcheDiscontinuite(ArrayList<ArrayList<Float>> tab, boolean premierNestPasUneDisc, int angle, int angleDeBase) {
		int idx = (premierNestPasUneDisc)?1:0;
		int idxPlusProcheDisc = (idx==1)?tab.get(0).size()+1:0;
		List<Integer> tabOcc = new ArrayList<Integer>();

		int occurenceCumule= 0;
		if(idx==1) {
			tabOcc.add(idxPlusProcheDisc);

		}
		else
			occurenceCumule=tab.get(0).size()/2;
		double angleVu=0;
		//(idx)
		ListIterator i = tab.listIterator();
		ArrayList<Float> tabPlusProche = tab.get(idx);
		float min=999999;


		//int totalSize=totalSize(tab);
		int totalSize=totalOccurence(tab,premierNestPasUneDisc);

		List<Double> tabAngleTh = new ArrayList<Double>();
		List<Double> tabAngle = new ArrayList<Double>();
		List<Float> tabDist = new ArrayList<Float>();
		List<Double> tabAngleVu = new ArrayList<Double>();

		int angleTrouve=0;


		List<String> resume = new ArrayList<String>();

		for(; i.hasNext() ;) {
			ArrayList<Float> sousTab = (ArrayList<Float>)i.next();
			if(i.nextIndex()-1 == tab.size()-1) {
				if(premierNestPasUneDisc) {
					ArrayList<Float> newTab = (tab.get(0));
					newTab.addAll(sousTab);
					sousTab=newTab;
					int occurence=sousTab.size()/2;
					occurenceCumule+=occurence/2;
				}
			}
			try {
				int occurence = sousTab.size();
				occurenceCumule+=occurence;
				float distanceCurrentElem = sousTab.get(occurence/2);
				float PremdistanceCurrentElem = sousTab.get(0);
				float DeuxdistanceCurrentElem = sousTab.get(sousTab.size()-1);
				//Y'avait pas le /2 j'ai fait un test là
				double angleVuDeCetElem = (occurence/2)*angle/totalSize;
				//2*
				double angleTheorique = angleTheorique(distanceCurrentElem);
				//idx au lieu de occurenceCumule-occurence/2
				int angleAtourner = (occurenceCumule)*angle/totalSize;
				//System.out.println(paletValide(new double[] {distanceCurrentElem, angleAtourner}, angleDeBase) + " " + distanceCurrentElem +  " " + angleAtourner +  " " + angleDeBase + " fini");

				if(//paletValide(new double[] {distanceCurrentElem, angleAtourner}, angleDeBase) &&
						occurence>=5) {
					resume.add("occurence cumule : " + occurenceCumule + " | occurence : " + occurence + " | 1e distance : " + PremdistanceCurrentElem + " | 2e distance : " + DeuxdistanceCurrentElem + " | angle vu : " + angleVuDeCetElem + " | angleTheorique : " + angleTheorique + " | angle a tourner : " + angleAtourner);
					tabAngle.add((double)angleAtourner);
					tabAngleVu.add(angleVuDeCetElem);
					tabAngleTh.add(angleTheorique(distanceCurrentElem));
					if(distanceCurrentElem>25 && distanceCurrentElem<min && isNotDifferent(angleVuDeCetElem,angleTheorique)) {
						min=distanceCurrentElem;
						idxPlusProcheDisc=occurenceCumule;
						angleVu=angleVuDeCetElem;
						tabPlusProche=sousTab;
						angleTrouve=angleAtourner;
					}

					tabDist.add(distanceCurrentElem);
					tabOcc.add(idx);
				}			
				idx+=sousTab.size();

			}
			catch(Exception e) {

			}
		}

		//double[] paletTrouve =max(tabAngleTh);
		//float dist = ((tabDist.get((int)(paletTrouve[1]))));
		//int angleTrouve = (tabOcc.get((int)paletTrouve[1])*angle/totalSize)%360;
		//	System.out.println(tabOcc);
		//	System.out.println(tabOcc.get((int)paletTrouve[1]));
		//	tourneOptimise(angle,angleTrouve, dist);

		for(int index=0;index<resume.size();index++) {
		//	System.out.println(resume.get(index));
		}
		//System.out.println("Il y a " + resume.size() + " discontinuites.");

		/*System.out.println(tabAngle);
		System.out.println("theorique : " + tabAngleTh);
		System.out.println("realite : " + tabAngleVu);
		//System.out.println("indice plus proche theorique/realite : " + indicePlusProche(tabAngleTh, tabAngleVu));
		System.out.println("La discontinuite la plus proche est : " + min + " d'un angle de " + angleVu + " que j'ai vu au bout de ma " + idxPlusProcheDisc + "e vision. J'ai vu " + totalSize + " fois au total. \nDonc l'angle a tourner est de : " + angleTrouve);
*/
	

		return tourneOptimise(angle,angleTrouve,min+5);

		//	System.out.println("La discontinuite la plus proche est : " + dist + " à l'angle " + angleTrouve);

		//	System.out.println(tabAngle);
		//	tourneOptimise(angle,angleTrouve,min);
		/*
		double sommeTourne=0;
		for(int j=0;j<tabAngle.size();j++) {
			tournerDe( (int)(tabAngle.get(j) - sommeTourne), false);
			sommeTourne=tabAngle.get(j);
			Delay.msDelay(2000);
		}	
		 */

	}

	private int totalSize(ArrayList<ArrayList<Float>> tab) {
		int idx = 0;
		ListIterator<ArrayList<Float>> i = tab.listIterator();
		while (i.hasNext()) {
			ArrayList<Float> sousTab = i.next();
			idx += sousTab.size(); // Ajoute simplement la taille de sousTab
		}
		return idx; // Le calcul de idx donne déjà la bonne taille totale
	}

	//A REVOIR POUR LES ANGLES < 360
	private boolean tourneOptimise(int angle, int angleTrouve, float dist) {
		if(angleTrouve<angle/2) {
			this.tournerDe(angleTrouve,false);
		}
		else	this.tournerDe(-(angle-angleTrouve), false);
		float[] valeurApresOrientation = new float[0];
		valeurApresOrientation = regarde(valeurApresOrientation);
		float distMtn = dist;
		//if((int)(100*valeurApresOrientation[0])>=distMtn-10 && (int)(100*valeurApresOrientation[0])<=distMtn+10) {
		//System.out.println("je rentre");
		//avancerWhileIsNotPressed((int)(10*distMtn));
		//}
		/*	else {
		System.out.println("Je ré-essaye");
		recherche3(360);
	}*/
		//Delay.msDelay(10000);
		return ajustement();
	}


	public ArrayList<ArrayList<Float>> decoupeValeursStricte(int angle) {
		float[] valeurs = rechercheDistances(angle);
		if(valeurs.length<2) {
			ArrayList<ArrayList<Float>> list = new ArrayList<ArrayList<Float>>();
			try {
				ArrayList<Float> sousList = new ArrayList<Float>();
				sousList.add(valeurs[valeurs.length-1]);
				list.add(sousList);
			}
			catch(ArrayIndexOutOfBoundsException e) {

			}
		}
		boolean croissant = valeurs[0]<=valeurs[1];
		ArrayList<ArrayList<Float>> liste = new ArrayList<>();
		ArrayList<Float> sousListe = new ArrayList<>();
		sousListe.add(valeurs[0]);
		for(int i=1;i<valeurs.length;i++) {
			if(Math.abs(valeurs[i]-valeurs[i-1])>2){
				liste.add(sousListe);
				sousListe=new ArrayList<>();
				sousListe.add(valeurs[i]);
				if(i+1<valeurs.length)
					croissant = valeurs[i]<=valeurs[i+1];
			}
			else if((croissant && valeurs[i-1]<=valeurs[i] || Math.abs(valeurs[i-1]-valeurs[i])<5) || (!croissant && valeurs[i-1]>=valeurs[i] || Math.abs(valeurs[i-1]-valeurs[i])<5)) {
				sousListe.add(valeurs[i]);
			}
			else {
				liste.add(sousListe);
				sousListe.remove(sousListe.size()-1);
				sousListe = new ArrayList<>();
				sousListe.add(valeurs[i-1]);
				sousListe.add(valeurs[i]);
				croissant = valeurs[i-1]<=valeurs[i];

			}	
		}
		liste.add(sousListe);
		//System.out.println(liste);
		for(int i=0;i<liste.size();i++) {
			//System.out.println(liste.get(i));
		}
		return liste;
	}

	private boolean premierNestPasUneDisc(ArrayList<ArrayList<Float>> list){
		if(list.size()==0)
			return false;
		ArrayList<Float> premierTab = list.get(0);
		ArrayList<Float> dernierTab = list.get(list.size()-1);
		float distanceCurrentElem = dernierTab.get(dernierTab.size()-1);
		float premierElem = premierTab.get(0);
		//System.out.println("Premier element : " + premierElem + "\nDernier element : " + distanceCurrentElem);
		return Math.abs(premierElem-distanceCurrentElem) < 5;

	}

	public double angleDeRechercheOptimise () {
		double x = getX(); 
		double y = getY();
		//System.out.println("en x : "+x+" en y : "+y);
		if ((x>=0 && x<=50) && (y>=0)&&(y<=60)) {
			double[] tab = plusPetitAngleAuRobot(50,180,150,60);
			tournerDe((int)tab[0]);
			return tab[1]*angleEntreDeuxPoint(50,180,150,60);
		}
		else if ((x>50&& x<150) && (y>=0)&&(y<=60)) {
			double[] tab = plusPetitAngleAuRobot(50,60,150,60);
			tournerDe((int)tab[0]);
			return tab[1]*angleEntreDeuxPoint(50,60,150,60);
		}
		else if ((x>=150&& x<=200) && (y>=0)&&(y<=60)) {
			double[] tab = plusPetitAngleAuRobot(50,60,150,180);
			tournerDe((int)tab[0]);
			return tab[1]*angleEntreDeuxPoint(50,60,150,180);
		}
		else if ((x>=150&& x<=200) && (y>60)&&(y<180)) {
			double[] tab = plusPetitAngleAuRobot(150,60,150,180);
			tournerDe((int)tab[0]);
			return tab[1]*angleEntreDeuxPoint(150,60,150,180);
		}
		else if ((x>=150&& x<=200) && (y>=180)&&(y<=240)) {
			double[] tab = plusPetitAngleAuRobot(150,60,50,180);
			tournerDe((int)tab[0]);
			return tab[1]*angleEntreDeuxPoint(150,60,50,180);
		}
		else if ((x>50&& x<150) && (y>=180)&&(y<=240)) {
			double[] tab = plusPetitAngleAuRobot(150,180,50,180);
			tournerDe((int)tab[0]);
			return tab[1]*angleEntreDeuxPoint(150,180,50,180);
		}
		else if ((x>=0&& x<=50) && (y>=180)&&(y<=240)) {
			double[] tab = plusPetitAngleAuRobot(150,180,50,60);
			tournerDe((int)tab[0]);
			return tab[1]*angleEntreDeuxPoint(150,180,50,60);
		}
		else if ((x>=0&& x<=50) && (y>60)&&(y<180)) {
			double[] tab = plusPetitAngleAuRobot(50,180,50,60);
			tournerDe((int)tab[0]);
			return tab[1]*angleEntreDeuxPoint(50,180,50,60);
		}
		else {
			return 360;
		}
	}

	public void MiseAjourPos() {
		//System.out.println(isMoving());
		tournerDe(45, true);

//		System.out.println(isMoving());
		VitesseRepére();
		tournerDe(90, true);
		float [] valeurs= new float[0];
//		System.out.println(isMoving());
		while(isMoving()) {
			valeurs = regarde(valeurs);
		}
	//	System.out.println("premiere recherche nombre de valeurs= "+valeurs.length);
		int x1 = recuperationCoordonnées(valeurs);
		VitesseRepéreRESET();
		tournerDe(90, true);
		VitesseRepére();
		tournerDe(90, true);
		float [] valeurs2= new float[0];
		while(isMoving()) {
			valeurs2 = regarde(valeurs2);
		}
//		System.out.println("deuxieme recherche nombre de valeurs= "+valeurs.length);
		int x2 = recuperationCoordonnées(valeurs2);
		System.out.println(x1+"  "+x2);
		int somme = x1+x2;
		if ((somme>195)&&(somme<205)) {
			this.setX(x1);
		}
//		System.out.println(isMoving());
		tournerDe(45,true);
	}

	public int  recuperationCoordonnées (float[] données) {
		int coordonées = 0;
		//System.out.println("Je suis juste devant le for ");
		int i =2;
		while (i<données.length-2) {
			float a = données[i-2];
			float b = données[i-1];
			float c = données[i];
			float d = données[i+1];
			float e = données[i+2];
			//System.out.println("Je commence a traverser le tableau ");
			if ((a<b)&&(b<c)&&(c>d)&&(d>e)) {
				//System.out.println("ca croix puis ca decroix");
				coordonées=(int)données[i];
				return coordonées;
			} else {
				if ((a>b)&&(b>c)&&(c<d)&&(d<e)) {
					//System.out.println("ca decroix puis ca croix");
					coordonées=(int)données[i];
					return coordonées;

				}else {
					i++;
					//System.out.println("ont n'a pas reussi a trouver la coordonées");
					//return coordonées;
				}

			}
		}
//		System.out.println("ont n'a pas reussi a trouver la coordonées");
		return coordonées;
	}

	public void VitesseRepére() {
		pilot.setAngularAcceleration(50);
		pilot.setLinearSpeed(50);


	}

	public void VitesseRepéreRESET() {
		pilot.setAngularAcceleration(500);
		pilot.setLinearSpeed(5000);
	}

	public boolean ajustement() {
		//Tu vas a gauche de 5° puis tu tournes de 10° à droite tout en regardant et tu t'orientes au plus proche
		tournerDe(-angleAjustement/2,false);

		tournerDe(angleAjustement,true);
		float[] valeurs = new float[0];
		while(isMoving()) {
			valeurs = regarde(valeurs);
		}	
		int[] tabVal=new int[valeurs.length];
		for(int i=0;i<valeurs.length;i++) {
			//On arrondit et on met en CM
			tabVal[i]=(int)(100*valeurs[i]);
		}		
		int[] min = minAngle(tabVal);
		int minDist = min[0];
		int minIdx = min[1];

		int angleAtourner = minIdx*angleAjustement/tabVal.length;

		tournerDe(-(angleAjustement-angleAtourner), false);
		System.out.println(Arrays.toString(valeurs));
		System.out.println(minIdx + " " + minDist);

		return avancerWhileIsNotPressed(minDist*10+10);


	}


}