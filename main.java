import extensions.CSVFile;
import extensions.File;

class main extends Program {

    // déroule le jeux
    void déroulementJeux(){
        
        char choixJoueur = menuJeux();

        switch (choixJoueur){

            case '1': // Lancement d'une partie solo
                CSVFile partieVide = loadCSV("fichiersCSV/partieVide.csv");
                partieSolo(partieVide);
                break;

            case '2': // Lancement d'une partie de deux joueurs
                print("choix2");
                break;
            default:
                print("Erreur");
        }
    }

    // Fonction qui gère le menu du jeux
    char menuJeux(){

	    File TxtBattleQuiz = newFile("Texts/BattleQuizDeluxe.txt"); //Stockage du contenu des fichiers texte dans une variable
        File TxtAccueil = newFile("Texts/accueil.txt");
        File TxtBarre = newFile("Texts/barre.txt");

        afficherFichierTxt(TxtBattleQuiz);
        afficherFichierTxt(TxtAccueil);
        afficherFichierTxt(TxtBarre);

        char choixUtilisateur = 0;

        do{ // L'utilisateur choisi un numéro entre 1 et 7 (inclus)
            print("Entrez un numéro entre 1 et 7 pour valider :");
            choixUtilisateur = readChar();
            if(choixUtilisateur < '1' || choixUtilisateur > '7') println("Numéro incorrect, veuillez réessayer.");
        } while(choixUtilisateur < '1' || choixUtilisateur > '7');
        return choixUtilisateur;
    }

    void partieSolo(CSVFile fichierSauvegarde){
        print("test");
    }

    // Fonction permettant de contenir le dessin ASCII de la carte passée en paramètre
    String imageASCIICarte(CSVFile fichierCSV, int indiceCarte){
        
            // Récupérer l'ASCII du premier monstre (index de la colonne ASCII = 7)
            String asciiCarte = getCell(fichierCSV, indiceCarte, 8); // Ligne d'indice de la carte, colonne 7 (index ASCII)
            
            asciiCarte = asciiCarte.replace("\"", "");    // Remplace les guillemets doubles par une chaîne vide
            asciiCarte = asciiCarte.replace("\\n", "\n"); // Remplacer "\n" par un retour à la ligne réel

            return asciiCarte; // Afficher l'ASCII
    }

    // Fonction permettant d'afficher toutes les données d'une carte sous un format structuré avec l'ASCII
    void afficherDetailsCarte(CSVFile fichierCSV, int indiceCarte, String[] dessinCarte) {

        // Récupère les différentes informations de la carte
        String typeCarte = getCell(fichierCSV, indiceCarte, 1);           // Colonne Type
        String nomCarte = getCell(fichierCSV, indiceCarte, 2);            // Colonne Nom
        String pvCarte = getCell(fichierCSV, indiceCarte, 3);             // Colonne PV
        String descriptionAttaque = getCell(fichierCSV, indiceCarte, 4);  // Colonne DescriptionAttaque
        String attaqueCarte = getCell(fichierCSV, indiceCarte, 5);        // Colonne Attaque
        String retraiteCarte = getCell(fichierCSV, indiceCarte, 6);       // Colonne Retraite
        String descriptionCarte = getCell(fichierCSV, indiceCarte, 7);    // Colonne Description
        String asciiCarte = "";
        if(length(dessinCarte) > 0) asciiCarte = dessinCouleur(dessinCarte);          // Dessin de la carte
        else asciiCarte = imageASCIICarte(fichierCSV, indiceCarte);

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
        String[] dessin = new String[] {};
        for(int i=0; i<nbLignes-1; i++){
            afficherDetailsCarte(fichierCSV, i+1, dessin);
        }
    }

    void algorithm() {

        CSVFile fichierCSV = loadCSV("fichiersCSV/cartes.csv"); // Charger le fichier CSV contenant les informations des cartes

        int nbLignes = rowCount(fichierCSV); // Compte le nombre de lignes dans le fichier

        //déroulementJeux();

        String[] electhor = {
            "bnnnbbnnbbbbbbbbbbbbbbbbbbbbb",
            "bnoonnnonnbbbbbbbnnbbbbbbbbbb",
            "bbnoonnngnnbbbbnnjnbnnbbbbbbb",
            "bbbnongnngnbbbnjnjnnjnbbbbbbb",
            "bnnnnnnggggnbbnjjjjjjjnbbbbbb",
            "njjjngngggnnnbnjjjjjnnnbbbbbb",
            "bnnjjngggnjngnnnjjjjjnbbbbbbb",
            "bbbnnjngnjjnnjjnjjjjnnbbbbbbb",
            "bbbbbnnnjjnjjjnjjnnnbbbbbbbbb",
            "bbbbbbnjjjjjjnnnnggnnnnnnbbbb",
            "bbbbbnjjjjgnjjjjngggggggnbbbb",
            "bbbbbnnjjgbnjjnngggggnnnnnbbb",
            "bbbbbbnnjnbjjjjnnggnngnjjjnnb",
            "bbbbbnoonjjjjjnjnngnjngnjjjjn",
            "bbbbnooonjjjjjnnjjnnjjnnjnnnn",
            "bbbnoonnbnjnnnbbnjjjnnjjjnbbb",
            "bbnonnbbbbnnbbbbbnnjjnnjjjnbb",
            "bbnnbbbbbbbbbbbbbbbnjnbnnjjnb",
            "bbbbbbbbbbbbbbbbbbbbnnbbbnnnb",
        };

        //print(dessinCouleur(electhor));
        afficherDetailsCarte(fichierCSV, 1, electhor);

        afficherPokedex(fichierCSV);
        
    }

    // Fonction qui génère le pixel art du dessin passé en paramètre
    String dessinCouleur(String[] dessinCouleur) {
        
        String reset = "\033[0m"; // Couleur par défaut
        String[] colors = {"\033[31m", "\033[38;5;214m", "\033[33m", "\033[32m", "\033[34m", "\033[35m", "\033[90m", "\033[30m", "\033[97m"}; // Couleurs rouge, orange, jeune, vert, Bleu, Violet, gris, noir, blanc
        String dessinFinal = "";

        for (int i = 0; i < length(dessinCouleur); i++) { // Affichage du pixel art avec des couleurs
            for (int j = 0; j < length(dessinCouleur[0]); j++) {
                char pixel = charAt(dessinCouleur[i], j); // Récupère le caractère actuel
                String color = reset;                     // Couleur par défaut (rouge en cas d'erreur)
                if (pixel == 'b') color = colors[8];      // Blanc
                else if (pixel == 'r') color = colors[0]; // Rouge
                else if (pixel == 'o') color = colors[1]; // Orange
                else if (pixel == 'j') color = colors[2]; // Jaune
                else if (pixel == 'v') color = colors[3]; // Vert
                else if (pixel == 'B') color = colors[4]; // Bleu
                else if (pixel == 'V') color = colors[5]; // Violet
                else if (pixel == 'g') color = colors[6]; // Gris
                else if (pixel == 'n') color = colors[7]; // Noir
                dessinFinal += color + "██" + reset;
            }
            dessinFinal += "\n";
        }
        return dessinFinal; // Retour du pixel art terminé
    }
}
