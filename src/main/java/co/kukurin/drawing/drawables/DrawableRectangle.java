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
    public Rectangle getAbsolutePositionedBoundingBox() {
        return getAsRectangleRelativeTo(0, 0, 1.0);
    }

    @Override
    protected void drawDelegate(Graphics2D graphics2D, Rectangle mouseOutlineRectangle) {
        graphics2D.setColor(this.background);
        graphics2D.fill(mouseOutlineRectangle);

        graphics2D.setColor(this.foreground);
        graphics2D.draw(mouseOutlineRectangle);
    }

}
