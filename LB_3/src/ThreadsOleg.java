public class ThreadsOleg {

    // Thread 3 – Oleg, parcurgere de la început
    public static class Task3 implements Runnable {
        @Override
        public void run() {
            System.out.println("Th3 (autor: Cuturov Oleg): De parcurs de la început intervalul [456, 1234]");
            System.out.println("Th3: Parcurgere DE LA ÎNCEPUT intervalul [456, 1234]");

            int[] array2 = LaboratorThreadsMain.ARRAY2;
            int count = 0;

            for (int i = 0; i < array2.length; i++) {
                count++;
                if (count % 100 == 0 || i == array2.length - 1) {
                    System.out.println("Th3 progress: " + array2[i] + " (" + count + "/" + array2.length + " elements)");
                    try { Thread.sleep(2); } catch (InterruptedException e) {}
                }
            }

            System.out.println(">>> Th3 FINAL: " + count + " elemente procesate");

            // sincronizare comună
            LaboratorThreadsMain.SYNC.markFinished();
            LaboratorThreadsMain.SYNC.waitAll();
            LaboratorThreadsMain.SYNC.displayInOrder(3, LaboratorThreadsMain.DISCIPLINA);
        }
    }

    // Thread 4 – Oleg, parcurgere de la sfârșit
    public static class Task4 implements Runnable {
        @Override
        public void run() {
            System.out.println("Th4 (autor: Cuturov Oleg): De parcurs de la sfârșit intervalul [456, 1234]");
            System.out.println("Th4: Parcurgere DE LA SFÂRȘIT intervalul [456, 1234]");

            int[] array2 = LaboratorThreadsMain.ARRAY2;
            int count = 0;

            for (int i = array2.length - 1; i >= 0; i--) {
                count++;
                if (count % 100 == 0 || i == 0) {
                    System.out.println("Th4 progress: " + array2[i] + " (" + count + "/" + array2.length + " elements)");
                    try { Thread.sleep(2); } catch (InterruptedException e) {}
                }
            }

            System.out.println(">>> Th4 FINAL: " + count + " elemente procesate");

            // sincronizare comună
            LaboratorThreadsMain.SYNC.markFinished();
            LaboratorThreadsMain.SYNC.waitAll();
            LaboratorThreadsMain.SYNC.displayInOrder(4, LaboratorThreadsMain.GRUPA);
        }
    }
}
