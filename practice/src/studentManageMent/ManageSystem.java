package studentManageMent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageSystem {
    private List<Student> studentsList;
    private Writer writer;
    private static final Reader reader = new Reader(".//data//student//");
    private static final Logger logger = Logger.getLogger("studentManageSystem");
    private static final CustomHandler handler = new CustomHandler(Level.ALL);
    private static final SimplateFormattor formattor = new SimplateFormattor();

    static {
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        handler.setFormatter(formattor);
        logger.addHandler(handler);
    }

    public ManageSystem() {

        this.studentsList =  new ArrayList<>();
        studentsList = reader.read(true);
    }

    //判断是否有学生
    public boolean isEmpty() {
        return studentsList.isEmpty();
    }

    //判断是否包含学生

    public boolean containsStudent(Student otherStudent) {
        return studentsList.contains(otherStudent);
    }

    //按照年龄查询学生
    public void findStudentByAge(int age) {
        for (Student s : studentsList) {
            if (s.getAge() == age) System.out.println(s);
        }
    }

    //按照等级查询信息
    public void findByRank(Rank rank) {
        for (Student s:studentsList) {
            if (s.getRank().equals(rank)) System.out.println(s);
        }
    }

    //按照Id查找学生信息
    public void findById(int id) {
        for (Student s:studentsList) {
            if (s.getStuId() == id) System.out.println(s);
        }
    }

    //按照分数查询学生
    public void findByScore(double score) {
        for (Student s:studentsList) {
            if (s.getScore() == score) System.out.println(s);
        }
    }

    //按照名字查询
    public void findByName(String name) {
        for (Student  s:studentsList) {
            if (Objects.equals(s.getName(),name)) System.out.println(s);
        }
    }

    //按照出生 年限 查询
    public void findStudentByYear(int year) {
        for  (Student s: studentsList) {
            if (s.getBirthYear() == year) System.out.println(s);
        }
    }

    //显示分数线在给定区间的学员的数量和信息
    public void showMessageByScore(double first,double second) {
        int num = 0;
        if (first > second) {
            double temp = second;
            second = first;
            first = temp;
        }

        for (Student s : studentsList) {
            if (s.getScore()>first&&s.getScore()<second) {
                num++;
                System.out.println(s);
            }
        }

        System.out.println("共找到"+num+"符合要求的学员");
    }

    //待完善……增加
    public void addStudent(Student otherStudent) {
        if (containsStudent(otherStudent)) logger.warning("已经包含该学生!");
        else {
            studentsList.add(otherStudent);
            logger.info("添加学生成功!");
        }
    }

    //删除
    public void removeStudent(Student otherStudent) {
        if (containsStudent(otherStudent)) {
            studentsList.remove(otherStudent);
            logger.info("删除学生成功!");
        }else logger.warning("学生不存在!");
    }

    //排序操作
    /*
    按照学生年龄升序排序
     */

    public void sortByAge(boolean b) {
        if (b) {
            studentsList.sort(Comparator.comparingInt(Student::getAge));
        }
        else studentsList.sort((first,second)->{
            return second.getAge() - first.getAge();
        });
    }

    public void sortedByScore(boolean b) {
        if (b) studentsList.sort(Comparator.comparingDouble(Student::getScore));
        else {
            studentsList.sort((first,second)->{
                return (int) (second.getScore() - first.getScore());
            });
        }
    }

    public int totalStudents() {
        return studentsList.size();
    }

    public Student getFirstStudent() {
        sortById();
        if (!studentsList.isEmpty())
            return studentsList.getLast();
        return null;
    }

    public Student getLastStudent() {
        sortById();
        if (!studentsList.isEmpty())
            return studentsList.getFirst();
        return null;
    }

    public Student getIndexStudent(int index) {
        try {
            Student asthdent = studentsList.get(index);
            return asthdent;
        } catch (IndexOutOfBoundsException e) {
            logger.throwing("Management","getIndexStudent",
                    e.initCause(e));
        }

        return null;
    }

    public Student getHighestStudent() {
        sortedByScore(false);
        return studentsList.getFirst();
    }

    public Student getLowestStudent() {
        sortedByScore(true);
        return studentsList.getFirst();
    }

    private void sortById() {
        studentsList.sort(Comparator.comparing(Student::getStuId));
    }

    public void showTotalStudent() {
        sortById();
        studentsList.forEach(System.out::println);
    }

    //修改学生的信息
    public void changeStudentAge(Student otherStudent,int age) {
        if (studentsList.contains(otherStudent)) {
            otherStudent.setAge(age);
        }
    }

    //修改学生的信息
    public void changeStudentScore(Student otherStudent,double score) {
        if (studentsList.contains(otherStudent)) {
            otherStudent.setScore(score);
        }
    }

    //修改学生的信息
    public void changeStudentRank(Student otherStudent,Rank rank) {
        if (studentsList.contains(otherStudent)) {
            otherStudent.setRank(rank);
        }
    }

    //修改学生信息

    public void changeStudentName(Student otherStudent,String name) {
        if (studentsList.contains(otherStudent)) {
            otherStudent.setName(name);
        }
    }

    //修改学生信息
    public void changeStudentBirthDay(Student otherStudent, LocalDate localDate) {
        if (studentsList.contains(otherStudent)) {
            otherStudent.setBitrhDay(localDate);
        }
    }



}
