package ThreadTest;

public class ThreadTestDemo2 {
    private static final int ACCOUNT = 100;
    private static final double INITIAL_BALANCE = 1000;
    private static final int DELAY = 10;
    private static final int MAX_COUNT = 2*1000;

    public static void main(String[] args) {
        Bank bank = new Bank(ACCOUNT,INITIAL_BALANCE);
        Runnable task1 = ()->{
            for (int i = 0; i < ACCOUNT; i++) {
//                int r = (int) (ACCOUNT * Math.random());
                try {
                    while (true) {
                        int r = (int) (ACCOUNT * Math.random());
                        int to = (int) (ACCOUNT * Math.random());
                        double amount = MAX_COUNT*Math.random();
                        bank.transfer(r,to,amount);
                        Thread.sleep((long) (DELAY*Math.random()));
                    }
                }catch (InterruptedException e)  {
                    e.printStackTrace();
                }
            }
        };
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(task1);
            t.setName("第[%d]线程".formatted(i));
            t.start();
        }
    }
}
