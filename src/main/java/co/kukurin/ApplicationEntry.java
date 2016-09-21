package co.kukurin;

import co.kukurin.drawing.attributes.DrawingAttributes;
import co.kukurin.drawing.drawables.DrawableRectangle;
import co.kukurin.drawing.attributes.DrawingAttributesPanel;
import co.kukurin.drawing.panel.DrawingModel;
import co.kukurin.drawing.panel.DrawingModelImpl;
import co.kukurin.drawing.panel.DrawingPanel;
import co.kukurin.drawing.panel.DrawingPanelState;

import javax.swing.*;
import java.awt.*;

public class ApplicationEntry {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApplicationEntry::defaultApplication);
    }

    private static Application defaultApplication() {
        DrawingModel drawingModel = new DrawingModelImpl();
        Dimension preferredSize = new Dimension(600, 600);
        Point origin = new Point(0, 0);
        Point endpoint = new Point(preferredSize.width, -preferredSize.height);
        DrawingAttributes drawingAttributes = new DrawingAttributes(origin, endpoint, 0.5, Color.BLACK, Color.WHITE);
        DrawingPanelState drawingPanelState = new DrawingPanelState(DrawableRectangle::new, null, origin, null, false, false);
        DrawingPanel drawingPanel = new DrawingPanel(drawingModel, drawingPanelState, drawingAttributes);
        DrawingAttributesPanel drawingAttributesPanel = new DrawingAttributesPanel(drawingAttributes);

        return new Application(drawingPanel, drawingAttributesPanel, preferredSize);
    }

}
