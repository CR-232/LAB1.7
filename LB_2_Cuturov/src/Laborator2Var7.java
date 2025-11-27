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
    displayGroupPriorities(mainGroup, G1, G2, G3, ThA, Th11, Th22, Th1, Th2, Th33, Thaa, Thbb, Thcc, Thdd);
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
 * Afișează prioritatea fiecărui grup din ierarhie în același format ca enumerarea firelor.
 */
public static void displayGroupPriorities(ThreadGroup mainGroup, ThreadGroup G1, ThreadGroup G2, ThreadGroup G3,
                                         Thread ThA, Thread Th11, Thread Th22, Thread Th1, Thread Th2, Thread Th33,
                                         Thread Thaa, Thread Thbb, Thread Thcc, Thread Thdd) {
    IO.println("\n=== Prioritatea grupurilor ===");
    
    // Main grup (rădăcină) - nu are părinte
    IO.println("Nume grup: " + mainGroup.getName() + 
               ", prioritate grup (maxPriority): " + mainGroup.getMaxPriority());
    
    // G2 subgrup
    ThreadGroup G2Parent = G2.getParent();
    IO.println("Nume grup: " + G2.getName() + 
               ", prioritate grup (maxPriority): " + G2.getMaxPriority() + 
               (G2Parent != null ? ", grup părinte: " + G2Parent.getName() : ""));
    
    // G1 subgrup
    ThreadGroup G1Parent = G1.getParent();
    IO.println("Nume grup: " + G1.getName() + 
               ", prioritate grup (maxPriority): " + G1.getMaxPriority() + 
               (G1Parent != null ? ", grup părinte: " + G1Parent.getName() : ""));
    
    // G3 subgrup (subgrup al lui G1)
    ThreadGroup G3Parent = G3.getParent();
    IO.println("Nume grup: " + G3.getName() + 
               ", prioritate grup (maxPriority): " + G3.getMaxPriority() + 
               (G3Parent != null ? ", grup părinte: " + G3Parent.getName() : ""));
    
    IO.println();
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
