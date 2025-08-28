package com.apress.bgn.char005.employee;

import java.util.Arrays;
import java.util.Objects;

public class EmployeeDemo {
    public static void main(String[] args) {
        Employee[] staff = new Employee[4];
        Employee zs = new Employee("张三",12700,2016,5,30);
        Employee ls = new Employee("李四",12700,2012,7,13);
        Employee ww = new Employee("王五",13025,2019,7,25);
        Manger gg = new Manger("积极报",12890,2016,6,12);
        gg.setBonus(4000);
        staff[0]  = zs;
        staff[1] = ls;
        staff[2] = ww;
        staff[3] = gg;
        for (Employee e : staff)  {
            if (e instanceof Employee) e.raiseSalary(15);
        }
        Arrays.stream(staff).forEach(i -> System.out.println(i));
    }
}
