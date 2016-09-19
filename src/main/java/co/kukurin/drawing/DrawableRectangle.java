package co.kukurin.drawing;

import java.awt.*;

public class DrawableRectangle implements Drawable {

    private Rectangle boundingBox;
    private Point start;
    private Point end;

    public DrawableRectangle(int x, int y) {
        this.start = new Point(x, y);
        this.end = new Point(x, y);
    }

    public DrawableRectangle(Rectangle boundingBox) {

    }

    @Override
    public void draw(Graphics2D g, int screenPositionX, int screenPositionY) {
        //g.drawString("test", screenPositionX + this.boundingBox.x, screenPositionY + this.boundingBox.y);
        int w = Math.abs(this.start.x - this.end.x);
        int h = Math.abs(this.start.y - this.end.y);
        int lx = Math.min(this.start.x, this.end.x) + screenPositionX;
        int ly = Math.min(this.start.y, this.end.y) + screenPositionY;
        Rectangle relativeRectangle = new Rectangle(lx, ly, w, h);

        Color cachedColor = g.getColor();
        g.setColor(Color.BLACK);
        g.draw(relativeRectangle);
        g.setColor(cachedColor);
    }

    @Override
    public void updateEndingPoint(int screenPositionX, int screenPositionY) {
        this.end.x = screenPositionX;
        this.end.y = screenPositionY;
    }

    // TODO remove basePositions
    @Override
    public Rectangle getBoundingBox(int basePositionX, int basePositionY) {
        return boundingBox;
    }
}
