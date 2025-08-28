package com.apress.bgn.char005.hashcode;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import static java.lang.System.out;
public class SetDemo {
    public static void main(String[] args) {
        ball a = new ball(12.26,"红色","塑料");
        ball b = new ball(12.26,"白色","塑料");
        ball a_copy = new ball(12.26,"红色","塑料");
        ball b_copy = new ball(12.26,"白色","塑料");
        //列表存放球类物体
        //List<ball> ballList = new ArrayList<>();


        //集合的用法
        Set<ball> ballset = new HashSet<>();
        ballset.add(a);
        ballset.add(b);
        out.println("集合长度："+ballset.size());
        boolean isAdd = ballset.add(a_copy) && ballset.add(b_copy);

        //判断球是否属于相同对象或者相等
        if (a == a_copy) {
            out.println("相同对象！！");
        }else {
            out.println("不同对象！");
        }

        if (a.equals(a_copy)) {
            out.println("相等物体！");
        }else {
            out.println("不相等物体！");
        }

        if (!isAdd) {
            out.println("a_copy球并没有加入到集合中！");
            out.println("b_copy没有加入到集合中！");
        }
        out.println("集合大小："+ballset.size());

        //
        out.printf("%8s\n","集合中的元素为：");
        ballset.forEach(ball -> out.println(ball));
    }
}


class ball {
    private double d;
    private String color;
    private String material;

    public ball(double d,String color ,String material) {
        this.d = d;
        this.color = color;
        this.material = material;
    }

    //设置器和获取器
    public double getD() {
        return this.d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor() {
        this.color = color;
    }

    public String getMaterial() {
        return this.material;
    }

    public void setMaterial() {
        this.material = material;
    }

    @Override
    public String toString()  {
        var name  = this.getClass().getName().substring(32);
        return name+"["+this.color+","+this.d+","+this.material+"]";
    }
    //未使用objects方法的实现
//   @Override
//   public boolean equals(Object o) {
//       if (this==o) return true;
//       if (o == null || this.getClass() != o.getClass()) return false;
//       ball otherball = (ball) o;
//       return this.d == otherball.d && this.color.equals(otherball.color) &&
//               this.material.equals(otherball.material);
//   }

//    @Override
//    public int hashCode() {
//        double result = 17 * this.d;
//        result = 31 * result + (this.color == null?0:this.color.hashCode());
//        result = 31 * result + ( this.material == null?0:this.material.hashCode());
//        return  (int) result;
//    }

    //使用object.equals方法的实现
    @Override
    public boolean equals(Object o) {
        if (this == o) return  true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ball otherball = (ball) o;
        return this.d == otherball.d && Objects.equals(this.color,otherball.color) &&
                Objects.equals(this.material,otherball.material);
    }

 //   @Override
 //   public int hashCode() {
 //       return Objects.hash(d,color,material);
 //   }

}
