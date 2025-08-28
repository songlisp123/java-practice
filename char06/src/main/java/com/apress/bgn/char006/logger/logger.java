package com.apress.bgn.char006.logger;

import java.nio.file.Path;
import java.util.logging.*;
import java.util.logging.Logger;

public class logger extends Formatter implements Filter {
    private static final Logger myLogger = Logger.getLogger("com.snl.bgn");
    private static final Logger mylogger02 = Logger.getLogger("com.snl");
    private static final Formatter aformatter = new SimpleFormatter();
    public static void main(String[] args) {
        myLogger.setUseParentHandlers(false);
        var hander = new ConsoleHandler();
        myLogger.setLevel(Level.FINE);
        myLogger.addHandler(hander);
        hander.setLevel(Level.FINE);
        //var hander02 = new FileHandler("%h/myapp.log",0,10);
        //Logger logger03 = Logger.getLogger("");
        //System.out.println(logger03.getClass());
        myLogger.info("这条纪律能被打印出来！");
        myLogger.fine("这条纪律将被隐藏！");
        //日志框架
        myLogger.severe("这是最高级别的信息！");
        myLogger.warning("这是第6级别的信息");
        myLogger.info("这是第五级别的信息");
        myLogger.config("这是一个第四集的信息");
        myLogger.fine("这是一个第三级别的信息");
        myLogger.finer("这是第二级别的信息");
        myLogger.finest("这是最低级别的信息");

        //logapi
        myLogger.log(Level.SEVERE,"这是另一种调用方法的api");

        //api
        //myLogger.config("这条记录将会只打印一次");
        /*
        设置记录的出入口
         */

        System.out.println("你好世界！");
        System.out.println(myLogger.getParent().getName());

    }

    @Override
    public boolean isLoggable(LogRecord record) {
        if (record.getLevel() == Level.INFO) return true;
        return false;
    }

    @Override
    public String format(LogRecord record) {
        return "";
    }


}
