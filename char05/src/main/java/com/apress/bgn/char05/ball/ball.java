package com.apress.bgn.char05.ball;

public class ball {
    private int d;
    private String color;
    private String material;

    public ball(int d, String color , String material) {
        this.d = d;
        this.color = color;
        this.material = material;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public String getColor() {
        return color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }


    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        String classname = this.getClass().getName().substring(27);
        return classname+"["+this.d+", "+this.color+", "+this.material
                +"]";
    }

}
