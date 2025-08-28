package com.apress.bgn.char005.employee;

public class Manger extends Employee {
    private double bonus;       //私有字段：奖金
    public Manger(String name,double salary,int year ,int month,int day) {
        super(name,salary,year,month,day);
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
        return this.bonus+super.getSalary();
    }

    @Override
    public String toString() {
        return "manager{" +
                "salary=" + this.getSalary() +
                ", name='" + this.getName() + '\'' +
                ", Hiredate=" + this.getHiredate() +
                ",奖金："+this.bonus+
                '}';
    }
}
