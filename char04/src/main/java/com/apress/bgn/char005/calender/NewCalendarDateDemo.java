package com.apress.bgn.char005.calender;


//import java.sql.SQLOutput;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
public class NewCalendarDateDemo {
    public static void main(String[] args) {
        var currenTime = LocalDateTime.now();
        System.out.println("当前时间（完全格式）"+currenTime);
        System.out.println("当前日期：(年/月/日）" +currenTime.toLocalDate());
        System.out.println("当前日期（时/分/秒）"+currenTime.toLocalTime());


        var myBrith = LocalDate.of(2001,6,23);
        System.out.println("生日:"+myBrith);

        int day = myBrith.getDayOfMonth();
        DayOfWeek week = myBrith.getDayOfWeek();
        System.out.println("星期:"+week);
        System.out.println("天数："+day);

        int month = myBrith.getMonth().getValue();
        System.out.println("月份="+month);

        int year = myBrith.getYear();
        System.out.println("年属："+year);

    }
}
