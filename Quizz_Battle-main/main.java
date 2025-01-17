import extensions.CSVFile;
import extensions.File;

class main extends Program {
//#region-----------------------------------------------DECLARATION DES CONSTANTES----------------------------------------------------------//
    final int TAILLE_DECK = 20;
    final int TAILLE_MAIN= 6;
    final int[][] indiceCarteJ1=new int[][]{{13,38},{11,5},{11,70}};
    final int[][] indiceCarteJ2=new int[][]{{28,38},{31,5},{31,70}};
    final String [] placementQuestion= new String[]{"QUESTIONS","CHOIX DE LA DIFFICULTÉ","(1) Facile","(2) Normal","(3) Difficile"};
    final double PROBA_RETRAITE_BOT=0.8;
    final double PROBA_ATTAQUE_BOT=0.55;
//#endregion

//#region-------------------------------------------CONSTRUCTEUR ET CONTROLES DE SAISIE-----------------------------------------------------//
    // Constructeur de classe Joueur
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
    // Constructeur de class Carte
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
    int premierChoixJoueur(int max, String[] historique){
        String choixUtilisateur = "";
        boolean saisieValide = false;
        String x=""+max;
        do{
            cursor(44,105);
            choixUtilisateur = readString();
            if (length(choixUtilisateur) == 0);
            else if(charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0) > charAt(x,0) || length(choixUtilisateur) > 1) { 
                afficherHistoriquePartie(historique,"Numéro incorrect, veuillez réessayer.");
            }
            else saisieValide = true;
        } while(saisieValide == false);
        return stringToInt(choixUtilisateur);
    }
    int choixJoueurMain(int max, String[] historique){
        String choixUtilisateur = "";
        boolean saisieValide = false;
        String x=""+max;
        do{
            cursor(44,105);
            choixUtilisateur = readString();
            if (length(choixUtilisateur) == 0);
            else if(((charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0) > charAt(x,0) )&& charAt(choixUtilisateur, 0)!='7') || length(choixUtilisateur) > 1) { 
                afficherHistoriquePartie(historique,"Numéro incorrect, veuillez réessayer.");
            }
            else saisieValide = true;
        } while(saisieValide == false);
        return stringToInt(choixUtilisateur);
    }
    int choixJoueurPartie(String[] historique){
        String choixUtilisateur = "";
        boolean saisieValide = false;
        do{
            cursor(44,105);
            choixUtilisateur = readString();
            if (length(choixUtilisateur) == 0);
            else if(charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0)> '6' || length(choixUtilisateur) > 1) { 
                afficherHistoriquePartie(historique,"Numéro incorrect, veuillez réessayer.");
            }
            else saisieValide = true;
        } while(saisieValide == false);
        return stringToInt(choixUtilisateur);
    }
    int choixSwitch(String[] historique){
        String choixUtilisateur = "";
        boolean saisieValide = false;
        do{
            cursor(44,105);
            choixUtilisateur = readString();
            if (length(choixUtilisateur) == 0);
            else if((charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0)> '2')&&(charAt(choixUtilisateur, 0)!='7') || length(choixUtilisateur) > 1) { 
                afficherHistoriquePartie(historique,"Numéro incorrect, veuillez réessayer.");
            }
            else saisieValide = true;
        } while(saisieValide == false);
        return stringToInt(choixUtilisateur);
    }
//#endregion

//#region-----------------------------------------FONCTIONS D'INTERACTION AVEC LES CARTES---------------------------------------------------//
    boolean echangerCarte(Carte[][] plateauDeCartes, Joueur joueurQuiJoue, String[] historiqueJeux,boolean cestJ1,String carteVide){
        String menuChoixEchange=TXTtoString(newFile("Texts/InteractionEchange.txt"));
        afficherHistoriquePartie(historiqueJeux,"Vous avez choisi de battre en retraite");
        cursor(2,0);
        print(menuChoixEchange);
        int choixSwitch=choixSwitch(historiqueJeux);
        if (choixSwitch==7){
            return false;
        }else{
            if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][choixSwitch]==null){
                afficherHistoriquePartie(historiqueJeux,"Erreur, veuillez entrer une carte de votre banc !");
                echangerCarte(plateauDeCartes,joueurQuiJoue,historiqueJeux,cestJ1,carteVide);
            }else{ 
            int choixDifficulté=plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0].retraite;
            chargementASCII();
            boolean attaqueAcceptée=QCMpourAttaquer(joueurQuiJoue,choixDifficulté,historiqueJeux);
            actualiserQuestions();
                if(attaqueAcceptée){
                    Carte tampon=plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0];
                    plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0]=plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][choixSwitch];
                    plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][choixSwitch]=tampon;
                    if(cestJ1){
                        afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0],indiceCarteJ1[0][0],indiceCarteJ1[0][1]);
                        if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][choixSwitch]==null){
                            afficherCarteVide(carteVide,indiceCarteJ1[choixSwitch][0],indiceCarteJ1[choixSwitch][1]);
                        }else{
                            afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][choixSwitch],indiceCarteJ1[choixSwitch][0],indiceCarteJ1[choixSwitch][1]);
                        }
                        afficherHistoriquePartie(historiqueJeux,"La retraite du monstre est un succès");
                        return false;
                    }else{
                        afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0],indiceCarteJ2[0][0],indiceCarteJ2[0][1]);
                        if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][choixSwitch]==null){
                            afficherCarteVide(carteVide,indiceCarteJ2[choixSwitch][0],indiceCarteJ2[choixSwitch][1]);
                        }else{
                            afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][choixSwitch],indiceCarteJ2[choixSwitch][0],indiceCarteJ2[choixSwitch][1]);
                        }
                        afficherHistoriquePartie(historiqueJeux,"La retraite du monstre est un succès");
                        return false;
                    }
                }else{
                    afficherHistoriquePartie(historiqueJeux,"Vous avez échoué, vous passez votre tour");
                    return true;
                }
            }
        }
        return false;
    }
    boolean retraiteBOT(Carte[][] plateauDeCartes, Joueur joueurQuiJoue, String[] historiqueJeux,boolean cestJ1,String carteVide){
        afficherHistoriquePartie(historiqueJeux,"Le BOT tente une retraite");
        afficherHistoriquePartie(historiqueJeux,"Veuillez patienter un instant...");
        if(random()<PROBA_RETRAITE_BOT){
            if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1]!=null){
                Carte tampon=plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0];
                plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0]=plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1];
                plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1]=tampon;
                if(cestJ1){
                    afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0],indiceCarteJ1[0][0],indiceCarteJ1[0][1]);
                    if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1]==null){
                        afficherCarteVide(carteVide,indiceCarteJ1[1][0],indiceCarteJ1[1][1]);
                    }else{
                        afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1],indiceCarteJ1[1][0],indiceCarteJ1[1][1]);
                    }
                    afficherHistoriquePartie(historiqueJeux,"La retraite du BOT est un succès");
                    return false;
                }else{
                    afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0],indiceCarteJ2[0][0],indiceCarteJ2[0][1]);
                    if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1]==null){
                        afficherCarteVide(carteVide,indiceCarteJ2[1][0],indiceCarteJ2[1][1]);
                    }else{
                        afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1],indiceCarteJ2[1][0],indiceCarteJ2[1][1]);
                    }
                    afficherHistoriquePartie(historiqueJeux,"La retraite du BOT est un succès");
                    return false;
                }
                
            }else if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2]!=null){
                Carte tampon=plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0];
                plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0]=plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2];
                plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2]=tampon;
                if(cestJ1){
                    afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0],indiceCarteJ1[0][0],indiceCarteJ1[0][1]);
                        if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2]==null){
                            afficherCarteVide(carteVide,indiceCarteJ1[2][0],indiceCarteJ1[2][1]);
                        }else{
                            afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2],indiceCarteJ1[2][0],indiceCarteJ1[2][1]);
                        }
                        afficherHistoriquePartie(historiqueJeux,"La retraite du BOT est un succès");
                        return false;
                }else{
                    afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0],indiceCarteJ2[0][0],indiceCarteJ2[0][1]);
                        if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2]==null){
                            afficherCarteVide(carteVide,indiceCarteJ2[2][0],indiceCarteJ2[2][1]);
                        }else{
                            afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2],indiceCarteJ2[2][0],indiceCarteJ2[2][1]);
                        }
                    afficherHistoriquePartie(historiqueJeux,"La retraite du BOT est un succès");
                        return false;
                }
            }
        }else{
            afficherHistoriquePartie(historiqueJeux,"La retraite du BOT a échoué");
                return true;
        }
        return false;
    }
    void attaqueMonstre(String[] historiqueJeux,Carte[][] plateauDeCartes,Joueur joueurQuiJoue, boolean cestJ1, String carteVide){
        afficherHistoriquePartie(historiqueJeux,"Vous avez choisi d'attaquer");
        delay(500);
        chargementASCII();
        actualiserQuestions();
        cadreDifficulté(placementQuestion);
        afficherHistoriquePartie(historiqueJeux,"La difficulté choisi impactera les dégats causés à l'adversaire");
        afficherHistoriquePartie(historiqueJeux,"Facile: -10 d'attaque, Normal ne change rien, Difficile: +10 d'attaque");
        afficherHistoriquePartie(historiqueJeux,"Si vous perdez une question difficile, votre monstre s'inflige 10 de dégats");
        int choixDifficulté=choixDifficulté(historiqueJeux);
        boolean attaqueAcceptée=QCMpourAttaquer(joueurQuiJoue,choixDifficulté,historiqueJeux);
        actualiserQuestions();
        if(attaqueAcceptée){
            if(cestJ1){
                int ptsAttaque=plateauDeCartes[0][0].attaque;
                if (choixDifficulté==1){
                    ptsAttaque-=10;
                    afficherHistoriquePartie(historiqueJeux,"Votre choix : Facile, -10 d'attaque");
                }
                if (choixDifficulté==3){
                    ptsAttaque+=10;
                    afficherHistoriquePartie(historiqueJeux,"Votre choix : Difficile, +10 d'attaque");
                }
                plateauDeCartes[1][0].PV-=ptsAttaque;
                afficherDetailsCarte(plateauDeCartes[1][0],indiceCarteJ2[0][0],indiceCarteJ2[0][1]);
                if(plateauDeCartes[1][0].PV<=0){
                    afficherCarteVide(carteVide,indiceCarteJ2[0][0],indiceCarteJ2[0][1]);
                    joueurQuiJoue.points++;
                    plateauDeCartes[1][0]=null;
                }
            }else{
                int ptsAttaque=plateauDeCartes[1][0].attaque;
                if (choixDifficulté==1){
                    ptsAttaque-=10;
                    afficherHistoriquePartie(historiqueJeux,"Votre choix : Facile, -10 d'attaque");
                }
                if (choixDifficulté==3){
                    ptsAttaque+=10;
                    afficherHistoriquePartie(historiqueJeux,"Votre choix : Difficile, +10 d'attaque");
                }
                plateauDeCartes[0][0].PV-=ptsAttaque;
                afficherDetailsCarte(plateauDeCartes[0][0],indiceCarteJ1[0][0],indiceCarteJ1[0][1]);
                if(plateauDeCartes[0][0].PV<=0){
                    afficherCarteVide(carteVide,indiceCarteJ1[0][0],indiceCarteJ1[0][1]);
                    joueurQuiJoue.points++;
                    plateauDeCartes[0][0]=null;

                }
            }
        }else if(choixDifficulté==3){
            plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0].PV-=10;
            afficherHistoriquePartie(historiqueJeux,"Votre monstre actif s'inflige 10 de dégats");
            afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0],joueurQuiJoue.coordonneesAffichageCartes[0][0],joueurQuiJoue.coordonneesAffichageCartes[0][1]);
        }
    }
    void attaqueDuBot(Carte[][]plateauDeCartes,String[]historiqueJeux,Joueur joueurQuiJoue, int[][]indiceCarteAdverse, String carteVide){
        boolean attaqueAcceptée=false;
        if(random()<PROBA_RETRAITE_BOT) attaqueAcceptée=true;
        afficherHistoriquePartie(historiqueJeux,"Le BOT vous attaque");
        int ptsAttaque=plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0].attaque;
        if(attaqueAcceptée){
            plateauDeCartes[(joueurQuiJoue.indiceLignePlateauDeCarte+1)%2][0].PV-=ptsAttaque;
            afficherDetailsCarte(plateauDeCartes[(joueurQuiJoue.indiceLignePlateauDeCarte+1)%2][0],indiceCarteAdverse[0][0],indiceCarteAdverse[0][1]);
            if(plateauDeCartes[(joueurQuiJoue.indiceLignePlateauDeCarte+1)%2][0].PV<=0){
                afficherCarteVide(carteVide,indiceCarteAdverse[0][0],indiceCarteAdverse[0][1]);
                joueurQuiJoue.points++;
                plateauDeCartes[(joueurQuiJoue.indiceLignePlateauDeCarte+1)%2][0]=null;
            }
            afficherHistoriquePartie(historiqueJeux,"L'attaque du BOT a réussi");
        }else{
            afficherHistoriquePartie(historiqueJeux,"L'attaque du BOT a échoué");
        }
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
    // Renvoie le deck du joueur
    int[] declarationDeck(CSVFile fichierSauvegarde, int numJoueur){
        String typeCarte = getCell(fichierSauvegarde, numJoueur, 2);
        int[] deckJoueur = new int[TAILLE_DECK];            // Déclaration d'un deck vide
        if (length(typeCarte) != 0){                        // Si il y a un deck dans le fichier de sauvegarde, alors on sélectionne ces cartes là 
            int index = 0;
            int nombreActuel = 0;
            for(int i=0; i<length(typeCarte); i++){
                char c = charAt(typeCarte, i);
                if (c == '/') {                             // Si il y a un '/', on termine le nombre actuel
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
    int compteurCarteMain(Carte[]main){
        int cpt=0;
        for(int i=0;i<TAILLE_MAIN;i++){
           if(main[i]!=null){
            cpt++;
            }
        }
        return cpt;
    }
    void placerCarteAlgo(Carte[][] plateauDeCartes,Joueur joueurQuiJoue,int carteChoisi, int posteCarte,String[] historiqueJeux){
        if(equals(joueurQuiJoue.main[carteChoisi-1].type,"Monstre")){
            plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][posteCarte]=joueurQuiJoue.main[carteChoisi-1];
            afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][posteCarte],joueurQuiJoue.coordonneesAffichageCartes[posteCarte][0],joueurQuiJoue.coordonneesAffichageCartes[posteCarte][1]);
            joueurQuiJoue.main[carteChoisi-1]=null;
            joueurQuiJoue.main=resetMain(joueurQuiJoue.main);
            viderMain();
            afficherMain(joueurQuiJoue.main); 
        }else{
            afficherHistoriquePartie(historiqueJeux,"Vous ne pouvez pas placer d'autre carte que Monstre");
            int choix=premierChoixJoueur(compteurCarteMain(joueurQuiJoue.main),historiqueJeux);
            placerCarteAlgo(plateauDeCartes,joueurQuiJoue,choix,posteCarte,historiqueJeux);
        }
    }
    void placerCarte(Carte[][] plateauDeCartes,Joueur joueurQuiJoue,String[]historiqueJeux){
        final Carte carteRef=definitCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0].idCarte);
        String affichageSoinChoix=TXTtoString(newFile("Texts/MenuCarteSoin.txt"));
        int carteChoisi=choixJoueurMain(compteurCarteMain(joueurQuiJoue.main),historiqueJeux);
        if(carteChoisi==7){   
            return;
        }else if(equals(joueurQuiJoue.main[carteChoisi-1].type,"Monstre")){
            if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1]==null){
                placerCarteAlgo(plateauDeCartes,joueurQuiJoue,carteChoisi,1,historiqueJeux);
                if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2]==null){
                    placerCarte(plateauDeCartes,joueurQuiJoue,historiqueJeux);
                }
            }else if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2]==null){
                placerCarteAlgo(plateauDeCartes,joueurQuiJoue,carteChoisi,2,historiqueJeux);
                if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1]==null){
                    placerCarte(plateauDeCartes,joueurQuiJoue,historiqueJeux);
                }
            }else{
                afficherHistoriquePartie(historiqueJeux,"Vous n'avez pas d'emplacement libre");
                return;
            }
        }else if(equals(joueurQuiJoue.main[carteChoisi-1].nom,"SuperBall")){
            afficherHistoriquePartie(historiqueJeux,"Vous libérez un monstre de sa SuperBall");
            joueurQuiJoue.nbCarteRestanteDeck-=addCartetoMain(1,joueurQuiJoue.main,joueurQuiJoue.deck,joueurQuiJoue.nbCarteRestanteDeck,historiqueJeux);
            joueurQuiJoue.main[carteChoisi-1]=null;
            joueurQuiJoue.main=resetMain(joueurQuiJoue.main);
            viderMain();
            afficherMain(joueurQuiJoue.main);
        }else if(equals(joueurQuiJoue.main[carteChoisi-1].nom,"Monsieur Baste")){
            afficherHistoriquePartie(historiqueJeux,"Monsieur Baste vous donne 2 cartes");
            joueurQuiJoue.nbCarteRestanteDeck-=addCartetoMain(2,joueurQuiJoue.main,joueurQuiJoue.deck,joueurQuiJoue.nbCarteRestanteDeck,historiqueJeux);
            joueurQuiJoue.main[carteChoisi-1]=null;
            joueurQuiJoue.main=resetMain(joueurQuiJoue.main);
            viderMain();
            afficherMain(joueurQuiJoue.main); 
        }else{
            cursor(2,0);
            print(affichageSoinChoix);
            int posteCarteChoisi=choixDifficulté(historiqueJeux);
            if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][posteCarteChoisi-1].PV<=(carteRef.PV-10)){
                plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][posteCarteChoisi-1].PV+=20;
                afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][posteCarteChoisi-1],joueurQuiJoue.coordonneesAffichageCartes[0][0],joueurQuiJoue.coordonneesAffichageCartes[0][1]);
                joueurQuiJoue.main[carteChoisi-1]=null;
                joueurQuiJoue.main=resetMain(joueurQuiJoue.main);
                viderMain();
                afficherMain(joueurQuiJoue.main); 
            }else{
                afficherHistoriquePartie(historiqueJeux,"Votre monstre actif est déjà à son maximum de PV");
            }
        }
    }
    void placerMonstreBot(Carte[][] plateauDeCartes,Joueur joueurQuiJoue,String[] historiqueJeux){
        int j=0;
        for(int i=0;i<compteurCarteMain(joueurQuiJoue.main);i++){
            if(equals(joueurQuiJoue.main[i].type,"Monstre") && plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][i]!=null);
                placerCarteAlgo(plateauDeCartes,joueurQuiJoue,i+1,j,historiqueJeux);
                j++;
        }
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
    boolean aDuSoin(Carte[] mainJoueur){
        boolean res=false;
        for(int i=0;i<compteurCarteMain(mainJoueur);i++){
            if (equals(mainJoueur[i].nom,"Sandwich du kiosk")){
                res=true;
            }
        }
        return res;
    }
    int idxCarteSoin(Carte[] mainJoueur){
        for(int i=0;i<compteurCarteMain(mainJoueur);i++){
            if (equals(mainJoueur[i].nom,"Sandwich du kiosk")){
                return i;
            }
        }
        return -1;
    }
    boolean aDesCartesPiocher(Carte[] mainJoueur){
        boolean res=false;
        for(int i=0;i<compteurCarteMain(mainJoueur);i++){
            if (equals(mainJoueur[i].nom,"SuperBall")||equals(mainJoueur[i].nom,"Monsieur Baste")){
                res=true;
            }
        }
        return res;
    }
    int idxCartePioche(Carte[] mainJoueur){
        for(int i=0;i<compteurCarteMain(mainJoueur);i++){
            if (equals(mainJoueur[i].nom,"SuperBall")||equals(mainJoueur[i].nom,"Monsieur Baste")){
                return i;
            }
        }
        return -1;
    }
//#endregion

//#region---------------------------------------------------FONCTION D'AFFICHAGE------------------------------------------------------------//
    void afficherCarteVide(String carteVide,int idxL, int idxC){
        String tmp="";
        int j=0;
        for(int i=0; i<length(carteVide);i++){
            if(charAt(carteVide,i)!='\n'){
                tmp=tmp+charAt(carteVide,i);
            }else{
                cursor(idxL+j,idxC);
                print(tmp);
                tmp="";
                j++;
            }  
        }
    }
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
    void viderMenuPartie(){
        for (int i=2;i<10;i++){
            cursor(i,53);
            println("                           ");
        }         
    }
    void afficherMain(Carte[] mainJoueur){
        viderMain();
        int cpt=0;
        int j=0;
        if (cpt == TAILLE_MAIN) return;
        int indiceColonne;
        int indiceLigne=11;
        int maxParLigne=3;
        for(int i=0;i<TAILLE_MAIN;i++){
            if (mainJoueur[i] == null) {
                return;
            }else{
                if (i>=maxParLigne){
                    indiceLigne=25;
                    indiceColonne=103+28*j;
                    j++;
                }else{
                    indiceColonne=103+28*i;
                }
                afficherDetailsCarte(mainJoueur[i],indiceLigne,indiceColonne);
                cpt++;
            } 
        }
    }    
    // Fonction permettant d'afficher toutes les données d'une carte sous un format structuré en ASCII
    void afficherDetailsCarte(Carte infosCarte, int lig, int col) {
        String PV_Carte = Integer.toString(infosCarte.PV); // Fonction int to String A faire
        String attaque_Carte = Integer.toString(infosCarte.attaque);
        String retraite_Carte = Integer.toString(infosCarte.retraite);
        if(equals(retraite_Carte,"1")){
            retraite_Carte="Facile";
        }else if(equals(retraite_Carte,"2")){
            retraite_Carte="Normal";
        }else{
            retraite_Carte="Difficile";
        }

        int ligIndice = 1;
        cursor(lig, col);
        print("___________________________"); 
        cursor(lig+ligIndice,col);                
        print("|  " + infosCarte.nom);        // Ajoute le nom

        for (int i = length(infosCarte.nom); i<18; i++) print(" ");
        if(equals(infosCarte.type, "Monstre")){                                 // Ajout des PVs pour les monstres
            if (length(PV_Carte) < 3) print(" ");
            if(infosCarte.PV<=20){
                print(ANSI_RED);
                print(PV_Carte + "PV");
                print(ANSI_RESET);
            }else{
                print(PV_Carte + "PV");
            }
            
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
            print("| Retraite : " + retraite_Carte ); // Ajoute les points de retraite
            cursor(lig+ligIndice,col+26);
            print("|");
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
            for (int j=length(historique[i]); j<75; j++) print(" ");
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
//#endregion

//#region ---------------------------------------FONCTION SERVANT AU FONCTIONNEMENT DU QCM--------------------------------------------------//
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

    int choixDifficulté(String[]historique){
        String choixUtilisateur = "";
        boolean saisieValide = false;
        do{
            cursor(44,105);
            choixUtilisateur = readString();
            if (length(choixUtilisateur) == 0);
            else if(charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0)> '3' || length(choixUtilisateur) > 1) { 
                afficherHistoriquePartie(historique,"Numéro incorrect, veuillez réessayer.");
            }
            else saisieValide = true;
        } while(saisieValide == false);
        return stringToInt(choixUtilisateur);
    }
    
    int controleChoixRéponses(String[]historique){
        String choixUtilisateur = "";
        boolean saisieValide = false;
        do{
            cursor(44,105);
            choixUtilisateur = readString();
            if (length(choixUtilisateur) == 0);
            else if(charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0)> '4' || length(choixUtilisateur) > 1) { 
                afficherHistoriquePartie(historique,"Numéro incorrect, veuillez réessayer.");
            }
            else saisieValide = true;
        } while(saisieValide == false);
        return stringToInt(choixUtilisateur);
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
/*Les 3 fonctions ci-dessous viennent créer les cadres de l'affichage des questions*/
    
    void cadreAffichageQuestions(CSVFile acharger, int aleaLigne){
        String[] tab=new String[5];
        tab[0]=getCell(acharger,aleaLigne,0);
        tab[1]=getCell(acharger,aleaLigne,1);
        tab[2]=getCell(acharger,aleaLigne,2);
        tab[3]=getCell(acharger,aleaLigne,3);
        tab[4]=getCell(acharger,aleaLigne,4);
        cursor(2,25);
        println("|");
        cursor(2,78);
        println("|");
        cursor(3,25);
        println("|");
        cursor(3,78);
        println("|");
        cursor(4,25);
        println("| "+tab[0]);
        cursor(4,78);
        println("|");
        cursor(5,25);
        println("|----------------------------------------------------|");
        cursor(6,25);
        println("|     (1)"+tab[1]+"  (2)"+tab[2]);
        cursor(6,78);
        println("|");
        cursor(7,25);
        println("|     (3)"+tab[3]+"  (4)"+tab[4]);
        cursor(7,78);
        println("|");
        cursor(8,25);
        println("|");
        cursor(8,78);
        println("|");
        cursor(9,25);
        println("|");
        cursor(9,78);
        println("|");
        
    }
    
    void cadreDifficulté(String[] tab){
        cursor(2,25);
        println("|");
        cursor(2,78);
        println("|");
        cursor(3,25);
        println("|");
        cursor(3,78);
        println("|");
        cursor(4,25);
        println("|  "+tab[0]+" :");
        cursor(4,78);
        println("|");
        cursor(5,25);
        println("|----------------------------------------------------|");
        cursor(6,25);
        println("|  "+tab[1]+" :");
        cursor(6,78);
        println("|");
        cursor(7,25);
        println("|  "+tab[2]+"   "+tab[3]+"   "+tab[4]);
        cursor(7,78);
        println("|");
        cursor(8,25);
        println("|");
        cursor(8,78);
        println("|");
        cursor(9,25);
        println("|");
        cursor(9,78);
        println("|");

    }
    
    void actualiserQuestions(){
        for (int i=2;i<10;i++){
            cursor(i,2);
            println("                                                                                  ");
        }         
    
    }
//Cette fonction execute toute les précédentes et indique à l'algorithme principale si l'attaque est validé par le biais d'un qcm qui s'adapte à la classe du joueur
    boolean QCMpourAttaquer(Joueur j,int choixDifficulté,String[] historique){
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
        cadreAffichageQuestions(acharger, aleaLigne);
        int entréeUtilisateur=controleChoixRéponses(historique);
        if (réussirQCM(entréeUtilisateur,aleaLigne,acharger)){
            afficherHistoriquePartie(historique,"Bonne Réponse ! Attaque Accordé");
            résultatQCM=true;
        }else{
            afficherHistoriquePartie(historique,"Mauvaise Réponse ! La réponse était"+getCell(acharger, aleaLigne,5));
        }
        return résultatQCM;
    }
//#endregion

//#region------------------------- Fonctions de sauvegarde --------------------------------------------------------------------------//
    String intToString(int nombre){
        String nombreString = "";
        while(nombre != 0){
            nombreString = (char) (nombre%10 + '0') + nombreString;
            nombre = nombre/10;
        }
        return nombreString;
    }
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
            if (i > 0){
                if (c == '/' && charAt(typeCarte, i-1) != c) {
                    joueur.main[index++] = definitCarte(nombreActuel);
                    nombreActuel = 0;
                } else {
                    nombreActuel = nombreActuel * 10 + (c - '0');
                }
            }
            else{
                if (c != '/') nombreActuel = nombreActuel * 10 + (c - '0');
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

//#region---------------------------------------------------BOUCLES DU JEU------------------------------------------------------------------//
    void choixJoueurPartie1vBOT(Carte[][] plateauDeCartes, Joueur joueurQuiJoue,String[]historiqueJeux,boolean cestJ1,String carteVide,String logoQCMString,String menuMainJoueur,Joueur j1, Joueur j2,int numTour,int joueurQuiCommence){
        int indiceCarteAdverse[][];
        boolean retraiteBot=false;
        if (cestJ1)indiceCarteAdverse=indiceCarteJ2;
        else indiceCarteAdverse=indiceCarteJ1; 
        if(!equals(joueurQuiJoue.pseudo,"BOT")){
            choixJoueurPartie1v1(plateauDeCartes,joueurQuiJoue,historiqueJeux,cestJ1,carteVide,logoQCMString,menuMainJoueur,j1,j2,numTour,joueurQuiCommence);

        }else{
            if(aDesCartesPiocher(joueurQuiJoue.main)){
                if(equals(joueurQuiJoue.main[idxCartePioche(joueurQuiJoue.main)].nom,"SuperBall")){
                    afficherHistoriquePartie(historiqueJeux,"Vous libérez un monstre de sa SuperBall");
                    joueurQuiJoue.nbCarteRestanteDeck-=addCartetoMain(1,joueurQuiJoue.main,joueurQuiJoue.deck,joueurQuiJoue.nbCarteRestanteDeck,historiqueJeux);
                    joueurQuiJoue.main[idxCartePioche(joueurQuiJoue.main)]=null;
                    joueurQuiJoue.main=resetMain(joueurQuiJoue.main);
                    viderMain();
                    afficherMain(joueurQuiJoue.main);
                }else if(equals(joueurQuiJoue.main[idxCartePioche(joueurQuiJoue.main)].nom,"Monsieur Baste")){
                    afficherHistoriquePartie(historiqueJeux,"Monsieur Baste vous donne 2 cartes");
                    joueurQuiJoue.nbCarteRestanteDeck-=addCartetoMain(2,joueurQuiJoue.main,joueurQuiJoue.deck,joueurQuiJoue.nbCarteRestanteDeck,historiqueJeux);
                    joueurQuiJoue.main[idxCartePioche(joueurQuiJoue.main)]=null;
                    joueurQuiJoue.main=resetMain(joueurQuiJoue.main);
                    viderMain();
                    afficherMain(joueurQuiJoue.main); 
                }
            }else if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0].PV>30){
                attaqueDuBot(plateauDeCartes,historiqueJeux, joueurQuiJoue, indiceCarteAdverse,carteVide);
            }else if(aDuSoin(joueurQuiJoue.main)){
                plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0].PV+=20;
                afficherDetailsCarte(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0],joueurQuiJoue.coordonneesAffichageCartes[0][0],joueurQuiJoue.coordonneesAffichageCartes[0][1]);
                joueurQuiJoue.main[idxCarteSoin(joueurQuiJoue.main)]=null;
                joueurQuiJoue.main=resetMain(joueurQuiJoue.main);
                viderMain();
                afficherMain(joueurQuiJoue.main);
                attaqueDuBot(plateauDeCartes,historiqueJeux, joueurQuiJoue, indiceCarteAdverse,carteVide);
            }else{
                retraiteBot=retraiteBOT(plateauDeCartes,joueurQuiJoue,historiqueJeux,cestJ1,carteVide);
                if(!retraiteBot){
                    attaqueDuBot(plateauDeCartes,historiqueJeux, joueurQuiJoue, indiceCarteAdverse,carteVide);
                }
            }
        }
    }
    boolean choixJoueurPartie1v1(Carte[][] plateauDeCartes, Joueur joueurQuiJoue,String[]historiqueJeux,boolean cestJ1,String carteVide,String logoQCMString,String menuMainJoueur,Joueur j1, Joueur j2,int numTour,int joueurQuiCommence){
        boolean finDePartie=false;
        boolean choixSortant=false;
        do{
            int choixJoueurPartie=choixJoueurPartie(historiqueJeux);
            switch(choixJoueurPartie){
                case 1:
                    attaqueMonstre(historiqueJeux,plateauDeCartes,joueurQuiJoue,cestJ1,carteVide);
                    choixSortant=true;
                    break;
                case 2:
                    choixSortant=echangerCarte(plateauDeCartes,joueurQuiJoue,historiqueJeux,cestJ1,carteVide);
                    cursor(2,0);
                    print(logoQCMString);
                    afficherHistoriquePartie(historiqueJeux,"Selectionnez une action à réaliser parmi celles proposées.");
                    break;
                case 3:
                    afficherHistoriquePartie(historiqueJeux,"Vous avez choisi de d'intéragir avec votre main");
                    cursor(2,0);
                    print(menuMainJoueur);
                    afficherHistoriquePartie(historiqueJeux,"Vous pouvez placer une carte banc sur le plateau");
                    placerCarte(plateauDeCartes,joueurQuiJoue,historiqueJeux);
                    cursor(2,0);
                    print(logoQCMString);
                    delay(500);
                    break;
                case 4:
                    afficherHistoriquePartie(historiqueJeux,"Vous avez choisi de passer votre tour");
                    delay(500);
                    choixSortant=true;
                    break;
                case 5:
                    afficherHistoriquePartie(historiqueJeux,"Vous avez choisi d'abandonner");
                    delay(500);
                    choixSortant=true;
                    finDePartie=true;
                    break;
                case 6:
                    afficherHistoriquePartie(historiqueJeux,"Vous avez choisi d'enregistrer et de quitter");
                    delay(500);
                    sauvegarderInfosPartie(j1, j2, joueurQuiJoue, numTour, joueurQuiCommence);
                    sauvegarderPlateau(plateauDeCartes);
                    sauvegarderInfosJoueurs(j1, j2);
                    choixSortant=true;
                    finDePartie=true;
                    break;
            }
        }while(!choixSortant);
     return finDePartie;
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

        do{ // L'utilisateur choisi un numéro entre 1 et 7 (inclus)
            cursor(20, 2);
            print("Entrez un numéro entre 1 et 7 pour valider :");
            choixUtilisateur = readString();
            if (length(choixUtilisateur) == 0);
            else if(charAt(choixUtilisateur, 0) < '1' || charAt(choixUtilisateur, 0) > '7' || length(choixUtilisateur) > 1) { 
                cursor(20, 45);
                print("                                     ");
                cursor(21, 2);
                print("Numéro incorrect, veuillez réessayer.");
            }
            else saisieValide = true;
        } while(saisieValide == false);
        return choixUtilisateur;
    }
    // Fonction qui gère la partie
    void partieEnCours(CSVFile fichierSauvegarde, String typeDePartie,boolean partieSauvegardee){
        //---------------------------INITIALISATION COMMUNE AUX 2 TYPES DE PARTIES--------------------------------------------------------//
            //#region Déclaration des variables nécessaires pour le bon fonctionnement du jeux
            int numTour = 0; // Cherche le nombre de tours actuel
            int joueurQuiCommence = -1;
            String pseudoJoueur;
            String[] historiqueJeux = {"", "", "", ""};// Stocke l'historique du jeux  
            Carte[][] plateauDeCartes=new Carte[2][3]; // permet de stocker les cartes choisies de la main dans le plateau
            boolean finDePartie=false;
            boolean tourFini=false;
            boolean cestJ1;
            //-------Tampon de mémoire pour intervertir à chaque fois les positions de placement des cartes de chaque joueur------//
            Joueur joueurQuiJoue;
            //-------Affichage générer----------//
            String BorduresJeux = TXTtoString(newFile("Texts/BorduresJeux.txt"));
            String logoQCMString=TXTtoString(newFile("Texts/EncadréQuestion.txt"));
            String menuMainJoueur=TXTtoString(newFile("Texts/InteractionMain.txt"));
            String stylePlateau=TXTtoString(newFile("Texts/Plateau.txt"));
            String carteVide=TXTtoString(newFile("Texts/carteVide.txt"));
            //#endregion
            cursor(0, 0);
            print(BorduresJeux);
            cursor(2,0);
            print(logoQCMString);
            cursor(2, 105);
            print("Historique :");
            Joueur j1=new Joueur();
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
            }else{  
                j1=formulaireCréationJoueur(1);          //Création des Joueurs avec leur classe
                viderPlateau();
                if(equals(typeDePartie,"1vs1")){
                    j2=formulaireCréationJoueur(2);
                    viderPlateau();
                }else{
                    j2=newJoueur("BOT","CM2");
                }
            }
            if (partieSauvegardee) joueurQuiCommence = stringToInt(getCell(fichierSauvegarde, 1, 5));
            else {
                joueurQuiCommence =1+(int) (random()*((2-1)+1));  // Détermine aléatoirement qui commencera la partie en retournant soit 1 soit 2
                afficherPiece(joueurQuiCommence);                 // On affiche le côté de la pièce du gagnant (Face : joueur1 ; Pile : joueur2)
                delay(1500);                                      // pendant 1.5 secondes
            }
            if(joueurQuiCommence==1){
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
            }else{
                if (!equals(getCell(fichierSauvegarde, 1, 1), "")) j1.points = stringToInt(getCell(fichierSauvegarde, 1, 1)); // Initialisation des points des joueurs
                if (!equals(getCell(fichierSauvegarde, 2, 1), "")) j2.points = stringToInt(getCell(fichierSauvegarde, 2, 1));
                j1.nbCarteRestanteDeck = stringToInt(getCell(fichierSauvegarde, 1, 6)); // Initialisation du nombre de cartes restantes dans le deck
                j2.nbCarteRestanteDeck = stringToInt(getCell(fichierSauvegarde, 2, 6));
                j1.main = new Carte[TAILLE_MAIN];
                j2.main = new Carte[TAILLE_MAIN];
                ajoutCarteSauvegardeeDansMain(j1, 1,  fichierSauvegarde);               // Initialisation de la main des joueurs
                ajoutCarteSauvegardeeDansMain(j2, 2, fichierSauvegarde);
                ajoutCarteSauvegardeeDansPlateau(plateauDeCartes);
            }

        //---------------------------BOUCLE DU JEU EN LUI-MEME--------------------------------------------------------//
            while(!finDePartie){// Lancement du gameplay, tourne tant que finDePartie est sur false.
                tourFini=false; // rénitialisation de la variable pour continuer switch en les joueurs.
                cursor(9, 150);
                print(ANSI_GREEN);
                print(""+j1.pseudo+" : "+j1.points+" | "+j2.points+" : "+j2.pseudo);
                print(ANSI_RESET);
                while(!tourFini){
                    cursor(9, 105);
                    print("Tour n°" + numTour);
                    //Switch d'un joueur à l'autre//////////
                    if(joueurQuiCommence==1){
                        joueurQuiJoue=j1;
                        joueurQuiJoue.indiceLignePlateauDeCarte=0;
                        joueurQuiJoue.coordonneesAffichageCartes=indiceCarteJ1;
                        cestJ1=true;
                        print(ANSI_YELLOW);
                        cursor(12,45);
                        print("*\\\\"+j1.pseudo+"//*");
                        print(ANSI_RESET);
                        print(ANSI_WHITE);
                        cursor(43,45);
                        print("*\\\\"+j2.pseudo+"//*");
                        print(ANSI_RESET);
                     }else{
                        joueurQuiJoue=j2;
                        joueurQuiJoue.indiceLignePlateauDeCarte=1;
                        joueurQuiJoue.coordonneesAffichageCartes=indiceCarteJ2;
                        cestJ1=false;
                        print(ANSI_WHITE);
                        cursor(12,45);
                        print("*\\\\"+j1.pseudo+"//*");
                        print(ANSI_RESET);
                        print(ANSI_YELLOW);
                        cursor(43,45);
                        print("*\\\\"+j2.pseudo+"//*");
                        print(ANSI_RESET);
                    }
                    /////////////////////////////////////////
                    afficherMain(joueurQuiJoue.main);
                    afficherHistoriquePartie(historiqueJeux, "C'est au Tour " + joueurQuiJoue.pseudo + " de jouer.");
                    if(numTour==0 || numTour==1){
                        viderMenuPartie();
                        cursor(2,0);
                        print(menuMainJoueur);
                        afficherHistoriquePartie(historiqueJeux,"Placez une carte sur le poste actif");
                        int carteChoisi;
                        if(!equals(joueurQuiJoue.pseudo,"BOT")){
                            carteChoisi=premierChoixJoueur(compteurCarteMain(joueurQuiJoue.main),historiqueJeux);
                            placerCarteAlgo(plateauDeCartes,joueurQuiJoue,carteChoisi,0,historiqueJeux);
                            afficherHistoriquePartie(historiqueJeux,"Placez au choix une ou deux cartes sur le banc");
                            placerCarte(plateauDeCartes,joueurQuiJoue,historiqueJeux);
                        }else{
                            placerMonstreBot(plateauDeCartes,joueurQuiJoue,historiqueJeux);
                        }     
                    }else{
                        if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][0]==null){
                            if(plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][1]!=null || plateauDeCartes[joueurQuiJoue.indiceLignePlateauDeCarte][2]!=null){
                                echangerCarte(plateauDeCartes,joueurQuiJoue,historiqueJeux,cestJ1,carteVide);
                            }else{
                                if(cestJ1) afficherHistoriquePartie(historiqueJeux,"Le joueur "+j2.pseudo+" qui a gagné");
                                else afficherHistoriquePartie(historiqueJeux,"Le joueur "+j1.pseudo+" qui a gagné");
                                return;
                            }
                        }
                        viderMenuPartie();
                        cursor(2,0);
                        print(logoQCMString);
                        afficherHistoriquePartie(historiqueJeux,"Vous piochez une carte pour le début de votre tour");
                        joueurQuiJoue.nbCarteRestanteDeck-=addCartetoMain(1,joueurQuiJoue.main, joueurQuiJoue.deck, joueurQuiJoue.nbCarteRestanteDeck, historiqueJeux);
                        afficherMain(joueurQuiJoue.main);
                        afficherHistoriquePartie(historiqueJeux,"Selectionnez une action à réaliser parmi celles proposées.");
                        if(equals(typeDePartie,"1vs1")){
                            finDePartie=choixJoueurPartie1v1(plateauDeCartes,joueurQuiJoue,historiqueJeux,cestJ1,carteVide,logoQCMString,menuMainJoueur,j1,j2,numTour,joueurQuiCommence);
                        }else{
                            choixJoueurPartie1vBOT(plateauDeCartes,joueurQuiJoue,historiqueJeux,cestJ1,carteVide,logoQCMString,menuMainJoueur,j1,j2,numTour,joueurQuiCommence);
                        }
                    }
                numTour++;
                joueurQuiCommence=(joueurQuiCommence+1)%2;
                tourFini=true;    
                }   
            }
    }
    
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
                    partieEnCours(partieSélectionnée,"1vsBOT",false);// On lance la partie
                    break;

                case "2": // Choix d'une partie 1v1 
                    partieSélectionnée = loadCSV("fichiersCSV/partieVide.csv");
                    partieEnCours(partieSélectionnée,"1vs1",false); // On lance la partie
                    break;

                case "3": // Choix d'une partie deja sauvegardee
                    partieSélectionnée = loadCSV("fichiersCSV/partieSave.csv");
                    partieEnCours(partieSélectionnée,"1v1", true);// On lance la partie;
                    break;

                case "4": // Afficher les règles du jeux 
                    
                    break;

                case "5": // Voir les decks disponibles 
                    
                    break;
                
                case "6": // Crédit du jeu

                    break;

                case "7": // Choix de fin du jeu
                    finJeu = true;
                    break;
                default:
                    print("\n| Erreur");
            }
        }while(!finJeu);
        
        println("\n| Merci d'avoir joué !");
    }

    // ------------------------- Fonctions de test ------------------------- //
    
    // Fonction de test pour la declaration de deck
    void testdeclarationDeck(){
        CSVFile CSV= loadCSV("fichiersCSV/partieSave.csv");
        int[] deck = new int[TAILLE_DECK];
        deck = declarationDeck(CSV, 1);
        assertEquals(length(deck), TAILLE_DECK);
        assertEquals(deck[0], 1);
        assertEquals(deck[1], 2);
    }

    // Fonction pour verifier si le deck est bien initialisé
    void testDeckInitialise(){
        int newDeck[] = new int[TAILLE_DECK];
        assertEquals(length(newDeck), TAILLE_DECK);
    }

    // Fonction pour vérifier si les joueurs sont bien initialisés
    void testJoueursInitialises(){
        Joueur joueurTest = new Joueur();
        joueurTest = newJoueur("Toto", "CP");
        assertEquals(joueurTest.pseudo, "Toto");
        assertEquals(joueurTest.classe, "CP");
    }

    // Fonction pour vérifier si le plateau est bien initialisé
    void testPlateauInitialise(){
        Carte plateauTest[][] = new Carte[2][3];
        assertEquals(length(plateauTest), 2);
        assertEquals(length(plateauTest[0]), 3);
        assertEquals(length(plateauTest[1]), 3);
    }
    
//#endregion
}
