/**
 * Earl Timothy D. Malaki
 * BSCS - II
 * CMSC22 - OOP
 *
 */

import java.awt.*;

public class Ball {
    private static final int DIAMETER = 30;

    private int x;
    private int y;
    private int xa;
    private int ya;

    public Ball(){
        x = 0;
        y = 0;
        xa = 1;
        ya = 1;
    }

    public static int getDIAMETER() {
        return DIAMETER;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getXa() {
        return xa;
    }

    public void setXa(int xa) {
        this.xa = xa;
    }

    public int getYa() {
        return ya;
    }

    public void setYa(int ya) {
        this.ya = ya;
    }

    public void paint(Graphics2D g) {
        g.fillOval(x, y, DIAMETER, DIAMETER);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER, DIAMETER);
    }


}