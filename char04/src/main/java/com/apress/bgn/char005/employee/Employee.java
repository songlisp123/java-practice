package com.apress.bgn.char005.employee;

import java.time.LocalDate;

public class Employee {
    private double salary;
    private String name;
    private LocalDate Hiredate;

    public Employee() {}
    public Employee(String name,double salary,int year ,int month,int day) {
        this.name = name;
        this.salary = salary;
        this.Hiredate = LocalDate.of(year,month,day);
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getHiredate() {
        return Hiredate;
    }

    public void setHiredate(LocalDate hiredate) {
        Hiredate = hiredate;
    }

    public void raiseSalary(int percent) {
        double p = (double) percent/100;
        double currentpay = this.salary;
        this.salary = currentpay+(currentpay*p);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "salary=" + salary +
                ", name='" + name + '\'' +
                ", Hiredate=" + Hiredate +
                '}';
    }
}
