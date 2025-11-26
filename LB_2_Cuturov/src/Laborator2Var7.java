/**
 * Laborator 2 – varianta 7.
 * <p>
 * creează ierarhia de grupuri și fire de execuție varianta 7,
 * conform tabelului din enunţ.
 * pornirea firelor se enumeră toate firele active pentru a afișa numele,
 * grupul din care fac parte, prioritatea lor şi prioritatea grupului.
 */
void main() {
    // Grupul principal (rădăcină)
    ThreadGroup mainGroup = new ThreadGroup("Main");
    // Setăm prioritatea maximă a grupului principal (poți adapta după tabelul din enunț)
    mainGroup.setMaxPriority(10);
    // Crearea subgrupurilor conform cerinței:
    // G2 și G1 sunt subgrupuri ale lui Main; G3 este subgrup al lui G1.
    ThreadGroup G2 = new ThreadGroup(mainGroup, "G2");
    G2.setMaxPriority(7);  // adaptează dacă ai alte valori în tabel
    ThreadGroup G1 = new ThreadGroup(mainGroup, "G1");
    G1.setMaxPriority(8);
    ThreadGroup G3 = new ThreadGroup(G1, "G3");
    G3.setMaxPriority(8);
    // Runnable care ține firul activ pentru scurt timp .
    Runnable sleeper = () -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };

    // Firele din grupul Main cu priorităţile indicate.
    Thread ThA = new Thread(mainGroup, sleeper, "ThA");
    ThA.setPriority(3);

    Thread Th11 = new Thread(mainGroup, sleeper, "Th11");
    Th11.setPriority(3);

    Thread Th22 = new Thread(mainGroup, sleeper, "Th22");
    Th22.setPriority(3);

    // Firele din subgrupul G2.
    Thread Th1 = new Thread(G2, sleeper, "Th1");
    Th1.setPriority(5);

    Thread Th2 = new Thread(G2, sleeper, "Th2");
    Th2.setPriority(3);

    Thread Th33 = new Thread(G2, sleeper, "Th33");
    Th33.setPriority(7);

    // Firele din subgrupul G3 (subgrup al lui G1).
    Thread Thaa = new Thread(G3, sleeper, "Thaa");
    Thaa.setPriority(2);

    Thread Thbb = new Thread(G3, sleeper, "Thbb");
    Thbb.setPriority(3);

    Thread Thcc = new Thread(G3, sleeper, "Thcc");
    Thcc.setPriority(8);

    Thread Thdd = new Thread(G3, sleeper, "Thdd");
    Thdd.setPriority(3);
    // Pornirea tuturor firelor.
    ThA.start();
    Th11.start();
    Th22.start();
    Th1.start();
    Th2.start();
    Th33.start();
    Thaa.start();
    Thbb.start();
    Thcc.start();
    Thdd.start();
    // Pauză scurtă pentru a permite firelor să se pornească.
    try {
        Thread.sleep(500);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    // Afișarea priorității fiecărui grup
    displayGroupPriorities(mainGroup, G1, G2, G3);
    // Enumerarea firelor active din grupul principal și subgrupurile sale.
    enumerateGroup(mainGroup);
    // Așteptăm terminarea firelor înainte de ieșire.
    try {
        ThA.join();
        Th11.join();
        Th22.join();
        Th1.join();
        Th2.join();
        Th33.join();
        Thaa.join();
        Thbb.join();
        Thcc.join();
        Thdd.join();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}

/**
 * Afișează prioritatea fiecărui grup din ierarhie.
 */
public static void displayGroupPriorities(ThreadGroup mainGroup, ThreadGroup G1, ThreadGroup G2, ThreadGroup G3) {
    IO.println("\n=== Prioritatea grupurilor ===");
    IO.println("Grup: " + mainGroup.getName() + " - Prioritate (maxPriority): " + mainGroup.getMaxPriority());
    IO.println("Grup: " + G1.getName() + " - Prioritate (maxPriority): " + G1.getMaxPriority() + 
               " (părinte: " + G1.getParent().getName() + ")");
    IO.println("Grup: " + G2.getName() + " - Prioritate (maxPriority): " + G2.getMaxPriority() + 
               " (părinte: " + G2.getParent().getName() + ")");
    IO.println("Grup: " + G3.getName() + " - Prioritate (maxPriority): " + G3.getMaxPriority() + 
               " (părinte: " + G3.getParent().getName() + ")");
    IO.println("================================\n");
}

/**
 * Enumeră recursiv firele active dintr-un grup dat (inclusiv subgrupurile) şi afişează
 * numele firului, grupul, prioritatea firului şi prioritatea grupului.
 * @param group grupul de fire în care se face enumerarea
 */
public static void enumerateGroup(ThreadGroup group) {
    // Estimează numărul de fire active (inclusiv din subgrupuri)
    int estimate = group.activeCount();
    // Punem un mic "buffer" ca să nu tăiem din fire dacă estimate nu e exact
    Thread[] threads = new Thread[estimate * 2 + 10];

    // enumerate(..., true) => ia și firele din subgrupuri
    int actualCount = group.enumerate(threads, true);

    IO.println("=== Enumerare fire pentru grupul rădăcină: "
            + group.getName() +
            " (maxPriority = " + group.getMaxPriority() + ") ===");

    for (int i = 0; i < actualCount; i++) {
        Thread t = threads[i];
        if (t != null) {
            ThreadGroup g = t.getThreadGroup();
            ThreadGroup parent = g.getParent();

            IO.println(
                    "Nume fir: " + t.getName() +
                            ", grup: " + g.getName() +
                            ", prioritate fir: " + t.getPriority() +
                            ", prioritate grup (maxPriority): " + g.getMaxPriority() +
                            (parent != null ? ", grup părinte: " + parent.getName() : "")
            );
        }
    }
}
