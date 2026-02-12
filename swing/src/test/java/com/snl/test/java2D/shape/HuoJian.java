package com.snl.test.java2D.shape;

import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HuoJian {
    protected Vector2D[] outShape,oldShapeCopy;
    protected  Vector2D[] shapes,shapeCopy;
    List<Vector2D> l;

    public HuoJian(int wordWidth, int wordHeight) {
        double x = wordWidth / 8.0;
        double y = wordHeight / 8.0;
        outShape = new Vector2D[]{
                new Vector2D(- x, y),
                new Vector2D(x,y),
                new Vector2D(x,-y),
                new Vector2D(-x,-y),
        };
        oldShapeCopy = outShape.clone();

        shapes = new Vector2D[]{
                new Vector2D(0,y),new Vector2D(x /2,y * 2 / 3),
                new Vector2D(x / 2.0, y / 3.0),new Vector2D(x,0),
                new Vector2D(x- 0.5,0),new Vector2D(x- 1,y / 6),
                new Vector2D(0.125,y / 6),new Vector2D(0.125,0),new Vector2D()
        };
        Matrix3x3f mat = Matrix3x3f.flipYAix();
        Vector2D[] v = new Vector2D[shapes.length];
        for (int i = 0;i<shapes.length;i++) {
            v[i] = mat.mul(shapes[i]);
        }

        l = new ArrayList<>();
        l.addAll(Arrays.stream(shapes).toList());
        l.addAll(Arrays.stream(v).toList());
        shapes = l.toArray(Vector2D[]::new);
        shapeCopy = shapes.clone();
    }

    public Vector2D[] getOutShape() {
        return outShape;
    }

    public Vector2D[] getShapes() {
        return shapes;
    }

    public void addPos(Vector2D p) {
        int i;
        for (i = 0;i<outShape.length;i++)
        {
            outShape[i] = outShape[i].add(p);
        }
        for (i =0;i<shapes.length;i++)
        {
            shapes[i] = shapes[i].add(p);
        }
    }

    public void rotate(Matrix3x3f mat) {
        int i;
        Vector2D rotateCenter = new Vector2D(1, 1);
        for (i = 0;i<outShape.length;i++)
        {
            outShape[i] = mat.mul(oldShapeCopy[i].sub(rotateCenter));
            outShape[i] = outShape[i].add(rotateCenter);
        }
        for (i =0;i<shapes.length;i++)
        {
            shapes[i] = mat.mul(shapeCopy[i]);
        }
    }
}
