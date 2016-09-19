package co.kukurin;

import co.kukurin.drawing.Drawable;

import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

public interface DrawingModel {

    Collection<Drawable> getDrawables();
    void addDrawable(Drawable drawable);

}