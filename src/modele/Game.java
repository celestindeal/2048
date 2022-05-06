package modele;
import javax.swing.JOptionPane;
import java.util.*;

public class Game extends Observable {

    private Case[][] tabCases;
    private boolean lose = false;
    private boolean win = false;

    private HashMap<Case, Point> caseMap;

    private boolean hasMove = false;

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

    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    private void shift(){
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 1; j < tabCases.length; j++) {
                tabCases[i][j].shift();
            }
        }

        for (int i = 0; i < tabCases.length; i++) {    // remettre les états de fusion des cases de la table de jeu
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[i][j] != null){
                    tabCases[i][j].changeMergeState(false);
                }
            }
        }
    }

    public Case getNeighbour(Case _case){
        Point p = caseMap.get(_case);
        p.x -= 1;
        return tabCases[p.x][p.y];
    }

    public void moveCase(Case _case){

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

            for (int i = 0; i < tabCases.length/2; i++) {  // inversion des colone
                tabCases[i]=tabCasesTurn[tabCases.length-1-i];
                tabCases[tabCases.length-1-i]=tabCasesTurn[i];
            }
        }
    }

    private boolean controlerVoisinLose(int i, int j,  boolean[] controles ){   //controles = haut droite bas gauche 
        if (controles[0]){
            if( tabCases[i][j].getValue() == tabCases[i][j-1].getValue()){
                return true;          // ce n'est pas encore perdu
            }
        }
        if (controles[1]){
            if( tabCases[i][j].getValue() == tabCases[i+1][j].getValue()){
                return true;          // ce n'est pas encore perdu
            }
        }
        if (controles[2]){
            if( tabCases[i][j].getValue() == tabCases[i][j+1].getValue()){
                return true;          // ce n'est pas encore perdu
            }
        }
        if (controles[3]){
            if( tabCases[i][j].getValue() == tabCases[i-1][j].getValue()){
                return true;          // ce n'est pas encore perdu
            }
        }
        return false;
    }

    private void  winLose(){

        boolean losetemo = true;  // true  perdu false pas perdu 

        for (int i = 0; i < tabCases.length; i++) {    // remette les états de fusions des cases de la table de jeu
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[i][j] == null){
                    losetemo = false;
                }
                else if (tabCases[i][j].getValue() == 2048 && !win ){
                    win = true;
                    infoBox("tu as gagné grosse merde", "Ferme bien ta geule");
                }  
            }
        }

        if( losetemo ){ // toutes les casses sont pleinne il faut controler si il y encore une possibiliter
            for (int i = 0; i < tabCases.length; i++) {    
                for (int j = 0; j < tabCases.length; j++) {
                    boolean[] controles = new boolean[4];//controles = haut droite bas gauche 
                    Arrays.fill(controles, Boolean.TRUE);
                    if(i==0){//gauche
                        controles[3] = false;
                    }
                    if(j==0){//haut
                        controles[0] = false;
                    }
                    if(i==tabCases.length-1){  //droit
                        controles[1] = false;
                    }
                    if(j==tabCases.length-1){  //bas
                        controles[2] = false;
                    }
                    if(controlerVoisinLose(i,j,controles)){  // si on as une solution on as pas perdu
                        losetemo=false; // tu na pas perdu 
                    }
                }
            }
        }

        if(losetemo){
            lose = true;
            infoBox("tu as PERDU grosse merde", "Ferme bien ta geule"); 
        }
    }

    public void action_joueur(Direction direction){
        if(!lose){
               switch (direction) {
            case UP:
                turnLeft(1);
                shift();
                turnLeft(3);
                break;
            case DOWN:
                turnLeft(3);
                shift();
                turnLeft(1);
                break;
            case RIGHT:
                turnLeft(2);

                shift();

                turnLeft(2);
                break;
            case LEFT:
                shift();
                break;
        }
        if(hasMove) {
            addCase();
            hasMove = false;
        }
        setChanged();
        notifyObservers();
        winLose();
        }else{
            infoBox("infoMessage", "titleBar");
        }
     
    }

    public void initCases() {
        // permet de libérer le processus graphique ou de la console
        int filled_cases = 0;

        while(filled_cases < 2){
            addCase();
            filled_cases++;
        }

        setChanged();
        notifyObservers();
    }

    private void addCase(){
        ArrayList<int[]> vides = new ArrayList<>();
        // On liste les coordonnées des cases vides
        for (int i = 0; i < tabCases.length; i++) {  // transposition de la matrice
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[i][j] == null){
                    vides.add(new int[] { i, j });
                }
            }
        }

        if(vides.isEmpty()){ // Si plus aucune case n'est libre
            return;
        }

        Random rd = new Random();
        int[] coordonnees = vides.get(rd.nextInt(vides.size()));

        int r = rd.nextInt(4);
        switch (r) {
            case 0:
                tabCases[coordonnees[0]][coordonnees[1]] = new Case(4, this);
                break;
            case 1:
            case 2:
            case 3:
                tabCases[coordonnees[0]][coordonnees[1]] = new Case(2, this);
                break;
        }
    }

    public void restart() {
        tabCases = new Case[tabCases.length][tabCases.length];
        initCases();
    }
}
