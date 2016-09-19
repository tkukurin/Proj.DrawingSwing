package co.kukurin.drawing.drawables;

import co.kukurin.drawing.drawables.Drawable;

import java.awt.*;

@FunctionalInterface
public interface DrawableProducer {
    //Drawable create(int baseX, int baseY);

    Drawable create(int baseX, int baseY, Color foreground, Color background);
}
