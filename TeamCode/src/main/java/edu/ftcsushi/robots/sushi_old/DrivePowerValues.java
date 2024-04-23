package edu.ftcsushi.robots.sushi_old;

public class DrivePowerValues {
    private double x;
    private double y;
    private double rx;
    public DrivePowerValues(double x, double y, double rx) {
        this.x = x;
        this.y = y;
        this.rx = rx;
    }

    public double getDrivePowerX() {
        return x;
    }

    public double getDrivePowerY() {
        return y;
    }
    public double getDrivePowerRx() {
        return rx;
    }
}
