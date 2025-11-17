package reflection.proxy;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;

/**
 * 此程序演示代理对象的使用
 */
public class ProxyTest {


    public static void main(String[] args) {
        var elements = new Object[1000];
        for (int i = 0; i< elements.length; i++) {
            Integer value = i + 1;
            var handler = new TraceHandler<Integer>(value);
            //创建代理
            //
            Object proxy = Proxy.newProxyInstance(
                    ClassLoader.getSystemClassLoader(),
                    new Class[]{Comparable.class},
                    handler
            );
            elements[i] = proxy;
        }
        //构建一个随机数
        Integer key = (int) (Math.random() * elements.length) + 1;
        //使用二分算法搜索键
        int searched = Arrays.binarySearch(elements, key);
        //如果找到
        if (searched>=0) System.out.println(elements[searched]);
    }

    private static class TraceHandler<T> implements InvocationHandler {

        private T target;

        public TraceHandler(T target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //打印隐式函数
            System.out.print(this.target);
            //打印方法名称
            System.out.print("."+method.getName()+"(");
            //打印显示参数
            if (Objects.nonNull(args)) {
                for (int i = 0; i < args.length; i++) {
                    //打印当前参数
                    System.out.print(args[i]);
                    if (i < args.length - 1) System.out.print(", ");
                }
            }
            System.out.println(")");
            //显示调用函数
            return method.invoke(this.target,args);
        }
    }
}
