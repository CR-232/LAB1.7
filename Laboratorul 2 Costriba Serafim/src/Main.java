class Fir extends Thread {
    String name;

    public Fir(String name) {
        this.name = name;
        start();
    }

    public void run() {
        System.out.println("Salut de la firul " + name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Main {

    public static void main(String[] args) {
        String name = null;

        ThreadGroup sys = Thread.currentThread().getThreadGroup();

        Thread t8 = null;
        t8 = new Thread(sys, new Fir(" Th1"));
        t8.setPriority(Thread.MAX_PRIORITY - 7);

        Thread t9 = null;
        t9 = new Thread(sys, new Fir(" Th2"));
        t9.setPriority(Thread.MAX_PRIORITY - 3);

        sys.list();


        ThreadGroup g = new ThreadGroup(" GO");
        g.list();
        ThreadGroup g1 = new ThreadGroup(g, "GZ");

        Thread t = null;
        t = new Thread(g1, new Fir(" Tha"));
        t.setPriority(Thread.MAX_PRIORITY - 9);

        Thread t1 = null;
        t1 = new Thread(g1, new Fir(" Thb"));
        t1.setPriority(Thread.MAX_PRIORITY - 7);

        Thread t2 = null;
        t2 = new Thread(g1, new Fir(" Thc"));
        t2.setPriority(Thread.MAX_PRIORITY - 7);

        Thread t3 = null;
        t3 = new Thread(g1, new Fir(" Thd"));
        t3.setPriority(Thread.MAX_PRIORITY - 3);

        g1.list();


        ThreadGroup g2 = new ThreadGroup(" GF");

        Thread t4 = null;
        t4 = new Thread(g2, new Fir(" Th1"));
        t4.setPriority(Thread.MAX_PRIORITY - 5);

        Thread t5 = null;
        t5 = new Thread(g2, new Fir(" Th2"));
        t5.setPriority(Thread.MAX_PRIORITY - 7);

        Thread t6 = null;
        t6 = new Thread(g2, new Fir(" Th3"));
        t6.setPriority(Thread.MAX_PRIORITY - 7);

        g2.list();

        ThreadGroup g3 = new ThreadGroup(" GV");

        Thread t7 = null;
        t7 = new Thread(g3, new Fir(" ThA"));
        t7.setPriority(Thread.MAX_PRIORITY - 7);

        new Thread(g3, new Fir(name));

        g3.list();
        System.out.println("Starting all threads:");
    }


}


