package co.kukurin.drawing.panel.mouse;

import co.kukurin.drawing.attributes.DrawingAttributes;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DrawingPanelScreenTranslateListener extends DrawingPanelMouseListener {

    private final DrawingAttributes drawingAttributes;
    private Point cachedMousePosition;

    public DrawingPanelScreenTranslateListener(DrawingAttributes drawingAttributes) {
        this.drawingAttributes = drawingAttributes;
        this.cachedMousePosition = null;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        this.cachedMousePosition = mouseEvent.getPoint();
        this.drawingPanel.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Point oldTopLeft = this.drawingAttributes.getTopLeftReferencePoint();
        Point oldBottomRight = this.drawingAttributes.getBottomRightReferencePoint();
        Point mouseEventPosition = mouseEvent.getPoint();
        Point delta = calculateMouseMoveDelta(this.cachedMousePosition, mouseEventPosition);

        Point newTopLeft = moveGivenPointOppositeFromMouseDelta(oldTopLeft, delta);
        Point newBottomRight = moveGivenPointOppositeFromMouseDelta(oldBottomRight, delta);

        this.drawingAttributes.setTopLeftReferencePoint(newTopLeft);
        this.drawingAttributes.setBottomRightReferencePoint(newBottomRight);
        this.cachedMousePosition = mouseEventPosition;
        this.drawingPanel.repaint();
    }

    private Point calculateMouseMoveDelta(Point cached, Point current) {
        Point actualCached = this.drawingPanel.getCoordinateSystemAbsolutePositionFromScreenPosition(cached);
        Point actualCurrent = this.drawingPanel.getCoordinateSystemAbsolutePositionFromScreenPosition(current);
        int deltaX = actualCached.x - actualCurrent.x;
        int deltaY = actualCached.y - actualCurrent.y;

        return new Point(deltaX, deltaY);
    }

    private Point moveGivenPointOppositeFromMouseDelta(Point currentPoint, Point delta) {
        currentPoint.setLocation(currentPoint.x + delta.x, currentPoint.y + delta.y);
        return currentPoint;

    }

}
