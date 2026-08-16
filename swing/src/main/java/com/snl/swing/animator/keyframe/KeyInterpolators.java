package com.snl.swing.animator.keyframe;

import com.snl.swing.animator.interpolator.Interpolator;
import com.snl.swing.animator.interpolator.LinearInterpolator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyInterpolators {
    private final List<Interpolator> interpolators = new ArrayList<>();

    public KeyInterpolators(int intervals,Interpolator...ies) {
        if (ies != null && ies[0] != null) {
            if (ies.length < intervals) {
                for(int i = 0; i < intervals; ++i) {
                    this.interpolators.add(ies[0]);
                }
            } else {
                this.interpolators.addAll(Arrays.asList(ies).subList(0, intervals));
            }
        } else {
            for(int i = 0; i < intervals; ++i) {
                this.interpolators.add(LinearInterpolator.getInstance());
            }
        }
    }

    public double interpolate(int interval, double fraction) {
        return this.interpolators.get(interval).interpolate(fraction);
    }
}
