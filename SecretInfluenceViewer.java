import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * UI to sequentially show each player's hidden influences,
 * with swap-seats warnings and external invocation.
 */
public class SecretInfluenceViewer {
    private final JFrame frame;
    private final JLabel messageLabel;
    private final JPanel influencePanel;
    private final JButton actionButton;
    private final Player[] players;
    private int currentPlayer = 0;
    private boolean showingInfluences = false;

    private SecretInfluenceViewer(Player[] players) {
        this.players = players;
        frame = new JFrame("Secret Influence Viewer");
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

        updateUI();
        frame.setVisible(true);
    }

    /**
     * Updates the UI based on current state, adding swap-seats prompts.
     */
    private void updateUI() {
        influencePanel.removeAll();
        Player p = players[currentPlayer];
        if (!showingInfluences) {
            messageLabel.setText(
                    "Pass the device to " + p.getName() + ". Click \"View\" when ready.");
            actionButton.setText("View");
        } else {
            messageLabel.setText(p.getName() + "'s Influences (keep secret!):");
            JLabel inf1 = new JLabel("\u2022 " + p.getInfluence1().getName());
            JLabel inf2 = new JLabel("\u2022 " + p.getInfluence2().getName());
            inf1.setAlignmentX(Component.CENTER_ALIGNMENT);
            inf2.setAlignmentX(Component.CENTER_ALIGNMENT);
            influencePanel.add(inf1);
            influencePanel.add(Box.createVerticalStrut(5));
            influencePanel.add(inf2);
            actionButton.setText(
                    currentPlayer < players.length - 1 ? "Next Player" : "Done");
        }
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Step through states: view vs. next player.
     */
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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

    /**
     * Exposed method to launch the viewer from other classes.
     */
    public static void show(Player[] players) {
        SwingUtilities.invokeLater(() -> new SecretInfluenceViewer(players));
    }

    /**
     * Example usage; can be removed when called externally.
     */
    public static void main(String[] args) {
        Player[] players = {
                new Player("Alice", 0),
                new Player("Bob",   1)
        };
        SecretInfluenceViewer.show(players);
    }
}
