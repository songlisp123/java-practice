package com.apress.bgn.char006.employee;

import java.util.Objects;

public class Manager extends Employee {
    private double bonus;       //私有字段：奖金

    public Manager(String name, double salary, int year, int month, int day) {
        super(name, salary, year, month, day);
        this.bonus = 0;

    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    @Override
    public double getSalary() {
        return this.bonus + super.getSalary();
    }


    @Override
    public String toString() {
        return "manager{" +
                "salary=" + this.getSalary() +
                ", name='" + this.getName() + '\'' +
                ", Hiredate=" + this.getHiredate() +
                ",奖金：" + this.bonus +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Manager) {
            Manager other = (Manager) o;
            if (this.getSalary() < other.getSalary()) return -1;
            else if (this.getSalary() > other.getSalary()) return 1;
        }
        return 0;
    }
}