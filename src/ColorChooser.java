package Interface;

import javax.swing.*;
import java.awt.*;

public class ColorChooser {

    public static Color chooseColor(Component parent) {
        Color initialColor = Color.BLACK; // Default color
        return JColorChooser.showDialog(parent, "Choose a Color", initialColor);
    }
}
