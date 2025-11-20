/**
 * Laborator 2 – varianta 7.
 * <p>
 * Acest program creează ierarhia de grupuri și fire de execuție cerută de varianta 7 ,
 * conform tabelului din enunţ. Nu se suprascrie metoda {run()}, iar după
 * pornirea firelor se enumeră toate firele active pentru a afișa numele,
 * grupul din care fac parte şi prioritatea lor.
 */
void main() {
    ThreadGroup mainGroup = new ThreadGroup("Main");
    // Crearea subgrupurilor conform cerinței: G2 și G1 sunt subgrupuri ale lui Main;
    // G3 este subgrup al lui G1.
    ThreadGroup G2 = new ThreadGroup(mainGroup, "G2");
    ThreadGroup G1 = new ThreadGroup(mainGroup, "G1");
    ThreadGroup G3 = new ThreadGroup(G1, "G3");
    // Runnable care ține firul activ pentru scurt timp (fără a suprascrie run()).
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
 * Enumeră recursiv firele active dintr-un grup dat (inclusiv subgrupurile) şi afişează
 * numele firului, grupul şi prioritatea.
 *
 * @param group grupul de fire în care se face enumerarea
 */
public static void enumerateGroup(ThreadGroup group) {
    int estimate = group.activeCount();
    Thread[] threads = new Thread[estimate];
    int actualCount = group.enumerate(threads, true);
    for (int i = 0; i < actualCount; i++) {
        Thread t = threads[i];
        if (t != null) {
            IO.println("Nume fir: " + t.getName() +
                    ", grup: " + t.getThreadGroup().getName() +
                    ", prioritate: " + t.getPriority());
        }
    }
}