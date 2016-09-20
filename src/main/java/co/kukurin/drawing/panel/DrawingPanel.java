package co.kukurin.drawing.panel;

import co.kukurin.drawing.attributes.DrawingAttributes;
import co.kukurin.actions.KeyListenerFactory;
import co.kukurin.actions.MouseListenerFactory;
import co.kukurin.custom.Optional;
import co.kukurin.drawing.drawables.Drawable;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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

    // TODO move those if statements into polymorphic behavior
    private void mousePressed(MouseEvent mouseEvent) {
        if (!this.drawingPanelState.isMouseDown()) {
            this.drawingPanelState.setCachedMousePosition(mouseEvent.getPoint());
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
        this.drawingPanelState.setMouseDown(false);
        this.drawingPanelState.setElementCurrentlyBeingDrawn(null);
        this.repaint();
    }

    private void mouseDragged(MouseEvent mouseEvent) {
        Point originLocation = this.drawingAttributes.getTopLeftReferencePoint();

        if (this.drawingPanelState.isSpaceDown()) {
            this.drawingAttributes.setTopLeftReferencePoint(updateOrigin(originLocation, mouseEvent));
            this.drawingPanelState.setCachedMousePosition(mouseEvent.getPoint());
        } else {
            Point absoluteCoordinates = getCoordinateSystemAbsolutePositionFromScreenPosition(mouseEvent.getX(), mouseEvent.getY());
            Optional.ofNullable(this.drawingPanelState.getElementCurrentlyBeingDrawn())
                    .ifPresent(element -> element.updateEndingPoint(absoluteCoordinates.x, absoluteCoordinates.y));
        }

        this.repaint();
    }

    private Point updateOrigin(Point currentOrigin, MouseEvent mouseEvent) {
        // move background opposite to the mouse direction
        int deltaX = this.drawingPanelState.getCachedMousePosition().x - mouseEvent.getX();
        int deltaY = mouseEvent.getY() - this.drawingPanelState.getCachedMousePosition().y;
        currentOrigin.setLocation(currentOrigin.x + deltaX, currentOrigin.y + deltaY);
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

        Point originLocation = this.drawingAttributes.getTopLeftReferencePoint();
        this.drawingModel.getDrawables().stream()
                .filter(this::isWithinBounds)
                .forEach(drawable -> drawable.draw(graphics2D, originLocation.x, originLocation.y));
    }

    private Point getCoordinateSystemAbsolutePositionFromScreenPosition(int screenX, int screenY) {
        Point topLeft = this.drawingAttributes.getTopLeftReferencePoint();
        return new Point(topLeft.x + screenX, topLeft.y - screenY);
    }

    private boolean isWithinBounds(Drawable drawable) {
        Rectangle drawableBounds = drawable.getAbsolutePositionedBoundingBox();
        Rectangle myBounds = Optional.of(this.drawingAttributes.getTopLeftReferencePoint())
                .map(reference -> new Rectangle(reference.x, reference.y, this.getWidth(), this.getHeight()))
                .get();

        Predicate<Integer> testX = i -> i >= myBounds.x && i <= myBounds.x + myBounds.width;
        Predicate<Integer> testY = i -> i <= myBounds.y && i >= myBounds.y - myBounds.height;

        boolean drawableXLargerThanScreen = (drawableBounds.x <= myBounds.x && drawableBounds.x + drawableBounds.width >= myBounds.x + myBounds.width);
        boolean drawableYLargerThanScreen = (-drawableBounds.y >= myBounds.y && -drawableBounds.y - drawableBounds.height <= myBounds.y - myBounds.height);
        boolean startingXOrEndingXWithinViewBounds = testX.test(drawableBounds.x) || testX.test(drawableBounds.x + drawableBounds.width);
        boolean startingYOrEndingYWithinViewBounds = testY.test(-drawableBounds.y) || testY.test(-drawableBounds.y - drawableBounds.height);

        boolean result = (startingXOrEndingXWithinViewBounds || drawableXLargerThanScreen)
                && (startingYOrEndingYWithinViewBounds || drawableYLargerThanScreen);

        return result;
    }

}
