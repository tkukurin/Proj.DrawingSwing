package co.kukurin.drawing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class DrawingAttributes {

    private Point originLocation;
    private Color selectedForegroundColor;
    private Color selectedBackgroundColor;

}
