package modele;

import java.util.Observable;
import java.util.Random;

public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random(4);

    public Jeu(int size) {
        tabCases = new Case[size][size];
        rnd();
    }

    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(int i, int j) {
        return tabCases[i][j];
    }

    public void action_joueur(Direction direction){
        private void trie_gauche(s){
            for (int i = 0; i < tabCases.length; i++) {
                for (int j = 1; j < tabCases.length; j++) {

                    if(tabCases[i][j-1] == null){
                        tabCases[i][j].setValeur(tabCases[i][j-1].getValeur());
                        tabCases[i][j-1]=null;
                    }else if (tabCases[i][j]== tabCases[i][j-1]){
                        tabCases[i][j].setValeur(tabCases[i][j-1].getValeur()*2) ;
                        tabCases[i][j-1]=null;
                    }

                }
            }
        }
        switch (direction) {
            case Direction.haut:
                for (int i = 0; i < tabCases.length; i++) {

                }
                break;
            case Direction.bas:
                action_joueur()
                trie_gauche();
                trouner_le_tableau()
                break;
            case Direction.droite:

                break;
            case Direction.gauche:
                break;
        }

    }


    public void rnd() {
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                int r;  // nombre aléatoire

                for (int i = 0; i < tabCases.length; i++) {  // construit toutes les cases
                    for (int j = 0; j < tabCases.length; j++) {
                        r = rnd.nextInt(3);

                        switch (r) {
                            case 0:
                                tabCases[i][j] = null;
                                break;
                            case 1:
                                tabCases[i][j] = new Case(2);
                                break;
                            case 2:
                                tabCases[i][j] = new Case(4);
                                break;
                        }
                    }
                }
            }

        }.start();

        // refrech la vue
        setChanged();
        notifyObservers();


    }

}
