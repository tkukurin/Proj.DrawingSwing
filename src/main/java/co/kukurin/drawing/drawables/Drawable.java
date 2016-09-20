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

    public void draw(Graphics2D graphics2D, int basePositionX, int basePositionY) {
        Color cached = graphics2D.getColor();
        drawDelegate(graphics2D, basePositionX, basePositionY);
        graphics2D.setColor(cached);
    }

    public abstract void updateEndingPoint(int basePositionX, int basePositionY);
    public abstract Rectangle getAbsolutePositionedBoundingBox();

    protected abstract void drawDelegate(Graphics2D graphics2D, int basePositionX, int basePositionY);

}
