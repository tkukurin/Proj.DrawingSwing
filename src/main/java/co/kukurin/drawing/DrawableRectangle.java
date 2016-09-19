package co.kukurin.drawing;

import java.awt.*;

public class DrawableRectangle extends Drawable {

    public DrawableRectangle(int x, int y, Color foreground, Color background) {
        super(x, y, foreground, background);
    }

    @Override
    public void drawDelegate(Graphics2D graphics2D, int screenPositionX, int screenPositionY) {
        Rectangle relativeRectangle = getAsRectangleRelativeTo(screenPositionX, screenPositionY);

        graphics2D.setColor(this.background);
        graphics2D.fill(relativeRectangle);

        graphics2D.setColor(this.foreground);
        graphics2D.draw(relativeRectangle);
    }

    @Override
    public void updateEndingPoint(int screenPositionX, int screenPositionY) {
        this.end.x = screenPositionX;
        this.end.y = screenPositionY;
    }

    // TODO remove basePositions
    @Override
    public Rectangle getBoundingBox(int basePositionX, int basePositionY) {
        return getAsRectangleRelativeTo(0, 0);
    }

    private Rectangle getAsRectangleRelativeTo(int screenPositionX, int screenPositionY) {
        int w = Math.abs(this.start.x - this.end.x);
        int h = Math.abs(this.start.y - this.end.y);
        int lx = Math.min(this.start.x, this.end.x) + screenPositionX;
        int ly = Math.min(this.start.y, this.end.y) + screenPositionY;
        return new Rectangle(lx, ly, w, h);
    }

}
