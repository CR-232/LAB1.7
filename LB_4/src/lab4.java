import java.util.ArrayList;
import java.util.Random;

public class lab4 {
    public static void main(String[] args) throws InterruptedException {

        Storage storage = new Storage();

        Producer Serafim_p1 = new Producer(storage, "Producător 1: Serafim");
        Producer Serafim_p2 = new Producer(storage, "Producător 2: Serafim");
        Producer Serafim_p3 = new Producer(storage, "Producător 3: Serafim");

        Consumer Cuturov_c1 = new Consumer(storage, "Consumator 1: Oleg");
        Consumer Cuturov_c2 = new Consumer(storage, "Consumator 2: Oleg");
        Consumer Cuturov_c3 = new Consumer(storage, "Consumator 3: Oleg");

        Serafim_p1.setDaemon(true);
        Serafim_p2.setDaemon(true);
        Serafim_p3.setDaemon(true);

        Serafim_p1.start();
        Serafim_p2.start();
        Serafim_p3.start();

        Cuturov_c1.start();
        Cuturov_c2.start();
        Cuturov_c3.start();

        // Așteptăm consumatorii să termine
        while (Cuturov_c1.isAlive() || Cuturov_c2.isAlive() || Cuturov_c3.isAlive()) {}

        System.out.println("\nToți consumatorii au fost îndestulați cu 5 vocale!");
    }
}

class   Storage {
    private final ArrayList<Character> depozit = new ArrayList<>();
    private final int MAX_SIZE = 6;
    private final char[] VOCALE = {'A','E','I','O','U'};

    private final Random rnd = new Random();

    // Consumatorul preia o vocală
    public synchronized char get(String consumerName) {
        while (depozit.size() == 0) {
            System.out.println("Atenție! " + consumerName + ": Depozitul este gol → așteaptă...");
            try { wait(); } catch (InterruptedException ignored) {} //Asteapta Oleg pana producatoru o sa puna vocala in dipozit
        }

        char vowel = depozit.remove(depozit.size() - 1);
        System.out.println(consumerName + " a consumat vocala: " + vowel);//am luat vocala

        afiseazaDepozit();

        notifyAll();//heeeeeeey! serafim! eu am luat o vocala!
        return vowel;
    }

    // Producătorul produce 2 vocale
    public synchronized void put(String producerName) {
        while (depozit.size() >= MAX_SIZE) {
            System.out.println("Atenție! " + producerName + ": Depozitul este plin → așteaptă...");
            try { wait(); } catch (InterruptedException ignored) {}
        }

        // Produce 2 vocale aleatorii
        char v1 = VOCALE[rnd.nextInt(VOCALE.length)];
        char v2 = VOCALE[rnd.nextInt(VOCALE.length)];

        depozit.add(v1);
        depozit.add(v2);

        System.out.println(producerName + " a produs vocalele: " + v1 + ", " + v2);

        afiseazaDepozit();

        notifyAll();
    }

    private void afiseazaDepozit() {
        if (depozit.size() == 0) {
            System.out.println("Depozitul este gol.");
            return;
        }

        System.out.print("Depozitul conține " + depozit.size() + " vocale → ");
        for (char v : depozit) System.out.print(v + " ");
        System.out.println();
    }
}

class Producer extends Thread {
    private final Storage storage;
    private final String name;

    public Producer(Storage s, String name) {
        this.storage = s;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            storage.put(name);
            try {
                sleep(200);
            } catch (InterruptedException ignored) {}
        }
    }
}

class Consumer extends Thread {
    private final Storage storage;
    private final String name;

    public Consumer(Storage s, String name) {
        this.storage = s;
        this.name = name;
    }

    @Override
    public void run() {
        int consumed = 0;

        while (consumed < 5) {
            storage.get(name);
            consumed++;

            try {
                sleep(300);
            } catch (InterruptedException ignored) {}
        }

        System.out.println("Succes " + name + " a consumat 5 vocale și s-a terminat.");
    }
}
