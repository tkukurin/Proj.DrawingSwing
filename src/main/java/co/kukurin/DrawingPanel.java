package co.kukurin;

import co.kukurin.actions.KeyListenerFactory;
import co.kukurin.actions.MouseListenerFactory;
import co.kukurin.custom.Optional;
import co.kukurin.drawing.Drawable;
import co.kukurin.drawing.DrawableRectangle;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Slf4j
public class DrawingPanel extends JPanel {

    private static final Cursor moveCursor = new Cursor(Cursor.HAND_CURSOR);
    private static final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

    private BiFunction<Integer, Integer, Drawable> currentSelectedDrawableSupplier;
    private DrawingModel drawingModel;
    private Rectangle currentPosition;
    private Point cachedMousePosition;
    private boolean isMouseDown;
    private boolean isSpaceDown;

    DrawingPanel(DrawingModel drawingModel,
                 int startX,
                 int startY,
                 int preferredWidth,
                 int preferredHeight) {
        this.drawingModel = drawingModel;
        this.currentPosition = new Rectangle(startX, startY, preferredWidth, preferredHeight);
        this.isMouseDown = false;
        this.currentSelectedDrawableSupplier = DrawableRectangle::new;

        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
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

    private void mousePressed(MouseEvent mouseEvent) {
        if(!this.isMouseDown) {
            this.cachedMousePosition = mouseEvent.getPoint();
        }
        this.isMouseDown = true;
        this.repaint();
    }

    private void mouseReleased(MouseEvent mouseEvent) {
        this.isMouseDown = false;
        if(!this.isSpaceDown) {
            Optional.ofNullable(this.currentSelectedDrawableSupplier)
                    .ifPresent(selectedDrawable -> {
                        this.drawingModel.addDrawable(selectedDrawable.apply(
                                mouseEvent.getX() - this.currentPosition.x,
                                mouseEvent.getY() - this.currentPosition.y));
                        this.repaint();
                    });
        }
    }

    private void mouseDragged(MouseEvent mouseEvent) {
        if(this.isSpaceDown) {
            int deltaX = mouseEvent.getX() - this.cachedMousePosition.x;
            int deltaY = mouseEvent.getY() - this.cachedMousePosition.y;
            this.currentPosition.setLocation(this.currentPosition.x + deltaX, this.currentPosition.y + deltaY);
            this.cachedMousePosition = mouseEvent.getPoint();

            this.repaint();
        }
    }

    private void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            this.isSpaceDown = true;
            this.setCursor(moveCursor);
        }
    }

    private void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            this.isSpaceDown = false;
            this.setCursor(defaultCursor);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;

        // TODO "lazy draw"
        this.drawingModel.getDrawables().stream()
                .forEach(drawable -> drawable.draw(graphics2D, this.currentPosition.x, this.currentPosition.y));
    }

}
