package modele;

import java.util.Observable;
import java.util.Random;

public class Game extends Observable {

    private Case[][] tabCases;

    public Game(int size) {
        tabCases = new Case[size][size];
        initCases();
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

    public void initCases() {
        // permet de libérer le processus graphique ou de la console
        new Thread(() -> {
            int r;
            int filled_cases = 0;
            Random rd = new Random(4);

            while(filled_cases < 2){
                int i = rd.nextInt(tabCases.length);
                int j = rd.nextInt(tabCases.length);

                if(tabCases[i][j] == null) {
                    r = rd.nextInt(4);
                    switch (r) {
                        case 0:
                            tabCases[i][j] = new Case(4);
                            filled_cases++;
                            break;
                        case 1:
                        case 2:
                        case 3:
                            tabCases[i][j] = new Case(2);
                            filled_cases++;
                            break;
                    }
                }
            }
        }).start();


        setChanged();
        notifyObservers();


    }

}
