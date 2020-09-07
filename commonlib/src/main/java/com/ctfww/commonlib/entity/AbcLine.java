package com.ctfww.commonlib.entity;

public class AbcLine {
    private double a;
    private double b;
    private double c;

    public AbcLine() {
        a = 0.0;
        b = 0.0;
        c = 0.0;
    }

    public AbcLine(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public AbcLine(double x1, double y1, double x2, double y2) {
        KbLine kb = new KbLine(x1, y1, x2, y2);

        a = kb.getK();
        b = -1.0;
        c = kb.getB();
    }

    public double[] calcPedal(double x, double y) {
        double[] ret = new double[]{0.0, 0.0};
        if (a == 0.0 & b == 0.0) {
            return ret;
        }

        ret[0] = (b * b * x - a * b * y - a * c) / (a * a + b * b);
        ret[1] = (a * a * y - a * b * x - b * c) / (a * a + b * b);

        return ret;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getA() {
        return a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getB() {
        return b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getC() {
        return c;
    }
}
