import extensions.CSVFile;
import extensions.File;
class JeuBattleQuiz extends Program{
    String [] placementQuestion= new String[]{"QUESTIONS","CHOIX DE LA DIFFICULTÉ","(1) Facile","(2) Normal","(3) Difficile"};
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
        cursor(3,60);
        println("|----------------------------------------------------|");
        cursor(4,60);
        println("|  "+tab[0]);
        cursor(4,113);
        println("|");
        cursor(5,60);
        println("|----------------------------------------------------|");
        cursor(6,60);
        println("|      (1)"+tab[1]+"  (2)"+tab[2]);
        cursor(6,113);
        println("|");
        cursor(7,60);
        println("|      (3)"+tab[3]+"  (4)"+tab[4]);
        cursor(7,113);
        println("|");
        cursor(8,60);
        println("|----------------------------------------------------|");
        print("Votre réponse :");
        
    }
    void cadreDifficulté(String[] tab){
        cursor(3,60);
        println("|----------------------------------------------------|");
        cursor(4,60);
        println("|  "+tab[0]+" :");
        cursor(4,113);
        println("|");
        cursor(5,60);
        println("|----------------------------------------------------|");
        cursor(6,60);
        println("|  "+tab[1]+" :");
        cursor(6,113);
        println("|");
        cursor(7,60);
        println("|   "+tab[2]+"   "+tab[3]+"   "+tab[4]);
        cursor(7,113);
        println("|");
        cursor(8,60);
        println("|----------------------------------------------------|");
        print("Votre réponse :");
    }
    void actualiserQuestions(){
        for (int i=3; i<8;i++){
            cursor(i,60);
            println("                                                      ");
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*Cette fonction execute toute les précédentes et indique à l'algorithme principale si l'attaque est validé par le biais d'un qcm qui s'adapte à la classe du joueur*/ 
    boolean QCMpourAttaquer(Joueur j,int choixDifficulté){
        boolean résultatQCM=false;
        //Liste des fichiers de questions
        CSVFile cp = loadCSV("./Questions/questionCP.csv");
        CSVFile ce1 = loadCSV("./Questions/questionCE1.csv");
        CSVFile ce2 = loadCSV("./Questions/questionCE2.csv");
        CSVFile cm1 = loadCSV("./Questions/questionCM1.csv");
        CSVFile cm2 = loadCSV("./Questions/questionCM2.csv");
        CSVFile sixième = loadCSV("./Questions/question6e.csv");
        //-------------------------------------------//
        //Genere un indice d'une ligne d'un fichier de question compris en 1 et 30
        int aleaLigne = (int) (random()*31)+1;
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
    Joueur newJoueur(String pseudo, String classe){
        Joueur j = new Joueur();
        j.pseudo=pseudo;
        j.classe=classe;
        return j;
    }
    String toString(Joueur j){
        return "Pseudo : "+j.pseudo+"\nClasse : "+j.classe;
    }
    void algorithm(){
        Joueur j1= newJoueur("Clément","CM2");
        /*--Cette partie encadré a été sortie de la fonction QCMpourAttaquer dans l'objectif de récupérer 
        la variable "choixDiffficulté" pour à l'avenir créer des fonctions bonus() et malus()--*/
        actualiserQuestions();
        cadreDifficulté(placementQuestion);
        int choixDifficulté=choixDifficulté();
        ///////////////////////////////////////////////////////////////////////////////////////
        boolean attaqueAcceptée=QCMpourAttaquer(j1,choixDifficulté);
    }
        
}
