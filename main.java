import extensions.CSVFile;
import extensions.File;

class main extends Program {

    // déroule le jeux
    void déroulementJeux(){
        
        File BorduresJeux = newFile("Texts/BorduresJeux.txt");
        cursor(0, 0);
        afficherFichierTxt(BorduresJeux); // Affichage des bordures du jeux

        CSVFile partieSélectionnée = null;
        int nbrJoueur = -1;
        boolean finJeu = false; // test

        do{ // Tant que le joueur ne quitte pas le jeu, il reste dans le menu // test de la boucle while
            String choixJoueur = menuJeux();

            switch (choixJoueur){

                case "1": // Choix d'une partie solo
                    partieSélectionnée = loadCSV("fichiersCSV/partieVide.csv");
                    nbrJoueur = 1;
                    break;

                case "2": // Choix d'une partie de deux joueurs
                    partieSélectionnée = loadCSV("fichiersCSV/partieVide.csv");
                    nbrJoueur = 2;
                    break;
                case "8": // Choix de fin du jeu
                    finJeu = true;
                    break;
                default:
                    print("\n| Erreur");
            }
            if (equals(choixJoueur,"1") || equals(choixJoueur,"2")) partieEnCours(partieSélectionnée); // On lance la partie
        }while(!finJeu);
        
        println("\n| Merci d'avoir joué !");
    }

    // Fonction qui gère le menu du jeux
    String menuJeux(){

	    File TxtBattleQuiz = newFile("Texts/BattleQuizDeluxe.txt"); //Stockage du contenu des fichiers texte dans une variable
        File TxtAccueil = newFile("Texts/accueil.txt");
        File TxtBarre = newFile("Texts/barre.txt");

        cursor(0, 0);
        afficherFichierTxt(TxtBattleQuiz); // Affichage du menu du jeux
        afficherFichierTxt(TxtAccueil);
        afficherFichierTxt(TxtBarre);

        String choixUtilisateur = "";

        do{ // L'utilisateur choisi un numéro entre 1 et 8 (inclus)
            cursor(20, 2);
            print("Entrez un numéro entre 1 et 8 pour valider :");
            choixUtilisateur = readString();
            if(charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0) > '8' || length(choixUtilisateur) == 0 || length(choixUtilisateur) > 1) { 
                cursor(20, 45);
                print("                                                                                                                               ");
                cursor(21, 2);
                print("Numéro incorrect, veuillez réessayer.");
            }
        } while(charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0) > '8' || length(choixUtilisateur) == 0 || length(choixUtilisateur) > 1);
        return choixUtilisateur;
    }

    // Fonction qui gère la partie
    void partieEnCours(CSVFile fichierSauvegarde){
        
        int numTour = (int) charAt(getCell(fichierSauvegarde, 1, 6), 0)-48; // Cherche le nombre de tours actuel
        int joueurQuiCommence = -1;
        
        if(numTour == 0){                                     // Si on est au tour 0, c'est une nouvelle partie
            joueurQuiCommence = tirageAleatoire();            // Détermine aléatoirement qui commencera la partie
            println(joueurQuiCommence);
            afficherPiece(joueurQuiCommence);                 // On affiche le côté de la pièce du gagnant (Face : joueur1 ; Pile : joueur2)

        }
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

    // Fonction qui gère l'affichage de la pièce (Pile ou Face)
    void afficherPiece(int tirage){

        File cotéGagnant = null;
        File piecePlat0 = newFile("Texts/Pieces/pieceEuroPlat0.txt");
        File piecePlat1 = newFile("Texts/Pieces/pieceEuroPlat1.txt");
        File piecePlat2 = newFile("Texts/Pieces/pieceEuroPlat2.txt");
        
        cursor(19, 0);
        afficherFichierTxt(piecePlat1);
        pause(500);
        cursor(19, 0);
        afficherFichierTxt(piecePlat0);
        pause(500);
        cursor(19, 0);
        afficherFichierTxt(piecePlat2);
        pause(500);
        cursor(19, 0);

        if(tirage == 1){ // Si on a tiré face
            cotéGagnant = newFile("Texts/Pieces/pieceFace.txt");
        }
        else{            // Si on a tiré pile
            cotéGagnant = newFile("Texts/Pieces/piecePile.txt");
        }
        afficherFichierTxt(cotéGagnant);
    }

    void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    

    // #region ----------------- Fonctions d'affichage ----------------------------------

    // Fonction permettant d'afficher toutes les données d'une carte sous un format structuré avec l'ASCII
    void afficherDetailsCarte(CSVFile fichierCSV, int indiceCarte) {

        // Récupère les différentes informations de la carte
        String typeCarte = getCell(fichierCSV, indiceCarte, 1);           // Colonne Type
        String nomCarte = getCell(fichierCSV, indiceCarte, 2);            // Colonne Nom
        String pvCarte = getCell(fichierCSV, indiceCarte, 3);             // Colonne PV
        String descriptionAttaque = getCell(fichierCSV, indiceCarte, 4);  // Colonne DescriptionAttaque
        String attaqueCarte = getCell(fichierCSV, indiceCarte, 5);        // Colonne Attaque
        String retraiteCarte = getCell(fichierCSV, indiceCarte, 6);       // Colonne Retraite
        String descriptionCarte = getCell(fichierCSV, indiceCarte, 7);    // Colonne Description
        String asciiCarte = imageASCIICarte(fichierCSV, indiceCarte);

        String carteAffichee = "";

        carteAffichee += "___________________________\n";
        carteAffichee += "|  " + nomCarte + "          ";                 // Ajoute le nom
        if(equals(typeCarte, "Monstre")) carteAffichee += pvCarte + "PV"; // Ajoute les PV
        carteAffichee +="|\n";
        
        String[] asciiLignes = asciiCarte.split("\n");              // A chaque passage à la ligne (\n), on passe a l'indice suivant du tableau

        int hauteurAscii = length(asciiLignes);
        
        for (int i = 0; i < 5 - hauteurAscii; i++) {                      // Ajoute de l'espacement vide si nécessaire (si le dessin est trop petit)
            carteAffichee+="|                         |\n";
        }
        
        for (int i = 0; i < asciiLignes.length; i++) {                    // Ajoute le dessin ASCII
            while (length(asciiLignes[i]) < 20) asciiLignes[i] += " "; // Ajoute des espaces à droite du dessin, puis rajoute le "|" de fin de ligne
            carteAffichee += "|     " + asciiLignes[i] + "|\n";
        }

        carteAffichee += "|_________________________|\n";
        carteAffichee += "|                         |\n";

        if(equals(typeCarte,"Monstre")){
            carteAffichee += "|  - " + descriptionAttaque + " : " + attaqueCarte + "  |\n";
            carteAffichee += "|                         |\n";
            carteAffichee += "| Points de retraite : " + retraiteCarte + "  |\n"; // Ajoute les points de retraite
        }
        else{
            carteAffichee += "|  - " + descriptionCarte +"  |\n";
        }
        carteAffichee += "---------------------------\n";

        print(carteAffichee); // Affichage de la carte
    }

    // Fonction pour afficher le contenu d'un fichier txt
    void afficherFichierTxt(File texte){
        while(ready(texte)){            // On s'arrête dès qu'on lit une ligne null (fin du fichier)
	        println(readLine(texte));   // Affichage du contenu de la ligne suivante
	    }	
    }

    // Fonction pour afficher toutes les cartes
    void afficherPokedex(CSVFile fichierCSV){
        int nbLignes = rowCount(fichierCSV); // Compte le nombre de lignes dans le fichier
        for(int i=0; i<nbLignes-1; i++){
            afficherDetailsCarte(fichierCSV, i+1);
        }
    }
    // #endregion

    void algorithm() {

        CSVFile fichierCSV = loadCSV("fichiersCSV/cartes.csv"); // Charger le fichier CSV contenant les informations des cartes

        int nbLignes = rowCount(fichierCSV); // Compte le nombre de lignes dans le fichier

        déroulementJeux();

        //print(dessinCouleur(electhor));
        //afficherDetailsCarte(fichierCSV, 1);

        //afficherPokedex(fichierCSV);
        
    }
}
