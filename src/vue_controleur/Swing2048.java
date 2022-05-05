package vue_controleur;

import modele.Case;
import modele.Game;
import modele.Direction;
import statics.Colors;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 160;
    // tableau de cases : i, j -> case graphique
    private final JLabel[][] tabC;
    private final Game game;




    public Swing2048(Game _game) {
        game = _game;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("../statics/icon.png");
        setIconImage(img.getImage());
        setSize(game.getSize() * PIXEL_PER_SQUARE, game.getSize() * PIXEL_PER_SQUARE);
        tabC = new JLabel[game.getSize()][game.getSize()];


        JPanel contentPane = new JPanel(new GridLayout(game.getSize(), game.getSize()));

        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.decode("#B7AA9C"), 10);
                tabC[i][j] = new JLabel();
                tabC[i][j].setFont(new Font("SansSerif", Font.BOLD, 48));
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setOpaque(true);

                contentPane.add(tabC[i][j]);

            }
        }
        setContentPane(contentPane);
        addKeyboardListener();
        refresh();

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
    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void addKeyboardListener() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : game.action_joueur(Direction.LEFT); break;
                    case KeyEvent.VK_RIGHT : game.action_joueur(Direction.RIGHT); break;
                    case KeyEvent.VK_DOWN : game.action_joueur(Direction.DOWN); break;
                    case KeyEvent.VK_UP : game.action_joueur(Direction.UP); break;
                }
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}