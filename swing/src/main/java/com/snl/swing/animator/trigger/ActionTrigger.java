package com.snl.swing.animator.trigger;

import com.snl.swing.animator.Animator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

public class ActionTrigger extends Trigger implements ActionListener {
    public ActionTrigger(Animator animator) {
        super(animator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.fire();
    }

    public static ActionTrigger addTrigger(Object object,Animator animator) {
        ActionTrigger ac = new ActionTrigger(animator);
        try {
            Method m = object.getClass().getMethod("addActionListener", ActionTrigger.class);
            m.invoke(object,ac);
            return ac;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
