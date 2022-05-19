package vue_controleur;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class BestScore {

    
    public static int get_bestScore(){
        // aller chercher le meilleur score 
        try {
            File doc = new  File("src/score.txt");
            Scanner obj = new Scanner(doc);
            int nombre  = Integer.parseInt(obj.nextLine());
            obj.close();
            return  nombre;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
        
    }

    public static void set_bestScore(int score){
        File myFile = new File("src/score.txt"); 
        myFile.delete();
        
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("src/score.txt", true)));
            out.write("");
            out.println(score);
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
