 // Fonction qui convertit un entier int en String
    String intToString(int nombre){
        String nombreString = "";
        while(nombre != 0){
            nombreString = (char) (nombre%10 + '0') + nombreString;
            nombre = nombre/10;
        }
        return nombreString;
    }

// Sauvegarde les informations du plateau
    void sauvegarderPlateau(Carte[][] plateau){

        String plateauSave[][] = new String[7][2];
        String nomPlateauSave = "fichiersCSV/plateauSave.csv";

        plateauSave[0][0] = "idCarte";
        plateauSave[0][1] = "PV";

        int compteur = 1;

       for (int i=0; i<2; i++){ // boucle qui enregistre dans le CSV les cartes du plateau avec leur id et leur PV
            for (int j=0; j<3; j++) {
                if (plateau[i][j] != null) {
                    plateauSave[compteur][0] = intToString(plateau[i][j].idCarte);
                    plateauSave[compteur][1] = intToString(plateau[i][j].PV);
                } else {
                    plateauSave[compteur][0] = "";
                    plateauSave[compteur][1] = "";
                }
                compteur++;
            }
       }

       saveCSV(plateauSave, nomPlateauSave);

    }

    // Sauvegarde les informations des joueurs
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

// Fonction qui sauvegarde les informations principales du jeux
void sauvegarderInfosJoueur(Joueur joueur1, Joueur joueur2, Joueur joueurQuiJoue, int numTour){
        
        String partieSave[][] = new String[3][7];
        String nomPartieSave = "fichiersCSV/partieSave.csv";

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
            //partieSave[i][5] = intToString(joueurQuiJoue); // A faire
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


