import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.locks.ReentrantLock;

public class ProdCons42 {

    // Capacitatea depozitului
    static final int CAPACITY = 6;

    // Numărul total de elemente ce trebuie produse și consumate
    static final int TARGET_TOTAL = 42;

    // Numărul de producători și consumatori
    static final int PRODUCERS = 3;
    static final int CONSUMERS = 3;

    // Faza pară = FILL (producătorii lucrează)
    // Faza impară = DRAIN (consumatorii lucrează)
    static class Depot {

        // Bufferul comun folosit ca depozit
        final Deque<Integer> buffer = new ArrayDeque<>(CAPACITY);

        // Lock pentru sincronizare pe acces la buffer
        // TRUE → fairness (ordinea thread-urilor care așteaptă)
        final ReentrantLock lock = new ReentrantLock(true);

        // Generator random pentru valori produse
        final Random rnd = new Random();

        // Contoare pentru elemente produse și consumate
        int producedTotal = 0;
        int consumedTotal = 0;

        // Semnal că procesul s-a terminat complet
        volatile boolean done = false;

        // Metoda pentru producție — încercare de a produce un element
        boolean tryProduce(String name) {
            lock.lock(); // SINCRONIZARE MUTEX — doar un thread poate modifica bufferul
            try {
                // Dacă s-au produs toate cele 42 obiecte → stop producția
                if (producedTotal >= TARGET_TOTAL) return false;

                // Dacă bufferul este plin → producătorul nu poate produce
                if (buffer.size() == CAPACITY) return false;

                // Creăm un element nou
                int value = 10 + rnd.nextInt(90);

                // Adăugăm elementul în depozit
                buffer.addLast(value);
                producedTotal++;

                // Afișare status
                System.out.printf("%s a produs %d | stoc=%d/%d | totalProd=%d%n",
                        name, value, buffer.size(), CAPACITY, producedTotal);

                // Mesaj când depozitul devine plin
                if (buffer.size() == CAPACITY) {
                    System.out.println(">>> Depozitul e plin (6/6)");
                }

                return true;

            } finally {
                lock.unlock(); // ELIBERAREA LOCK-ULUI
            }
        }

        // Metoda pentru consum — încercare de a consuma un element
        boolean tryConsume(String name) {
            lock.lock(); // SINCRONIZARE MUTEX
            try {
                // Dacă bufferul e gol → nu putem consuma
                if (buffer.isEmpty()) return false;

                // Scoatem un element din buffer
                int value = buffer.removeFirst();
                consumedTotal++;

                // Afișăm statusul
                System.out.printf("%s a consumat %d | stoc=%d/%d | totalCons=%d%n",
                        name, value, buffer.size(), CAPACITY, consumedTotal);

                // Mesaj când depozitul devine gol
                if (buffer.isEmpty()) {
                    System.out.println("<<< Depozitul e gol (0/6)");
                }

                // Dacă am consumat toate cele 42 obiecte, iar bufferul e gol → gata procesul
                if (consumedTotal >= TARGET_TOTAL && buffer.isEmpty()) {
                    done = true;
                }

                return true;

            } finally {
                lock.unlock(); // SINCRONIZARE: eliberarea lock-ului
            }
        }
    }

    // Clasa Producer — un thread producător
    static class Producer extends Thread {
        private final Depot depot;
        private final Phaser phaser;

        Producer(Depot depot, Phaser phaser, String name) {
            super(name);
            this.depot = depot;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                while (!depot.done) { // rulează până procesul este finalizat
                    int phase = phaser.getPhase(); // aflăm faza curentă (FILL sau DRAIN)

                    // FILL → producătorii au voie să lucreze
                    if (phase % 2 == 0) {
                        boolean made;
                        while (!depot.done) {
                            made = depot.tryProduce(getName());
                            if (!made) break;  // dacă depozitul e plin sau am terminat
                        }

                        // Așteaptă ca TOATE thread-urile să termine faza
                        phaser.arriveAndAwaitAdvance();

                    } else {
                        // În faza DRAIN → producătorii stau
                        phaser.arriveAndAwaitAdvance();
                    }
                }
            } finally {
                // De-registrăm thread-ul din Phaser după ce termină
                try { phaser.arriveAndDeregister(); } catch (Exception ignored) {}
            }
        }
    }

    // Clasa Consumer — un thread consumator
    static class Consumer extends Thread {
        private final Depot depot;
        private final Phaser phaser;

        Consumer(Depot depot, Phaser phaser, String name) {
            super(name);
            this.depot = depot;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                while (!depot.done) {
                    int phase = phaser.getPhase(); // aflăm faza curentă

                    // DRAIN → consumatorii au voie să lucreze
                    if (phase % 2 == 1) {
                        boolean took;
                        while (!depot.done) {
                            took = depot.tryConsume(getName());
                            if (!took) break; // depozit gol → stop consum
                        }

                        // Toate thread-urile trebuie să ajungă aici ca să treacă la faza următoare
                        phaser.arriveAndAwaitAdvance();

                    } else {
                        // În faza FILL → consumatorii stau
                        phaser.arriveAndAwaitAdvance();
                    }
                }
            } finally {
                try { phaser.arriveAndDeregister(); } catch (Exception ignored) {}
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Creăm depozitul
        Depot depot = new Depot();

        // Phaser cu 6 participanți: 3 producători + 3 consumatori
        Phaser phaser = new Phaser(PRODUCERS + CONSUMERS);

        // Liste pentru thread-uri
        Thread[] producers = new Thread[PRODUCERS];
        Thread[] consumers = new Thread[CONSUMERS];

        // Creăm producătorii
        for (int i = 0; i < PRODUCERS; i++)
            producers[i] = new Producer(depot, phaser, "Producator-" + (i + 1));

        // Creăm consumatorii
        for (int i = 0; i < CONSUMERS; i++)
            consumers[i] = new Consumer(depot, phaser, "Consumator-" + (i + 1));

        // Pornirea thread-urilor
        for (Thread t : producers) t.start();
        for (Thread t : consumers) t.start();

        // Așteptăm finalizarea tuturor
        for (Thread t : producers) t.join();
        for (Thread t : consumers) t.join();

        // Mesaj final
        System.out.println("\n=== Gata: au fost produse și consumate "
                + TARGET_TOTAL + " obiecte în cicluri FILL→DRAIN. ===");
    }
}
