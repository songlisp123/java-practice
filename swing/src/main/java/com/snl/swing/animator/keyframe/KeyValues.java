package com.snl.swing.animator.keyframe;

import com.snl.swing.animator.evaluator.Evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyValues<T> {
    private List<T> values;
    private Evaluator<T> evaluator;
    private final Class<?> type;
    private T startValue;

    private KeyValues(T...paras) {
        this(Evaluator.create(paras.getClass().getComponentType()),paras);
    }

    private KeyValues(Evaluator evaluator, T...paras) {
        this.values = new ArrayList<>();
        if (paras == null)
            throw new IllegalArgumentException("参数不能为null");
        else if (paras.length == 0) {
            throw new IllegalArgumentException("参数为0");
        }else {
            if (paras.length == 1)
                this.values.add(null);
            Collections.addAll(this.values,paras);
            this.type = paras.getClass().getComponentType();
            this.evaluator = evaluator;
        }
    }

    public Class<?> getType() {
        return type;
    }

    public T getStartValue() {
        return startValue;
    }

    public int getSize() {
        return this.values.size();
    }

    //是否开始动画
    public boolean isToAnimation() {
        return this.values.getFirst() == null;
    }

    public void setStartValue(T startValue) {
        if (isToAnimation())
            this.startValue = startValue;
    }

    //核心算法
    public T getValue(int i0,int i1,double fraction) {
        T lowerValue = this.values.get(i0);
        if (lowerValue == null) {
            lowerValue = this.startValue;
        }

        T value;
        if (i0 == i1) {
            value = lowerValue;
        } else {
            T v1 = this.values.get(i1);
            value = this.evaluator.evaluate(lowerValue, v1, fraction);
        }
        return value;
    }


    @SafeVarargs
    public static <T> KeyValues<T> create(T...paras) {
        return new KeyValues<>(paras);
    }

    @SafeVarargs
    public static <T> KeyValues<T> create(Evaluator evaluator, T... params) {
        return new KeyValues<T>(evaluator, params);
    }
}
