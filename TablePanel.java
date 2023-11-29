package Interface;

import Buisness.Table;

import javax.swing.*;
import java.awt.*;

class TablePanel extends JPanel {
    Table table;
    JScrollPane scrollPane;
    JTextArea textArea;
    public TablePanel(int r, int c) {
        table=new Table(r,c);
       // tableData = new String[t.getRows()][t.getCols()];
         textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane= new JScrollPane(textArea);

    }
    void setRows(int r,int c){
        table.setRows(r);
        table.setCols(c);
    }
    void addTable() {
        int row = Integer.parseInt(JOptionPane.showInputDialog("Enter row index:"));
        int column = Integer.parseInt(JOptionPane.showInputDialog("Enter column index:"));
        ;
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
            table.setTableData(row,column,textArea.getText());
            repaint();
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellWidth = getWidth() / table.getTableData()[0].length;
        int cellHeight = getHeight() / table.getTableData().length;

        for (int i = 0; i < table.getTableData().length; i++) {
            for (int j = 0; j < table.getTableData()[0].length; j++) {
                if (table.getTableData()[i][j] != null) {
                    g.drawString(table.getTableData()[i][j], j * cellWidth, (i + 1) * cellHeight);
                }
            }
        }

        for (int i = 0; i <= table.getTableData().length; i++) {
            g.drawLine(0, i * cellHeight, getWidth(), i * cellHeight);
        }

        for (int j = 0; j <= table.getTableData()[0].length; j++) {
            g.drawLine(j * cellWidth, 0, j * cellWidth, getHeight());
        }
    }
}
