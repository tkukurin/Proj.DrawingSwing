package co.kukurin.drawing.panel.mouse;

import co.kukurin.drawing.attributes.DrawingAttributes;
import co.kukurin.drawing.panel.CoordinateSystem;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DrawingPanelScreenTranslateListener extends DrawingPanelMouseListener {

    private Point cachedMousePosition;
    private final CoordinateSystem referentCoordinateSystem;

    public DrawingPanelScreenTranslateListener(DrawingAttributes drawingAttributes,
                                               CoordinateSystem referentCoordinateSystem) {
        this.referentCoordinateSystem = referentCoordinateSystem;
        this.cachedMousePosition = null;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        this.cachedMousePosition = mouseEvent.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Point oldTopLeft = this.referentCoordinateSystem.getTopLeft();
        Point oldBottomRight = this.referentCoordinateSystem.getBottomRight();
        Point mouseEventPosition = mouseEvent.getPoint();
        Point delta = calculateMouseMoveDelta(this.cachedMousePosition, mouseEventPosition);

        Point newTopLeft = moveGivenPointOppositeFromMouseDelta(oldTopLeft, delta);
        Point newBottomRight = moveGivenPointOppositeFromMouseDelta(oldBottomRight, delta);

        this.referentCoordinateSystem.setTopLeft(newTopLeft);
        this.referentCoordinateSystem.setBottomRight(newBottomRight);
        this.cachedMousePosition = mouseEventPosition;
    }

    private Point calculateMouseMoveDelta(Point cached, Point current) {
        Point actualCached = this.referentCoordinateSystem.getCoordinateSystemAbsolutePositionFromScreenPosition(cached);
        Point actualCurrent = this.referentCoordinateSystem.getCoordinateSystemAbsolutePositionFromScreenPosition(current);
        int deltaX = actualCached.x - actualCurrent.x;
        int deltaY = actualCached.y - actualCurrent.y;

        return new Point(deltaX, deltaY);
    }

    private Point moveGivenPointOppositeFromMouseDelta(Point currentPoint, Point delta) {
        currentPoint.setLocation(currentPoint.x + delta.x, currentPoint.y + delta.y);
        return currentPoint;
    }

}
