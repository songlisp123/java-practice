package com.snl.swing.animator.keyframe;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTargetAdapter;
import com.snl.swing.animator.evaluator.Evaluator;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class PropertySetter extends TimingTargetAdapter {
    private Object object;
    private String propertyName;
    private KeyFrames keyFrames;
    private Method propertySetter;
    private Method propertyGetter;

    public static Animator createAnimator(int duration, Object object, String propertyName, KeyFrames keyFrames) {
        PropertySetter ps = new PropertySetter(object, propertyName, keyFrames);
        Animator animator = new Animator(duration, ps);
        return animator;
    }

    public static <T> Animator createAnimator(int duration, Object object, String propertyName, T... params) {
        PropertySetter ps = new PropertySetter(object, propertyName, params);
        Animator animator = new Animator(duration, ps);
        return animator;
    }

    public static <T> Animator createAnimator(int duration, Object object, String propertyName, Evaluator evaluator, T... params) {
        PropertySetter ps = new PropertySetter(object, propertyName, evaluator, params);
        Animator animator = new Animator(duration, ps);
        return animator;
    }

    public PropertySetter(Object object, String propertyName, KeyFrames keyFrames) {
        this.object = object;
        this.propertyName = propertyName;
        this.keyFrames = keyFrames;

        try {
            this.setupMethodInfo();
        } catch (NoSuchMethodException var5) {
            throw new IllegalArgumentException("Bad property name (" + propertyName + "): could not find " + "an appropriate setter or getter method for that property");
        }
    }

    public <T> PropertySetter(Object object, String propertyName, T... params) {
        this(object, propertyName, new KeyFrames(KeyValues.create(params)));
    }

    public <T> PropertySetter(Object object, String propertyName, Evaluator evaluator, T... params) {
        this(object, propertyName, new KeyFrames(KeyValues.create(evaluator, params)));
    }

    private void setupMethodInfo() throws NoSuchMethodException {
        try {
            String firstChar = this.propertyName.substring(0, 1);
            String remainder = this.propertyName.substring(1);
            Class propertyType = this.getType();
            String propertySetterName = "set" + firstChar.toUpperCase() + remainder;
            PropertyDescriptor prop = new PropertyDescriptor(this.propertyName, this.object.getClass(), (String)null, propertySetterName);
            this.propertySetter = prop.getWriteMethod();
            if (this.isToAnimation()) {
                String propertyGetterName = "get" + firstChar.toUpperCase() + remainder;
                prop = new PropertyDescriptor(this.propertyName, this.object.getClass(), propertyGetterName, (String)null);
                this.propertyGetter = prop.getReadMethod();
            }

        } catch (Exception e) {
            throw new NoSuchMethodException("Cannot find property methods: " + e);
        }
    }

    public void begin() {
        if (this.isToAnimation()) {
            try {
                this.setStartValue(this.propertyGetter.invoke(this.object));
            } catch (Exception e) {
                System.out.println("Problem setting start value on object " + this.object + ": " + e);
            }
        }

    }

    public void timingEvent(double fraction) {
        try {
            this.setValue(this.object, this.propertySetter, fraction);
        } catch (Exception e) {
            System.out.println("Problem calling setValue in PropertySetter.timingEvent: " + e);
        }

    }

    private String getPropertyName() {
        return this.propertyName;
    }

    private void setStartValue(Object object) {
        this.keyFrames.getKeyValues().setStartValue(object);
    }

    private void setValue(Object object, Method method, double fraction) {
        try {
            method.invoke(object, this.keyFrames.getValue(fraction));
        } catch (Exception e) {
            System.out.println("Problem invoking method " + this.propertySetter + " in object " + object + " in setValue" + e);
        }

    }

    private Class getType() {
        return this.keyFrames.getType();
    }

    private boolean isToAnimation() {
        return this.keyFrames.getKeyValues().isToAnimation();
    }
}
