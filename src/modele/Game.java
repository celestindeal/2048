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

    public void turnLeft(int nbTurn){
        Case[][] tabCasesTurn;
        tabCasesTurn = new Case[tabCases.length][tabCases.length];

        for (int i = 0; i < tabCases.length; i++) {  // transposition de la matrice
            for (int j = 0; j < tabCases.length; j++) {
                tabCasesTurn[i][j] = tabCases[j][i];
            }
        }
        tabCases =tabCasesTurn.clone();

        for (int i = 0; i < tabCases.length/2; i++) {  // transposition de la matrice
            System.out.print(i);
            tabCases[i]=tabCasesTurn[tabCases.length-1-i];
            tabCases[tabCases.length-1-i]=tabCasesTurn[i];
        }

        setChanged();
        notifyObservers();
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
    }

    public void initCases() {
        // permet de libérer le processus graphique ou de la console
        new Thread(() -> {
            int r;
            int filled_cases = 0;
            Random rd = new Random(4);

            while(filled_cases < 6){
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
