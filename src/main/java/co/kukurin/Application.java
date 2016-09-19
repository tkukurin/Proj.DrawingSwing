package co.kukurin;

import co.kukurin.actions.KeyListenerFactory;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class Application extends JFrame {

    private final DrawingPanel drawingPanel;

    public Application(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        this.transferFocusDownCycle();
        this.setLayout(new BorderLayout());
        this.add(drawingPanel, BorderLayout.CENTER);
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
