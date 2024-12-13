import extensions.CSVFile;
import extensions.File;

class main extends Program {

 //#region déclaration des constantes du jeux
    final int TAILLE_DECK = 20;
    String [] placementQuestion= new String[]{"QUESTIONS","CHOIX DE LA DIFFICULTÉ","(1) Facile","(2) Normal","(3) Difficile"};
    
    //Constructeur de la classe joueur
    Joueur newJoueur(String pseudo, String classe){
        Joueur j = new Joueur();
        j.pseudo=pseudo;
        j.classe=classe;
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
    Joueur formulaireCréationJoueur(int numJoueur){
        String pseudo; String classe; int choix;
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
            choix=readInt();
            if(choix!=1 || choix!=2 || choix!=3 || choix!=4 ){
                cursor(25, 15);
                print("Erreur, entrer un chiffre entre 1 et 4");
            }
        }while(choix<1 || choix>4);
        if(choix==1) classe="CE1";
        else if(choix==2) classe="CE2";
        else if(choix==3) classe="CM1";
        else classe="CM2";

        return newJoueur(pseudo,classe);
    }
    // Fonction qui gère la partie
    void partieEnCours(CSVFile fichierSauvegarde, String typeDePartie){
        
        int numTour = (int) charAt(getCell(fichierSauvegarde, 1, 6), 0)-48; // Cherche le nombre de tours actuel
        int joueurQuiCommence = -1;
        String[] historiqueJeux = {"", "", "", ""};// Stocke l'historique du jeux
        int[] deckJoueur1 = new int[TAILLE_DECK];  // Déclaration des decks avec kes id des cartes
        int[] deckJoueur2 = new int[TAILLE_DECK];
        int nbrCartesDeckJoueur1 = 0;              // Stocke le nombre de cartes dans le deck de chaque joueur
        int nbrCartesDeckJoueur2 = 0;
        Carte[] mainJoueur1 = new Carte[6];      // Max de 6 cartes dans la main du joueur
        Carte[] mainJoueur2 = new Carte[6];
        String pseudoJoueur="";

        //#region Mise en place de l'affichage du début de partie
        File BorduresJeux = newFile("Texts/BorduresJeux.txt");
        File logoQCM = newFile("Texts/EncadréQuestion.txt");
        String logoQCMString=TXTtoString(logoQCM);
        cursor(0, 0);
        afficherFichierTxt(BorduresJeux); // Affichage des bordures du jeux
        cursor(2,0);
        print(logoQCMString);
        cursor(2, 105);
        print("Historique :");            // Affichage de l'historique du jeux

        if(equals(typeDePartie,"1vs1")){            ////GESTION DU MODE 1V1
            Joueur j1=formulaireCréationJoueur(1);
            viderPlateau();
            Joueur j2=formulaireCréationJoueur(2);
            viderPlateau();
            cursor(9, 105);
            print("Tour n°" + numTour);
            joueurQuiCommence = tirageAleatoire();            // Détermine aléatoirement qui commencera la partie
            afficherPiece(joueurQuiCommence);                 // On affiche le côté de la pièce du gagnant (Face : joueur1 ; Pile : joueur2)
            delay(1500);                                      // pendant 1.5 secondes
            if(joueurQuiCommence==1){
                pseudoJoueur=j1.pseudo;
            }else{
                pseudoJoueur=j2.pseudo;
            }
            afficherHistoriquePartie(historiqueJeux, "C'est le joueur " + pseudoJoueur + " qui commence !"); // Met à jour l'historique de la partie
            afficherHistoriquePartie(historiqueJeux, "Chaque joueur dispose d'un deck sélectionné aléatoirement.");
            afficherHistoriquePartie(historiqueJeux, "Chaque joueur pioche 3 cartes.");
            deckJoueur1 = declarationDeck(fichierSauvegarde, 1); // Initialisation du deck du joueur 1
            nbrCartesDeckJoueur1 = 20;
            deckJoueur2 = declarationDeck(fichierSauvegarde, 2); // Initialisation du deck du joueur 2
            nbrCartesDeckJoueur2 = 20;
            for (int carteEnMain=0; carteEnMain<3; carteEnMain++){
                ajoutCarteDansMain(mainJoueur1, deckJoueur1, nbrCartesDeckJoueur1, historiqueJeux);  // Ajout de 3 cartes aléatoire dans la main de chaque joueur
                ajoutCarteDansMain(mainJoueur2, deckJoueur2, nbrCartesDeckJoueur2, historiqueJeux);
            }
            nbrCartesDeckJoueur1 = 17;
            nbrCartesDeckJoueur2 = 17;
            if (joueurQuiCommence == 1) afficherMain(mainJoueur1); // Affichage de la main du joueur qui commence
            else afficherMain(mainJoueur2);
            afficherHistoriquePartie(historiqueJeux, "C'est au joueur n°" + joueurQuiCommence + " de placer ses cartes.");
            delay(10000);
        
        }else{
            cursor(28,31);
            println("pas encore fait...1 jour peut-etre");
            delay(5000);
        }         
    }

    void placerCarte(Carte[] mainJoueur, Carte[] actif){

        boolean saisie = false;

        do{

        }while(saisie);
    }

    void afficherMain(Carte[] mainJoueur){

        boolean finMain = false; // Vrai si toutes les cartes ont été affichées
        int indiceCarte = 0, lig = 11, col = 103, lig2 = 0, col2 = 0;

        do{
            if (indiceCarte == 6) return; // Si les 6 cartes ont étés affichées, on quitte la fonction
            if (mainJoueur[indiceCarte] == null) finMain = true; // Si il n'y a plus de carte à afficher, on quitte la fonction
            else {
                afficherDetailsCarte(mainJoueur[indiceCarte], lig+lig2, col+col2);
                if (col2 == 0) col2 = 28; // Rajoiute un espacement de 14 caractère pour afficher la prochaine carte
                else {                    // Sinon, on rajoute l'espacement sur les colonnes
                    col2 = 0;
                    lig2 += 14;
                }
            }
            indiceCarte++;
        }while(!finMain);
    }

    // Rajoute une carte aléatoire (provenant du deck du joueur) dans la main du joueur
    void ajoutCarteDansMain(Carte[] mainJoueur, int[] deckJoueur, int nbrCartesDeckJoueur1, String[] historique){

        if (mainJoueur[length(mainJoueur)-1] == null){ // Si il y a de la place dans la main du joueur on ajoute une carte du deck à sa main.
            for (int i=0; i<length(mainJoueur); i++){
                if (mainJoueur[i] == null) {
                    int cartePiocheeAleatoire = (int) (random()*nbrCartesDeckJoueur1 + 1); // On prend l'indice d'une des cartes au hasard dans le deck
                    int numCarte = deckJoueur[cartePiocheeAleatoire];
                    mainJoueur[i] = definitCarte(numCarte);// On ajoute la carte dans la main du joueur
                    deckJoueur[cartePiocheeAleatoire] = 0; // On enlève la carte du deck
                    triDeck(deckJoueur);                   // Et on tri le deck (carte vide placée à la fin)
                    return;
                }
            }
        } // Sinon, on indique qu'il n'y a pas de place dans sa main.
        else afficherHistoriquePartie(historique, "Vous avez atteint le nombre de cartes maximale dans votre main.");
    }

    // Tri le deck du joueur (carte déja utilisée placée au fond du deck ayant comme valeur 0)
    void triDeck(int[] deckJoueur){

        boolean carteVideTrouvee = false;

        for (int i=0; i<length(deckJoueur)-1; i++){     // On parcours le deck du joueur
            if(deckJoueur[i] == 0 || carteVideTrouvee){ // Dès que l'on trouve la carte jouée,
                deckJoueur[i] = deckJoueur[i+1];        // On décale toutes les cartes suivantes d'un indice
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
        if (equals(nouvelleCarte.nom, "Monstre")){ // Si c'est un monstre, on lui rajoute ces caractéristiques.
            nouvelleCarte.PV = stringToInt(getCell(listeCartes, indiceCarte, 3));      // Colonne PV
            nouvelleCarte.description = getCell(listeCartes, indiceCarte, 4);          // Colonne DescriptionAttaque
            nouvelleCarte.attaque = stringToInt(getCell(listeCartes, indiceCarte, 5)); // Colonne Attaque
            nouvelleCarte.retraite = stringToInt(getCell(listeCartes, indiceCarte, 6));// Colonne Retraite
        }
        return nouvelleCarte; // Retourne la carte contenant toute ces informations
    }

    // Renvoie le deck du joueur
    int[] declarationDeck(CSVFile fichierSauvegarde, int numJoueur){

        String typeCarte = getCell(fichierSauvegarde, 1, 2);
        int[] deckJoueur = new int[TAILLE_DECK];            // Déclaration d'un deck vide

        if (length(typeCarte) != 0){                        // Si il y a un deck dans le fichier de sauvegarde, alors on sélectionne ces cartes là
            
            // A faire
        }
        else{                                               // Sinon, le deck et vide et on rajoute les cartes normalement
            int choixDeck = 8*(numJoueur-1);                // Sélectionne le deck 1 pour le joueur 1 et le deck 2 pour le joueur 2 (test)
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
        int nbrAleatoire = (int) (random()*2 + 1);
        return nbrAleatoire; // Renvoie un numéro entre 1 ou 2
    }

// #region ----------------- Fonctions d'affichage ----------------------------------
    void viderPlateau(){
        for (int i=12;i<45;i++){
            cursor(i,2);
            println("                                                                                      ");
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
        print("---------------------------");// Fin de l'affichage de la carte
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
            print(historique[i]);
            for (int j=length(historique[i]); j<68; j++) print(" ");
        }
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
    // #endregion

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
    String TXTtoString(File Fichier){
        String affichage="";
        while(ready(Fichier)){
            affichage= affichage+(readLine(Fichier))+"\n";
        }
        return affichage;
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
                    partieEnCours(partieSélectionnée,"1vsBOT");// On lance la partie
                    break;

                case "2": // Choix d'une partie 1v1 
                    partieSélectionnée = loadCSV("fichiersCSV/partieVide.csv");
                    typeDePartie="1vs1";
                    partieEnCours(partieSélectionnée,"1vs1");// On lance la partie;
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
