package co.kukurin.drawing;

import java.awt.*;

public class DrawableRectangle implements Drawable {

    private Rectangle boundingBox;

    public DrawableRectangle(int x, int y) {
        this(new Rectangle(x, y, 0, 0)); // TODO 0,0
    }

    public DrawableRectangle(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public void draw(Graphics2D g, int basePositionX, int basePositionY) {
        g.drawString("test", basePositionX + this.boundingBox.x, basePositionY + this.boundingBox.y);
    }

    @Override
    public Rectangle getBoundingBox(int basePositionX, int basePositionY) {
        return boundingBox;
    }
}
