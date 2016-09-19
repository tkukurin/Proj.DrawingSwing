package co.kukurin;

import co.kukurin.drawing.Drawable;

import java.util.Collection;

public interface DrawingModel {

    Collection<Drawable> getDrawables();
    void addDrawable(Drawable drawable);

}
