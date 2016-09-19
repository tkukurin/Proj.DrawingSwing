package co.kukurin.drawing;

import java.awt.*;

public abstract class Drawable {

    protected Point start;
    protected Point end;
    protected Color foreground;
    protected Color background;

    public Drawable(int startX, int startY, Color foreground, Color background) {
        this.start = new Point(startX, startY);
        this.end = new Point(startX, startY);
        this.foreground = foreground;
        this.background = background;
    }

    public void draw(Graphics2D graphics2D, int basePositionX, int basePositionY) {
        Color cached = graphics2D.getColor();
        drawDelegate(graphics2D, basePositionX, basePositionY);
        graphics2D.setColor(cached);
    }

    public abstract void updateEndingPoint(int basePositionX, int basePositionY);
    public abstract Rectangle getBoundingBox(int basePositionX, int basePositionY);

    protected abstract void drawDelegate(Graphics2D graphics2D, int basePositionX, int basePositionY);

}
