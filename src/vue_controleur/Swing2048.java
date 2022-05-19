package vue_controleur;

import modele.Case;
import modele.Game;
import modele.Direction;
import statics.Colors;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 160;
    // tableau de cases : i, j -> case graphique

    private final JLabel[][] tabC;
    private final JLabel score;
    
    private final JLabel bestScore;
    private final Game game;
    private int best_score = 0;

   

    public Swing2048(Game _game) {
        game = _game;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("../statics/icon.ico");
        setIconImage(img.getImage());
        setSize(game.getSize() * PIXEL_PER_SQUARE, game.getSize() * PIXEL_PER_SQUARE);
        tabC = new JLabel[game.getSize()][game.getSize()];
        addMenus();
        best_score = BestScore.get_bestScore();

        JPanel contentGrid = new JPanel(new GridLayout(game.getSize(), game.getSize()));
        

        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.decode("#B7AA9C"), 10);
                tabC[i][j] = new JLabel();
                tabC[i][j].setFont(new Font("SansSerif", Font.BOLD, 48));
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setOpaque(true);

                contentGrid.add(tabC[i][j]);
            }
        }
        
        JPanel contentScore = new JPanel(new BorderLayout());
        // Currente Score
        JPanel contentCurrentScore = new JPanel();
        score = new JLabel( "Score : "+ String.valueOf(game.score) );
        contentCurrentScore.add(score);
        // best score 
        JPanel contentBestScore = new JPanel();
        bestScore = new JLabel( "Best Score : "+ String.valueOf(best_score));
        contentBestScore.add( bestScore);
        contentScore.add(contentCurrentScore,BorderLayout.EAST);
        contentScore.add(contentBestScore,BorderLayout.WEST);

       
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(contentGrid,BorderLayout.CENTER);
        contentPane.add(contentScore,BorderLayout.NORTH);
        setContentPane(contentPane);
        addKeyboardListener();
        refresh();

    }

    private void addMenus() {
        JMenuBar mb = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.restart();
            }
        });
        gameMenu.add(restartItem);
        mb.add(gameMenu);
        JMenu MoreMenu = new JMenu("More");
        JMenuItem createrItem = new JMenuItem("Voir les créateurs"); 
        createrItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URI uri2= new URI("https://www.linkedin.com/in/c%C3%A9lestin-deal-772ba9194/");
                    java.awt.Desktop.getDesktop().browse(uri2);
                    
                    URI uri1= new URI("https://www.linkedin.com/in/robin-lusson-ba753019a/");
                    java.awt.Desktop.getDesktop().browse(uri1);

                } catch (Exception problem) {
                    problem.printStackTrace();
                }
            }
        });
        MoreMenu.add(createrItem);
        mb.add(MoreMenu);

        setJMenuBar(mb);
    }

    private void manageScore(){  // à chaque tour on mets les scores à jour dans l'affichage et dans le fichier de sauvegarde du best score 
        this.score.setText("Score : " + String.valueOf(game.score) );

        if (game.score > best_score ){
            BestScore.set_bestScore(game.score);
            this.bestScore.setText("Best Score : "+ String.valueOf(game.score));
            
        }
    }

    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void refresh()  {

        // demande au processus graphique de réaliser le traitement
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < game.getSize(); i++) {
                for (int j = 0; j < game.getSize(); j++) {
                    Case c = game.getCase(i, j);
                    String[] col;
                    if (c == null) {
                        tabC[i][j].setText("");
                        col = Colors.get(0);
                    } else {
                        tabC[i][j].setText(c.getValue() + "");
                        col = Colors.get(c.getValue());
                    }
                    tabC[i][j].setBackground(Color.decode(col[0]));
                    tabC[i][j].setForeground(Color.decode(col[1]));
                }
            }
        });
        manageScore();
        
    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void addKeyboardListener() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : game.actionPlayer(Direction.LEFT); break;
                    case KeyEvent.VK_RIGHT : game.actionPlayer(Direction.RIGHT); break;
                    case KeyEvent.VK_DOWN : game.actionPlayer(Direction.DOWN); break;
                    case KeyEvent.VK_UP : game.actionPlayer(Direction.UP); break;
                }
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}