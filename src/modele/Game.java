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

    private void shiftLeft(){
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 1; j < tabCases.length; j++) {

                while( j>0 && tabCases[i][j] != null  ){
                    if(tabCases[i][j-1] == null){
                        tabCases[i][j-1] = tabCases[i][j];
                        tabCases[i][j]=null;
                        j--;
                    }else if (tabCases[i][j].getValue() == tabCases[i][j-1].getValue() && !tabCases[i][j-1].hasMerge()){
                        tabCases[i][j-1].doubleValue();
                        tabCases[i][j]=null;
                    }else{
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < tabCases.length; i++) {    // remette les états de fusions des cases de la table de jeu
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[i][j] != null){
                    tabCases[i][j].changeMergeState(false);
                }
            }
        }
    }

    public void turnLeft(int nbTurn){
        int n=0;
        while(nbTurn > n){
            n++;
            Case[][] tabCasesTurn;
            tabCasesTurn = new Case[tabCases.length][tabCases.length];

            for (int i = 0; i < tabCases.length; i++) {  // transposition de la matrice
                for (int j = 0; j < tabCases.length; j++) {
                    tabCasesTurn[i][j] = tabCases[j][i];
                }
            }
            tabCases =tabCasesTurn.clone();

            for (int i = 0; i < tabCases.length/2; i++) {  // transposition de la matrice
                tabCases[i]=tabCasesTurn[tabCases.length-1-i];
                tabCases[tabCases.length-1-i]=tabCasesTurn[i];
            }
        }

    }

    public void action_joueur(Direction direction){
        switch (direction) {
            case haut:
                turnLeft(1);
                shiftLeft();
                turnLeft(3);
                break;
            case bas:
                turnLeft(3);
                shiftLeft();
                turnLeft(1);
                break;
            case droite:
                turnLeft(2);

                shiftLeft();

                turnLeft(2);
                break;
            case gauche:
                shiftLeft();
                break;
        }
        addCase();
        setChanged();
        notifyObservers();
    }

    public void initCases() {
        // permet de libérer le processus graphique ou de la console
        new Thread(() -> {
            int filled_cases = 0;

            while(filled_cases < 6){
                addCase();
                filled_cases++;
            }
        }).start();

        setChanged();
        notifyObservers();
    }

    private void addCase(){
        Random rd = new Random();
        int i = rd.nextInt(tabCases.length);
        int j = rd.nextInt(tabCases.length);

        while(tabCases[i][j] != null) {
            i = rd.nextInt(tabCases.length);
            j = rd.nextInt(tabCases.length);
        }

        int r = rd.nextInt(4);
        switch (r) {
            case 0:
                tabCases[i][j] = new Case(4);
                break;
            case 1:
            case 2:
            case 3:
                tabCases[i][j] = new Case(2);
                break;
        }
    }
}
