import extensions.CSVFile;
import extensions.File;

class main extends Program {

 //#region déclaration des constantes du jeux ou variables utilisées dans plusieurs fonctions
    final int TAILLE_DECK = 20;
    final int TAILLE_MAIN= 6;
    final int[][] indiceCarteJ1=new int[][]{{13,38},{11,5},{11,70}};
    final int[][] indiceCarteJ2=new int[][]{{28,38},{31,5},{31,70}};
    String [] placementQuestion= new String[]{"QUESTIONS","CHOIX DE LA DIFFICULTÉ","(1) Facile","(2) Normal","(3) Difficile"};
    
    //Constructeur de la classe joueur
    Joueur newJoueur(String pseudo, String classe){
        Joueur j = new Joueur();
        j.pseudo=pseudo;
        j.classe=classe;
        j.deck=new int[TAILLE_DECK];
        j.main=new Carte[TAILLE_MAIN];
        j.nbCarteRestanteDeck=TAILLE_DECK;
        j.indiceLignePlateauDeCarte=0;
        j.coordonneesAffichageCartes=new int[3][2];
        j.points=0;
        return j;
    }

    String toString(Joueur j){
        return "Pseudo : "+j.pseudo;
    }
       
    // Fonction qui gère le menu du jeux
    String menuJeux(){

	    File TxtBattleQuiz = newFile("Texts/BattleQuizDeluxe.txt"); //Stockage du contenu des fichiers texte dans une variable
        File TxtAccueil = newFile("Texts/accueil.txt");

        cursor(0, 0);
        afficherFichierTxt(TxtBattleQuiz); // Affichage du menu du jeux
        afficherFichierTxt(TxtAccueil);

        String choixUtilisateur = "";
        boolean saisieValide = false;

        do{ // L'utilisateur choisi un numéro entre 1 et 8 (inclus)
            cursor(20, 2);
            print("Entrez un numéro entre 1 et 8 pour valider :");
            choixUtilisateur = readString();
            if (length(choixUtilisateur) == 0);
            else if(charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0) > '8' || length(choixUtilisateur) > 1) { 
                cursor(20, 45);
                print("                                     ");
                cursor(21, 2);
                print("Numéro incorrect, veuillez réessayer.");
            }
            else saisieValide = true;
        } while(saisieValide == false);
        return choixUtilisateur;
    }
    // Fonction qui vient créer un joueur dans un encart spécifique
    Joueur formulaireCréationJoueur(int numJoueur){
        String pseudo; String classe; String choix;
        cursor(18,14);
        println("|-------------------------------------------------|");
        cursor(19,14);
        println("|            Création du joueur n°"+numJoueur+"               |");
        cursor(20,14);
        println("|-------------------------------------------------|");
        cursor(21,14);
        print("|    Pseudo :                                     |");
        cursor(22,14);
        println("|                                                 |");
        cursor(23,14);
        println("|-------------------------------------------------|");
        cursor(24,14);
        println("| Votre classe [(1)CE1 (2)CE2 (3)CM1 (4)CM2] :    |");
        cursor(25,14);
        println("|                                                 |");
        cursor(26,14);
        println("|-------------------------------------------------|");
        do{
            cursor(21,28);
            pseudo=readString();
            if(equals(pseudo,"")){
                cursor(22,16);
                println("Entrer un pseudo d'au moins 1 caractère");
            }
        }while(equals(pseudo,""));
        
        do{
            cursor(24,61);
            choix=readString();
            if(!equals(choix,"1") ||!equals(choix,"2")  || !equals(choix,"3")  || !equals(choix,"4")){
                cursor(25, 15);
                print("Erreur, entrer un chiffre entre 1 et 4");
            }
        }while(!equals(choix,"1") && !equals(choix,"2")  && !equals(choix,"3")  && !equals(choix,"4"));
        if(!equals(choix,"1")) classe="CE1";
        else if(!equals(choix,"2")) classe="CE2";
        else if(!equals(choix,"3")) classe="CM1";
        else classe="CM2";

        return newJoueur(pseudo,classe);
    }
    
    // Fonction qui gère la partie
    void partieEnCours(CSVFile fichierSauvegarde, String typeDePartie, boolean partieSauvegardee){
        //---------------------------INITIALISATION COMMUNE AUX 2 TYPES DE PARTIES--------------------------------------------------------//
        //#region Déclaration des variables nécessaires pour le bon fonctionnement du jeux
        int numTour = 0; // Nombre de tour de base
        int joueurQuiCommence = -1;
        String pseudoJoueur;
        String[] historiqueJeux = {"", "", "", ""};// Stocke l'historique du jeux  
        Carte[][] plateauDeCartes=new Carte[2][3]; // permet de stocker les cartes choisies de la main dans le plateau
        boolean finDePartie=false;
        boolean tourFini=false;
        boolean reussiCartePose;
        //-------Tampon de mémoire pour intervertir à chaque fois les positions de placement des cartes de chaque joueur------//
        Joueur joueurQuiJoue;
        //-------Affichage générer----------//
        String BorduresJeux = TXTtoString(newFile("Texts/BorduresJeux.txt"));
        String logoQCMString=TXTtoString(newFile("Texts/EncadréQuestion.txt"));
        String menuMainJoueur=TXTtoString(newFile("Texts/InteractionMain.txt"));
        String stylePlateau=TXTtoString(newFile("Texts/Plateau.txt"));
        //#endregion
        cursor(0, 0);
        print(BorduresJeux);
        cursor(2,0);
        print(logoQCMString);
        cursor(2, 105);
        print("Historique :");
        
        //---------------------------------------PARTIE JOUEUR CONTRE JOUEUR--------------------------------------------//
        if(equals(typeDePartie,"1vs1")){                    
            //-----------------------------------------MISE EN PLACE DE LA PARTIE---------------------------------------//
            Joueur j1 = new Joueur(); 
            Joueur j2 = new Joueur();
            if (partieSauvegardee){ // Chargement des Joueurs avec leur classe
                j1.pseudo = getCell(loadCSV("fichiersCSV/infosJoueursSave.csv"), 1, 0); 
                j1.classe = getCell(loadCSV("fichiersCSV/infosJoueursSave.csv"), 1, 1); 
                viderPlateau();
                j2.pseudo = getCell(loadCSV("fichiersCSV/infosJoueursSave.csv"), 2, 0);
                j2.classe = getCell(loadCSV("fichiersCSV/infosJoueursSave.csv"), 2, 1); 
                viderPlateau();
                cursor(9, 105);
                numTour = stringToInt(getCell(fichierSauvegarde, 1, 4)); // Récupération du nombre de tour
            }
            else{ // Création des Joueurs avec leur classe
                j1=formulaireCréationJoueur(1);
                viderPlateau();
                j2=formulaireCréationJoueur(2);
                viderPlateau();
                cursor(9, 105);
            }
            print("Tour n°" + numTour);
            if (partieSauvegardee) joueurQuiCommence = stringToInt(getCell(fichierSauvegarde, 1, 5));
            else{
                joueurQuiCommence = tirageAleatoire();            // Détermine aléatoirement qui commencera la partie
                afficherPiece(joueurQuiCommence);                 // On affiche le côté de la pièce du gagnant (Face : joueur1 ; Pile : joueur2)
                delay(1500);                                      // pendant 1.5 secondes
            }
            if(joueurQuiCommence==1){ // Stocke la donnée de si c'est au joueur 1 ou 2 de commencer
                pseudoJoueur=j1.pseudo;
            }else{
                pseudoJoueur=j2.pseudo;
            }
            viderPlateau();
            cursor(10, 1);
            print(stylePlateau);
            j1.deck = declarationDeck(fichierSauvegarde, 1); // Initialisation du deck du joueur 1
            j2.deck = declarationDeck(fichierSauvegarde, 2); // Initialisation du deck du joueur 2
            if (numTour == 0){
                afficherHistoriquePartie(historiqueJeux, "C'est le joueur " + pseudoJoueur + " qui commence !"); // Met à jour l'historique de la partie
                afficherHistoriquePartie(historiqueJeux, "Chaque joueur dispose d'un deck sélectionné aléatoirement.");
                afficherHistoriquePartie(historiqueJeux, "Chaque joueur pioche 3 cartes.");
                j1.nbCarteRestanteDeck = addCartetoMain(3,j1.main, j1.deck, j1.nbCarteRestanteDeck, historiqueJeux);  // Ajout de 3 cartes aléatoire dans la main de chaque joueur
                j2.nbCarteRestanteDeck = addCartetoMain(3,j2.main, j2.deck, j2.nbCarteRestanteDeck, historiqueJeux);
                j1.points = 0; // Initialisation des points des joueurs
                j2.points = 0;
            }
            else{ // Chargement des données sauvegardées du joueur
                if (!equals(getCell(fichierSauvegarde, 1, 1), "")) j1.points = stringToInt(getCell(fichierSauvegarde, 1, 1)); // Initialisation des points des joueurs
                if (!equals(getCell(fichierSauvegarde, 2, 1), "")) j2.points = stringToInt(getCell(fichierSauvegarde, 2, 1));
                j1.nbCarteRestanteDeck = stringToInt(getCell(fichierSauvegarde, 1, 6)); // Initialisation du nombre de cartes restantes dans le deck
                j2.nbCarteRestanteDeck = stringToInt(getCell(fichierSauvegarde, 2, 6));
                j1.main = new Carte[TAILLE_MAIN];
                j2.main = new Carte[TAILLE_MAIN];
                ajoutCarteSauvegardeeDansMain(j1, 1,  fichierSauvegarde);               // Initialisation de la main des joueurs
                ajoutCarteSauvegardeeDansMain(j2, 2, fichierSauvegarde);
                ajoutCarteSauvegardeeDansPlateau(plateauDeCartes);                      // Initialisation du plateau de cartes
            }

            while(!finDePartie){// Lancement du gameplay, tourne tant que finDePartie est sur false.
                tourFini=false; // rénitialisation de la variable pour continuer switch en les joueurs.
                while(!tourFini){
                    if(joueurQuiCommence==1){
                        joueurQuiJoue=j1;
                        joueurQuiJoue.indiceLignePlateauDeCarte=0;
                        joueurQuiJoue.coordonneesAffichageCartes=indiceCarteJ1;
                    }else{
                        joueurQuiJoue=j2;
                        joueurQuiJoue.indiceLignePlateauDeCarte=1;
                        joueurQuiJoue.coordonneesAffichageCartes=indiceCarteJ2;
                    }
                    if(numTour==0 || numTour==1){
                        cursor(2,0);
                        print(menuMainJoueur); 
                        afficherMain(joueurQuiJoue.main);// Affichage de la main du joueur qui joue
                        afficherHistoriquePartie(historiqueJeux, "C'est à " + joueurQuiJoue.pseudo + " de placer ses cartes.");
                        afficherHistoriquePartie(historiqueJeux,"Placez une carte sur le poste actif.");
                        placerCarteFinal(plateauDeCartes,joueurQuiJoue,0,historiqueJeux);
                        afficherHistoriquePartie(historiqueJeux,"Placez si vous le souhaitez une ou plusieurs cartes sur le banc.");
                        placerCarteFinal(plateauDeCartes,joueurQuiJoue,1,historiqueJeux);
                        placerCarteFinal(plateauDeCartes,joueurQuiJoue,2,historiqueJeux);
                        
                    }else{
                        if (joueurQuiCommence == 1 && !partieSauvegardee) j1.nbCarteRestanteDeck = addCartetoMain(1,j1.main, j1.deck, j1.nbCarteRestanteDeck, historiqueJeux);
                        else if(!partieSauvegardee) j2.nbCarteRestanteDeck = addCartetoMain(1,j2.main, j2.deck, j2.nbCarteRestanteDeck, historiqueJeux);
                        afficherHistoriquePartie(historiqueJeux, joueurQuiJoue.pseudo + " pioche une carte.");
                        cursor(2,0);
                        print(menuMainJoueur);
                        afficherMain(joueurQuiJoue.main);
                        int test = readInt(); // Test de la fonction de sauvegarde
                        if (test == 1){
                            sauvegarderInfosPartie(j1, j2, joueurQuiJoue, numTour, joueurQuiCommence);
                            sauvegarderPlateau(plateauDeCartes);
                            sauvegarderInfosJoueurs(j1, j2);
                        }
                        // inclure les choix possibles ( voir encadréQuestion.txt)
                        // bouton attaque ou retraite préssé -> QCMpourAttaquer() --> décomenter ce qui suit :
                        //chargementASCII();
                        //actualiserQuestions();
                        //cadreDifficulté(placementQuestion);
                        //int choixDifficulté=choixDifficulté();
                        //boolean attaqueAcceptée=QCMpourAttaquer(joueurQuiJoue,choixDifficulté);
                        //actualiserQuestions();
                        //cursor(2,0);
                        //println(logoQCMString);
                        partieSauvegardee = false;
                        delay(1000); // test , delai temporaire
                    }
                numTour++;
                joueurQuiCommence=(joueurQuiCommence+1)%2;
                tourFini=true;    
                }   
            }
        
        //----------------------------------PARTIE BOT CONTRE JOUEUR--------------------------------------//
        }else{
            cursor(28,31);
            println("pas encore fait...1 jour peut-etre");
            delay(5000);   
        }
    }

    int compteurCarteMain(Carte[]main){
        int cpt=0;
        for(int i=0;i<TAILLE_MAIN;i++){
           if(main[i]!=null){
            cpt++;
            }
        }
    return cpt;
    }

    boolean placerCarte(Carte[][] plateauDeCartes,Joueur joueurQuiJoue,int carteChoisi, int posteCarte,String[] historiqueJeux){
            if(equals(joueurQuiJoue.main[carteChoisi-1].type,"Monstre")){
                plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][posteCarte]=joueurQuiJoue.main[carteChoisi-1];
                afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][posteCarte],joueurQuiJoue.coordonneesAffichageCartes[posteCarte][0],joueurQuiJoue.coordonneesAffichageCartes[posteCarte][1]);
                joueurQuiJoue.main[carteChoisi-1]=null;
                joueurQuiJoue.main=resetMain(joueurQuiJoue.main);
                viderMain();
                afficherMain(joueurQuiJoue.main);
                return true;
            }else{
                afficherHistoriquePartie(historiqueJeux,"Vous ne pouvez placer que des monstres sur le terrain.");
                return false;
            }
    }
    
    void placerCarteFinal(Carte[][] plateauDeCartes,Joueur joueurQuiJoue,int posteCarte,String[] historiqueJeux){
        int carteChoisi;
        boolean estPlace;
        do{
            carteChoisi=choixJoueur(compteurCarteMain(joueurQuiJoue.main));
            if(carteChoisi==7){
                return;
            }
            estPlace=placerCarte(plateauDeCartes,joueurQuiJoue,carteChoisi,posteCarte,historiqueJeux);
        }while(!estPlace);
    }

    Carte[] resetMain(Carte[] mainInitiale){
        Carte[] newMain=new Carte[TAILLE_MAIN];
        int j=0;
        for(int i=0;i<length(mainInitiale);i++){
           if(mainInitiale[i]!=null){
                newMain[j]=mainInitiale[i];
                j++;
           }
        }
        return newMain; 
    }

    int choixJoueur(int max){
        int choixJoueurInteractionMain;
        do{
            choixJoueurInteractionMain=readInt();
        }while((choixJoueurInteractionMain<1 || choixJoueurInteractionMain >max)&&(choixJoueurInteractionMain!=7));
        return choixJoueurInteractionMain;

    }
    
    // Rajoute une carte aléatoire (provenant du deck du joueur) dans la main du joueur
    int ajoutCarteDansMain(Carte[] mainJoueur, int[] deckJoueur, int nbrCartesDeckJoueur, String[] historique){

        if (mainJoueur[length(mainJoueur)-1] == null){ // Si il y a de la place dans la main du joueur on ajoute une carte du deck à sa main.
            for (int i=0; i<length(mainJoueur); i++){
                if (mainJoueur[i] == null) {
                    int cartePiocheeAleatoire = (int) (random()*nbrCartesDeckJoueur); // On prend l'indice d'une des cartes au hasard dans le deck
                    if (deckJoueur[19] != 0) { // Si on a pas encore ajoute de cartes en main, il faut au moins 1 monstre dans la main pour lancer le jeu
                        while(cartePiocheeAleatoire > 16) cartePiocheeAleatoire = (int) (random()*nbrCartesDeckJoueur);
                    }
                    int numCarte = deckJoueur[cartePiocheeAleatoire];
                    mainJoueur[i] = definitCarte(numCarte);// On ajoute la carte dans la main du joueur
                    deckJoueur[cartePiocheeAleatoire] = 0; // On enlève la carte du deck
                    triDeck(deckJoueur);                   // Et on tri le deck (carte vide placée à la fin)
                    nbrCartesDeckJoueur--;
                    return nbrCartesDeckJoueur;
                }
            }
        } // Sinon, on indique qu'il n'y a pas de place dans sa main.
        else afficherHistoriquePartie(historique, "Vous avez atteint le nombre de cartes maximale dans votre main.");
        return nbrCartesDeckJoueur;
    }

    int addCartetoMain(int nombreDeCarteàAjouter, Carte[] mainJoueur, int[] deckJoueur, int nbrCartesDeckJoueur, String[] historique){
        for(int i=0;i<nombreDeCarteàAjouter;i++){
            nbrCartesDeckJoueur = ajoutCarteDansMain(mainJoueur,deckJoueur,nbrCartesDeckJoueur,historique);
        }
        return nbrCartesDeckJoueur;
    }

    // Tri le deck du joueur (carte déja utilisée placée au fond du deck ayant comme valeur 0)
    void triDeck(int[] deckJoueur){

        for (int i=0; i<TAILLE_DECK-1; i++){            // On parcours le deck du joueur
            if(deckJoueur[i] == 0){                     // Dès que l'on trouve la carte jouée,
                deckJoueur[i] = deckJoueur[i+1];        // On décale toutes les cartes suivantes d'un indice
                deckJoueur[i+1] = 0;
            }
        }
    }

    // Permet de définir une nouvelle carte avec ses caractéristiques
    Carte definitCarte(int indiceCarte){

        CSVFile listeCartes = loadCSV("fichiersCSV/cartes.csv");
        
        Carte nouvelleCarte = new Carte();                                  // Création d'une nouvelle carte
        // On récupère les différentes informations de la carte
        nouvelleCarte.idCarte = indiceCarte;                                           // Numéro de la carte
        nouvelleCarte.type = getCell(listeCartes, indiceCarte, 1);                     // Colonne Type
        nouvelleCarte.nom = getCell(listeCartes, indiceCarte, 2);                      // Colonne Nom
        nouvelleCarte.description = getCell(listeCartes, indiceCarte, 7);              // Colonne Description
        nouvelleCarte.ASCII = imageASCIICarte(listeCartes, indiceCarte);               // Colonne ASCII
        if (equals(nouvelleCarte.type, "Monstre")){ // Si c'est un monstre, on lui rajoute ces caractéristiques.
            nouvelleCarte.PV = stringToInt(getCell(listeCartes, indiceCarte, 3));      // Colonne PV
            nouvelleCarte.description = getCell(listeCartes, indiceCarte, 4);          // Colonne DescriptionAttaque
            nouvelleCarte.attaque = stringToInt(getCell(listeCartes, indiceCarte, 5)); // Colonne Attaque
            nouvelleCarte.retraite = stringToInt(getCell(listeCartes, indiceCarte, 6));// Colonne Retraite
        }
        return nouvelleCarte; // Retourne la carte contenant toute ces informations
    }

    // Renvoie le deck du joueur
    int[] declarationDeck(CSVFile fichierSauvegarde, int numJoueur){

        String typeCarte = getCell(fichierSauvegarde, numJoueur, 2);
        int[] deckJoueur = new int[TAILLE_DECK];            // Déclaration d'un deck vide

        if (length(typeCarte) != 0){                        // Si il y a un deck dans le fichier de sauvegarde, alors on sélectionne ces cartes là
            
            int index = 0;
            int nombreActuel = 0;

            for(int i=0; i<length(typeCarte); i++){
                char c = charAt(typeCarte, i);
                if (c == '/' && charAt(typeCarte, i-1) != c) {// Si il y a un '/', on termine le nombre actuel
                    deckJoueur[index++] = nombreActuel;
                    nombreActuel = 0;                       // Réinitialiser pour le prochain nombre
                } else {
                    nombreActuel = nombreActuel * 10 + (c - '0');
                }
            }
            deckJoueur[index] = nombreActuel;               // Ajouter la derniere carte du deck
        }
        else{                                               // Sinon, le deck est vide et on rajoute les cartes normalement
            int choixDeck = 8*(numJoueur-1);                // Sélectionne le deck 1 pour le joueur 1 et le deck 2 pour le joueur 2
            for (int i = 0; i<8; i++){
                deckJoueur[i] += i+choixDeck+1;
                deckJoueur[i+8] += i+choixDeck+1;
            }
            deckJoueur[16] += 17;                           // Ajout manuel des dernières cartes qui elles sont contenues dans tous les decks
            deckJoueur[17] += 17;
            deckJoueur[18] += 18;
            deckJoueur[19] += 18;
        }
        return deckJoueur;                                  // Retoure le deck du joueur (chaque numéro correspond à un id de carte)
    }
    
    // Fonction permettant de contenir le dessin ASCII de la carte passée en paramètre
    String imageASCIICarte(CSVFile fichierCSV, int indiceCarte){
        
            // Récupérer l'ASCII du premier monstre (index de la colonne ASCII = 7)
            String asciiCarte = getCell(fichierCSV, indiceCarte, 8); // Ligne d'indice de la carte, colonne 7 (index ASCII)
            
            asciiCarte = asciiCarte.replace("\"", "");    // Remplace les guillemets doubles par une chaîne vide
            asciiCarte = asciiCarte.replace("\\n", "\n"); // Remplacer "\n" par un retour à la ligne réel

            return asciiCarte; // Afficher l'ASCII
    }

    // Fonction qui tire un nombre aléatoire entre 1 et 2
    int tirageAleatoire(){
        int nbrAleatoire = 1+(int) (random()*((2-1)+1));
        return nbrAleatoire; // Renvoie un numéro entre 1 ou 2
    }

    // Fonction qui convertit un entier int en String
    String intToString(int nombre){
        String nombreString = "";
        while(nombre != 0){
            nombreString = (char) (nombre%10 + '0') + nombreString;
            nombre = nombre/10;
        }
        return nombreString;
    }

// #region ----------------- Fonctions d'affichage ----------------------------------------------------------------------------------------//
    void viderPlateau(){
        for (int i=11;i<45;i++){
            cursor(i,2);
            println("                                                                                                   |                                                                                               ");
        }
    }
    
    void viderMain(){
        for (int i=11;i<45;i++){
            cursor(i,101);
            println("|                                                                                               ");
        }
    }
    
    void afficherMain(Carte[] mainJoueur){
        int cpt=0;
        if (cpt == TAILLE_MAIN) return;
        int indiceColonne;
        int indiceLigne=11;
        int maxParLigne=3;
        for(int i=0;i<TAILLE_MAIN;i++){
            if (mainJoueur[i] == null) {
                return;
            }else{
                indiceColonne=103+28*i;
                afficherDetailsCarte(mainJoueur[i],indiceLigne,indiceColonne);
                if (i==maxParLigne){
                    indiceLigne=indiceLigne+14;
                }
                cpt++;
            } 
        }
    }    
    // Fonction permettant d'afficher toutes les données d'une carte sous un format structuré en ASCII
    
    void afficherDetailsCarte(Carte infosCarte, int lig, int col) {

        String PV_Carte = Integer.toString(infosCarte.PV); // Fonction int to String A faire
        String attaque_Carte = Integer.toString(infosCarte.attaque);
        String retraite_Carte = Integer.toString(infosCarte.retraite);
        
        int ligIndice = 1;
        cursor(lig, col);
        print("___________________________"); 
        cursor(lig+ligIndice,col);                
        print("|  " + infosCarte.nom);        // Ajoute le nom

        for (int i = length(infosCarte.nom); i<18; i++) print(" ");
        if(equals(infosCarte.type, "Monstre")){                                 // Ajout des PVs pour les monstres
            if (length(PV_Carte) < 3) print(" ");
            print(PV_Carte + "PV");
        }
        else print("     ");
        print("|");
        ligIndice++;
        cursor(lig+ligIndice,col);

        int hauteurAscii = 0;
        for (int i=0; i<length(infosCarte.ASCII); i++){
            if (charAt(infosCarte.ASCII, i) == '\n') hauteurAscii++;
        }
        
        for (int i = 0; i < 5 - hauteurAscii; i++) {                       // Ajoute de l'espacement vide si nécessaire (si le dessin est trop petit)
            print("|                         |");
            ligIndice++;
            cursor(lig+ligIndice,col);
        }
        
        int colIndice = 0;
        for (int i = 0; i < length(infosCarte.ASCII); i++) {               // Ajoute le dessin ASCII
            if (colIndice == 0) print("|     ");                // Ajoute le début de la ligne de la carte
            if (charAt(infosCarte.ASCII, i) == '\n' || i == length(infosCarte.ASCII)-1){
                while (colIndice < 20){                         // Ajoute des espaces à droite du dessin,
                    print(" ");
                    colIndice++;
                }
                print("|");                                     // Puis rajoute le "|" de fin de ligne
                ligIndice++;
                colIndice = 0;
                cursor(lig+ligIndice,col);
            }
            else{
                print(charAt(infosCarte.ASCII, i));
                colIndice++;
            }
        }

        print("|_________________________|");
        ligIndice++;
        cursor(lig+ligIndice,col);
        print("|                         |");
        ligIndice++;
        cursor(lig+ligIndice,col);
        
        if(equals(infosCarte.type,"Monstre")){                                   // Description de la carte pour les Monstres
            print("|  - " + attaque_Carte + " : " + infosCarte.description);
            for (int i = length(attaque_Carte) + length(infosCarte.description); i<18; i++) print(" ");
            print("|");
            ligIndice++;
            cursor(lig+ligIndice,col);
            print("|                         |");
            ligIndice++;
            cursor(lig+ligIndice,col);
            print("| Points de retraite : " + retraite_Carte + "  |"); // Ajoute les points de retraite
            ligIndice++;
            cursor(lig+ligIndice,col);
        }
        else{                                                              // Description de la carte pour les autres types de cartes
            print("|  - ");
            if (length(infosCarte.description) > 21){                            // Si la description de la carte est trop grande,
                int tailleEspaceRestant = 42 - length(infosCarte.description);   // on l'affiche sur deux lignes.
                for (int i=0; i<42; i++) {
                    if (i < length(infosCarte.description)) print(charAt(infosCarte.description, i));
                    else print(" ");
                    if (i == 20) {
                        print("|");
                        ligIndice++;
                        cursor(lig+ligIndice,col);
                        print("|    ");
                    }
                }
                print("|");
                ligIndice++;
                cursor(lig+ligIndice,col);
            }
            else {
                print(infosCarte.description);
                for (int i = length(infosCarte.description); i<21; i++) print(" ");
                print("|");
                ligIndice++;
                cursor(lig+ligIndice,col);
                print("|                         |");
                ligIndice++;
                cursor(lig+ligIndice,col);
            } 
            print("|                         |");
            ligIndice++;
            cursor(lig+ligIndice,col);
        }
        print("---------------------------"); // Fin de l'affichage de la carte
    }

    // Fonction pour afficher le contenu d'un deck
    void afficherDeck(Joueur joueur){
        for (int i=0; i<length(joueur.deck); i++){
            print(joueur.deck[i] + " ");
        }
    }

    // Fonction pour afficher toutes les cartes
    void afficherPokedex(CSVFile fichierCSV){
        int nbLignes = rowCount(fichierCSV); // Compte le nombre de lignes dans le fichier
        for(int i=0; i<nbLignes-1; i++){
            //afficherDetailsCarte(fichierCSV, i+1); test a faire
        }
    }

    // Fonction qui gère l'affichage de la pièce (Pile ou Face)
    void afficherPiece(int tirage){

        File cotéGagnant = null;
        File[] fichiersPieces = { // Fichiers texte pour l'animation de la pièce
            new File("Texts/Pieces/pieceEuroPlat1.txt"),
            new File("Texts/Pieces/pieceEuroPlat0.txt"),
            new File("Texts/Pieces/pieceEuroPlat2.txt")
        };

        for (int i=0; i<3; i++){
            cursor(10, 0);
            afficherFichierTxt(fichiersPieces[i]);
            delay(500);
        }
        cursor(10, 0);

        if(tirage == 1){ // Si on a tiré face
            cotéGagnant = newFile("Texts/Pieces/pieceFace.txt");
        }
        else{            // Si on a tiré pile
            cotéGagnant = newFile("Texts/Pieces/piecePile.txt");
        }
        afficherFichierTxt(cotéGagnant);
    }

    // Fonction qui affiche l'historique du jeu en haut à gauche de l'écran
    void afficherHistoriquePartie(String[] historique, String Maj){
        if (!equals(historique[3],"")){           // Met à jour l'historique du jeu
            for(int i=0; i<3; i++) historique[i] = historique[i+1];
            historique[3] = Maj;
        }
        else{                                     // Si l'historique n'est pas rempli à fond, on rajoute l'évènement à la suite
            for (int i=0; i<4; i++){
                if (historique[i] == ""){
                    historique[i] = Maj;
                    i = 3;
                }
            }
        }
        for(int i=0; i<length(historique); i++){ // Affiche l'historique du jeu
            cursor(i+4, 105);
            if (i == length(historique)-1) print(ANSI_BLUE);
            print(historique[i]);
            if (i == length(historique)-1) print(ANSI_RESET);
            for (int j=length(historique[i]); j<68; j++) print(" ");
        }
        delay(1000);
    }

    // Fonction pour afficher le contenu d'un fichier txt
    void afficherFichierTxt(File texte){
        while(ready(texte)){            // On s'arrête dès qu'on lit une ligne null (fin du fichier)
	        println(readLine(texte));   // Affichage du contenu de la ligne suivante
	    }	
    }
    
    // Fonction pour faire une animation du chargement du QCM pour Attaquer
    void chargementASCII(){
            cursor(8,37);
            print("██║");
            delay(500);
            cursor(8,42);
            print("██║");
            delay(500);
            cursor(8,47);
            print("██║");
            delay(500);
    }
    
    // Fonction pour Transformer un ficher TXT en une String afin de pouvoir utiliser plusieur fois un même fichier
     String TXTtoString(File Fichier){
        String affichage="";
        while(ready(Fichier)){
            affichage= affichage+(readLine(Fichier))+"\n";
        }
        return affichage;
    }

//#region ----------------- Fonctions pour le QCM avant chaque attaque --------------------------------------------------------------------//
/*Les quatres fonctions ci-dessous permette une selection d'une question aléatoire en fonction de la classe du joueur (ce1, ce2, cm1,cm2) et du choix de la difficulté
et test si la réponse donné par l'utilisateur est correct ou non */
    
    boolean réussirQCM(int entréeUtilisateur, int aleaLigne, CSVFile acharger){
        boolean réussi=false;
        String réponseUtilisateur=getCell(acharger, aleaLigne, entréeUtilisateur);
        if (equals(getCell(acharger,aleaLigne,5),réponseUtilisateur)){
            réussi=true;
        }
        return réussi;   
    }

    int choixDifficulté(){
        int choixDifficulté;
        String messageErreur="Veuillez entrez un chiffre valable";
        do{
            choixDifficulté=readInt();
            if (choixDifficulté<1 || choixDifficulté >3){
                println(messageErreur);
            }
        }while(choixDifficulté<1 || choixDifficulté >3);
        return choixDifficulté;
    }
    
    int controleChoixRéponses(){
        int choixQCM;
        String messageErreur="Veuillez entrez un chiffre entre 1 et 4";
        do{
            choixQCM=readInt();
            if (choixQCM<1 || choixQCM >4){
                println(messageErreur);
                print("Votre réponse : ");
            }
        }while(choixQCM<1 || choixQCM >4);
        return choixQCM;
    }
    
    CSVFile aCharger(Joueur j,int choixDifficulté,CSVFile cp,CSVFile ce1,CSVFile ce2,CSVFile cm1,CSVFile cm2,CSVFile sixième){
        CSVFile aCharger;
        if(j.classe=="CE1"){
            if(choixDifficulté==1)aCharger=cp;
            else if(choixDifficulté==2)aCharger=ce1;
            else aCharger=ce2;
        
        }else if(j.classe=="CE2"){
            if(choixDifficulté==1)aCharger=ce1;
            else if(choixDifficulté==2)aCharger=ce2;
            else aCharger=cm1;
            
        }else if(j.classe=="CM1"){
            if(choixDifficulté==1)aCharger=ce2;
            else if(choixDifficulté==2)aCharger=cm1;
            else aCharger=cm2;
        
        }else{
            if(choixDifficulté==1)aCharger=cm1;
            else if(choixDifficulté==2)aCharger=cm2;
            else aCharger=sixième;
        }
        return aCharger;
    }    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*Les 3 fonctions ci-dessous viennent créer les cadres de l'affichage des questions*/
    
    void cadreAffichageQuestions(CSVFile acharger, int aleaLigne, String [] tab){
        tab[0]=getCell(acharger,aleaLigne,0);
        tab[1]=getCell(acharger,aleaLigne,1);
        tab[2]=getCell(acharger,aleaLigne,2);
        tab[3]=getCell(acharger,aleaLigne,3);
        tab[4]=getCell(acharger,aleaLigne,4);
        cursor(4,25);
        println("| "+tab[0]);
        cursor(5,25);
        println("|----------------------------------------------------|");
        cursor(6,25);
        println("|     (1)"+tab[1]+"  (2)"+tab[2]);
        cursor(7,25);
        println("|     (3)"+tab[3]+"  (4)"+tab[4]);
        
    }
    
    void cadreDifficulté(String[] tab){
        cursor(1,25);
        println("|");
        cursor(1,78);
        println("|");
        cursor(2,25);
        println("|");
        cursor(2,78);
        println("|");
        cursor(4,25);
        println("|  "+tab[0]+" :");
        cursor(5,25);
        println("|----------------------------------------------------|");
        cursor(6,25);
        println("|  "+tab[1]+" :");
        cursor(7,25);
        println("|  "+tab[2]+"   "+tab[3]+"   "+tab[4]);
        cursor(9,25);
        println("|");
        cursor(9,78);
        println("|");
        cursor(10,25);
        println("|");
        cursor(10,78);
        println("|");

    }
    
    void actualiserQuestions(){
        for (int i=2; i<11;i++){
            cursor(i,25);
            println("|                                                    |");
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*Cette fonction execute toute les précédentes et indique à l'algorithme principale si l'attaque est validé par le biais d'un qcm qui s'adapte à la classe du joueur*/ 
    
    boolean QCMpourAttaquer(Joueur j,int choixDifficulté){
        boolean résultatQCM=false;
        //Liste des fichiers de questions
        CSVFile cp = loadCSV("./fichiersCSV/Questions/questionCP.csv");
        CSVFile ce1 = loadCSV("./fichiersCSV//Questions/questionCE1.csv");
        CSVFile ce2 = loadCSV("./fichiersCSV//Questions/questionCE2.csv");
        CSVFile cm1 = loadCSV("./fichiersCSV//Questions/questionCM1.csv");
        CSVFile cm2 = loadCSV("./fichiersCSV//Questions/questionCM2.csv");
        CSVFile sixième = loadCSV("./fichiersCSV//Questions/question6e.csv");
        //-------------------------------------------//
        //Genere un indice d'une ligne d'un fichier de question compris en 1 et 30
        int aleaLigne = (int) (random()*31);
        //-------------------------------------------//
        // Création d'un tableau pour ordonné l'affichage plus tard
        //-------------------------------------------//
        CSVFile acharger=aCharger(j,choixDifficulté,cp,ce1,ce2,cm1,cm2,sixième);
        actualiserQuestions();
        cadreAffichageQuestions(acharger, aleaLigne, placementQuestion);
        int entréeUtilisateur=controleChoixRéponses();
        if (réussirQCM(entréeUtilisateur,aleaLigne,acharger)){
            println("Bravo, bonne réponse !");
            résultatQCM=true;
        }else{
            println("FAUX !, la bonne réponse était "+getCell(acharger, aleaLigne,5));
        }
        return résultatQCM;
    }
// #endregion-----------------------------------------------------------------------------------------------------------------------------------------//

//#region------------------------- Fonctions de sauvegarde --------------------------------------------------------------------------//

    // Fonction pour sauvegarder les informations de la partie
    void sauvegarderInfosPartie(Joueur joueur1, Joueur joueur2, Joueur joueurQuiJoue, int numTour, int joueurQuiCommence){
        
    String partieSave[][] = new String[3][7];
    String nomPartieSave = "fichiersCSV/partieSave.csv";

    String joueurActif = intToString(joueurQuiCommence);
    if (joueurActif == "") joueurActif = "2";

    partieSave[0][0] = "numJoueur";
    partieSave[0][1] = "pointsJoueur";
    partieSave[0][2] = "cartesDeck";
    partieSave[0][3] = "cartesMain";
    partieSave[0][4] = "numTour";
    partieSave[0][5] = "joueurActif";
    partieSave[0][6] = "nbrCartesDeck";
    partieSave[1][2] = "";
    partieSave[2][2] = "";
    partieSave[1][3] = "";
    partieSave[2][3] = "";

    for(int i=1; i<3; i++){
        partieSave[i][0] = intToString(i);
        partieSave[i][4] = intToString(numTour);
        partieSave[i][5] = joueurActif; 
        if (i == 1){
            partieSave[i][1] = intToString(joueur1.points);
            partieSave[i][6] = intToString(joueur1.nbCarteRestanteDeck);
        }
        else{
            partieSave[i][1] = intToString(joueur2.points);
            partieSave[i][6] = intToString(joueur2.nbCarteRestanteDeck);
        }
    }

    for (int i=0; i<TAILLE_DECK; i++){
        partieSave[1][2] += intToString(joueur1.deck[i]);
        partieSave[2][2] += intToString(joueur2.deck[i]);
        if (i<TAILLE_DECK-1) {
            partieSave[1][2] += "/";
            partieSave[2][2] += "/";
        }
    }

    for(int i=0; i<length(joueur1.main); i++){
        
        if (joueur1.main[i] != null) partieSave[1][3] += intToString(joueur1.main[i].idCarte);
        if (joueur2.main[i] != null) partieSave[2][3] += intToString(joueur2.main[i].idCarte);
        if (i<length(joueur1.main)-1) {
            partieSave[1][3] += "/";
            partieSave[2][3] += "/";
        }
    }

    saveCSV(partieSave, nomPartieSave);
}

    // Fonction pour sauvegarder les informations du plateau
    void sauvegarderPlateau(Carte[][] plateau){

        String plateauSave[][] = new String[7][2];
        String nomPlateauSave = "fichiersCSV/plateauSave.csv";

        plateauSave[0][0] = "idCarte";
        plateauSave[0][1] = "PV";

        int compteur = 1;

       for (int i=0; i<2; i++){ // boucle que enregistre dans le CSV les cartes du plateau avec leur id et leur PV
        // plateau est de taille 2x3 et je veux stocker les informations des 6 cartes dans un csv de taille 2x6
            for (int j=0; j<3; j++) {
                if (plateau[i][j] != null) {
                    plateauSave[compteur][0] = intToString(plateau[i][j].idCarte);
                    plateauSave[compteur][1] = intToString(plateau[i][j].PV);
                } else {
                    plateauSave[compteur][0] = "0";
                    plateauSave[compteur][1] = "0";
                }
                compteur++;
            }
       }

       saveCSV(plateauSave, nomPlateauSave);

    }

    // Fonction pour sauvegarder les informations des joueurs
    void sauvegarderInfosJoueurs(Joueur joueur1, Joueur joueur2){

        String infosJoueursSave[][] = new String[3][2];
        String nomInfosJoueursSave = "fichiersCSV/infosJoueursSave.csv";

        infosJoueursSave[0][0] = "pseudo";
        infosJoueursSave[0][1] = "classe";
        infosJoueursSave[1][0] = joueur1.pseudo;
        infosJoueursSave[1][1] = joueur1.classe;
        infosJoueursSave[2][0] = joueur2.pseudo;
        infosJoueursSave[2][1] = joueur2.classe;

        saveCSV(infosJoueursSave, nomInfosJoueursSave);
    }

    // Définit la carte de la main à parti du fichier de sauvegarde
    void ajoutCarteSauvegardeeDansMain(Joueur joueur, int numJoueur, CSVFile partieSave){

        String typeCarte = getCell(partieSave, numJoueur, 3);
        int index = 0;
        int nombreActuel = 0;

        for(int i=0; i<length(typeCarte); i++){
            char c = charAt(typeCarte, i);
            if (c == '/' && charAt(typeCarte, i-1) != c) {
                joueur.main[index++] = definitCarte(nombreActuel);
                nombreActuel = 0;
            } else {
                nombreActuel = nombreActuel * 10 + (c - '0');
            }
        }
    }

    // Définit les cartes du plateau à partir du fichier de sauvegarde
    void ajoutCarteSauvegardeeDansPlateau(Carte[][] plateau){

        CSVFile partieSave = loadCSV("fichiersCSV/plateauSave.csv");

        int joueur = 1;
        int indiceCarte = 0;
        int lig = 13;
        int col = 38;

        for (int i=1; i<7; i++){
            if(!equals(getCell(partieSave, i, 0), "0")){
                if (i == 2 || i == 3) lig = 11;
                if (i == 5 || i == 6) lig = 31;
                if (i == 2 || i == 5) col = 5;
                if (i == 3 || i == 6) col = 70;
                int idCarte = stringToInt(getCell(partieSave, i, 0));
                plateau[joueur-1][indiceCarte] = definitCarte(idCarte);
                plateau[joueur-1][indiceCarte].PV = stringToInt(getCell(partieSave, i, 1));
                afficherDetailsCarte(plateau[joueur-1][indiceCarte],lig,col);
            }
            if (i == 3){
                joueur = 2;
                indiceCarte = 0;
                lig = 28;
                col = 38;
            }
            else indiceCarte++;
        }
    }

//#endregion

    void algorithm() {
        //CSVFile fichierCSV = loadCSV("fichiersCSV/cartes.csv"); // Charger le fichier CSV contenant les informations des cartes
        //int nbLignes = rowCount(fichierCSV); // Compte le nombre de lignes dans le fichier
        
        CSVFile partieSélectionnée = null;
        boolean finJeu = false;
        String typeDePartie="";

        do{ // Tant que le joueur ne quitte pas le jeu, il reste dans le menu // test de la boucle while
            File BorduresJeux = newFile("Texts/BorduresJeux.txt");
            cursor(0, 0);
            afficherFichierTxt(BorduresJeux); // Affichage des bordures du jeux
            cursor(0, 0);
            String choixJoueur = menuJeux();
            switch (choixJoueur){
                case "1": // Choix d'une partie solo contre bot
                    partieSélectionnée = loadCSV("fichiersCSV/partieVide.csv");
                    typeDePartie="1vsBOT";
                    partieEnCours(partieSélectionnée,"1vsBOT", false);// On lance la partie
                    break;

                case "2": // Choix d'une partie 1v1 
                    partieSélectionnée = loadCSV("fichiersCSV/partieVide.csv");
                    typeDePartie="1vs1";
                    partieEnCours(partieSélectionnée,"1vs1", false);// On lance la partie;
                    break;

                case "3": // Choix d'une partie deja sauvegardee
                    partieSélectionnée = loadCSV("fichiersCSV/partieSave.csv");
                    typeDePartie="1vs1";
                    partieEnCours(partieSélectionnée,"1vs1", true);// On lance la partie;
                    break;

                case "8": // Choix de fin du jeu
                    finJeu = true;
                    break;
                default:
                    print("\n| Erreur");
            }
        }while(!finJeu);
        
        println("\n| Merci d'avoir joué !");
    }
}
