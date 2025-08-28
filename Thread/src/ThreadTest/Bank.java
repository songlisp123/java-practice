package ThreadTest;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    private final double[] account;
    private Lock lock = new ReentrantLock();
    private Condition suffientFunds;

    public Bank(int account, double initialBalance) {
        this.account = new double[account];
        Arrays.fill(this.account,initialBalance);
        suffientFunds = lock.newCondition();
    }

    public void transfer(int from,int to,double count) {
        lock.lock();
        try {
            Thread thread = Thread.currentThread();
//            System.out.println(thread.getState());
            while (account[from]<count) {
                suffientFunds.await();
                System.out.println("[%s]阻塞".formatted(thread.getName()));
                System.out.println(thread.getState());
                System.out.println("==========================");
            }
            System.out.println(thread);
            account[from] -= count;
            System.out.println(" %10.2f from %d to %d".formatted(count, from, to));
            account[to] += count;
            System.out.printf(" Total Balance: %10.2f%n", getTotalBalance());
            suffientFunds.signalAll();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public double getTotalBalance() {
        return Arrays.stream(account)
                .sum();
    }

    public int getSize() {
        return account.length;
    }
}
