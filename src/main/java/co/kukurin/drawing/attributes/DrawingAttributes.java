package co.kukurin.drawing.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class DrawingAttributes {

    private Point topLeftReferencePoint;
    private Point bottomRightReferencePoint;
    private Color selectedForegroundColor;
    private Color selectedBackgroundColor;

}
