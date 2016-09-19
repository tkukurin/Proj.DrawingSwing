package co.kukurin;

import co.kukurin.drawing.Drawable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class DrawingModelImpl implements DrawingModel {

    private Collection<Drawable> drawables;

    public DrawingModelImpl() {
        this.drawables = new LinkedList<>();
    }

    //    public DrawingModelImpl() {
//        this.drawables = new PriorityQueue<>();
//    }

    @Override
    public Collection<Drawable> getDrawables() {
        return this.drawables;
    }

    @Override
    public void addDrawable(Drawable drawable) {
        this.drawables.add(drawable);
    }
}
