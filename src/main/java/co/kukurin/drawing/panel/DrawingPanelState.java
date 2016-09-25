package co.kukurin.drawing.panel;

import co.kukurin.drawing.drawables.Drawable;
import co.kukurin.drawing.drawables.DrawableProducer;
import co.kukurin.drawing.panel.mouse.DrawingPanelMouseListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class DrawingPanelState {

    private DrawableProducer activeDrawableProducer;
    private DrawingPanelMouseListener activeMouseListener;

    public DrawingPanelState(DrawableProducer activeDrawableProducer) {
        this.activeDrawableProducer = activeDrawableProducer;
    }

}
