import java.io.IO;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementare varianta 7: Producer-Consumer cu vocale
 * Autori: Cuturov Oleg și Costriba Serafim
 *
 * Parametri:
 * - X = 3 producători ( Costriba Serefim )
 * - Y = 3 consumatori  ( Cuturov Oleg )
 * - Z = 5 obiecte per consumator
 * - D = 6 (capacitate buffer)
 * - Tip obiecte: Vocale (A, E, I, O, U)
 * - F = 2 obiecte per producător
 */
public class ProducerConsumer {

    private static final int PRODUCER_COUNT = 3;  // X
    private static final int CONSUMER_COUNT = 3;  // Y
    private static final int CONSUMER_GOAL = 5;   // Z
    private static final int BUFFER_CAPACITY = 6; // D
    private static final int OBJECTS_PER_PRODUCER = 2; // F

    private static final BlockingQueue<Character> buffer =
            new ArrayBlockingQueue<>(BUFFER_CAPACITY);

    private static final AtomicInteger totalProduced = new AtomicInteger(0);
    private static final AtomicInteger totalConsumed = new AtomicInteger(0);

    private static final Map<Integer, AtomicInteger> consumerCounters = new HashMap<>();

    private static final char[] vowels = {'A', 'E', 'I', 'O', 'U'};

    public static void main(String[] args) {
        IO.println("========== PRODUCER-CONSUMER VARIANT 7 ==========");
        IO.println("Producători (Costriba Serafim): " + PRODUCER_COUNT);
        IO.println("Consumatori (Cuturov Oleg): " + CONSUMER_COUNT);
        IO.println("Obiecte per consumator: " + CONSUMER_GOAL);
        IO.println("Capacitate buffer: " + BUFFER_CAPACITY);
        IO.println("Tip obiecte: Vocale (A, E, I, O, U)");
        IO.println("Obiecte per producător: " + OBJECTS_PER_PRODUCER);
        IO.println("===============================================\n");

        for (int i = 1; i <= CONSUMER_COUNT; i++) {
            consumerCounters.put(i, new AtomicInteger(0));
        }

        ExecutorService executor = Executors.newFixedThreadPool(
                PRODUCER_COUNT + CONSUMER_COUNT
        );

        for (int i = 1; i <= PRODUCER_COUNT; i++) {
            executor.execute(new Producer(i));
        }

        for (int i = 1; i <= CONSUMER_COUNT; i++) {
            executor.execute(new Consumer(i));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        IO.println("\n========== RAPORT FINAL ==========");
        IO.println("Total produse: " + totalProduced.get());
        IO.println("Total consumate: " + totalConsumed.get());
        consumerCounters.forEach((id, count) ->
                IO.println("Consumator " + id + ": " + count.get() +
                        " obiecte consumate"));
        IO.println("==================================");
    }


    static class Producer implements Runnable {
        private final int id;
        private final Random random = new Random();

        Producer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (totalConsumed.get() < CONSUMER_COUNT * CONSUMER_GOAL) {

                    if (buffer.remainingCapacity() == 0) {
                        IO.println("Δ [Producător " + id + "] Depozitul e plin, așteaptă...");
                    }

                    // GENERĂM DOUĂ VOCALE DISTINCTE
                    char v1 = vowels[random.nextInt(vowels.length)];
                    char v2;
                    do {
                        v2 = vowels[random.nextInt(vowels.length)];
                    } while (v2 == v1);

                    // Dacă nu mai este nevoie, ieșim
                    if (totalConsumed.get() >= CONSUMER_COUNT * CONSUMER_GOAL) {
                        break;
                    }

                    // Adăugăm în buffer cele două vocale
                    buffer.put(v1);
                    buffer.put(v2);

                    // Incrementăm totalul produs cu 2
                    int produced = totalProduced.addAndGet(2);

                    // Afișăm PERECHEA pe un singur rând
                    IO.println("+ [Producător " + id + "] a produs: "
                            + v1 + " " + v2
                            + " | Total produse: " + produced
                            + " | Capacitate: " + buffer.size() + "/" + BUFFER_CAPACITY);

                    Thread.sleep(100);
                }

                IO.println("✓ Producătorul " + id + " s-a finalizat");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // =================================
    // CLASA CONSUMATOR
    // =================================
    static class Consumer implements Runnable {
        private final int id;

        Consumer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (consumerCounters.get(id).get() < CONSUMER_GOAL) {

                    if (buffer.isEmpty()) {
                        IO.println("Δ [Consumator " + id + "] Depozitul e gol, așteaptă...");
                    }

                    char item = buffer.take();

                    int consumed = consumerCounters.get(id).incrementAndGet();
                    int total = totalConsumed.incrementAndGet();

                    IO.println("- [Consumator " + id + "] a consumat: " +
                            item + " | Consumat de acest consumator: " +
                            consumed + "/" + CONSUMER_GOAL +
                            " | Total consumate: " + total +
                            " | Capacitate: " + buffer.size() + "/" + BUFFER_CAPACITY);

                    Thread.sleep(150);
                }

                IO.println("✓ Consumatorul " + id + " s-a finalizat (a consumat " +
                        CONSUMER_GOAL + " obiecte)");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
