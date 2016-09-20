package co.kukurin.drawing.attributes.components;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BorderFactory.*;

public class ColorPickerPanel extends JPanel {

    private Color color;

    public ColorPickerPanel(Color color) {
        this.setColor(color);
        this.setBorder(createLineBorder(Color.WHITE, 2));
    }

    public void setColor(Color color) {
        this.color = color;
        setBackground(this.color);
    }
}
