public class ThreadsSerafim {


    public static class Task1 implements Runnable {
        @Override
        public void run() {

            System.out.println("Th1 started: Procesare array1 (de la început)");

            long totalSum = 0;
            long groupSum = 0;
            int prevEven = -1;
            int groupCount = 0;

            for (int x : LaboratorThreadsMain.array1) {
                if (x % 2 == 0) {
                    if (prevEven != -1) {
                        long prod = (long) prevEven * x;
                        groupSum += prod;
                        groupCount++;

                        System.out.println("Th1: (" + prevEven + ", " + x + ") = " + prod);

                        if (groupCount == 2) {
                            totalSum += groupSum;
                            groupSum = 0;
                            groupCount = 0;
                            System.out.println("-----------------------------------");
                        }

                        prevEven = -1;
                    } else prevEven = x;
                }
            }

            if (groupSum > 0) totalSum += groupSum;

            System.out.println(">>> Th1 FINAL: Total = " + totalSum);

            LaboratorThreadsMain.threadFinished();
            LaboratorThreadsMain.waitForAllThreads();

            LaboratorThreadsMain.displayInOrder("Thread-1", LaboratorThreadsMain.PRENUME_STUDENT);
        }
    }

    // ============================
    // THREAD 2 – Serafim
    // ============================
    public static class Task2 implements Runnable {
        @Override
        public void run() {

            System.out.println("Th2 started: Procesare array1 (de la sfârșit)");

            long totalSum = 0;
            long groupSum = 0;
            int prevEven = -1;
            int groupCount = 0;

            for (int i = LaboratorThreadsMain.array1.length - 1; i >= 0; i--) {
                int x = LaboratorThreadsMain.array1[i];

                if (x % 2 == 0) {
                    if (prevEven != -1) {
                        long prod = (long) prevEven * x;
                        groupSum += prod;
                        groupCount++;

                        System.out.println("Th2: (" + prevEven + ", " + x + ") = " + prod);

                        if (groupCount == 2) {
                            totalSum += groupSum;
                            groupSum = 0;
                            groupCount = 0;
                            System.out.println("-----------------------------------");
                        }

                        prevEven = -1;
                    } else prevEven = x;
                }
            }

            if (groupSum > 0) totalSum += groupSum;

            System.out.println(">>> Th2 FINAL: Total = " + totalSum);

            LaboratorThreadsMain.threadFinished();
            LaboratorThreadsMain.waitForAllThreads();

            LaboratorThreadsMain.displayInOrder("Thread-2", LaboratorThreadsMain.NUME_STUDENT);
        }
    }
}
