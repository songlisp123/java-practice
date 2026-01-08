package com.snl.data.homework.charptor03;

public class Student implements Comparable<Student> {
    private int age;
    private double score;
    private String name;
    private boolean isFemale;

    public Student(int age, double score, String name, boolean isFemale) {
        this.age = age;
        this.score = score;
        this.name = name;
        this.isFemale = isFemale;
    }

    public Student(String name) {
        this.name = name;
    }

    public Student(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public void setFemale(boolean female) {
        isFemale = female;
    }

    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", score=" + score +
                ", name='" + name + '\'' +
                ", isFemale=" + isFemale +
                '}';
    }

    @Override
    public int compareTo(Student o) {
        if (this.score > o.score) return 1;
        else if (this.score == o.score) return 0;
        return -1;
    }
}
