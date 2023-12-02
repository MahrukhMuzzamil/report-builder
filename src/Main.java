import Interface.Report;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Report().setVisible(true);
        });
    }
}
