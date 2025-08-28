package ThreadTest;

public class ThreadTest {
    public static final int DELAY = 10;
    public static final int STEPS =100;
    public static final double MAX_COUNT = 1000;

    public static void main(String[] args) {
        var bank = new Bank(4,100000);
        Runnable task1 = ()->{
            for (int i = 0; i < STEPS; i++) {
                double amount =MAX_COUNT *Math.random();
                bank.transfer(0,1,amount);
                try {
                    Thread.sleep((long)(DELAY*Math.random()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

//        Runnable task2 = ()->{
//            for (int i = 0; i < STEPS; i++) {
//                double amount =MAX_COUNT *Math.random();
//                bank.transfer(1,2,amount);
//                try {
//                    Thread.sleep((long)(DELAY*Math.random()));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };

        Runnable task3 = ()->{
            for (int i = 0; i < STEPS; i++) {
                double amount =MAX_COUNT *Math.random();
                bank.transfer(2,3,amount);
                try {
                    Thread.sleep((long)(DELAY*Math.random()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t1 = new Thread(task1);
        t1.setName("线程一");
//        Thread t2 = new Thread(task2);
//        t2.setName("线程二");
        Thread t3 = new Thread(task3);
        t3.setName("线程三");

        t1.start();
//        t2.start();
        t3.start();
    }

}
