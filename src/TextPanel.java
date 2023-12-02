package Interface;

import Buisness.Text;
import DataAccess.CsvDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class TextPanel extends JPanel {
    Text text;
    private Point textPosition;
    private JTextArea textArea;
    JScrollPane scrollPane;


    public TextPanel() {
        setBackground(Color.white);
        text = new Text();
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    createContextMenu().show(TextPanel.this, e.getX(), e.getY());
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (text.getText() != null) {
                    textPosition = new Point(e.getX(), e.getY());
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (text.getText() != null) {
                    textPosition = new Point(e.getX(), e.getY());
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (text.getText() != null) {
                    textPosition = new Point(e.getX(), e.getY());
                    repaint();
                }
            }
        });
    }
    private JPopupMenu createContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Save");
        menu.add(menuItem);

        menuItem.addActionListener(e -> {
            saveText();
        });
        return menu;
    }
    protected void addText() {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);

        int result = JOptionPane.showOptionDialog(
                this,
                scrollPane,
                "Enter Text:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );

        if (result == JOptionPane.OK_OPTION) {
            text.setText(textArea.getText());
            Font font = new Font("Times New Roman", Font.PLAIN, 15);
            text.setFont(font);
            textPosition = new Point(50, 50);
            repaint();
        }
    }
    public void saveText(){
        CsvDAO.saveText(text.getText(),this);
    }
    public void loadText(){
        text.setText(CsvDAO.loadText());
        Font font = new Font("Times New Roman", Font.PLAIN, 15);
        text.setFont(font);
        textPosition = new Point(50, 50);
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (text.getText() != null) {
            Font font = text.getFont();
            FontMetrics fontMetrics = getFontMetrics(font);
            FontRenderContext frc = new FontRenderContext(null, true, true);

            String[] lines = text.getText().split("\n");
            int lineHeight = fontMetrics.getHeight();
            int currentY = textPosition.y; // Store the initial Y position

            for (String line : lines) {
                Rectangle2D bounds = font.getStringBounds(line, frc);
                g.setFont(font); // Set the font before drawing each line
                g.drawString(line, textPosition.x, currentY);
                currentY += lineHeight;
            }
        }
    }

}
