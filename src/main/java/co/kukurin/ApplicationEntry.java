package co.kukurin;

import co.kukurin.drawing.*;
import co.kukurin.drawing.drawables.DrawableRectangle;
import co.kukurin.drawing.gui.DrawingAttributesPanel;
import co.kukurin.drawing.gui.DrawingPanel;

import javax.swing.*;
import java.awt.*;

// TODO color inject, change drawingattributes to the panel
public class ApplicationEntry {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApplicationEntry::defaultApplication);
    }

    private static Application defaultApplication() {
        DrawingModel drawingModel = new DrawingModelImpl();
        DrawingAttributes drawingAttributes = new DrawingAttributes(new Point(0, 0), Color.BLACK, Color.WHITE);
        DrawingPanelState drawingPanelState = new DrawingPanelState(DrawableRectangle::new, null, null, false, false);
        DrawingPanel drawingPanel = new DrawingPanel(drawingModel, drawingPanelState, drawingAttributes);
        DrawingAttributesPanel drawingAttributesPanel = new DrawingAttributesPanel();
        Dimension preferredSize = new Dimension(600, 600);

        return new Application(drawingPanel, drawingAttributesPanel, preferredSize);
    }

}
