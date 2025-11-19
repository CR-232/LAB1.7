public class ThreadsOleg {

    public static class Task3 implements Runnable {
        @Override
        public void run() {
            System.out.println("=== TH3 – Cuturov Oleg ===");
            System.out.println("Rol: de parcurs de la ÎNCEPUT intervalul [234, 1000]");
            System.out.println("Th3: Parcurgere DE LA ÎNCEPUT intervalul [234, 1000]");

            int[] array1 = LaboratorThreadsMain.ARRAY1; // [234, 1000]
            int countInLine = 0;

            for (int i = 0; i < array1.length; i++) {
                System.out.print("Th3:" + array1[i] + " ");
                countInLine++;

                // după fiecare 10 elemente, trecem pe rând nou (ca în exemplul din laborator)
                if (countInLine == 10) {
                    System.out.println();
                    countInLine = 0;
                }

                try { Thread.sleep(2); } catch (InterruptedException e) {}
            }

            System.out.println("\n>>> Th3 FINAL: " + array1.length +
                    " elemente procesate [realizat de: Cuturov Oleg]");

            LaboratorThreadsMain.SYNC.markFinished();
            LaboratorThreadsMain.SYNC.waitAll();
            LaboratorThreadsMain.SYNC.displayInOrder(3, LaboratorThreadsMain.DISCIPLINA);
        }
    }

    public static class Task4 implements Runnable {
        @Override
        public void run() {
            System.out.println("=== TH4 – Cuturov Oleg ===");
            System.out.println("Rol: de parcurs de la SFÂRȘIT intervalul [456, 1234]");
            System.out.println("Th4: Parcurgere DE LA SFÂRȘIT intervalul [456, 1234]");

            int[] array2 = LaboratorThreadsMain.ARRAY2; // [456, 1234]
            int countInLine = 0;

            for (int i = array2.length - 1; i >= 0; i--) {
                System.out.print("Th4:" + array2[i] + " ");
                countInLine++;

                // după fiecare 10 elemente, trecem pe rând nou
                if (countInLine == 10) {
                    System.out.println();
                    countInLine = 0;
                }

                try { Thread.sleep(2); } catch (InterruptedException e) {}
            }

            System.out.println("\n>>> Th4 FINAL: " + array2.length +
                    " elemente procesate [realizat de: Cuturov Oleg]");

            LaboratorThreadsMain.SYNC.markFinished();
            LaboratorThreadsMain.SYNC.waitAll();
            LaboratorThreadsMain.SYNC.displayInOrder(4, LaboratorThreadsMain.GRUPA);
        }
    }
}
