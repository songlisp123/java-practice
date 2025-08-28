package com.apress.bgn.char006.intefaces;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import javax.swing.*;
import com.apress.bgn.char006.employee.Employee;
import com.apress.bgn.char006.employee.Manager;
public class intefaces {
    public static void main(String[] args) {
        //测试Arrays.sort方法实现方式
        int[] a = new int[10];
        for (int i=0;i<a.length;i++) {
            a[i] = (int) (Math.random()*10+1);
        }
        //未排序前
        Arrays.stream(a).forEach( i -> System.out.printf("%3d",i));
        Arrays.sort(a);
        //排序后
        System.out.println();
        Arrays.stream(a).forEach( i -> System.out.printf("%3d",i));
        System.out.println();
        //当然你能比较一个整数数组，是因为整数数组实现了comparabe接口，倘若你想比较employee类？
        //会发生生么？
        //创建Employee数组
        Employee[] staff = new Employee[6];
        Employee zs = new Employee("张三",12700,2016,5,30);
        Employee ls = new Employee("李四",1600,2012,7,13);
        Employee ww = new Employee("王五",10000,2019,7,25);
        staff[0] = zs;
        staff[1] = ls;
        staff[2] = ww;

        //测试Employee类能否被排序？
        //Arrays.sort(staff);
        /**
        答案是：不能！
         返回错误：
        {@code Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.lang.Comparable.compareTo(Object)" because "a[runHi]" is null
	    at java.base/java.util.ComparableTimSort.countRunAndMakeAscending(ComparableTimSort.java:320)
	    at java.base/java.util.ComparableTimSort.sort(ComparableTimSort.java:188)
	    at java.base/java.util.Arrays.sort(Arrays.java:1041)
	    at com.apress.bgn.char006.intefaces.intefaces.main(intefaces.java:28)}
         */
        /**
         * 这会抛出{@code NullPointerException}  原因是你没有为其实现
         * {@code java.lang.Comparable.compareTo(Object)}接口,导致在比较的时候，出现null空值
         */

        //实现compareable.compareto方法的employee类
        //排序前
        //Arrays.stream(staff).forEach(e -> System.out.println(e));
        //Arrays.sort(staff);
        ////排序后
        //System.out.println();
        //Arrays.stream(staff).forEach(e -> System.out.println(e));

        //看起来按照正确的薪资水平排序
        //可是如果我们想一下，经理类继承了雇员类，但是您的雇员类是这样子定义的：
        //public class Employee implements Comparable
        //如果我们在尚未实现compareto方法的前提下比较经理呢？我觉得是个好尝试！
        Manager[] managers = new Manager[3];
        managers[0] = new Manager("张三",12580,2012,6,10);
        managers[1] = new Manager("赵四",13580,2014,8,25);
        managers[2] = new Manager("老六",8000,2023,10,1);

        //我们声名了一个管理员数组，并填充了三个管理员到列表中中，现在我们使用Arrays.sort方法 会发生什么？

        //排序前
        //Arrays.stream(managers).forEach(m-> System.out.println(m));
        //Arrays.sort(managers);
        //System.out.println();
        //Arrays.stream(managers).forEach(m-> System.out.println(m));

        /*
        manager{salary=125800.0, name='张三', Hiredate=2012-06-10,奖金：0.0}
        manager{salary=135800.0, name='赵四', Hiredate=2014-08-25,奖金：0.0}
        manager{salary=8000.0, name='老六', Hiredate=2023-10-01,奖金：0.0}

        manager{salary=8000.0, name='老六', Hiredate=2023-10-01,奖金：0.0}
        manager{salary=125800.0, name='张三', Hiredate=2012-06-10,奖金：0.0}
        manager{salary=135800.0, name='赵四', Hiredate=2014-08-25,奖金：0.0}
         */

        //输出看起来不错，为我们排序按照薪资排序了，但是仔细思考一下，是不是有遗漏的？
        //不错，我们的经理类重写了getsalary方法，这代表着经理类还存在奖金，这一次带入奖金会发生什么？

        managers[0].setBonus(500);
        managers[1].setBonus(-1000);
        managers[2].setBonus(10000);

        //Arrays.stream(managers).forEach(m-> System.out.println(m));
        //Arrays.sort(managers);
        //System.out.println();
        //Arrays.stream(managers).forEach(m-> System.out.println(m));

        /*
        输出：
        manager{salary=12580.0, name='张三', Hiredate=2012-06-10,奖金：0.0}
        manager{salary=14080.0, name='赵四', Hiredate=2014-08-25,奖金：500.0}
        manager{salary=18000.0, name='老六', Hiredate=2023-10-01,奖金：10000.0}

        manager{salary=18000.0, name='老六', Hiredate=2023-10-01,奖金：10000.0}
        manager{salary=12580.0, name='张三', Hiredate=2012-06-10,奖金：0.0}
        manager{salary=14080.0, name='赵四', Hiredate=2014-08-25,奖金：500.0}

         */

        //与我们预想完全不一样！我们希望按照总薪资排序，可是，他却按照薪资排序，于是
        //出现了18000出现在前头的问题，默认情况下，是升序排序？该怎么解决这个问题呢？
        //我觉得重写这个方法如下所是：
        /*
        public int compareTo(Object o) {
            Manager other = (Manager) o;
            if (this.getSalary() < other.getSalary()) return -1;
            else if (this.getSalary() > other.getSalary())  return 1;
            return 0;
        }
         */
       // Arrays.stream(managers).forEach(m-> System.out.println(m));
       // Arrays.sort(managers);
       // System.out.println();
       // Arrays.stream(managers).forEach(m-> System.out.println(m));

        /*
        输出如下：
        manager{salary=13080.0, name='张三', Hiredate=2012-06-10,奖金：500.0}
        manager{salary=12580.0, name='赵四', Hiredate=2014-08-25,奖金：-1000.0}
        manager{salary=18000.0, name='老六', Hiredate=2023-10-01,奖金：10000.0}

        manager{salary=12580.0, name='赵四', Hiredate=2014-08-25,奖金：-1000.0}
        manager{salary=13080.0, name='张三', Hiredate=2012-06-10,奖金：500.0}
        manager{salary=18000.0, name='老六', Hiredate=2023-10-01,奖金：10000.0}
         */

        //很好我们是解决了这个问题，现在下一步是什么？你也许会想一下，既然我们的经理类继承雇员类
        //那么为什么不试着比较这两者？尽管没有比较的可能性，但是我们还是试着比较以下：
        //staff[3] = managers[0];
        //staff[4] = managers[1];
        //staff[5] = managers[2];
        //我们将管理员添加到雇员数组，如果比较重新比较staff数组会发生什么？
        //第一种猜测：会抛出错误
        //第二中猜测：会正常运行
        //那一个符合我们的猜测？执行和一
        //Arrays.stream(staff).forEach(m-> System.out.println(m));
        //Arrays.sort(staff);
        //System.out.println();
        //Arrays.stream(staff).forEach(m-> System.out.println(m));

        //结果是符合我们的预测，抛出错误：
        /*
        Exception in thread "main" java.lang.<i>ClassCastException</i>: class com.apress.bgn.char006.employee.Employee cannot be cast to class com.apress.bgn.char006.employee.Manager (com.apress.bgn.char006.employee.Employee and com.apress.bgn.char006.employee.Manager are in unnamed module of loader 'app')
	    at com.apress.bgn.char006.employee.Manager.compareTo(Manager.java:39)
	    at java.base/java.util.ComparableTimSort.binarySort(ComparableTimSort.java:262)
	    at java.base/java.util.ComparableTimSort.sort(ComparableTimSort.java:189)
	    at java.base/java.util.Arrays.sort(Arrays.java:1041)
	    at com.apress.bgn.char006.intefaces.intefaces.main(intefaces.java:143)
         */

        /*
        错误原因：
        雇员类不能转化成经理类，当然！雇员类是经理类的超类，不能向下转化，那么我们该怎么解决这个问题？
        staff[3] = managers[0];
        staff[4] = managers[1];
        staff[5] = managers[2];
        尝试修改这个
         */
        staff[3] = managers[0];
        staff[4] = managers[1];
        staff[5] = managers[2];
        Arrays.stream(staff).forEach(System.out::println);
        Arrays.sort(staff);
        System.out.println();
        Arrays.stream(staff).forEach(System.out::println);

        //依旧报错，也许不能比较？但是为什么？雇员类是经理类的超类也就意味着，经理类承诺的更多！
        //我们 可以比较x.compareTo(y)如果x是雇员类的实例，y是经理类的实例。但是，我们却不能比较
        //y.euals(x)，由于y是经理类，所以他会自动调用经理类的方法，但是雇员类根本不能转化为经理类
        //原因就是：经理类承诺更多！
        //所以说当经理类比较雇员类的时候回报错！类型转换失败！
        //问题是该如何解决呢？

        //也许我们可以禁止比较不一样的类别的员工！

        //在compareTo的接口内，增加一行测试

        /*
        if (this.getcalss()!=other.getclass()) throw new ClassCastException();
         */

        /*
        Employee{salary=12700.0, name='张三', Hiredate=2016-05-30}
        Employee{salary=1600.0, name='李四', Hiredate=2012-07-13}
        Employee{salary=10000.0, name='王五', Hiredate=2019-07-25}
        manager{salary=13080.0, name='张三', Hiredate=2012-06-10,奖金：500.0}
        manager{salary=12580.0, name='赵四', Hiredate=2014-08-25,奖金：-1000.0}
        manager{salary=18000.0, name='老六', Hiredate=2023-10-01,奖金：10000.0}

        Employee{salary=1600.0, name='李四', Hiredate=2012-07-13}
        Employee{salary=10000.0, name='王五', Hiredate=2019-07-25}
        Employee{salary=12700.0, name='张三', Hiredate=2016-05-30}
        manager{salary=12580.0, name='赵四', Hiredate=2014-08-25,奖金：-1000.0}
        manager{salary=13080.0, name='张三', Hiredate=2012-06-10,奖金：500.0}
        manager{salary=18000.0, name='老六', Hiredate=2023-10-01,奖金：10000.0}
         */

        //经理类与经理类比较，雇员类与雇员类比较，确实实现了我们的想法！

        /*
        接口特征：
        1、接口不是类，不能被初始化
        2、接口中的方法只有抽象、默认和私有
        3、接口内没有实例字段，但是可以有常量
        4\接口可以被其他接口扩展，从而实现更丰富的内容
        5、接口与超类的区别：类优先原则--超类中的方法会覆盖接口内的方法，你因该避免
        在不同的接口内重复使用相同的方法签名！
         */

        /*
        通过这个安列你应该了解了compare接口：
        1、调用Arrays.sort方法会必须保证您的类实现了compareable接口
        2、在类层次接口上，从下到上可以扎转换类型，但是从上到小不可以
        3、关于comparea接口的更多方法，请参考源码。
         */

        /*
        关于::的更多用法
        1、对象::实例方法
        2、类：实例方法
        3、类：：静态方法
         */

        //尝试使用salary字段对staff数组排序
        Arrays.sort(staff,Employee::compareTo);
        Arrays.toString(staff);

        //目前阅读：java核心卷1阅读接口第一、二节！
        //尚未阅读：方法引用和内联类第三节和第四节

        Throwable as = new Throwable();
        System.out.println(as.getMessage());

        /*
        接口内的方法默认是：
        1、抽象公开的
        但是你还可以提供静态和私有方法
        以2java中的path为解释：
        public interface Path
           {
                public static Path of(URI uri) { . . . }
                public static Path of(String first, String... more) { . . . }
                . . .
                }

        上面的Path接口，拥有of静态方法，该方法结合艘一个字符串或者文件路径。奇观是的，java中还有一个Paths类
        提供了一个静态方法get作用与上面类似。

         */

        /*
        默认方法：default方法
        public interface Iterator<E>
        {
            boolean hasNext();
            E next();
            default void remove() { throw new
            UnsupportedOperationException("remove"); }
            . . .
        }

        上面的Iterator接口中的remove的方法声明为默认的
         */



        /*
        你已经知道Array.sort()方法比较的数组必须是实现compareable接口，但是对于String的数组，我们该怎么
        按照长度或者马元排序而不是默认的字典顺序呢？
        要按照不同方法比较，需要实现一个comparator接口的类,比如我们想按照长度排序，这时候需要一个比较器：
        class LengthComparator implements Comparator<String>
            {
            public int compare(String first, String second)
            {
                return first.length() - second.length();
            }
         }

         然后创建一个长度比较器
         var lengthComparator = new LengthComparator();
         然后将长度比较器安装在Arrays.sort(T,lengthComparator)中，这样子，你的字符串数组就能按照长度而不是默认的
         字典比较了。
         */


        try {
            DataInputStream dataInputStream = new DataInputStream(
                    new BufferedInputStream(new FileInputStream("."))
            );


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



    }
}
