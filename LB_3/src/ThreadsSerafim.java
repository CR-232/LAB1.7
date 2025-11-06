public class ThreadsSerafim {


    public static class Task1 implements Runnable {
        @Override
        public void run() {
            System.out.println("Th1 started: Calcul suma produse numere pare consecutive (incepand cu primul)");
            System.out.println("Th1: Parcurgere DE LA ÎNCEPUT intervalul [234, 1000]");

            int[] array1 = LaboratorThreadsMain.ARRAY1;
            long totalSum = 0;
            int pairCount = 0;
            int previousEven = -1;
            long currentGroupSum = 0;
            int pairsInCurrentGroup = 0;

            for (int value : array1) {
                if (value % 2 == 0) {
                    if (previousEven != -1) {
                        long product = (long) previousEven * value;
                        currentGroupSum += product;
                        pairCount++;
                        pairsInCurrentGroup++;

                        System.out.println("Th1: Perechea " + pairCount + ": (" + previousEven + ", " + value + ") -> " +
                                previousEven + " * " + value + " = " + product);

                        if (pairsInCurrentGroup == 2) {
                            System.out.println("Th1: Rezultat combinat: " + currentGroupSum);
                            totalSum += currentGroupSum;
                            System.out.println("-----------------------------------");
                            currentGroupSum = 0;
                            pairsInCurrentGroup = 0;
                        }
                        previousEven = -1;
                        try { Thread.sleep(2); } catch (InterruptedException e) {}
                    } else {
                        previousEven = value;
                    }
                }
            }

            if (currentGroupSum > 0) {
                totalSum += currentGroupSum;
                System.out.println("Th1: Rezultat combinat final: " + currentGroupSum);
            }

            System.out.println("\n>>> Th1 FINAL: Suma totala = " + totalSum + " (" + pairCount + " perechi calculate)");

            // sincronizare
            LaboratorThreadsMain.SYNC.markFinished();
            LaboratorThreadsMain.SYNC.waitAll();
            LaboratorThreadsMain.SYNC.displayInOrder(1, LaboratorThreadsMain.PRENUME_STUDENT);
        }
    }

    // =============================
    // Task2 – ca în codul tău
    // =============================
    public static class Task2 implements Runnable {
        @Override
        public void run() {
            System.out.println("Th2 started: Calcul suma produse numere pare consecutive (incepand cu ultimul)");
            System.out.println("Th2: Parcurgere DE LA SFÂRȘIT intervalul [234, 1000]");

            int[] array1 = LaboratorThreadsMain.ARRAY1;
            long totalSum = 0;
            int pairCount = 0;
            int previousEven = -1;
            long currentGroupSum = 0;
            int pairsInCurrentGroup = 0;

            for (int i = array1.length - 1; i >= 0; i--) {
                int value = array1[i];
                if (value % 2 == 0) {
                    if (previousEven != -1) {
                        long product = (long) previousEven * value;
                        currentGroupSum += product;
                        pairCount++;
                        pairsInCurrentGroup++;

                        System.out.println("Th2: Perechea " + pairCount + ": (" + previousEven + ", " + value + ") -> " +
                                previousEven + " * " + value + " = " + product);if (pairsInCurrentGroup == 2) {
                            System.out.println("Th2: Rezultat combinat: " + currentGroupSum);
                            totalSum += currentGroupSum;
                            System.out.println("-----------------------------------");
                            currentGroupSum = 0;
                            pairsInCurrentGroup = 0;
                        }
                        previousEven = -1;
                        try { Thread.sleep(2); } catch (InterruptedException e) {}
                    } else {
                        previousEven = value;
                    }
                }
            }

            if (currentGroupSum > 0) {
                totalSum += currentGroupSum;
                System.out.println("Th2: Rezultat combinat final: " + currentGroupSum);
            }

            System.out.println("\n>>> Th2 FINAL: Suma totala = " + totalSum + " (" + pairCount + " perechi calculate)");

            LaboratorThreadsMain.SYNC.markFinished();
            LaboratorThreadsMain.SYNC.waitAll();
            LaboratorThreadsMain.SYNC.displayInOrder(2, LaboratorThreadsMain.NUME_STUDENT);
        }
    }
}