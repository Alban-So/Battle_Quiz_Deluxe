import extensions.CSVFile;
import extensions.File;

class main extends Program {

    // Fonction permettant de contenir le dessin ASCII de la carte passée en paramètre
    String imageCarte(CSVFile fichierCSV, int indiceCarte){
        
            // Récupérer l'ASCII du premier monstre (index de la colonne ASCII = 7)
            String asciiCarte = getCell(fichierCSV, indiceCarte, 7); // Ligne d'indice de la carte, colonne 7 (index ASCII)
            
            asciiCarte = asciiCarte.replace("\"", "");    // Remplace les guillemets doubles par une chaîne vide
            asciiCarte = asciiCarte.replace("\\n", "\n"); // Remplacer "\n" par un retour à la ligne réel

            return asciiCarte; // Afficher l'ASCII
    }

    // Fonction permettant d'afficher toutes les données d'une carte sous un format structuré avec l'ASCII
    void afficherDetailsCarte(CSVFile fichierCSV, int indiceCarte) {

        // Récupère les différentes informations de la carte
        String typeCarte = getCell(fichierCSV, indiceCarte, 0);           // Colonne Type
        String nomCarte = getCell(fichierCSV, indiceCarte, 1);            // Colonne Nom
        String pvCarte = getCell(fichierCSV, indiceCarte, 2);             // Colonne PV
        String descriptionAttaque = getCell(fichierCSV, indiceCarte, 3);  // Colonne DescriptionAttaque
        String attaqueCarte = getCell(fichierCSV, indiceCarte, 4);        // Colonne Attaque
        String retraiteCarte = getCell(fichierCSV, indiceCarte, 5);       // Colonne Retraite
        String descriptionCarte = getCell(fichierCSV, indiceCarte, 6);    // Colonne Description
        String asciiCarte = imageCarte(fichierCSV, indiceCarte);

        String carteAffichee = "";

        carteAffichee += "___________________________\n";
        carteAffichee += "|  " + nomCarte + "          ";                 // Ajoute le nom
        if(equals(typeCarte, "Monstre")) carteAffichee += pvCarte + "PV"; // Ajoute les PV
        carteAffichee +="|\n";

        String[] asciiLignes = asciiCarte.split("\n");

        int hauteurAscii = asciiLignes.length;

        for (int i = 0; i < 5 - hauteurAscii; i++) {                      // Ajoute de l'espacement vide si nécessaire (si le dessin est trop petit)
            carteAffichee+="|                         |\n";
        }

        for (String ligne : asciiLignes) {                                // Ajouter le dessin ASCII
            carteAffichee += "|     " + String.format("%-20s", ligne) + "|\n";
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

        println(carteAffichee); // Affichage de la carte
    }


    void algorithm() {

            CSVFile fichierCSV = loadCSV("cartes.csv"); // Charger le fichier CSV contenant les informations des cartes

            int nbLignes = rowCount(fichierCSV); // Compte le nombre de lignes dans le fichier

            println("Dessin ASCII du premier monstre :");
            afficherDetailsCarte(fichierCSV, 1); // Affichage du 1er pokemon de la liste du fichier CSV
            afficherDetailsCarte(fichierCSV, 2);
    }
}
