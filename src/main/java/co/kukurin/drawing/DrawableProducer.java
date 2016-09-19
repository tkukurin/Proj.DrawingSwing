package co.kukurin.drawing;

import java.awt.*;

@FunctionalInterface
public interface DrawableProducer {
    //Drawable create(int baseX, int baseY);

    Drawable create(int baseX, int baseY, Color foreground, Color background);
}
