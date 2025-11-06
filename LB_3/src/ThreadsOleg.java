public class ThreadsOleg {

    public static class Task3 implements Runnable {
        @Override
        public void run() {
            System.out.println("=== TH3 – Cuturov Oleg ===");
            System.out.println("Rol: de parcurs de la ÎNCEPUT intervalul [456, 1234] și de afișat progresul");
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

            System.out.println(">>> Th3 FINAL: " + count +
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
            System.out.println("Rol: de parcurs de la SFÂRȘIT intervalul [456, 1234] și de afișat progresul");
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

            System.out.println(">>> Th4 FINAL: " + count +
                    " elemente procesate [realizat de: Cuturov Oleg]");

            LaboratorThreadsMain.SYNC.markFinished();
            LaboratorThreadsMain.SYNC.waitAll();
            LaboratorThreadsMain.SYNC.displayInOrder(4, LaboratorThreadsMain.GRUPA);
        }
    }
}
