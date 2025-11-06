import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerGrupeFire {

    // păstrăm toate firele create
    private static final List<Thread> TOATE_FIRELE = new ArrayList<>();
    private static final int PAUZA_FIR_MS = 120;

    // fir pentru modul avansat
    static class FirAvansat extends Thread {
        FirAvansat(ThreadGroup grup, String nume, int prioritate) {
            super(grup, nume);
            setPriority(prioritate);
        }

        @Override
        public void run() {
            System.out.printf("Pornit: %s | Grup: %s | Prio: %d%n",
                    getName(), getThreadGroup().getName(), getPriority());
            try {
                Thread.sleep(PAUZA_FIR_MS);
            } catch (InterruptedException ignored) {}
        }
    }

    // descriere simplă a unui fir
    private static class ConfigFir {
        final ThreadGroup grup;
        final String nume;
        final int prio;
        ConfigFir(ThreadGroup g, String n, int p) {
            grup = g; nume = n; prio = p;
        }
    }

    //creează un fir
    private static Thread creeazaFir(ThreadGroup g, String nume, int prio, boolean avansat) {
        Thread t = avansat ? new FirAvansat(g, nume, prio) : new Thread(g, nume);
        t.setPriority(prio);
        TOATE_FIRELE.add(t);
        return t;
    }

    //ierarhia: Main, G2, G1->G3 și firele
    private static void construiesteIerarhia(boolean avansat) {
        ThreadGroup main = Thread.currentThread().getThreadGroup();
        ThreadGroup g2   = new ThreadGroup("G2");
        ThreadGroup g1   = new ThreadGroup("G1");
        ThreadGroup g3   = new ThreadGroup(g1, "G3");

        List<ConfigFir> lista = List.of(
                new ConfigFir(main, "ThA", 3),
                new ConfigFir(main, "Th11", 3),
                new ConfigFir(main, "Th22", 3),

                new ConfigFir(g2, "Th1", 5),
                new ConfigFir(g2, "Th2", 3),
                new ConfigFir(g2, "Th33", 7),

                new ConfigFir(g3, "Thaa", 2),
                new ConfigFir(g3, "Thbb", 3),
                new ConfigFir(g3, "Thcc", 8),
                new ConfigFir(g3, "Thdd", 3)
        );

        for (ConfigFir c : lista) {
            creeazaFir(c.grup, c.nume, c.prio, avansat);
        }
    }

    // modul avansat
    private static void pornesteFire(boolean avansat) {
        if (!avansat) return;
        for (Thread t : TOATE_FIRELE) t.start();
    }

    // consolă
    private static void afiseazaConsola() {
        System.out.println("=== Fire create ===");
        for (Thread t : TOATE_FIRELE) {
            String g = t.getThreadGroup() != null ? t.getThreadGroup().getName() : "null";
            System.out.printf("%s | %s | %d%n", t.getName(), g, t.getPriority());
        }
    }

    // fereastra cu arborele
    private static void afiseazaGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Varianta 7 - ThreadGroup");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(450, 450);
            f.setLocationRelativeTo(null);

            JTree arbore = creeazaArbore();
            f.add(new JScrollPane(arbore), BorderLayout.CENTER);

            f.setVisible(true);
        });
    }

    private static JTree creeazaArbore() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Main");

        // fire Main
        root.add(new DefaultMutableTreeNode("ThA (3)"));
        root.add(new DefaultMutableTreeNode("Th11 (3)"));
        root.add(new DefaultMutableTreeNode("Th22 (3)"));

        // G2
        DefaultMutableTreeNode nG2 = new DefaultMutableTreeNode("G2");
        nG2.add(new DefaultMutableTreeNode("Th1 (5)"));
        nG2.add(new DefaultMutableTreeNode("Th2 (3)"));
        nG2.add(new DefaultMutableTreeNode("Th33 (7)"));
        root.add(nG2);

        // G1 -> G3
        DefaultMutableTreeNode nG1 = new DefaultMutableTreeNode("G1");
        DefaultMutableTreeNode nG3 = new DefaultMutableTreeNode("G3");
        nG3.add(new DefaultMutableTreeNode("Thaa (2)"));
        nG3.add(new DefaultMutableTreeNode("Thbb (3)"));
        nG3.add(new DefaultMutableTreeNode("Thcc (8)"));
        nG3.add(new DefaultMutableTreeNode("Thdd (3)"));
        nG1.add(nG3);
        root.add(nG1);

        return new JTree(new DefaultTreeModel(root));
    }

    // așteaptă să termine (doar avansat)
    private static void asteapta(boolean avansat) throws InterruptedException {
        if (!avansat) return;
        for (Thread t : TOATE_FIRELE) t.join();
        System.out.println("Gata toate firele.");
    }

    public static void main(String[] args) throws InterruptedException {
        boolean avansat = false;
        for (String a : args)
            if ("--avansat".equalsIgnoreCase(a)) avansat = true;

        construiesteIerarhia(avansat);
        afiseazaConsola();
        pornesteFire(avansat);
        afiseazaGUI();
        asteapta(avansat);
    }
}
