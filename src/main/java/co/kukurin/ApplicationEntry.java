package co.kukurin;

import javax.swing.*;
import java.awt.*;

public class ApplicationEntry {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Application(new DrawingPanel(new DrawingModelImpl(), 0, 0, 600, 600)));
    }

    public void printAll(String baseString) {
        printAll(baseString.toCharArray(), 0);
    }

    private void printAll(char[] items, int i) {
        if(i >= items.length - 1) {
            System.out.println(items);
            return;
        }

        char c = items[i];
        for(int j = i + 1; j < items.length; j++) {
            printAll(items, i + 1);

            items[j - 1] = items[i];
            items[i] = items[j];
            items[j] = c;
        }

        printAll(items, i + 1);
        items[items.length - 1] = items[i];
        items[i] = c;
    }

}
