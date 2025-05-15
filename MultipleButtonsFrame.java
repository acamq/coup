import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A turn-based Coup UI with full rules implementation:
 * Income, Foreign Aid, Tax, Assassinate, Steal, Coup,
 * blocks: BlockSteal, BlockAid, BlockAssassination,
 * and a simple logging terminal.
 */
public class MultipleButtonsFrame {
    private static JTextArea terminalArea;

    /** Log messages to the integrated terminal. */
    public static void log(String message) {
        if (terminalArea != null) {
            SwingUtilities.invokeLater(() -> {
                terminalArea.append(message + "\n");
                terminalArea.setCaretPosition(terminalArea.getDocument().getLength());
            });
        }
    }

    /** Launch the UI on the Event Dispatch Thread. */
    public static void showUI(Player[] players) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Coup Game UI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout(5, 5));
            frame.setSize(1200, 800);

            // Terminal
            terminalArea = new JTextArea(8, 50);
            terminalArea.setEditable(false);
            JScrollPane terminalScroll = new JScrollPane(terminalArea,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            // Player panels
            JPanel playersPanel = new JPanel(new GridLayout(players.length, 1, 5, 5));
            List<JPanel> panels = new ArrayList<>();
            List<JLabel> infoLabels = new ArrayList<>();

            // Action buttons and menus
            List<JButton[]> actionBtns = new ArrayList<>();   // income, foreignAid, tax
            List<JButton> assassinateBtns = new ArrayList<>();
            List<JComboBox<Player>> assassinateMenus = new ArrayList<>();
            List<JButton> stealBtns = new ArrayList<>();
            List<JComboBox<Player>> stealMenus = new ArrayList<>();
            List<JButton> coupBtns = new ArrayList<>();
            List<JComboBox<Player>> coupMenus = new ArrayList<>();
            List<JButton> blockStealBtns = new ArrayList<>();
            List<JButton> blockAidBtns = new ArrayList<>();
            List<JButton> blockAssassinateBtns = new ArrayList<>();

            // State tracking
            AtomicInteger currentIndex = new AtomicInteger(0);
            AtomicInteger lastStealer = new AtomicInteger(-1);
            AtomicInteger lastVictim = new AtomicInteger(-1);
            AtomicInteger lastForeignAider = new AtomicInteger(-1);
            AtomicBoolean aidBlocked = new AtomicBoolean(false);
            AtomicInteger lastAssassin = new AtomicInteger(-1);
            AtomicInteger lastAssasVictim = new AtomicInteger(-1);
            AtomicBoolean assassinationBlocked = new AtomicBoolean(false);

            for (int i = 0; i < players.length; i++) {
                final int idx = i;
                Player p = players[i];

                JPanel panel = new JPanel(new BorderLayout(5, 5));
                panel.setBorder(BorderFactory.createTitledBorder(p.getName()));
                JLabel info = new JLabel();
                panel.add(info, BorderLayout.CENTER);

                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 5));
                JButton income = new JButton("Income");
                JButton foreignAid = new JButton("Foreign Aid");
                JButton tax = new JButton("Tax");

                JButton assassinate = new JButton("Assassinate");
                JComboBox<Player> assassinateMenu = new JComboBox<>();
                JButton steal = new JButton("Steal");
                JComboBox<Player> stealMenu = new JComboBox<>();
                JButton coup = new JButton("Coup");
                JComboBox<Player> coupMenu = new JComboBox<>();

                JButton blockSteal = new JButton("Block Steal");
                JButton blockAid = new JButton("Block Aid");
                JButton blockAssassination = new JButton("Block Assassination");

                // Populate menus
                for (Player o : players) {
                    if (o != p) {
                        assassinateMenu.addItem(o);
                        stealMenu.addItem(o);
                        coupMenu.addItem(o);
                    }
                }

                btnPanel.add(income);
                btnPanel.add(foreignAid);
                btnPanel.add(tax);
                btnPanel.add(assassinateMenu);
                btnPanel.add(assassinate);
                btnPanel.add(stealMenu);
                btnPanel.add(steal);
                btnPanel.add(coupMenu);
                btnPanel.add(coup);
                btnPanel.add(blockSteal);
                btnPanel.add(blockAid);
                btnPanel.add(blockAssassination);
                panel.add(btnPanel, BorderLayout.SOUTH);

                panels.add(panel);
                infoLabels.add(info);
                actionBtns.add(new JButton[]{income, foreignAid, tax});
                assassinateBtns.add(assassinate);
                assassinateMenus.add(assassinateMenu);
                stealBtns.add(steal);
                stealMenus.add(stealMenu);
                coupBtns.add(coup);
                coupMenus.add(coupMenu);
                blockStealBtns.add(blockSteal);
                blockAidBtns.add(blockAid);
                blockAssassinateBtns.add(blockAssassination);
                playersPanel.add(panel);

                ActionListener al = e -> {
                    Object src = e.getSource();
                    boolean blockAction = false;
                    Player current = players[idx];

                    // Must Coup if coins>=10
                    if (current.mustCoup()) {
                        Player t = (Player)coupMenu.getSelectedItem();
                        current.coup(t);
                        log(current.getName()+" coups " + t.getName());
                    } else {
                        if (src == income) {
                            current.income(); log(current.getName()+" uses Income");
                        } else if (src == foreignAid) {
                            current.foreignAid();
                            lastForeignAider.set(idx);
                            aidBlocked.set(false);
                            log(current.getName()+" uses Foreign Aid");
                        } else if (src == tax) {
                            current.tax(); log(current.getName()+" uses Tax");
                        } else if (src == assassinate) {
                            Player t = (Player)assassinateMenu.getSelectedItem();
                            current.assassinate(t);
                            lastAssassin.set(idx);
                            lastAssasVictim.set(java.util.Arrays.asList(players).indexOf(t));
                            assassinationBlocked.set(false);
                            log(current.getName()+" assassinates " + t.getName());
                        } else if (src == steal) {
                            Player t = (Player)stealMenu.getSelectedItem();
                            current.steal(t);
                            lastStealer.set(idx);
                            lastVictim.set(java.util.Arrays.asList(players).indexOf(t));
                            log(current.getName()+" steals from " + t.getName());
                        } else if (src == coup) {
                            Player t = (Player)coupMenu.getSelectedItem();
                            current.coup(t); log(current.getName()+" coups " + t.getName());
                        } else if (src == blockSteal) {
                            int thief = lastStealer.get(), victim = lastVictim.get();
                            current.blockSteal(players[victim], players[thief]);
                            log(current.getName()+" blocks steal: " + players[thief].getName()+"->"+players[victim].getName());
                            blockAction = true;
                        } else if (src == blockAid) {
                            if (!aidBlocked.get()) {
                                int fa = lastForeignAider.get();
                                if (fa >= 0) {
                                    current.blockAid(players[fa]);
                                    log(current.getName()+" blocks foreign aid by " + players[fa].getName());
                                    aidBlocked.set(true);
                                    blockAction = true;
                                }
                            }
                        } else if (src == blockAss assassination) {
                            if (!assassinationBlocked.get()) {
                                int asn = lastAssassin.get(), vic = lastAssasVictim.get();
                                current.blockAssassination(players[vic], players[asn]);
                                log(current.getName()+" blocks assassination: " + players[asn].getName()+"->"+players[vic].getName());
                                assassinationBlocked.set(true);
                                blockAction = true;
                            }
                        }
                    }

                    if (!blockAction) {
                        currentIndex.set((currentIndex.get()+1)%players.length);
                    }
                    refreshUI(players, currentIndex.get(), lastStealer.get(), lastVictim.get(),
                            lastForeignAider.get(), aidBlocked.get(), lastAssassin.get(),
                            lastAssasVictim.get(), assassinationBlocked.get(),
                            panels, infoLabels, actionBtns, assassinateBtns,
                            assassinateMenus, stealBtns, stealMenus, coupBtns, coupMenus,
                            blockStealBtns, blockAidBtns, blockAssassinateBtns);
                };

                income.addActionListener(al);
                foreignAid.addActionListener(al);
                tax.addActionListener(al);
                assassinate.addActionListener(al);
                steal.addActionListener(al);
                coup.addActionListener(al);
                blockSteal.addActionListener(al);
                blockAid.addActionListener(al);
                blockAssassination.addActionListener(al);
            }

            frame.add(playersPanel, BorderLayout.CENTER);
            frame.add(terminalScroll, BorderLayout.SOUTH);
            frame.setVisible(true);

            refreshUI(players, 0, -1, -1, -1, false, -1, -1, false,
                    panels, infoLabels, actionBtns, assassinateBtns,
                    assassinateMenus, stealButtons, stealMenus,
                    coupButtons, coupMenus, blockStealButtons,
                    blockAidButtons, blockAssassinateButtons);
        });
    }

    /** Update UI enabling/disabling and highlights. */
    private static void refreshUI(Player[] players, int cur, int lastStealer, int lastVictim,
                                  int lastAid, boolean aidBlocked, int lastAssassin, int lastAsVictim,
                                  boolean assassinBlocked,
                                  List<JPanel> panels, List<JLabel> infos,
                                  List<JButton[]> acts, List<JButton> asBtns,
                                  List<JComboBox<Player>> asMenus, List<JButton> stBtns,
                                  List<JComboBox<Player>> stMenus, List<JButton> coupBtns,
                                  List<JComboBox<Player>> coupMenus, List<JButton> bSteal,
                                  List<JButton> bAid, List<JButton> bAs) {
        boolean forced = players[cur].mustCoup();
        for (int i=0;i<players.length;i++){
            boolean isCur=(i==cur);
            infos.get(i).setText(players[i].getInfo());
            boolean canAs = players[i].canAssassinate();
            boolean canCoup = players[i].canCoup();
            boolean canSteal = true;
            boolean canBlockSteal = (lastStealer>=0 && i!=lastStealer);
            boolean canBlockAid = (lastAid>=0 && i!=lastAid && !aidBlocked);
            boolean canBlockAs = (lastAssassin>=0 && i!=lastAssassin && !assassinBlocked);

            if(forced){
                for(JButton b:acts.get(i))b.setEnabled(false);
                asBtns.get(i).setEnabled(false); asMenus.get(i).setEnabled(false);
                stBtns.get(i).setEnabled(false); stMenus.get(i).setEnabled(false);
                bSteal.get(i).setEnabled(false); bAid.get(i).setEnabled(false);
                bAs.get(i).setEnabled(false);
                coupBtns.get(i).setEnabled(isCur); coupMenus.get(i).setEnabled(isCur);
            } else {
                for(JButton b:acts.get(i))b.setEnabled(isCur);
                asBtns.get(i).setEnabled(isCur && canAs); asMenus.get(i).setEnabled(isCur && canAs);
                stBtns.get(i).setEnabled(isCur && canSteal); stMenus.get(i).setEnabled(isCur && canSteal);
                coupBtns.get(i).setEnabled(isCur && canCoup); coupMenus.get(i).setEnabled(isCur && canCoup);
                bSteal.get(i).setEnabled(canBlockSteal);
                bAid.get(i).setEnabled(canBlockAid);
                bAs.get(i).setEnabled(canBlockAs);
            }
            TitledBorder br=(TitledBorder)panels.get(i).getBorder();
            br.setTitleColor(isCur?Color.BLUE:Color.BLACK);
            panels.get(i).repaint();
        }
    }
}
