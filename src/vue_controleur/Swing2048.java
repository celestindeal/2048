package vue_controleur;

import modele.Case;
import modele.Game;
import modele.Direction;
import statics.Colors;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.Observable;
import java.util.Observer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// affiche le nom de la partie sur laquel on joue
// quand on sauvegarde une partie déjà sauvegarder on ne demande pas le nom et on garde la même 
// on préviens quand on vas écrasser une sauvegarde 

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 160;
    // tableau de cases : i, j -> case graphique

    private final JLabel[][] tabC;
    private final JLabel score;
    private final JLabel titre;

    
    private final JLabel bestScore;
    private Game game;
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

        JPanel contentBesttitre = new JPanel();
        titre = new JLabel( "Partie non sauvegardée");
        contentBesttitre.add( titre);
        contentScore.add(contentCurrentScore,BorderLayout.EAST);
        contentScore.add(contentBestScore,BorderLayout.WEST);
        contentScore.add(contentBesttitre,BorderLayout.CENTER);


       
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(contentGrid,BorderLayout.CENTER);
        contentPane.add(contentScore,BorderLayout.NORTH);
        setContentPane(contentPane);
        addKeyboardListener();
        refresh();

    }
    
    private void refreshVue(){   // utiliser quand on change de partie 
        game.addObserver(this);
        refresh();
    }

    private String askPlayer(String message, String value ){
        return JOptionPane.showInputDialog(message, value);
    }

    private void addMenus() {
        JMenuBar mb = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart");
        JMenu chargerItem = new JMenu("Charger partie");
        

        restartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.restart();
                titre.setText("Partie non sauvegardée");
            }
        });
        
        JMenuItem saveItem = new JMenuItem("Sauver la partie");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nomSave = askPlayer("Le nom de ta sauvegarde ?","");

                File fichier =  new File("partie/"+nomSave+".ser") ;
                // ouverture d'un flux sur un fichier
                ObjectOutputStream oos;
                try {
                    oos = new ObjectOutputStream(new FileOutputStream(fichier));
                    // sérialization de l'objet
                    oos.writeObject(game) ;
                    titre.setText("Partie non sauvegardée");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
               
            }
        });

        File dir  = new File("partie/");
        File[] liste = dir.listFiles();
        for(File item : liste ){ // faire les propositions de partie enregister
            JMenuItem Item = new JMenuItem(  item.getName().substring(0, item.getName().lastIndexOf('.')  ));  
            Item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    File fichier =  new File("partie/"+item.getName()) ;
                    try {
                        // ouverture d'un flux sur un fichier
                        ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(fichier)) ;

                        // désérialization de l'objet
                        game = (Game)ois.readObject() ;
                        ois.close();
                        titre.setText( item.getName().substring(0, item.getName().lastIndexOf('.')));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    refreshVue();
                }
            });
            chargerItem.add(Item);
        }

        gameMenu.add(restartItem);
        gameMenu.add(saveItem);
        gameMenu.add(chargerItem);

        
        
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

        mb.add(gameMenu);
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