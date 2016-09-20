package co.kukurin.drawing.drawables;

import java.awt.*;

public class DrawableCircle extends Drawable {

    DrawableCircle(int coordinateSystemX, int coordinateSystemY, Color foreground, Color background) {
        super(coordinateSystemX, coordinateSystemY, foreground, background);
    }

    @Override
    public Rectangle getAbsolutePositionedBoundingBox() {
        return null;
    }

    @Override
    protected void drawDelegate(Graphics2D graphics2D, Rectangle mouseOutlineRectangle) {

    }

}
