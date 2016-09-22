package co.kukurin;

import co.kukurin.drawing.attributes.DrawingAttributes;
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
        DrawingModel drawingModel = new DrawingModelImpl();
        Dimension preferredSize = new Dimension(600, 600);
        Point origin = new Point(0, 0); // TODO turn into rectangle
        Point endpoint = new Point(preferredSize.width, -preferredSize.height);
        DrawingAttributes drawingAttributes = new DrawingAttributes(origin, endpoint, Color.BLACK, Color.WHITE);
        DrawingPanelState drawingPanelState = new DrawingPanelState(DrawableRectangle::new, null, origin, null, false, false);
        DrawingPanelDrawListener drawListener = new DrawingPanelDrawListener(drawingModel, drawingAttributes);
        DrawingPanelScreenTranslateListener screenTranslateListener = new DrawingPanelScreenTranslateListener(drawingAttributes);
        DrawingPanel drawingPanel = new DrawingPanel(drawingModel, drawingPanelState, drawingAttributes, drawListener, screenTranslateListener);
        DrawingAttributesPanel drawingAttributesPanel = new DrawingAttributesPanel(drawingAttributes);

        // TODO get rid of this asap.
        screenTranslateListener.setDrawingPanel(drawingPanel);
        drawListener.setDrawingPanel(drawingPanel);

        return new Application(drawingPanel, drawingAttributesPanel, preferredSize);
    }

}
