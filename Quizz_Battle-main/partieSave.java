    // Sauvegarde les informations des joueurs
    /*
    void sauvegarderInfosJoueur(Joueur joueur1, Joueur joueur2, Joueur joueurQuiJoue, int[] deckJoueur1, int[] deckJoueur2, Carte[] mainJoueur1, Carte[] mainJoueur2, int numTour, int nbrCartesDeckJ1, int nbrCartesDeckJ2){
        /*String partieSave[][] = new String[3][7];
        String nomPartieSave = "partieSave.csv";

        partieSave[0][0] = "numJoueur";
        partieSave[0][1] = "pointsJoueur";
        partieSave[0][2] = "cartesDeck";
        partieSave[0][3] = "cartesMain";
        partieSave[0][4] = "numTour";
        partieSave[0][5] = "joueurActif";
        partieSave[0][6] = "nbrCartesDeck";

        for(int i=1; i<3; i++){
            partieSave[i][0] = (String) i;
            partieSave[i][4] = (String) numTour;
            partieSave[i][5] = (String) joueurQuiJoue;
            if (i == 1){
                partieSave[i][1] = (String) joueur1.points;
                partieSave[i][6] = (String) nbrCartesDeckJ1;
            }
            else{
                partieSave[i][1] = (String) joueur2.points;
                partieSave[i][6] = (String) nbrCartesDeckJ2;
            }
        }

        for (int i=0; i<TAILLE_DECK; i++){
            partieSave[1][2] += (String) deckJoueur1[i];
            partieSave[2][2] += (String) deckJoueur2[i];
            if (i<TAILLE_DECK-1) {
                partieSave[1][2] += "/";
                partieSave[2][2] += "/";
            }
        }

        for(int i=0; i<length(mainJoueur1); i++){
            
            if (mainJoueur1[i] != null) partieSave[1][3] += (String) mainJoueur1.idCarte[i];
            if (mainJoueur2[i] != null) partieSave[2][3] += (String) mainJoueur2.idCarte[i];
            if (i<length(mainJoueur1)-1) {
                partieSave[1][3] += "/";
                partieSave[2][3] += "/";
            }
        }

        saveCSV(partieSave, nomPartieSave);
    }

    // Sauvegarde les informations du plateau
    void sauvegarderPlateau(Carte[][] plateau){

        String plateauSave[][] = new String[2][7];
        String nomPlateauSave = "partieSave.csv";

        plateauSave[0][0] = "idCarte";
        plateauSave[0][1] = "PV";
        

       for (int i=1; i<4; i++){
            for (int j=0; j<2; j++){
                plateauSave[i][j] = plateau.idCarte[j][i];   // test
                plateauSave[i+3][j] = plateau.idCarte[j][i]; // A verifier
            }
       }

    }*/
