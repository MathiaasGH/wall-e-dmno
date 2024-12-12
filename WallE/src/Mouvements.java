import lejos.robotics.navigation.MovePilot;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.*;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.utility.Delay;


/**
 * Cette classe permet de gérer tous les mouvements du robot.
 * Cette classe hérite de la classe position.
 *
 * @see Mouvements
 * @author DEVILLIERS, MITTON, NDONG, OZTURK
 */

public class Mouvements extends Position {

	/** Le réel représentant la vitesse du robot. */
	private final static double vitesse=20/1338.55;
	/** Le réel représentant le diamètre du palet. */
	private final static double tailleDuPalet=6;
	/** L'entier représentant l'angle à tourner pour réajuster la robot face au palet vu. */
	private final static int angleAjustement = 40;
	/** Le roue représentant la roue gauche.*/
	private Wheel wheel1;
	/** Le roue représentant la roue droite.*/
	private Wheel wheel2;
	/** Le chassis représentant le chassis liant les roues gauche et droite.*/
	private Chassis chassis;
	/** Le pilot pour avancer.*/
	private MovePilot pilot; 
	/** Le booléen représentant l'état des bras.*/
	private boolean brasOuvert;
	/** L'entier représentant le temps a attendre lorsqu'un obstacle est devant le robot*/
	private final static int timeToWait = 4000;

	/**
	 * Constructeur pour la classe Mouvements
	 * Permet d'initialiser la position intiale et l'état des bras initial.
	 * 
	 * @param x un entier prenant une valeur suivante {50,100,150}
	 * @param y un entier = 0
	 * @param etatBras un booléen représentant l'état des bras initial.
	 */
	public Mouvements(int x, int y, boolean etatBras) {
		super(x, y);
		brasOuvert = etatBras;
		wheel1 = WheeledChassis.modelWheel(Motor.B, 56).offset(-62);
		wheel2 = WheeledChassis.modelWheel(Motor.C, 56).offset(62);
		chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, WheeledChassis.TYPE_DIFFERENTIAL); 
		pilot = new MovePilot(chassis);
	}

	/**
	 * Avance 10 fois de 20 cm et 
	 * affiche le temps pour avancer de 20cm avec une vitesse définie.
	 */
	private void avanceChronometre() {
		for(int i=0;i<10;i++) {
			long tempsAvant= System.currentTimeMillis();
			avancerDe(200,false);
			long tempsApres = System.currentTimeMillis();
			System.out.println(tempsApres-tempsAvant);
		}

	}

	/**
	 * Permet d'avancer d'une certaine distance de manière asynchrone
	 * @param dist en dm
	 * @return une distance en dm
	 */

	public long avancerDe(int dist) {
		return avancerDe(dist,true); 
	}


	/**
	 * Permet d'avancer d'une certaine distance de manière asynchrone ou pas
	 * @param dist en dm
	 * @param b si true asynchrone si false synchrone
	 * @return le temps avant de commencer à avancer
	 */
	public long avancerDe(int dist,boolean b) {
		pilot.setAngularSpeed(200);
		pilot.setLinearSpeed(200);
		long tempsAvant= System.currentTimeMillis();
		pilot.travel(dist,b);                 
		updatePosition(dist/10);
		return tempsAvant;
	}

	/**
	 * Permet d'avancer d'une certaine distance de manière asynchrone rapidement
	 * @param dist en dm
	 * @return le temps avant de commencer à avancer
	 */
	public void avancerDeRapide(int dist) {
		avancerDeRapide(dist, true);
	}

	/**
	 * Permet d'avancer d'une certaine distance de manière asynchrone ou pas mais rapidement
	 * @param dist en dm
	 * @param b si true asynchrone si false synchrone
	 */
	public void avancerDeRapide(int dist,boolean b) {
		pilot.setAngularSpeed(500);
		pilot.setLinearSpeed(500);
		pilot.travel(dist,b);
	}

	/**
	 * Calcule la distance restante à parcourir
	 * @param tempsAvantDeRouler le temps auquel le robot a commencé à avancer
	 * @param distanceAavancer la distance totale à parcourir pour le robot en dm
	 * @return une distance en dm
	 */
	private float calculeDistRestante(long tempsAvantDeRouler, int distanceAavancer) {
		long tempsMaintenant= System.currentTimeMillis();
		long diffTemp = tempsMaintenant-tempsAvantDeRouler;
		int distParcouru = (int)(diffTemp*vitesse);
		return distanceAavancer-distParcouru;
	}

	/**
	 * Permet de tourner d'un certain angle de manière asynchrone
	 * @param angle en degrés
	 */
	public void tournerDe(int angle) {
		tournerDe(angle, true);
	}

	/**
	 * Permet de tourner d'un certain angle de manière asynchrone ou non
	 * @param angle en degrés 
	 * @param asynchrone si true asynchrone si false synchrone
	 */
	public void tournerDe(int angle, boolean asynchrone) {
		pilot.setAngularSpeed(40);
		pilot.setLinearSpeed(40);
		pilot.rotate(angle, asynchrone); 
		updateOrientation(angle);
	}

	/**
	 * Permet de tourner rapidement d'un certain agnle de manière asynchrone ou non
	 * @param angle en degrés
	 * @param asynchrone si true asynchrone, si false synchrone
	 */
	public void tournerDeRapide(int angle, boolean asynchrone) {
		pilot.setAngularSpeed(500);
		pilot.setLinearSpeed(500);
		pilot.rotate(angle, asynchrone); 
		updateOrientation(angle);
	}


	/**
	 * Permet de fermer les bras si ils ne sont pas déjà fermés
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
	 * Permet d'ouvrir les bras si ils ne sont pas déjà ouverts
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
	 * Permet d'ouvrir les bras si ils ne sont pas déjà ouverts de manière asynchrone
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
	 * Permet de savoir si le robot est en mouvement 
	 * @return un boolean true si le robot bouge, false sinon
	 */
	private boolean isMoving() {
		return pilot.isMoving();
	}

	/**
	 * Permet de faire stopper les mouvement du robot
	 */
	private void stop() {
		pilot.stop(); 
	}

	/**
	 * Renvoie le minimum d'un tableau de float ainsi que l'indice auquel il à été trouvé
	 * @param tab tableau de float avec des distances
	 * @return un tableau de float de taille 2 avec le minimum du tableau passé en paramètre ainsi que son indice
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
		tabR[1] = indice;
		return tabR;
	}

	/**
	 * Renvoie un boolean qui indique si la distance à un objet diminue.
	 * @param tab un tableau de float
	 * @return true si la distance diminue, false sinon
	 */
	private boolean distanceDiminue(float[] tab) {
		if(tab.length>=2)
			return Math.abs((int)(100*tab[tab.length-1])-(int)(100*tab[tab.length-2]))<=5;
		return true;
	}

	/**
	 * Va au centre du terrain
	 */
	public void allerAuCentre() { 
		double x= getX();
		double y=getY();
		if ((x>=0&&x<100) && (y>=0&&y<120)) {
			if (!(getDegres()==45)){
				tournerDeRapide((int)-getDegres()+45, false); 
				avancerDeRapide(500, false); 
			}
			else {
				avancerDeRapide(500, false);  
			}
		}
		else if ((x>=100&&x<=200)&&(y>=0&&y<120)) {
			if (!(getDegres()==-45)) {
				tournerDeRapide((int)-getDegres()-45, false); 
				avancerDeRapide(500, false); 
			}
			else {
				avancerDeRapide(500, false);
			}
		}
		else if ((x>=100&&x<=200)&&(y>=120&&y<=200)) {
			if (!(getDegres()==-135)) {
				tournerDeRapide((int)-getDegres()-135, false); 
				avancerDeRapide(500, false); 
			}
			else {
				avancerDeRapide(500, false);
			}
		}
		else {
			if (!(getDegres()==135)) {
				tournerDeRapide((int)-getDegres()+135, false); 
				avancerDeRapide(500, false); 
			}
			else {
				avancerDeRapide(500, false);
			}
		}

	}

	/**
	 * Permet d'avancer jusqu'au camp adverse en s'arrêtant si un obstacle est présent 
	 * (et tourner pour se ré-ajuster si l'obstacle persiste au bout de 4 secondes.
	 */
	public void avancerVigilantAllerAuCamp() {
		float[] distance = regarde(new float[0]);
		String couleur="";
		boolean flag=true;
		this.avancerDeRapide(3000);
		while (isMoving() && !(couleur=="Blanc")) {
			couleur = capteurDeCouleur();
			distance = regarde(distance);
			if (distance.length > 0) {
				float derniereDistance = distance[distance.length - 1];
				if(derniereDistance<0.2) {
					flag = false;
					avancerDe(-1,false);
					Delay.msDelay(timeToWait);
					distance = regarde(distance);
					derniereDistance = distance[distance.length - 1];
					if (derniereDistance<0.2 && !flag) {
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

	/**
	 * Permet d'avancer jusqu'à vision d'une couleur
	 * @param couleur1 une chaîne de caractères correspondant à la couleur souhaitée ("blanc","rouge","noir",...)
	 */
	private void avancerJusquaCouleur(String couleur1) {
		avancerDeRapide(3000,true);
		String couleur = capteurDeCouleur();
		if (couleur1==couleur) {
			setY(240);
			avancerDe(-1,false);
		}

	}

	/**
	 * Avance tant que le capteur de toucher n'a pa sété activé
	 * @param dist la distance en dm
	 * @return un boolean true si le capteur a été activé, false sinon
	 */
	public boolean avancerWhileIsNotPressed(int dist) {
		float[] distance = regarde(new float[0]);
		ouvreBras();
		long currentTime = this.avancerDe(dist + 50);
		boolean flag = true;
		boolean retour=false;
		while (isMoving() && !isPressed()){
			distance = regarde(distance);
			if(isPressed()) {
				avancerDe(-1,false);
				retour=true;
			}
			if (distance.length > 0) {
				float derniereDistance = distance[distance.length - 1];
				if(derniereDistance<0.2) {
					flag = false;
					avancerDe(-1,false);
					Delay.msDelay(timeToWait);
					distance = regarde(distance);
					derniereDistance = distance[distance.length - 1];
					if (derniereDistance<0.2 && !flag) {
						tournerDeRapide(180,false);
						fermeBras();
						return recherche(360);
					}
					else {
						//
						int distanceRestante = (int)calculeDistRestante(currentTime,dist);
						return avancerWhileIsNotPressed(distanceRestante);

					}
				}
			}       
		}
		while(isMoving()) {
		}
		fermeBras();
		return retour;
	}

	/**
	 * Permet de savoir siu deux nombres ne sont pas significativement différents 
	 * @param v1 le premier nombre
	 * @param v2 le deuxième nombre
	 * @return un boolean true si la différence entre les deux valeurs est comprise entre 0 et 10
	 */
	private static boolean isNotDifferent(double v1, double v2, int distanceCurrentElem) {
		return Math.abs((int)v1-(int)v2)<=10;
	}

	/**
	 * Permet de regarder le mur en face quand on pose un palet dans le camp adverse afin de retrouver l'angle 0 pour se 
	 * replacer bien en face. 
	 * Se place tout droit devant le mur et met à jour l'orientation à 0 degrès. 
	 */
	public void reOrientationMur() {
		tournerDe(-45, false);
		pilot.setAngularSpeed(40);
		tournerDe(90, true);
		float[] valeurs = new float[0];
		int indice = 0;
		while(isMoving()) {
			valeurs = regarde(valeurs);
			indice++;
		}
		float[] min = min(valeurs);
		int angleMin = ((int)min[1] * 90 ) / (int)indice;
		tournerDe(angleMin-85,false);
		setDegres(0);
	}

	/**
	 * Tourne vers le camp adverse puis qui avance jusqu'à la ligne blanche. Relache le palet, recule
	 * de 5 cm puis fait un demi tour complet arpès avoir appelé reOrientationMur() pour actualiser l'orientation. 
	 */
	public void allerChezAdversaire() {
		tournerDeRapide((int)degresAuCampAdverse(),false);
		avancerVigilantAllerAuCamp();
		ouvreBras();
		reOrientationMur();
		avancerDe(-150,false);
		tournerDe(135,false);
		fermeBras();
	}

	/**
	 * Permet de supprimer l'élément d'un tableau situé à un indice donné
	 * @param tab un tableau de float dans lequel on retrouve l'élément à supprimer
	 * @param indiceAsupp un entier correspondant à l'indice auquel se trouve l'élément à supprimer
	 * @return un tableau de float comportant tous les éléments du tableau initial sans l'élément à supprimer
	 */
	private float[] supprime(float[] tab, int indiceAsupp) {
		float[] nouveauTab = new float[tab.length - 1];
		for (int i = 0, j = 0; i < tab.length; i++) {
			if (i != indiceAsupp) {
				nouveauTab[j++] = tab[i];
			}
		}
		return nouveauTab;
	}


	/**
	 * Permet de supprimer toutes les valeurs condidérées absurdes d'un tableau de float
	 * @param valeurs le tableau de float
	 * @return un tableau de float comportant tous les éléments du tableau inital sans les valeurs nulles 
	 */
	private float[] nettoyageDesValeursAbsurdes(float[] valeurs) {
		for (int i = 0; i < valeurs.length; i++) {
			if (valeurs[i] == 0 ) {
				valeurs = supprime(valeurs, i);
				i--;
			}
		}
		return valeurs;
	}

	/**
	 * Permet d'afficher le temps de rotation du robot sur 360 degrés.
	 * @param angle l'angle à tourner
	 */
	private void testTempsRotation(int angle) {
		long debutTemps = System.currentTimeMillis();
		tournerDe(angle, false);
		long finTemps = System.currentTimeMillis();
		long tempsEcoule = finTemps - debutTemps;
		System.out.println("Temps de rotation pour " + angle + "dg : "+ tempsEcoule + " ms");
	}

	/**
	 * Permet de supprimer les doublons successifs d'un tableau
	 * @param tab un tableau de float
	 * @return un tableau de float comportant tous les éléments du tableau initial sans doublons successifs
	 */
	private static float[] supprimerDoublonsSuccessifs(float[] tab) {
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
	 * Permet de renvoyer un tableau de toutes les distances autour du robot
	 * @param angle l'angle à tourner  
	 * @return un tableau de float contenant les distances
	 */
	private float[] rechercheDistances(int angle) {
		tournerDe(angle, true);

		float[] valeurs = new float[0];
		while(isMoving()) {
			valeurs = regarde(valeurs);
		}
		for(int i=0;i<valeurs.length;i++) {
			valeurs[i]=(100*valeurs[i]);
		}
		valeurs = supprimerDoublonsSuccessifs(valeurs);
		return valeurs;
	}

	/**
	 * Permet de regarder autour de lui d'un certain angle et d'aller chercher l'objet situé à la discontinuité la plus proche.
	 * @param angle l'angle auquel tourner
	 * @return un boolean : true si le capteur de toucher a été activé durant la recherche, false sinon
	 */
	public boolean recherche(int angle) {
		double angleDeBase = getDegres();
		ArrayList<ArrayList<Float>> valeurs = decoupeValeursStricte(angle);
		if(valeurs.size()<2) {
			return recherche((int)this.angleDeRechercheOptimise());
		}
		boolean premierNestPasUneDisc = premierNestPasUneDisc(valeurs);
		boolean retour=plusProcheDiscontinuite(valeurs, premierNestPasUneDisc, angle, (int)angleDeBase);
		return retour;
	}

	/**
	 * Permet de calculer le double d'un angle de vision théorique pour un objet 
	 * situé à une certaine distance.
	 * @param la distance de l'objet
	 * @return un double correspondant à l'angle théorique
	 */
	private double angleTheorique(float dist) {
		return 2*Math.atan(tailleDuPalet/(2*dist)) * 180/Math.PI;
	}


	/**
	 * Renvoie true si le palet est accessible (en dehors des camps)
	 * @param tab un tableau de double qui contient la distance au palet en [0] et l'angle où le robot l'a vu en [1]
	 * @param orientationAvantDeTourner l'angle relatif duquel était le robot avant de chercher
	 * @return boolean true si le palet n'est pas dans un camp adverse false sinon 
	 */
	private boolean paletValide(double[] tab, double orientationAvantDeTourner) {
		double degres = tab[1]+ orientationAvantDeTourner;
		double degresrad= degres*Math.PI/180;
		double posY = (Math.cos(degresrad) * tab[0])+getY() ;
		if (posY<240 && posY>0) {
			return true;
		}
		return false;
	}

	/**
	 * Cherche le minimum dans un tableau ainsi que son indice d'apparition
	 * @param tab un tableau d'entiers
	 * @return un tableau d'entiers de 2 éléments correspondants respectivement au minimum trouvé, et à son indice
	 */
	private static int[] minAngle(int[] tab) {
		int[] min = new int[] {tab[0],0};
		for(int i=1;i<tab.length;i++) {
			if(tab[i]<min[0])
				min=new int[]{tab[i],i};
		}
		return min;
	}

	
	/**
	 * Compte le nombre total d'éléments d'une liste de liste de float 
	 * @param tab la liste de liste de float
	 * @param premierNestPasUneDisc un boolean affirmant si la première valeur de tab correspond à une discontinuité du champ 
	 * visuel ou non
	 * @return le nombre total d'éléments de la liste passsée en paramètres
	 */
	private int totalOccurence(ArrayList<ArrayList<Float>> tab, boolean premierNestPasUneDisc) {
		int idx = (premierNestPasUneDisc)?1:0;

		int occurenceCumule= 0;
		if(idx==1) {
		}
		else
			occurenceCumule=tab.get(0).size()/2;
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
		return occurenceCumule;
	}

	/**
	 * Relève toutes les discontinuités remarquées dans une liste de liste de valeurs telles que les discontinuités sont assez visibles
	 * @param tab une liste de liste de float correspondant au cluster de distances
	 * @param premierNestPasUneDisc un boolean affirmant si la première valeur de tab correspond à une discontinuité du champ 
	 * visuel ou non 
	 * @param angle l'angle total duquel le robot a du tourner
	 * @param angleDeBase l'angle relatif auquel le robot se trouvait avant de tourner
	 */
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
		ListIterator i = tab.listIterator();
		ArrayList<Float> tabPlusProche = tab.get(idx);
		float min=999999;
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
				double angleVuDeCetElem = (occurence/2)*angle/totalSize;
				double angleTheorique;
				if(distanceCurrentElem>30)
					angleTheorique= angleTheorique(distanceCurrentElem);
				else angleTheorique= 2*angleTheorique(distanceCurrentElem)*distanceCurrentElem/30;
				int angleAtourner = (occurenceCumule)*angle/totalSize;
				if(paletValide(new double[] {distanceCurrentElem, angleAtourner}, angleDeBase) && occurence>=5) {
					resume.add(((isNotDifferent(angleVuDeCetElem,angleTheorique, (int)distanceCurrentElem))?"palet " : "mur ") + " occurence cumule : " + occurenceCumule + " | occurence : " + occurence + " | 1e distance : " + PremdistanceCurrentElem + " | 2e distance : " + DeuxdistanceCurrentElem + " | angle vu : " + angleVuDeCetElem + " | angleTheorique : " + angleTheorique + " | angle a tourner : " + angleAtourner);
					tabAngle.add((double)angleAtourner);
					tabAngleVu.add(angleVuDeCetElem);
					tabAngleTh.add(angleTheorique(distanceCurrentElem));
					if(distanceCurrentElem>25 && distanceCurrentElem<min && isNotDifferent(angleVuDeCetElem,angleTheorique, (int)distanceCurrentElem)) {
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
		return tourneOptimise(angle,angleTrouve,min+5);
	}

	/**
	 * Tourner de façon optimisée vers un angle (à droite si l'angle a tourner est à  droite, à gauche sinon) et avancer 
	 * en direction de cet angle.
	 * @param angle un entier correspondant à l'angle total duquel le robot a tourné
	 * @param angleTrouve un entier correspondant à l'angle à tourner
	 * @param dist un float correspondant à la distance de l'objet trouvé situé à l'angle duquel le robot doit tourner
	 * @return un boolean affirmant si le capteur de palet a été touché durant son voyage ou pas
	 */
	private boolean tourneOptimise(int angle, int angleTrouve, float dist) {
		if(angleTrouve<angle/2) {
			this.tournerDe(angleTrouve,false);
		}
		else	this.tournerDe(-(angle-angleTrouve), false);
		float[] valeurApresOrientation = new float[0];
		valeurApresOrientation = regarde(valeurApresOrientation);
		return ajustement();
	}

	/**
	 * Permet d'effectuer un clustering sur les distances trouvées autour du robot
	 * @param angle un entier correspondant à l'angle total duquel le robot doit tourner pour effectuer sa recherche des distances
	 * @return une liste de listes contenant les clusters des distances
	 */
	private ArrayList<ArrayList<Float>> decoupeValeursStricte(int angle) {
		float[] valeurs = rechercheDistances(angle);

		if(valeurs.length<2) {

			ArrayList<ArrayList<Float>> list = new ArrayList<ArrayList<Float>>();
			try {
				ArrayList<Float> sousList = new ArrayList<Float>();
				sousList.add(valeurs[valeurs.length-1]);
				list.add(sousList);
				return list;
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
		for(int i=0;i<liste.size();i++) {
		}
		return liste;
	}

	/**
	 * Permet d'affirmer si la première valeur de l'ensemble des clusters des distances correspond à une discontinuité
	 * @param list la liste de listes des distances
	 * @return un boolean : true si la premire valeur est une discontinuité (le premier element est plus loin 
	 * ou plus proche du denrier élément de 5cm), false sinon.
	 */
	private boolean premierNestPasUneDisc(ArrayList<ArrayList<Float>> list){
		if(list.size()==0)
			return false;
		ArrayList<Float> premierTab = list.get(0);
		ArrayList<Float> dernierTab = list.get(list.size()-1);
		float distanceCurrentElem = dernierTab.get(dernierTab.size()-1);
		float premierElem = premierTab.get(0);
		return Math.abs(premierElem-distanceCurrentElem) < 5;

	}

	/** Permet de retourner un angle à tourner pour effectuer une recherche optimisée en fonction de notre position sur le terrain
	 * pour éviter un maximum de regarder les murs.
	 * @return un double correspondant à l'angle à tourner.
	 */
	public double angleDeRechercheOptimise() {
		System.out.println("Angle de recherche opti");
		double x = getX();
		double y = getY(); 
		if ((x>=0 && x<=50) && (y>=0)&&(y<=60)) {
			tournerDeRapide((int)-getDegres()+20, false); 
			return(90);
		}
		else if ((x>50&& x<150) && (y>=0)&&(y<=60)) {
			tournerDeRapide((int)-getDegres()-35, false); 
			return(90);
		}
		else if ((x>=150&& x<=200) && (y>=0)&&(y<=60)) {
			tournerDeRapide((int)-getDegres()-45, false); 
			return(90);
		}
		else if ((x>=150&& x<=200) && (y>60)&&(y<180)) {
			tournerDeRapide((int)-getDegres()-135, false); 
			return(90);
		}
		else if ((x>=150&& x<=200) && (y>=180)&&(y<=240)) {
			tournerDeRapide((int)-getDegres()-150, false); 
			recherche(90);
		}
		else if ((x>50&& x<150) && (y>=180)&&(y<=240)) {
			tournerDeRapide((int)-getDegres()+135, false); 
			return(90);
		}
		else if ((x>=0&& x<=50) && (y>=180)&&(y<=240)) {
			tournerDeRapide((int)-getDegres()+115, false); 
			return(90);
		}
		else if ((x>=0&& x<=50) && (y>60)&&(y<180)) {
			tournerDeRapide((int)-getDegres()+35, false); 
			return(90); 
		}
		return 360; 
	}

	/**
	 * Permet de regarder à droite puis à gauche et d'avancer vers la distance la plus proche
	 * @return un booléen : true si le capteur de toucher à été activé pendant sont trajet, fale sinon.
	 */
	private boolean ajustement() {
		tournerDe(-angleAjustement/2,false);
		tournerDe(angleAjustement,true);
		float[] valeurs = new float[0];
		while(isMoving()) {
			valeurs = regarde(valeurs);
		}	
		int[] tabVal=new int[valeurs.length];
		for(int i=0;i<valeurs.length;i++) {
			tabVal[i]=(int)(100*valeurs[i]);
		}		
		int[] min = minAngle(tabVal);
		int minDist = min[0];
		int minIdx = min[1];
		int angleAtourner = minIdx*angleAjustement/tabVal.length;
		tournerDe(-(angleAjustement-angleAtourner), false);
		return avancerWhileIsNotPressed(minDist*10+10);
	}
}