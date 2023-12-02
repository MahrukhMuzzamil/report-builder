package Interface;

import Buisness.Table;
import DataAccess.CsvDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TablePanel extends JPanel {
    Table table;
    JScrollPane scrollPane;
    JTextArea textArea;
    public TablePanel() {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    createContextMenu().show(TablePanel.this, e.getX(), e.getY());
                }
            }
        });
    }
    private JPopupMenu createContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Save");
        menu.add(menuItem);

        menuItem.addActionListener(e -> {
            CsvDAO.saveTableData(table.getTableData(),table.getRows(),table.getCols(),this);
        });
        return menu;
    }

    void setRows(int r, int c) {
        if (table == null) {
            table = new Table(r, c);
        } else {
              table = new Table(r, c);
        }
    }
    public void collectData() {
        int rows = 0, columns = 0;

        String rowsInput = JOptionPane.showInputDialog("Enter number of rows:");
        String columnsInput = JOptionPane.showInputDialog("Enter number of columns:");

        if (rowsInput != null && columnsInput != null) {
            try {
                rows = Integer.parseInt(rowsInput);
                columns = Integer.parseInt(columnsInput);

                setRows(rows, columns);

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        textArea.setText("");
                        int result = JOptionPane.showOptionDialog(
                                this,
                                scrollPane,
                                "Enter Text for cell (" + i + ", " + j + "):",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                null,
                                null
                        );

                        if (result == JOptionPane.OK_OPTION) {
                            table.setTableData(i, j, textArea.getText());
                        }
                        else {
                        }
                    }
                }
                repaint();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for rows and columns.");
            }
        }
    }

    public void loadFromFile(){
        int []arr = new int[2];
        String [][] data=CsvDAO.loadTableData(arr);
        table=new Table(arr[0],arr[1]);
        table.setDataSet(data);
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Check if table is null
        if (table != null) {
            int numRows = table.getTableData().length;
            int numCols = table.getTableData()[0].length;

            int cellWidth = getWidth() / numCols;
            int cellHeight = getHeight() / numRows;

            // Adjust the scale factor for cell size
            double scaleFactor = 0.8; // You can change this value to make cells smaller or larger

            cellWidth = (int) (cellWidth * scaleFactor);
            cellHeight = (int) (cellHeight * scaleFactor);

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    if (table.getTableData()[i][j] != null) {
                        g.drawString(table.getTableData()[i][j], j * cellWidth, (i + 1) * cellHeight);
                    }
                }
            }

            for (int i = 0; i <= numRows; i++) {
                g.drawLine(0, i * cellHeight, getWidth(), i * cellHeight);
            }

            for (int j = 0; j <= numCols; j++) {
                g.drawLine(j * cellWidth, 0, j * cellWidth, getHeight());
            }
        }
    }

}
