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

    public CoordinateSystem(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getCoordinateSystemAbsolutePositionFromScreenPosition(Point point) {
        return this.getCoordinateSystemAbsolutePositionFromScreenPosition(point.x, point.y);
    }

    public Point getCoordinateSystemAbsolutePositionFromScreenPosition(int screenX, int screenY) {
        double scaleFactorX = (bottomRight.x - topLeft.x) / componentDimensions.getWidth();
        return new Point(
                (int) (topLeft.x + screenX * scaleFactorX),
                (int) (topLeft.y - screenY * scaleFactorX));
    }

    public void setComponentDimensions(Component component) {
        this.componentDimensions = component.getBounds(this.componentDimensions);
    }

    public void updateScale(Component component) {
        this.componentDimensions = component.getBounds(this.componentDimensions);
    }

    public double getScale() {
        return (this.bottomRight.x - this.topLeft.x) / this.componentDimensions.getWidth();
    }
}
