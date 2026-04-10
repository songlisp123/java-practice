package com.snl.swing.game.math;

public class Ray {

    protected Vector2D start;
    protected Vector2D direction;

    public Ray(Vector2D direction) {
        this(new Vector2D(), direction);
    }

    public Ray(Vector2D start, Vector2D direction) {
        if (start == null) {
            throw new IllegalArgumentException("start");
        } else if (direction == null) {
            throw new IllegalArgumentException("direction");
        } else if (direction.isZero()) {
            throw new IllegalArgumentException("The direction cannot be the zero vector");
        } else {
            this.start = start;
            this.direction = direction.norm();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ray[Start=").append(this.start).append("|Direction=").append(this.getDirection()).append("]");
        return sb.toString();
    }


    public void setStart(Vector2D start) {
        if (start == null) {
            throw new IllegalArgumentException("start");
        } else {
            this.start = start;
        }
    }

    public void setDirection(Vector2D direction) {
        if (direction == null) {
            throw new IllegalArgumentException("direction");
        } else if (direction.isZero()) {
            throw new IllegalArgumentException("The direction cannot be the zero vector");
        } else {
            this.direction = direction;
        }
    }

    public Vector2D getDirectionVector() {
        return this.direction;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public Vector2D getStart() {
        return this.start;
    }
}
