package com.apress.bgn.char05.Map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiConsumer;

import com.apress.bgn.char05.ball.ball;
public class map {
    public static void main(String[] args) {
       // Map<ball,Integer> newhashmap = new HashMap<>();
        //这是java10中引入的var来进一步简化声明
        var newhashmap = new HashMap<ball,Integer>();
        ball redBall = new ball(15,"红色" ,"金属");
        newhashmap.put(new ball(12,"红色","塑料"),5);
        newhashmap.put(new ball(16,"绿色","塑料"),6);
        newhashmap.put(new ball(9,"蓝色","金属"),12);
        newhashmap.put(redBall,8);
        //newhashmap.forEach(ball,value -> s);
        //for (Map.Entry(ball,Integer) entry : newhashmap.entrySet())  action.accept(entry.getKey(), entry.getValue());

       // //这是java默认的实现方式
       // for (Map.Entry<ball,Integer> entry : newhashmap.entrySet()) {
       //     System.out.println(entry.getKey()+":"+entry.getValue());
       // }

        //这是java1.8中引入的foreach方法，实现了lamda方式
        newhashmap.forEach((k,v) -> System.out.println(k +":" +v));
        System.out.println("\n");
        //请注意：将已经存在的条目存放到map映射的时候，会覆盖原有的条目
        //有一种方法可以避免覆盖原有条目,我们声明了一个renBall对象，放置在
        //map映射中，现在我们将再次将红色的球放置在映射中，会发生什么？
        //newhashmap.put(redBall,3);

        //newhashmap.forEach((k,v) -> System.out.println(k + ":"+v));

        //红色球变成3个！有一种方法可以避免此覆盖
        //if (!newhashmap.containsKey(redBall)) newhashmap.put(redBall,3);
        //newhashmap.forEach((k,v) -> System.out.println(k + ":"+v));
        //结果依旧是8个,另一种方法是：
        newhashmap.putIfAbsent(redBall,3);
        newhashmap.forEach((k,v) -> System.out.println(k+":"+v));
        //红色球依旧是8个

    }
}
