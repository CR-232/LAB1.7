public class LaboratorThreadsMain {


    public static final String NUME_STUDENT = "Cuturov Oleg si Costriba Serafim";
    public static final String PRENUME_STUDENT = "Oleg";
    public static final String GRUPA = "CR-232";
    public static final String DISCIPLINA = "Programarea Concurentă și distribuită";


    public static final int[] ARRAY1 = generateArray(234, 1000);
    public static final int[] ARRAY2 = generateArray(456, 1234);

    // sincronizator comun
    public static final SyncHelper SYNC = new SyncHelper(new int[]{2, 4, 1, 3}, 4);

    public static void main(String[] args) {

        System.out.println("=== LABORATOR CR-232 ===");
        System.out.println("Echipa: Cuturov Oleg si Costriba Serafim\n");

        System.out.println("Array 1  [234, 1000]:");
        //printArray(ARRAY1);
        System.out.println();

        System.out.println("Array 2  [456, 1234]:");
       // printArray(ARRAY2);
        System.out.println();

        System.out.println("Starting Thread 1");
        System.out.println("Starting Thread 2");
        System.out.println("Starting Thread 3");
        System.out.println("Starting Thread 4\n");

        // firele sunt în alte fișiere
       Thread th1 = new Thread(new ThreadsSerafim.Task1(), "Thread-1");
       Thread th2 = new Thread(new ThreadsSerafim.Task2(), "Thread-2");
        Thread th3 = new Thread(new ThreadsOleg.Task3(), "Thread-3");
        Thread th4 = new Thread(new ThreadsOleg.Task4(), "Thread-4");

        th1.start();
        th2.start();
        th3.start();
        th4.start();

        try {
           th1.join();
            th2.join();
            th3.join();
            th4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nToate firele de execuție s-au încheiat.");
    }



    private static int[] generateArray(int start, int end) {
        int size = end - start + 1;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = start + i;
        }
        return array;
    }




    public static class SyncHelper {
        private volatile int finishedThreads = 0;
        private volatile int currentDisplay = 0;
        private final int[] displayOrder;
        private final int totalThreads;

        public SyncHelper(int[] displayOrder, int totalThreads) {
            this.displayOrder = displayOrder;
            this.totalThreads = totalThreads;
        }

        public void markFinished() {
            finishedThreads++;
        }

        public void waitAll() {
            while (finishedThreads < totalThreads) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void displayInOrder(int threadNumber, String text) {
            while (currentDisplay < displayOrder.length &&
                    displayOrder[currentDisplay] != threadNumber) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            for (char c : (threadNumber + ": " + text).toCharArray()) {
                System.out.print(c);
                try { Thread.sleep(50); } catch (InterruptedException e) {}
            }
            System.out.println();

            currentDisplay++;
        }
    }
}
