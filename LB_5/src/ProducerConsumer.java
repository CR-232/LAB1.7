/**
 * Implementare varianta 7: Producer-Consumer cu vocale
 * Autori: Cuturov Oleg și Costriba Serafim
 * <p>
 * Parametri:
 * - X = 3 producători ( Costriba Serefim )
 * - Y = 3 consumatori  ( Cuturov Oleg )
 * - Z = 5 obiecte per consumator
 * - D = 6 (capacitate buffer)
 * - Tip obiecte: Vocale (A, E, I, O, U)
 * - F = 2 obiecte per producător
 */
private static final int PRODUCER_COUNT = 3;  // X
private static final int CONSUMER_COUNT = 3;  // Y
private static final int CONSUMER_GOAL = 5;   // Z
private static final int BUFFER_CAPACITY = 6; // D
private static final int OBJECTS_PER_PRODUCER = 2; // F

// Buffer comun folosind BlockingQueue
private static final BlockingQueue<Character> buffer =
        new ArrayBlockingQueue<>(BUFFER_CAPACITY);

// Contoare atomice pentru sincronizare
private static final AtomicInteger totalProduced = new AtomicInteger(0);
private static final AtomicInteger totalConsumed = new AtomicInteger(0);

// Contoare per consumator
private static final Map<Integer, AtomicInteger> consumerCounters = new HashMap<>();

// Vocalele disponibile
private static final char[] vowels = {'A', 'E', 'I', 'O', 'U'};

void main() {
    IO.println("========== PRODUCER-CONSUMER VARIANT 7 ==========");
    IO.println("Producători ( Costriba Serafim ): " + PRODUCER_COUNT);
    IO.println("Consumatori ( Cuturov Oleg ): " + CONSUMER_COUNT);
    IO.println("Obiecte per consumator: " + CONSUMER_GOAL);
    IO.println("Capacitate buffer: " + BUFFER_CAPACITY);
    IO.println("Tip obiecte: Vocale (A, E, I, O, U)");
    IO.println("Obiecte per producător: " + OBJECTS_PER_PRODUCER);
    IO.println("===============================================\n");

    // Inițializăm contoarele pentru fiecare consumator
    for (int i = 1; i <= CONSUMER_COUNT; i++) {
        consumerCounters.put(i, new AtomicInteger(0));
    }

    // Creăm ExecutorService cu pool fix de thread-uri
    ExecutorService executor = Executors.newFixedThreadPool(
            PRODUCER_COUNT + CONSUMER_COUNT
    );

    // Pornim producătorii
    for (int i = 1; i <= PRODUCER_COUNT; i++) {
        executor.execute(new Producer(i));
    }

    // Pornim consumatorii
    for (int i = 1; i <= CONSUMER_COUNT; i++) {
        executor.execute(new Consumer(i));
    }

    // Închidem executorul și așteptăm terminarea
    executor.shutdown();
    try {
        // Așteptăm ca toate thread-urile să se termine
        executor.awaitTermination(60, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Raport final
    IO.println("\n========== RAPORT FINAL ==========");
    IO.println("Total produse: " + totalProduced.get());
    IO.println("Total consumate: " + totalConsumed.get());
    consumerCounters.forEach((id, count) ->
            IO.println("Consumator " + id + ": " + count.get() +
                    " obiecte consumate"));
    IO.println("==================================");
}

// =================================
// CLASA PRODUCĂTOR
// =================================
static class Producer implements Runnable {
    private final int id;
    private final Random random = new Random();

    Producer(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            // Continuă producerea până când toți consumatorii sunt satisfăcuți
            while (totalConsumed.get() < CONSUMER_COUNT * CONSUMER_GOAL) {
                // Verifică dacă buffer-ul este plin
                if (buffer.remainingCapacity() == 0) {
                    IO.println("Δ [Producător " + id + "] Depozitul e plin, așteaptă...");
                }

                // Produce F=2 obiecte de fiecare dată
                for (int i = 0; i < OBJECTS_PER_PRODUCER; i++) {
                    // Verifică dacă mai sunt necesare obiecte
                    if (totalConsumed.get() >= CONSUMER_COUNT * CONSUMER_GOAL) {
                        break;
                    }

                    // Generează o vocală aleatorie
                    char item = vowels[random.nextInt(vowels.length)];

                    // Adaugă în buffer (blochează dacă buffer-ul e plin)
                    buffer.put(item);
                    int produced = totalProduced.incrementAndGet();

                    IO.println("+ [Producător " + id + "] a produs: " +
                            item + " | Total produse: " + produced +
                            " | Capacitate curentă: " + buffer.size() + "/" +
                            BUFFER_CAPACITY);
                }

                // Pauză mică pentru a permite consumatorilor să lucreze
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
            // Continuă consumarea până când consumă Z=5 obiecte
            while (consumerCounters.get(id).get() < CONSUMER_GOAL) {
                // Verifică dacă buffer-ul este gol
                if (buffer.isEmpty()) {
                    IO.println("Δ [Consumator " + id + "] Depozitul e gol, așteaptă...");
                }

                // Extrage din buffer (blochează dacă buffer-ul e gol)
                char item = buffer.take();

                // Incrementează contoarele
                int consumed = consumerCounters.get(id).incrementAndGet();
                int total = totalConsumed.incrementAndGet();

                IO.println("- [Consumator " + id + "] a consumat: " +
                        item + " | Consumat de acest consumator: " + consumed + "/" +
                        CONSUMER_GOAL + " | Total consumate: " + total +
                        " | Capacitate curentă: " + buffer.size() + "/" +
                        BUFFER_CAPACITY);

                // Pauză mică pentru simularea procesării
                Thread.sleep(150);
            }
            IO.println("✓ Consumatorul " + id + " s-a finalizat (a consumat " +
                    CONSUMER_GOAL + " obiecte)");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

