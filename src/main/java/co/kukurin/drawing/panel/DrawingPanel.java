package co.kukurin.drawing.panel;

import co.kukurin.actions.ComponentListenerFactory;
import co.kukurin.drawing.attributes.DrawingAttributes;
import co.kukurin.actions.KeyListenerFactory;
import co.kukurin.actions.MouseListenerFactory;
import co.kukurin.custom.Optional;
import co.kukurin.drawing.drawables.Drawable;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Predicate;

@Slf4j
public class DrawingPanel extends JPanel {

    private static final Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
    private static final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

    private DrawingAttributes drawingAttributes;
    private DrawingPanelState drawingPanelState;
    private DrawingModel drawingModel;

    public DrawingPanel(DrawingModel drawingModel,
                        DrawingPanelState drawingPanelState,
                        DrawingAttributes drawingAttributes) {
        this.drawingModel = drawingModel;
        this.drawingAttributes = drawingAttributes;
        this.drawingPanelState = drawingPanelState;

        this.setBackground(Color.WHITE);
        this.addMouseMotionListener(MouseListenerFactory.createMouseDragListener(this::mouseDragged));
        this.addComponentListener(ComponentListenerFactory.createResizeListener(this::onResize));
        this.addMouseWheelListener(this::scrollWheelMoved);
        this.addMouseListener(MouseListenerFactory.builder()
                .onPress(this::mousePressed)
                .onRelease(this::mouseReleased).build());
        this.addKeyListener(KeyListenerFactory.builder()
                .onPress(this::keyPressed)
                .onRelease(this::keyReleased).build());
        this.setFocusable(true);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.requestFocusInWindow();
    }

    private void onResize(ComponentEvent componentEvent) {
        Point bottomRightAfterResize = getCoordinateSystemAbsolutePositionFromScreenPosition(getWidth(), getHeight());
        drawingAttributes.setBottomRightReferencePoint(bottomRightAfterResize);
    }

    private void scrollWheelMoved(MouseWheelEvent mouseWheelEvent) {
        int rotation = mouseWheelEvent.getWheelRotation();
        double scale = this.getWidth() / (double) this.getHeight();

        Point currentReference = this.drawingAttributes.getBottomRightReferencePoint();
        currentReference.x += rotation * 10 * scale; // todo max(topleft, current)
        currentReference.y -= rotation * 10;

        Point currentOrigin = this.drawingAttributes.getTopLeftReferencePoint();
        if(currentOrigin.x < currentReference.x
                && currentOrigin.y > currentReference.y) {
            this.drawingAttributes.setBottomRightReferencePoint(currentReference);
        } else {
            log.debug("reached largest available size");
        }

        this.repaint();
    }

    // TODO move those if statements into polymorphic behavior
    private void mousePressed(MouseEvent mouseEvent) {
        if (!this.drawingPanelState.isMouseDown()) {
            this.drawingPanelState.setCachedMousePosition(mouseEvent.getPoint());
            this.drawingPanelState.setCachedTopLeftReferencePosition(this.drawingAttributes.getTopLeftReferencePoint());
        }

        if (!this.drawingPanelState.isSpaceDown()) {
            newDrawableFromSelected(mouseEvent)
                    .ifPresent(drawable -> {
                        this.drawingModel.addDrawable(drawable);
                        this.drawingPanelState.setElementCurrentlyBeingDrawn(drawable);
                    });
        }

        this.drawingPanelState.setMouseDown(true);
        this.repaint();
    }

    private Optional<Drawable> newDrawableFromSelected(MouseEvent mouseEvent) {
        Point absoluteCoordinates = getCoordinateSystemAbsolutePositionFromScreenPosition(mouseEvent.getX(), mouseEvent.getY());
        return Optional.ofNullable(this.drawingPanelState.getActiveDrawableProducer())
                .map(selectedDrawable -> selectedDrawable.create(
                        absoluteCoordinates.x,
                        absoluteCoordinates.y,
                        this.drawingAttributes.getSelectedForegroundColor(),
                        this.drawingAttributes.getSelectedBackgroundColor()));
    }

    private void mouseReleased(MouseEvent mouseEvent) {
        log.info("origin position {}", this.drawingAttributes.getTopLeftReferencePoint());

        this.drawingPanelState.setMouseDown(false);
        this.drawingPanelState.setElementCurrentlyBeingDrawn(null);
        this.repaint();
    }

    private void mouseDragged(MouseEvent mouseEvent) {
        Point originLocation = this.drawingAttributes.getTopLeftReferencePoint();

        if (this.drawingPanelState.isSpaceDown()) {
            Point newTopLeft = updatePositionAccordingToMouseDelta(originLocation, mouseEvent);
            Point newBottomRight = updatePositionAccordingToMouseDelta(this.drawingAttributes.getBottomRightReferencePoint(), mouseEvent);

            this.drawingAttributes.setTopLeftReferencePoint(newTopLeft);
            this.drawingAttributes.setBottomRightReferencePoint(newBottomRight);
            this.drawingPanelState.setCachedMousePosition(mouseEvent.getPoint());
        } else {
            Point absoluteCoordinates = getCoordinateSystemAbsolutePositionFromScreenPosition(mouseEvent.getX(), mouseEvent.getY());
            Optional.ofNullable(this.drawingPanelState.getElementCurrentlyBeingDrawn())
                    .ifPresent(element -> element.updateEndingPoint(absoluteCoordinates.x, absoluteCoordinates.y));
        }

        this.repaint();
    }

    private Point updatePositionAccordingToMouseDelta(Point currentOrigin, MouseEvent mouseEvent) {
        // move background opposite to the mouse direction
        Point mouseEventPoint = getCoordinateSystemAbsolutePositionFromScreenPosition(mouseEvent.getPoint());
        Point cachedMousePoint = getCoordinateSystemAbsolutePositionFromScreenPosition(this.drawingPanelState.getCachedMousePosition());

        double deltaX = cachedMousePoint.getX() - mouseEventPoint.getX();
        double deltaY = mouseEventPoint.getY() - cachedMousePoint.getY();

        currentOrigin.setLocation(currentOrigin.x + deltaX, currentOrigin.y - deltaY);
        return currentOrigin;

    }

    private void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            this.drawingPanelState.setSpaceDown(true);
            this.setCursor(moveCursor);
        }
    }

    private void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            this.drawingPanelState.setSpaceDown(false);
            this.setCursor(defaultCursor);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;

        final Point originLocation = this.drawingAttributes.getTopLeftReferencePoint();
        Point topLeft = this.drawingAttributes.getTopLeftReferencePoint();
        Point bottomRight = this.drawingAttributes.getBottomRightReferencePoint();
        final double scale = (bottomRight.x - topLeft.x) / (double)this.getWidth();
        this.drawingModel.getDrawables().stream()
                .filter(this::isWithinBounds)
                .forEach(drawable -> drawable.draw(graphics2D, originLocation.x, originLocation.y, scale));
    }

    private Point getCoordinateSystemAbsolutePositionFromScreenPosition(Point point) {
        return this.getCoordinateSystemAbsolutePositionFromScreenPosition(point.x, point.y);
    }

    private Point getCoordinateSystemAbsolutePositionFromScreenPosition(int screenX, int screenY) {
        Point topLeft = this.drawingAttributes.getTopLeftReferencePoint();
        Point bottomRight = this.drawingAttributes.getBottomRightReferencePoint();
        double scaleFactorX = (bottomRight.x - topLeft.x) / (double)this.getWidth();
        return new Point(
                (int) (topLeft.x + (screenX) * scaleFactorX),
                (int) (topLeft.y - (screenY) * scaleFactorX));
    }

    // "almost" working. probably will be fixed when the x:y relationship issue is resolved
    private boolean isWithinBounds(Drawable drawable) {
        Rectangle drawableBounds = drawable.getAbsolutePositionedBoundingBox();
        Point topLeft = this.drawingAttributes.getTopLeftReferencePoint();
        Point bottomRight = this.drawingAttributes.getBottomRightReferencePoint();

        Predicate<Integer> testX = i -> i >= topLeft.x && i <= bottomRight.x;
        Predicate<Integer> testY = i -> i <= topLeft.y && i >= bottomRight.y;

        boolean drawableXLargerThanScreen = (drawableBounds.x <= topLeft.x && drawableBounds.x + drawableBounds.width >= bottomRight.x);
        boolean drawableYLargerThanScreen = (-drawableBounds.y >= topLeft.y && -drawableBounds.y - drawableBounds.height <= bottomRight.y);
        boolean startingXOrEndingXWithinViewBounds = testX.test(drawableBounds.x) || testX.test(drawableBounds.x + drawableBounds.width);
        boolean startingYOrEndingYWithinViewBounds = testY.test(-drawableBounds.y) || testY.test(-drawableBounds.y - drawableBounds.height);

        boolean result = (startingXOrEndingXWithinViewBounds || drawableXLargerThanScreen)
                && (startingYOrEndingYWithinViewBounds || drawableYLargerThanScreen);

        return result;
    }

}
