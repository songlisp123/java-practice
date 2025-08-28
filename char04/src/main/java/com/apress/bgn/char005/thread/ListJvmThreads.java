package com.apress.bgn.char005.thread;

public class ListJvmThreads {
    public static void main(String[] args) {
        var threadSet = Thread.getAllStackTraces().keySet();
        var threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for (int i=0;i<threadArray.length;++i) {
            System.out.println("线程："+threadArray[i].getName());
        }
    }
}
