package com.apress.bgn.char05.Thread;

public class thread {
    public static void main(String[] args) {
        var threadSet = Thread.getAllStackTraces().keySet();
        var stackTrace = Thread.getAllStackTraces().values();
        var threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for (int i=0;i<threadArray.length;i++) {
            System.out.println("线程名称："+threadArray[i].getName());
        }
        stackTrace.forEach(i -> System.out.println(i));
    }
}

//这些代码是什么意思？别着急，我一一讲解
//