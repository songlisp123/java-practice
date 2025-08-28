package com.apress.bgn.char005.calendar;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class calendar {
    public static void main(String[] args) {
        LocalDate currentTime = LocalDate.now();
        System.out.println("时间是："+currentTime.toString());
        int year = currentTime.getYear();
        int month = currentTime.getMonthValue();
        int day = currentTime.getDayOfMonth();

        currentTime = currentTime.minusDays(day-1);     //设置月份的第一天
        DayOfWeek week = currentTime.getDayOfWeek();
        int value = week.getValue();
        System.out.println("星期一  星期二  星期三  星期四  星期五  星期六  星期七");

    }
}
