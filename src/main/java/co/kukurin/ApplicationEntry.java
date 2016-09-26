package co.kukurin;

import co.kukurin.drawing.attributes.DrawingAttributes;
import co.kukurin.drawing.drawables.DrawableCircle;
import co.kukurin.drawing.drawables.DrawableRectangle;
import co.kukurin.drawing.attributes.DrawingAttributesPanel;
import co.kukurin.drawing.panel.*;
import co.kukurin.drawing.panel.mouse.DrawingPanelDrawListener;
import co.kukurin.drawing.panel.mouse.DrawingPanelScreenTranslateListener;

import javax.swing.*;
import java.awt.*;

public class ApplicationEntry {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApplicationEntry::defaultApplication);
    }

    private static Application defaultApplication() {

        Dimension preferredSize = new Dimension(600, 600);
        Point origin = new Point(0, 0);
        Point endpoint = new Point(preferredSize.width, -preferredSize.height);
        CoordinateSystem coordinateSystem = new CoordinateSystem(origin, endpoint);

        DrawingAttributes drawingAttributes = new DrawingAttributes(Color.BLACK, Color.WHITE);
        DrawingPanelState drawingPanelState = new DrawingPanelState(DrawableCircle::new);
        DrawingModel drawingModel = new DrawingModelImpl();

        DrawingPanelDrawListener drawListener = new DrawingPanelDrawListener(
                drawingModel, drawingAttributes, coordinateSystem, drawingPanelState);
        DrawingPanelScreenTranslateListener screenTranslateListener = new DrawingPanelScreenTranslateListener(coordinateSystem);

        DrawingPanel drawingPanel = new DrawingPanel(
                drawingModel, drawingPanelState, drawingAttributes, drawListener, screenTranslateListener, coordinateSystem);
        DrawingAttributesPanel drawingAttributesPanel = new DrawingAttributesPanel(drawingAttributes);

        return new Application(drawingPanel, drawingAttributesPanel, preferredSize);
    }

}
