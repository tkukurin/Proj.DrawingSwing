package co.kukurin.drawing.drawables;

import co.kukurin.drawing.drawables.Drawable;

import java.awt.*;

@FunctionalInterface
public interface DrawableProducer {
    Drawable create(int coordinateSystemX, int coordinateSystemY, Color foreground, Color background);
}
