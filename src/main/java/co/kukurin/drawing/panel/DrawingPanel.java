package co.kukurin.drawing.panel;

import co.kukurin.actions.ComponentListenerFactory;
import co.kukurin.custom.Optional;
import co.kukurin.drawing.attributes.DrawingAttributes;
import co.kukurin.actions.KeyListenerFactory;
import co.kukurin.actions.MouseListenerFactory;
import co.kukurin.drawing.drawables.Drawable;
import co.kukurin.drawing.panel.mouse.DrawingPanelDrawListener;
import co.kukurin.drawing.panel.mouse.DrawingPanelMouseListener;
import co.kukurin.drawing.panel.mouse.DrawingPanelScreenTranslateListener;
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

    private final DrawingPanelDrawListener drawListener;
    private final DrawingPanelScreenTranslateListener screenTranslateListener;
    private DrawingPanelMouseListener activeMouseListener;
    private final CoordinateSystem coordinateSystem;

    public DrawingPanel(DrawingModel drawingModel,
                        DrawingPanelState drawingPanelState,
                        DrawingAttributes drawingAttributes,
                        DrawingPanelDrawListener drawListener,
                        DrawingPanelScreenTranslateListener screenTranslateListener,
                        CoordinateSystem coordinateSystem) {
        this.drawingModel = drawingModel;
        this.drawingAttributes = drawingAttributes;
        this.drawingPanelState = drawingPanelState;
        this.drawListener = drawListener;
        this.screenTranslateListener = screenTranslateListener;
        this.activeMouseListener = drawListener;
        this.coordinateSystem = coordinateSystem;

        this.setBackground(Color.WHITE);
        this.addComponentListener(ComponentListenerFactory.createResizeListener(this::onResize));
        this.addMouseWheelListener(this::scrollWheelMoved);
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

    private void onResize(ComponentEvent componentEvent) {
        if(this.coordinateSystem.getComponentDimensions() == null) {
            this.coordinateSystem.updateScale(this);
        }

        Point br = this.coordinateSystem.getCoordinateSystemAbsolutePositionFromScreenPosition(getWidth(), getHeight());
        this.coordinateSystem.setBottomRight(br);
        this.coordinateSystem.setComponentDimensions(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D graphics2D = (Graphics2D) g;
        final Point originLocation = this.coordinateSystem.getTopLeft();
        final double scale = this.coordinateSystem.getScaleX();
        this.drawingModel.getDrawables().stream()
                         .filter(this::isWithinBounds)
                         .forEach(drawable -> drawable.draw(graphics2D, originLocation.x, originLocation.y, scale));
    }

    private boolean isWithinBounds(Drawable drawable) {
        Rectangle drawableBounds = drawable.getAbsolutePositionedBoundingBox();
        Point topLeft = this.coordinateSystem.getTopLeft();
        Point bottomRight = this.coordinateSystem.getBottomRight();

        Predicate<Integer> isWithinXBounds = i -> i >= topLeft.x && i <= bottomRight.x;
        Predicate<Integer> isWithinYBounds = i -> i <= topLeft.y && i >= bottomRight.y;

        boolean drawableXLargerThanScreen = (drawableBounds.x <= topLeft.x && drawableBounds.x + drawableBounds.width >= bottomRight.x);
        boolean drawableYLargerThanScreen = (-drawableBounds.y >= topLeft.y && -drawableBounds.y - drawableBounds.height <= bottomRight.y);
        boolean startingXOrEndingXWithinViewBounds = isWithinXBounds.test(drawableBounds.x) || isWithinXBounds.test(drawableBounds.x + drawableBounds.width);
        boolean startingYOrEndingYWithinViewBounds = isWithinYBounds.test(-drawableBounds.y) || isWithinYBounds.test(-drawableBounds.y - drawableBounds.height);

        boolean result = (startingXOrEndingXWithinViewBounds || drawableXLargerThanScreen)
                && (startingYOrEndingYWithinViewBounds || drawableYLargerThanScreen);

        return result;
    }

    private void scrollWheelMoved(MouseWheelEvent mouseWheelEvent) {
        int rotation = mouseWheelEvent.getWheelRotation();
        double actualWidthToActualHeightScale = this.getWidth() / (double) this.getHeight();

        Point currentReference = this.coordinateSystem.getBottomRight();
        currentReference.x += rotation * 10 * actualWidthToActualHeightScale;
        currentReference.y -= rotation * 10;

        Point currentOrigin = this.coordinateSystem.getTopLeft();
        boolean leftXIsSmallerThanRightX = currentOrigin.x < currentReference.x;
        boolean topYIsLargerThanBottomY = currentOrigin.y > currentReference.y;

        if(leftXIsSmallerThanRightX && topYIsLargerThanBottomY) {
            this.coordinateSystem.setBottomRight(currentReference);
            this.coordinateSystem.updateScale(this);
        }

        this.repaint();
    }

    private void mousePressed(MouseEvent mouseEvent) {
        activeMouseListener.mousePressed(mouseEvent);
        this.repaint();
    }
    private void mouseReleased(MouseEvent mouseEvent) {
        activeMouseListener.mouseReleased(mouseEvent);
        this.repaint();
    }

    private void mouseDragged(MouseEvent mouseEvent) {
        activeMouseListener.mouseDragged(mouseEvent);
        this.repaint();
    }

    private void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            this.setCursor(moveCursor);
            this.activeMouseListener = this.screenTranslateListener;
        }
    }

    private void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            this.setCursor(defaultCursor);
            this.activeMouseListener = this.drawListener;
        }
    }
}
