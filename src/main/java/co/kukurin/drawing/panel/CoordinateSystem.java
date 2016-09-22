package co.kukurin.drawing.panel;

import java.awt.*;

public class CoordinateSystem {

    private Point topLeft;
    private Point bottomRight;

    public CoordinateSystem(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public int getWidth() {
        return this.bottomRight.x - this.topLeft.x;
    }

    public int getHeight() {
        return this.topLeft.y - this.bottomRight.y;
    }

}
