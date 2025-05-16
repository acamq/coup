import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * UI to sequentially show each player's hidden influences,
 * with swap-seats warnings and external invocation,
 * and to display influences in various in-game situations.
 */
public class SecretInfluenceViewer {
    private final JFrame frame;
    private JLabel messageLabel;
    private JPanel influencePanel;
    private JButton actionButton;

    // For player reveal mode
    private final Player[] players;
    private int currentPlayer = 0;
    private boolean showingInfluences = false;

    // For in-game mode
    private final boolean ambassadorMode;
    private final boolean isInGameView;
    private final Influence[] vInfluences;

    // Constructor for player-reveal sequence.
    private SecretInfluenceViewer(Player[] players) {
        this.players = players;
        this.isInGameView = false;
        this.ambassadorMode = false;
        this.vInfluences = null;
        frame = new JFrame("Secret Influence Viewer");
        initCommonUI();
        updateUI();
        frame.setVisible(true);
    }

    // Constructor for in-game exchange or view display.
    private SecretInfluenceViewer(Influence[] influences, boolean isAmbassador) {
        this.players = null;
        this.isInGameView = true;
        this.ambassadorMode = isAmbassador;
        this.vInfluences = influences;
        frame = new JFrame("Influence Viewer");
        initCommonUI();
        updateUI();
        frame.setVisible(true);
    }

    // Factor out common UI setup
    private void initCommonUI() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(messageLabel.getFont().deriveFont(16f));
        frame.add(messageLabel, BorderLayout.NORTH);

        influencePanel = new JPanel();
        influencePanel.setLayout(new BoxLayout(influencePanel, BoxLayout.Y_AXIS));
        frame.add(influencePanel, BorderLayout.CENTER);

        actionButton = new JButton();
        actionButton.addActionListener(new ButtonListener());
        frame.add(actionButton, BorderLayout.SOUTH);
    }

    // Updates the UI based on mode and whether the user has clicked “View”.
    private void updateUI() {
        influencePanel.removeAll();

        if (isInGameView) {
            // In-game modes (ambassador or simple view)
            if (!showingInfluences) {
                messageLabel.setText("<html>Click \"View\" to see your influences.<br>Make sure nobody else is looking!</html>");
                actionButton.setText("View");
            } else {
                if (ambassadorMode) {
                    messageLabel.setText("<html>Ambassador: view and choose exchange cards.<br>(Make sure nobody else is looking!)</html>");
                } else {
                    messageLabel.setText("Your Influences (keep secret!):");
                }
                for (int i = 0; i < vInfluences.length; i++) {
                    Influence inf = vInfluences[i];
                    String name = (inf == null ? "None" : inf.getName());
                    JLabel lbl = new JLabel((i + 1) + ". " + name);
                    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                    influencePanel.add(lbl);
                    influencePanel.add(Box.createVerticalStrut(5));
                }
                actionButton.setText("Done");
            }

        } else {
            // Player-reveal sequence
            Player p = players[currentPlayer];
            if (!showingInfluences) {
                messageLabel.setText("<html>Pass the device to " + p.getName() + ". Click \"View\" when ready.<br>Make sure nobody else is looking!</html>");
                actionButton.setText("View");
            } else {
                messageLabel.setText(p.getName() + "'s Influences (keep secret!):");
                JLabel inf1 = new JLabel("1. " + p.getInfluence1().getName());
                JLabel inf2 = new JLabel("2. " + p.getInfluence2().getName());
                inf1.setAlignmentX(Component.CENTER_ALIGNMENT);
                inf2.setAlignmentX(Component.CENTER_ALIGNMENT);
                influencePanel.add(inf1);
                influencePanel.add(Box.createVerticalStrut(5));
                influencePanel.add(inf2);
                actionButton.setText(
                        currentPlayer < players.length - 1 ? "Next Player" : "Done");
            }
        }

        frame.revalidate();
        frame.repaint();
    }


    // Shared button logic for both modes
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isInGameView) {
                if (!showingInfluences) {
                    // user clicked “View” in in-game
                    showingInfluences = true;
                    updateUI();
                } else {
                    // user clicked “Done” in in-game
                    frame.dispose();
                }
            } else {
                // player-reveal mode
                if (!showingInfluences) {
                    showingInfluences = true;
                } else {
                    showingInfluences = false;
                    currentPlayer++;
                }
                if (currentPlayer >= players.length) {
                    frame.dispose();
                } else {
                    updateUI();
                }
            }
        }
    }

    // Exposed methods to launch the viewer
    public static void show(Player[] players) {
        SwingUtilities.invokeLater(() -> new SecretInfluenceViewer(players));
    }

    public static void showInfluences(Influence[] influences, boolean isAmbassadorMode) {
        SwingUtilities.invokeLater(() ->
                new SecretInfluenceViewer(influences, isAmbassadorMode));
    }
}
