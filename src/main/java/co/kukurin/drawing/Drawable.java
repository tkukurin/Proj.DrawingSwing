package co.kukurin.drawing;

import java.awt.*;

public interface Drawable {

    void draw(Graphics2D g, int basePositionX, int basePositionY);
    void updateEndingPoint(int basePositionX, int basePositionY);
    Rectangle getBoundingBox(int basePositionX, int basePositionY);

}
