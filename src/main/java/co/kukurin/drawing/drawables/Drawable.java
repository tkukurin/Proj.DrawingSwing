package co.kukurin.drawing.drawables;

import java.awt.*;

public abstract class Drawable {

    protected Point fixedPoint;
    protected Point movablePoint;
    protected Color foreground;
    protected Color background;

    Drawable(int coordinateSystemX, int coordinateSystemY, Color foreground, Color background) {
        this.fixedPoint = new Point(coordinateSystemX, coordinateSystemY);
        this.movablePoint = new Point(coordinateSystemX, coordinateSystemY);
        this.foreground = foreground;
        this.background = background;
    }

    public void draw(Graphics2D graphics2D, int basePositionX, int basePositionY, double scaleFactor) {
        Color cached = graphics2D.getColor();
        drawDelegate(graphics2D, getAsRectangleRelativeTo(basePositionX, basePositionY, scaleFactor));
        graphics2D.setColor(cached);
    }

    public void updateEndingPoint(int coordinateSystemX, int coordinateSystemY) {
        this.movablePoint.x = coordinateSystemX;
        this.movablePoint.y = coordinateSystemY;
    }

    protected Rectangle getAsRectangleRelativeTo(int topLeftOriginX, int topLeftOriginY, double scaleFactor) {
        int width = (int) (Math.abs(this.fixedPoint.x - this.movablePoint.x) / scaleFactor);
        int height = (int) (Math.abs(this.fixedPoint.y - this.movablePoint.y) / scaleFactor);
        int relativeX = (int) ((Math.min(this.fixedPoint.x, this.movablePoint.x) - topLeftOriginX) / scaleFactor);
        int relativeY = (int) ((topLeftOriginY - Math.max(this.fixedPoint.y, this.movablePoint.y)) / scaleFactor);
        return new Rectangle(relativeX, relativeY, width, height);
    }

    public abstract Rectangle getAbsolutePositionedBoundingBox();
    protected abstract void drawDelegate(Graphics2D graphics2D, Rectangle mouseOutlineRectangle);

}
