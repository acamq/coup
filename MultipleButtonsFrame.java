import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MultipleButtonsFrame extends JFrame {
    // Constructor to set up the UI
    public MultipleButtonsFrame() {
        setTitle("Multiple Buttons UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Center on screen

        // Create a panel to hold buttons
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        // Create buttons
        JButton button1 = new JButton("Action 1");
        JButton button2 = new JButton("Action 2");
        JButton button3 = new JButton("Action 3");

        // Add action listeners to buttons
        button1.addActionListener(e -> doAction1());
        button2.addActionListener(e -> doAction2());
        button3.addActionListener(e -> doAction3());

        // Add buttons to panel
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);

        // Add panel to frame
        add(panel);
    }

    // Method called by button 1
    private void doAction1() {
        JOptionPane.showMessageDialog(this, "Action 1 performed!");
    }

    // Method called by button 2
    private void doAction2() {
        JOptionPane.showMessageDialog(this, "Action 2 performed!");
    }

    // Method called by button 3
    private void doAction3() {
        JOptionPane.showMessageDialog(this, "Action 3 performed!");
    }

    /**
     * Static helper to launch the frame from any other class.
     */
    public static void showUI() {
        SwingUtilities.invokeLater(() -> {
            MultipleButtonsFrame frame = new MultipleButtonsFrame();
            frame.setVisible(true);
        });
    }

    // Main method to launch the UI standalone
    public static void main(String[] args) {
        showUI();
    }
}
