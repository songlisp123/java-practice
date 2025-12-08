package com.snl.test.table;

import java.time.LocalDateTime;
import java.util.Objects;

public class Student {
    String username;
    int age;
    LocalDateTime birthDay;
    double hight;
    double score;

    public Student() {
    }

    public Student(String username) {
        this.username = username;
    }

    public Student(int age) {
        this.age = age;
    }

    public Student(LocalDateTime birthDay) {
        this.birthDay = birthDay;
    }

    public Student(double hight) {
        this.hight = hight;
    }

    public Student(String username, int age, LocalDateTime birthDay, double hight, double score) {
        this.username = username;
        this.age = age;
        this.birthDay = birthDay;
        this.hight = hight;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDateTime birthDay) {
        this.birthDay = birthDay;
    }

    public double getHight() {
        return hight;
    }

    public void setHight(double hight) {
        this.hight = hight;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Student student)) return false;
        return age == student.age && Double.compare(hight, student.hight) == 0
                && Double.compare(score, student.score) == 0
                && Objects.equals(username, student.username)
                && Objects.equals(birthDay, student.birthDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, age, birthDay, hight, score);
    }

    @Override
    public String toString() {
        return "Student{" +
                "username='" + username + '\'' +
                ", age=" + age +
                ", birthDay=" + birthDay +
                ", hight=" + hight +
                ", score=" + score +
                '}';
    }
}
