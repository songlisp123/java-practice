package studentManageMent;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student implements Serializable {
    private static int ID = 1;
    private int stuId;
    private String name;
    private LocalDate bitrhDay;
    private int age;
    private double score;
    private Rank rank;
    private static final Writer write = new Writer();
    private static List<Student> studentList = new ArrayList<>();


    public Student(String name,int year,int month,int day,
                   double score, Rank rank)
    {
        this.stuId = advancedId();
        this.name = name;
        this.bitrhDay = LocalDate.of(year,month,day);
        this.age = LocalDate.now().getYear()-year;
        this.score = score;
        this.rank = rank;
        studentList.add(this);

    }


    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //查询年
    public int getBirthYear() {
        return this.bitrhDay.getYear();
    }

    public LocalDate getBitrhDay() {
        return bitrhDay;
    }

    public void setBitrhDay(LocalDate bitrhDay) {
        this.bitrhDay = bitrhDay;
    }

    //查询月
    public int month () {
        return this.bitrhDay.getMonthValue();
    }

    //查询天
    public int day() {
        return this.bitrhDay.getDayOfMonth();
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

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    private int advancedId() {
        int r = ID;
        ID++;
        return r;
    }

    public static void writeIntoFile() {
        write.write(studentList);
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuId=" + stuId +
                ", name='" + name + '\'' +
                ", bitrhDay=" + bitrhDay +
                ", age=" + age +
                ", score=" + score +
                ", rank=" + rank +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return stuId == student.stuId && age == student.age && Double.compare(score, student.score) == 0 && Objects.equals(name, student.name) && Objects.equals(bitrhDay, student.bitrhDay) && rank == student.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stuId, name, bitrhDay, age, score, rank);
    }
}
