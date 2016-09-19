package co.kukurin.drawing.gui;

import javax.swing.*;
import java.awt.*;

public class DrawingAttributesPanel extends JPanel {

    private final ColorPickerPanel foregroundColorChooser;
    private final ColorPickerPanel backgroundColorChooser;

    public DrawingAttributesPanel() {
        this.foregroundColorChooser = new ColorPickerPanel(Color.RED);
        this.backgroundColorChooser = new ColorPickerPanel(Color.RED);

        this.add(this.foregroundColorChooser);
        this.add(this.backgroundColorChooser);
    }
}
