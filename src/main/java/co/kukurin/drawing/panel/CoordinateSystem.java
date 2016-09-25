package co.kukurin.drawing.panel;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class CoordinateSystem {

    @Setter @Getter
    private Point topLeft;

    @Setter @Getter
    private Point bottomRight;

    @Getter
    private Rectangle componentDimensions;

    private double scale;

    public CoordinateSystem(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getCoordinateSystemAbsolutePositionFromScreenPosition(Point point) {
        return this.getCoordinateSystemAbsolutePositionFromScreenPosition(point.x, point.y);
    }

    public Point getCoordinateSystemAbsolutePositionFromScreenPosition(int screenX, int screenY) {
        return new Point(
                (int) (topLeft.x + screenX * getScaleX()),
                (int) (topLeft.y - screenY * getScaleX()));
    }

    public void setComponentDimensions(Component component) {
        this.componentDimensions = component.getBounds(this.componentDimensions);
    }

    public void updateScale(Component component) {
        this.componentDimensions = component.getBounds(this.componentDimensions);
        this.scale = (this.bottomRight.x - this.topLeft.x) / this.componentDimensions.getWidth();
    }

//    public double getScaleX() {
//        return (this.bottomRight.x - this.topLeft.x) / this.componentDimensions.getWidth();
//    }

    public double getScaleX() {
        return this.scale;
    }

    public double getScaleY() {
        return (-this.bottomRight.y + this.topLeft.y) / this.componentDimensions.getHeight();
    }
}
