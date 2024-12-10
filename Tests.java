public class Tests {
    
    void afficherDetailsCarte(CSVFile fichierCSV, int indiceCarte, String[] dessinCarte) {
        String asciiCarte = "";
        if(length(dessinCarte) > 0) asciiCarte = dessinCouleur(dessinCarte);          // Dessin de la carte
        else asciiCarte = imageASCIICarte(fichierCSV, indiceCarte);
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

    // Fonction permettant d'afficher toutes les données d'une carte sous un format structuré avec l'ASCII
    void afficherDetailsCarte(carte infosCarte) {

        // Récupère les différentes informations de la carte
        String typeCarte = infosCarte           // Colonne Type
        String nomCarte = getCell(fichierCSV, indiceCarte, 2);            // Colonne Nom
        String pvCarte = getCell(fichierCSV, indiceCarte, 3);             // Colonne PV
        String descriptionAttaque = getCell(fichierCSV, indiceCarte, 4);  // Colonne DescriptionAttaque
        String attaqueCarte = getCell(fichierCSV, indiceCarte, 5);        // Colonne Attaque
        String retraiteCarte = getCell(fichierCSV, indiceCarte, 6);       // Colonne Retraite
        String descriptionCarte = getCell(fichierCSV, indiceCarte, 7);    // Colonne Description
        String asciiCarte = imageASCIICarte(fichierCSV, indiceCarte);

        String carteAffichee = "";

        carteAffichee += "___________________________\n";                 // Ajoute le nom
        carteAffichee += "|  " + nomCarte; 
        for (int i=length(nomCarte); i<18; i++) carteAffichee += " ";
        if(equals(typeCarte, "Monstre")){                                 // Ajout des PVs pour les monstres
            if (length(pvCarte) < 3) carteAffichee += " ";
            carteAffichee += pvCarte + "PV";
        }
        else carteAffichee += "     ";
        carteAffichee +="|\n";
        
        String[] asciiLignes = asciiCarte.split("\n");               // A chaque passage à la ligne (\n), on passe a l'indice suivant du tableau

        int hauteurAscii = length(asciiLignes);
        
        for (int i = 0; i < 5 - hauteurAscii; i++) {                       // Ajoute de l'espacement vide si nécessaire (si le dessin est trop petit)
            carteAffichee+="|                         |\n";
        }
        
        for (int i = 0; i < hauteurAscii; i++) {                           // Ajoute le dessin ASCII
            while (length(asciiLignes[i]) < 20) asciiLignes[i] += " ";     // Ajoute des espaces à droite du dessin, puis rajoute le "|" de fin de ligne
            carteAffichee += "|     " + asciiLignes[i] + "|\n";
        }

        carteAffichee += "|_________________________|\n";
        carteAffichee += "|                         |\n";

        if(equals(typeCarte,"Monstre")){                                   // Description de la carte pour les Monstres
            carteAffichee += "|  - " + descriptionAttaque + " : " + attaqueCarte;
            for (int i = length(descriptionAttaque) + length(attaqueCarte); i<18; i++) carteAffichee += " ";
            carteAffichee += "|\n";
            carteAffichee += "|                         |\n";
            carteAffichee += "| Points de retraite : " + retraiteCarte + "  |\n"; // Ajoute les points de retraite
        }
        else{                                                              // Description de la carte pour les autres types de cartes
            carteAffichee += "|  - ";
            if (length(descriptionCarte) > 21){                            // Si la description de la carte est trop grande,
                int tailleEspaceRestant = 42 - length(descriptionCarte);   // on l'affiche sur deux lignes.
                for (int i=0; i<42; i++) {
                    if (i < length(descriptionCarte)) carteAffichee += charAt(descriptionCarte, i);
                    else carteAffichee += " ";
                    if (i == 20) carteAffichee += "|\n|    ";
                }
                carteAffichee += "|\n";
            }
            else {
                carteAffichee += descriptionCarte;
                for (int i = length(descriptionCarte); i<21; i++) carteAffichee += " ";
                carteAffichee += "|\n" + "|                         |\n";
            } 
            carteAffichee += "|                         |\n";
        }
        carteAffichee += "---------------------------\n";

        print(carteAffichee); // Affichage de la carte
    }

    void algorithm(){

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



}
