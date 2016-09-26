package co.kukurin.drawing.drawables;

import java.awt.*;

public class DrawableCircle extends Drawable {

    public DrawableCircle(int coordinateSystemX, int coordinateSystemY, Color foreground, Color background) {
        super(coordinateSystemX, coordinateSystemY, foreground, background);
    }

    @Override
    public Rectangle getAbsolutePositionedBoundingBox() {
        return getAsRectangleRelativeTo(0, 0, 1.0);
    }

    @Override
    protected void drawDelegate(Graphics2D graphics2D, Rectangle mouseOutlineRectangle) {
        graphics2D.setColor(this.background);
        graphics2D.fillOval(mouseOutlineRectangle.x,
                            mouseOutlineRectangle.y,
                            mouseOutlineRectangle.width,
                            mouseOutlineRectangle.height);

        graphics2D.setColor(this.foreground);
        graphics2D.drawOval(mouseOutlineRectangle.x,
                            mouseOutlineRectangle.y,
                            mouseOutlineRectangle.width,
                            mouseOutlineRectangle.height);
    }

}
