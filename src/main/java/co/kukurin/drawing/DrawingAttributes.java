package co.kukurin.drawing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class DrawingAttributes {

    private Point topLeftReferencePoint;
    private Color selectedForegroundColor;
    private Color selectedBackgroundColor;

}
