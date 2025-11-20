public class LaboratorThreadsMain {


    public static final String NUME_STUDENT = "Cuturov Oleg si Costriba Serafim";
    public static final String PRENUME_STUDENT = "Oleg";
    public static final String GRUPA = "CR-232";
    public static final String DISCIPLINA = "Programarea Concurentă și distribuită";


    public static final int[] array1 = generateArray(234, 1000);
    public static final int[] array2 = generateArray(456, 1234);


    public static volatile int finishedThreads = 0;
    public static volatile int currentDisplay = 0;

    public static final String[] DISPLAY_ORDER = {
            "Thread-2", "Thread-4", "Thread-1", "Thread-3"
    };


    public static void main(String[] args) {

        System.out.println("=== LABORATOR CR-232 ===");
        System.out.println("Echipa: Cuturov Oleg si Costriba Serafim\n");

        Thread th1 = new Thread(new ThreadsSerafim.Task1(), "Thread-1");
        Thread th2 = new Thread(new ThreadsSerafim.Task2(), "Thread-2");
        Thread th3 = new Thread(new ThreadsOleg.Task3(), "Thread-3");
        Thread th4 = new Thread(new ThreadsOleg.Task4(), "Thread-4");

        System.out.println("Starting Thread 1");
        System.out.println("Starting Thread 2");
        System.out.println("Starting Thread 3");
        System.out.println("Starting Thread 4\n");

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

        System.out.println("\n=== Toate firele s-au încheiat. ===");
    }



    public static int[] generateArray(int start, int end) {
        int size = end - start + 1;
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = start + i;
        return arr;
    }



    public static synchronized void threadFinished() {
        finishedThreads++;
    }

    public static void waitForAllThreads() {
        while (finishedThreads < 4) {
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }
    }

    public static void printWithDelay(String text, int delay) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try { Thread.sleep(delay); } catch (InterruptedException ignored) {}
        }
        System.out.println();
    }

    public static void displayInOrder(String threadName, String text) {

        while (!DISPLAY_ORDER[currentDisplay].equals(threadName)) {
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }

        System.out.print(threadName + ": ");
        printWithDelay(text, 100);

        currentDisplay++;
    }
}
