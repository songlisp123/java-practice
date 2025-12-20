package com.snl.data.linkedList;

import java.time.LocalDateTime;
import java.util.Objects;

public class StudentDemo {

    private String name;
    private int age;
    private LocalDateTime birth;
    private double height;

    public StudentDemo(double height, LocalDateTime birth, int age, String name) {
        this.height = height;
        this.birth = birth;
        this.age = age;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getBirth() {
        return birth;
    }

    public void setBirth(LocalDateTime birth) {
        this.birth = birth;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StudentDemo that)) return false;
        return age == that.age && Double.compare(height, that.height) == 0
                && Objects.equals(name, that.name) && Objects.equals(birth, that.birth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, birth, height);
    }

    @Override
    public String toString() {
        return "StudentDemo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birth=" + birth +
                ", height=" + height +
                '}';
    }
}
