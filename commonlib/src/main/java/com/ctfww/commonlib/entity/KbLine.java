package com.ctfww.commonlib.entity;

public class KbLine {
    private double k;
    private double b;

    public KbLine() {
        k = 0.0;
        b = 0.0;
    }

    public KbLine(double k, double b) {
        this.k = k;
        this.b = b;
    }

    public KbLine(double a, double b, double c) {
        this.k = -a / b;
        this.b = -c / b;
    }

    public KbLine(double x1, double y1, double x2, double y2) {
        if (x1 == x2) {
            x2 += 0.00000001;
        }

        k = (y2 - y1) / (x2 - x1);
        b = y1 - k * x1;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getK() {
        return k;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getB() {
        return b;
    }
}
