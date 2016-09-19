package co.kukurin.drawing.gui;

import co.kukurin.drawing.DrawingAttributes;
import co.kukurin.drawing.DrawingModel;
import co.kukurin.drawing.DrawingPanelState;
import co.kukurin.actions.KeyListenerFactory;
import co.kukurin.actions.MouseListenerFactory;
import co.kukurin.custom.Optional;
import co.kukurin.drawing.drawables.Drawable;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
        this.requestFocusInWindow();
    }

    // TODO move those if statements into polymorphic behavior
    private void mousePressed(MouseEvent mouseEvent) {
        if(!this.drawingPanelState.isMouseDown()) {
            this.drawingPanelState.setCachedMousePosition(mouseEvent.getPoint());
        }

        if(!this.drawingPanelState.isSpaceDown()) {
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
        Point originLocation = this.drawingAttributes.getOriginLocation();
        return Optional.ofNullable(this.drawingPanelState.getActiveDrawableProducer())
                .map(selectedDrawable -> selectedDrawable.create(
                        mouseEvent.getX() - originLocation.x,
                        mouseEvent.getY() - originLocation.y,
                        Color.BLACK,
                        Color.WHITE));
    }

    private void mouseReleased(MouseEvent mouseEvent) {
        this.drawingPanelState.setMouseDown(false);
        this.drawingPanelState.setElementCurrentlyBeingDrawn(null);
        this.repaint();
    }

    private void mouseDragged(MouseEvent mouseEvent) {
        Point originLocation = this.drawingAttributes.getOriginLocation();

        if(this.drawingPanelState.isSpaceDown()) {
            this.drawingAttributes.setOriginLocation(updateOrigin(originLocation, mouseEvent));
            this.drawingPanelState.setCachedMousePosition(mouseEvent.getPoint());
        } else {
            Optional.ofNullable(this.drawingPanelState.getElementCurrentlyBeingDrawn())
                    .ifPresent(element -> element.updateEndingPoint(
                            mouseEvent.getX() - originLocation.x,
                            mouseEvent.getY() - originLocation.y));
        }

        this.repaint();
    }

    private Point updateOrigin(Point currentOrigin, MouseEvent mouseEvent) {
        int deltaX = mouseEvent.getX() - this.drawingPanelState.getCachedMousePosition().x;
        int deltaY = mouseEvent.getY() - this.drawingPanelState.getCachedMousePosition().y;
        currentOrigin.setLocation(currentOrigin.x + deltaX, currentOrigin.y + deltaY);
        return currentOrigin;
    }

    private void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            this.drawingPanelState.setSpaceDown(true);
            this.setCursor(moveCursor);
        }
    }

    private void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            this.drawingPanelState.setSpaceDown(false);
            this.setCursor(defaultCursor);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;

        // TODO "lazy drawDelegate"
        Point originLocation = this.drawingAttributes.getOriginLocation();
        this.drawingModel.getDrawables().stream()
                .forEach(drawable -> drawable.draw(graphics2D, originLocation.x, originLocation.y));
    }

}
