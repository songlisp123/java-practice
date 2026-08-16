package com.snl.swing.animator.evaluator;

import com.snl.swing.game.math.Vector2D;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class Evaluator<T> {

    private static final Map<Class<?>,Class<? extends Evaluator>> impl =
            new HashMap<>();

    public static void addRegister(Class<?> type,Class<? extends Evaluator> mpl) {
        impl.put(type,mpl);
    }

    public static void removeRegister(Class<?> type) {
        impl.remove(type);
    }

    public static  <T> Evaluator<T> create(Class<?> type) {
        System.out.println("type = " + type);
        Class<? extends Evaluator> interClass = null;
        for (Class<?> key : impl.keySet()) {
            if (key.isAssignableFrom(type)) {
                interClass = impl.get(key);
                break;
            }
        }

        if (interClass == null)
            //如果为null
            throw new IllegalStateException("暂无更多差值器");
        else {
            //否则
            try {
                //调用反射接口创建新的插值器
                Constructor<? extends Evaluator> constructor = interClass.getConstructor();
                return (Evaluator<T>) constructor.newInstance();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 核心方法
     */
    public abstract T evaluate(T t1, T t2, double fraction);

    static {
        impl.put(Double.class,EvaluateDouble.class);
        impl.put(Vector2D.class,EvaluateVector2D.class);
        impl.put(Color.class, EvaluateColor.class);
    }
}
