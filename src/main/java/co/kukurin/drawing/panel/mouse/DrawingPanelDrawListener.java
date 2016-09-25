package co.kukurin.drawing.panel.mouse;

import co.kukurin.custom.Optional;
import co.kukurin.drawing.attributes.DrawingAttributes;
import co.kukurin.drawing.drawables.Drawable;
import co.kukurin.drawing.panel.CoordinateSystem;
import co.kukurin.drawing.panel.DrawingModel;
import co.kukurin.drawing.panel.DrawingPanelState;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DrawingPanelDrawListener extends DrawingPanelMouseListener {

    private final DrawingModel drawingModel;
    private Drawable elementCurrentlyBeingDrawn;
    private final DrawingAttributes drawingAttributes;
    private final CoordinateSystem referentCoordinateSystem;
    private final DrawingPanelState drawingPanelState;

    public DrawingPanelDrawListener(DrawingModel drawingModel,
                                    DrawingAttributes drawingAttributes,
                                    CoordinateSystem referentCoordinateSystem,
                                    DrawingPanelState drawingPanelState) {
        this.drawingModel = drawingModel;
        this.drawingAttributes = drawingAttributes;
        this.referentCoordinateSystem = referentCoordinateSystem;
        this.drawingPanelState = drawingPanelState;
        this.elementCurrentlyBeingDrawn = null;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        newDrawableFromSelected(mouseEvent)
            .ifPresent(drawable -> {
                this.drawingModel.addDrawable(drawable);
                this.elementCurrentlyBeingDrawn = drawable;
            });
    }

    private Optional<Drawable> newDrawableFromSelected(MouseEvent mouseEvent) {
        Point absoluteCoordinates = this.referentCoordinateSystem.getCoordinateSystemAbsolutePositionFromScreenPosition(mouseEvent.getPoint());
        return Optional.ofNullable(this.drawingPanelState.getActiveDrawableProducer())
                       .map(selectedDrawable -> selectedDrawable.create(
                               absoluteCoordinates.x,
                               absoluteCoordinates.y,
                               this.drawingAttributes.getSelectedForegroundColor(),
                               this.drawingAttributes.getSelectedBackgroundColor()));
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Point absoluteCoordinates = this.referentCoordinateSystem.getCoordinateSystemAbsolutePositionFromScreenPosition(mouseEvent.getPoint());
        Optional.ofNullable(this.elementCurrentlyBeingDrawn)
                .ifPresent(element -> element.updateEndingPoint(absoluteCoordinates.x, absoluteCoordinates.y));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.elementCurrentlyBeingDrawn = null;
    }
}
