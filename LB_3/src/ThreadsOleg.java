public class ThreadsOleg {

    // Task-ul 3 – rulează pe Thread-3
    public static class Task3 implements Runnable {
        @Override
        public void run() {
            // Metodă din clasa Thread: obține referința la thread-ul curent
            Thread currentThread = Thread.currentThread();

            // Metode din Thread folosite pentru informații despre thread
            System.out.println("=== TH3 – Cuturov Oleg ===");
            System.out.println("Thread name: " + currentThread.getName()); // Thread.getName()
            System.out.println("Thread ID: " + currentThread.getId());     // Thread.getId()
            System.out.println("Rol: de parcurs de la ÎNCEPUT intervalul [234, 1000]");
            System.out.println("Th3: Parcurgere DE LA ÎNCEPUT intervalul [234, 1000]");

            // Preluăm tabloul generat în clasa principală LaboratorThreadsMain
            int[] array1 = LaboratorThreadsMain.array1; // valori în intervalul [234, 1000]
            int countInLine = 0; // contor pentru a rupe linia după un anumit număr de elemente

            try {
                // Parcurgem tabloul de la început la sfârșit
                for (int i = 0; i < array1.length; i++) {

                    // Metodă din Thread: verificăm dacă thread-ul a fost întrerupt
                    if (currentThread.isInterrupted()) {
                        System.out.println("\nTh3: Thread întrerupt înainte de final.");
                        // Metodă din Thread: re-setăm flag-ul de întrerupere
                        currentThread.interrupt();
                        return; // ieșim din run()
                    }

                    // Afișăm elementul curent, prefixat cu "Th3:"
                    System.out.print("Th3:" + array1[i] + " ");
                    countInLine++;

                    // La fiecare 25 de elemente trecem pe o linie nouă pentru lizibilitate
                    if (countInLine == 25) {
                        System.out.println();
                        countInLine = 0;
                    }

                    // Metodă din Thread: punem thread-ul să "doarmă" 2 ms
                    Thread.sleep(2);
                }
            } catch (InterruptedException e) {
                // Dacă sleep este întrerupt, ajungem aici
                System.out.println("\nTh3: Sleep întrerupt!");
                // Metodă din Thread: refacem flag-ul de întrerupere
                currentThread.interrupt();
                return;
            }

            // Mesaj final cu numărul de elemente prelucrate
            System.out.println("\n>>> Th3 FINAL: " + array1.length +
                    " elemente procesate [realizat de: Cuturov Oleg]");

            // Semnalăm în clasa principală că acest thread și-a terminat treaba
            LaboratorThreadsMain.threadFinished();

            // Așteptăm terminarea tuturor thread-urilor (logica este în LaboratorThreadsMain)
            LaboratorThreadsMain.waitForAllThreads();

            // Afișăm textul în ordinea stabilită (Thread-3 are poziția lui în DISPLAY_ORDER)
            LaboratorThreadsMain.displayInOrder("Thread-3", LaboratorThreadsMain.GRUPA);
        }
    }

    // Task-ul 4 – rulează pe Thread-4
    public static class Task4 implements Runnable {
        @Override
        public void run() {
            // Metodă din clasa Thread: obține referința la thread-ul curent
            Thread currentThread = Thread.currentThread();

            // Metode din Thread folosite pentru informații despre thread
            System.out.println("=== TH4 – Cuturov Oleg ===");
            System.out.println("Thread name: " + currentThread.getName()); // Thread.getName()
            System.out.println("Thread ID: " + currentThread.getId());     // Thread.getId()
            System.out.println("Rol: de parcurs de la SFÂRȘIT intervalul [456, 1234]");
            System.out.println("Th4: Parcurgere DE LA SFÂRȘIT intervalul [456, 1234]");

            // Preluăm al doilea tablou, generat în clasa principală
            int[] array2 = LaboratorThreadsMain.array2; // valori în intervalul [456, 1234]
            int countInLine = 0; // contor pentru formatat afișarea

            try {
                // Parcurgem tabloul de la sfârșit spre început
                for (int i = array2.length - 1; i >= 0; i--) {

                    // Metodă din Thread: verificăm dacă thread-ul a fost întrerupt
                    if (currentThread.isInterrupted()) {
                        System.out.println("\nTh4: Thread întrerupt înainte de final.");
                        // Metodă din Thread: re-setăm flag-ul de întrerupere
                        currentThread.interrupt();
                        return; // ieșim din run()
                    }

                    // Afișăm elementul curent, prefixat cu "Th4:"
                    System.out.print("Th4:" + array2[i] + " ");
                    countInLine++;

                    // La fiecare 25 de elemente, trecem pe linia următoare
                    if (countInLine == 25) {
                        System.out.println();
                        countInLine = 0;
                    }

                    // Metodă din Thread: pauză de 2 ms între afișări
                    Thread.sleep(2);
                }
            } catch (InterruptedException e) {
                System.out.println("\nTh4: Sleep întrerupt!");
                // Metodă din Thread: refacem flag-ul de întrerupere
                currentThread.interrupt();
                return;
            }

            // Mesaj final cu numărul de elemente prelucrate
            System.out.println("\n>>> Th4 FINAL: " + array2.length +
                    " elemente procesate [realizat de: Cuturov Oleg]");

            // Anunțăm că și acest thread a terminat
            LaboratorThreadsMain.threadFinished();

            // Așteptăm toate thread-urile
            LaboratorThreadsMain.waitForAllThreads();

            // Afișăm textul în ordinea stabilită (Thread-4 are poziția lui în DISPLAY_ORDER)
            LaboratorThreadsMain.displayInOrder("Thread-4", LaboratorThreadsMain.GRUPA);
        }
    }
}
