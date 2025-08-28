package com.apress.bgn.char005.calender;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
public class CalendarDemo {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = new GregorianCalendar();
        Date currentTime = calendar.getTime();
        System.out.println("当前时间是："+sdf.format(currentTime));

        calendar.set(2001,calendar.JUNE,12);
        Date myBrith = calendar.getTime();
        System.out.println("我的生日"+sdf.format(myBrith));


        int day = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println("day="+day);
        int month =  calendar.get(Calendar.MONTH)+1;
        System.out.println("月份="+month);

        int year = calendar.get(Calendar.YEAR);
        System.out.println("年份="+year);



    }
}
