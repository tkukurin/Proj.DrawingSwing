package co.kukurin;

import co.kukurin.drawing.gui.DrawingAttributesPanel;
import co.kukurin.drawing.gui.DrawingPanel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.*;

@Slf4j
public class Application extends JFrame {

    private final DrawingPanel drawingPanel;
    private final DrawingAttributesPanel drawingAttributesPanel;

    public Application(DrawingPanel drawingPanel,
                       DrawingAttributesPanel drawingAttributesPanel,
                       Dimension preferredSize) {
        this.drawingPanel = drawingPanel;
        this.drawingAttributesPanel = drawingAttributesPanel;

        initLayout(drawingPanel, drawingAttributesPanel, preferredSize);
        this.transferFocusDownCycle();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void initLayout(DrawingPanel drawingPanel,
                            DrawingAttributesPanel drawingAttributesPanel,
                            Dimension preferredSize) {
        this.setPreferredSize(preferredSize);
        this.setLayout(new BorderLayout());
        this.add(drawingPanel, CENTER);
        this.add(drawingAttributesPanel, LINE_END);
        this.pack();
    }
}
