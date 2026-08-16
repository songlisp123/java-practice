package com.snl.swing.game.curve;

import com.snl.swing.game.math.Vector2D;

public class Hermite {
    public static Vector2D create(final Vector2D pi1,final Vector2D pi2,final Vector2D to
            , final Vector2D in,float t) {

        Vector2D r = new Vector2D();
        float base0 = HermiteBase(0, t);
        float base1 = HermiteBase(1, t);
        float base2 = HermiteBase(2, t);
        float base3 = HermiteBase(3, t);

        r.x = base0 * pi1.x + base1 * pi2.x + base2 * to.x + base3 * in.x;
        r.y = base0 * pi1.y + base1 * pi2.y + base2 * to.y + base3 * in.y;
        return  r;
    }

    public static float HermiteBase(int base,float t) {
        float result = -999;
        float t3 = t * t * t;
        float t2 = t * t;
        result = switch (base) {
            case 0 -> 2 * t3 - 3 * t2 + 1;
            case 1 -> -2 * t3 + 3 * t2;
            case 2 -> t3 - 2 * t2 + t;
            case 3 -> t3 - t2;
            default -> result;
        };
        return result;
    }


//    public static float[] hermite_to_points(int nPoints,Vector2D[] positions) {}
}
