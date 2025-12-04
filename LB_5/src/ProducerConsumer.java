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
 * - X = 3 producători (Costriba Serefim)
 * - Y = 3 consumatori (Cuturov Oleg)
 * - Z = 5 obiecte per consumator
 * - D = 6 (capacitate buffer)
 * - Tip obiecte: Vocale (A, E, I, O, U)
 * - F = 2 obiecte per producător
 */
public class ProducerConsumer {

    private static final int PRODUCER_COUNT = 3;
    private static final int CONSUMER_COUNT = 3;
    private static final int CONSUMER_GOAL = 5;
    private static final int BUFFER_CAPACITY = 6;
    private static final int OBJECTS_PER_PRODUCER = 2;

    private static final BlockingQueue<Character> buffer =
            new ArrayBlockingQueue<>(BUFFER_CAPACITY);

    private static final AtomicInteger totalProduced = new AtomicInteger(0);
    private static final AtomicInteger totalConsumed = new AtomicInteger(0);

    private static final Map<Integer, AtomicInteger> consumerCounters = new HashMap<>();

    private static final char[] vowels = {'A', 'E', 'I', 'O', 'U'};

    public static void main(String[] args) {

        System.out.println("========== PRODUCER-CONSUMER VARIANT 7 ==========");
        System.out.println("Producători (Costriba Serafim): " + PRODUCER_COUNT);
        System.out.println("Consumatori (Cuturov Oleg): " + CONSUMER_COUNT);
        System.out.println("Obiecte per consumator: " + CONSUMER_GOAL);
        System.out.println("Capacitate buffer: " + BUFFER_CAPACITY);
        System.out.println("Tip obiecte: Vocale (A, E, I, O, U)");
        System.out.println("Obiecte per producător: " + OBJECTS_PER_PRODUCER);
        System.out.println("===============================================\n");

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

        System.out.println("\n========== RAPORT FINAL ==========");
        System.out.println("Total produse: " + totalProduced.get());
        System.out.println("Total consumate: " + totalConsumed.get());
        consumerCounters.forEach((id, count) ->
                System.out.println("Consumator " + id + ": " + count.get() + " obiecte consumate"));
        System.out.println("==================================");
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
                        System.out.println("Δ [Producător " + id + "] Depozitul e plin, așteaptă...");
                    }

                    // GENERĂM DOUĂ VOCALE DISTINCTE
                    char v1 = vowels[random.nextInt(vowels.length)];
                    char v2;
                    do {
                        v2 = vowels[random.nextInt(vowels.length)];
                    } while (v2 == v1);

                    if (totalConsumed.get() >= CONSUMER_COUNT * CONSUMER_GOAL) {
                        break;
                    }

                    buffer.put(v1);
                    buffer.put(v2);

                    int produced = totalProduced.addAndGet(2);

                    System.out.println("+ [Producător " + id + "] a produs: "
                            + v1 + " " + v2
                            + " | Total produse: " + produced
                            + " | Capacitate: " + buffer.size() + "/" + BUFFER_CAPACITY);

                    Thread.sleep(100);
                }

                System.out.println("✓ Producătorul " + id + " s-a finalizat");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


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
                        System.out.println("Δ [Consumator " + id + "] Depozitul e gol, așteaptă...");
                    }
                    char item = buffer.take();

                    int consumed = consumerCounters.get(id).incrementAndGet();
                    int total = totalConsumed.incrementAndGet();

                    System.out.println("- [Consumator " + id + "] a consumat: " +
                            item + " | Consumat de acest consumator: " +
                            consumed + "/" + CONSUMER_GOAL +
                            " | Total consumate: " + total +
                            " | Capacitate: " + buffer.size() + "/" + BUFFER_CAPACITY);

                    Thread.sleep(150);
                }

                System.out.println("✓ Consumatorul " + id + " s-a finalizat (a consumat "
                        + CONSUMER_GOAL + " obiecte)");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
