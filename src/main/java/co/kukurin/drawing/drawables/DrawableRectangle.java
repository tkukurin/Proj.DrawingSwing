package co.kukurin.drawing.drawables;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
@ToString
public class DrawableRectangle extends Drawable {

    public DrawableRectangle(int coordinateSystemX, int coordinateSystemY, Color foreground, Color background) {
        super(coordinateSystemX, coordinateSystemY, foreground, background);
    }

    @Override
    public void drawDelegate(Graphics2D graphics2D, int topLeftOriginX, int topLeftOriginY) {
        Rectangle relativeRectangle = getAsRectangleRelativeTo(topLeftOriginX, topLeftOriginY);

        graphics2D.setColor(this.background);
        graphics2D.fill(relativeRectangle);

        graphics2D.setColor(this.foreground);
        graphics2D.draw(relativeRectangle);
    }

    @Override
    public void updateEndingPoint(int coordinateSystemX, int coordinateSystemY) {
        this.movablePoint.x = coordinateSystemX;
        this.movablePoint.y = coordinateSystemY;
    }

    @Override
    public Rectangle getAbsolutePositionedBoundingBox() {
        return getAsRectangleRelativeTo(0, 0);
    }

    private Rectangle getAsRectangleRelativeTo(int topLeftOriginX, int topLeftOriginY) {
        int width = Math.abs(this.fixedPoint.x - this.movablePoint.x);
        int height = Math.abs(this.fixedPoint.y - this.movablePoint.y);
        int relativeX = Math.min(this.fixedPoint.x, this.movablePoint.x) - topLeftOriginX;
        int relativeY = topLeftOriginY - Math.max(this.fixedPoint.y, this.movablePoint.y);
        return new Rectangle(relativeX, relativeY, width, height);
    }

}
