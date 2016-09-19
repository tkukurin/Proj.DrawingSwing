package co.kukurin;

import javax.swing.*;
import java.awt.*;

public class ApplicationEntry {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApplicationEntry::defaultApplication);
    }

    private static Application defaultApplication() {
        return new Application(new DrawingPanel(new DrawingModelImpl(), 0, 0, 600, 600));
    }

}
