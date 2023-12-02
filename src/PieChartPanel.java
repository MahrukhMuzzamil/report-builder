package Interface;

import Buisness.PieChart;
import DataAccess.CsvDAO;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;

public class PieChartPanel extends JPanel {
    public HashMap<String, Integer> pieChartData;
    private ChartPanel pieChartPanelComponent;
    private double chartScaleFactor = 0.8;
    public PieChartPanel() {
        setSize(400, 400);
        pieChartPanelComponent = new ChartPanel(null);
        pieChartPanelComponent.setLayout(new BorderLayout());
        pieChartPanelComponent.setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    createContextMenu().show(PieChartPanel.this, e.getX(), e.getY());
                }
            }
        });

        pieChartPanelComponent.setPreferredSize(new Dimension(
                (int) (pieChartPanelComponent.getPreferredSize().width * chartScaleFactor),
                (int) (pieChartPanelComponent.getPreferredSize().height * chartScaleFactor)
        ));

        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.add(pieChartPanelComponent);
    }
    void loadPieChart(){
        pieChartData = CsvDAO.loadPieChart();
        drawPieChart();
    }
    void addPieChart(){
        String dataset = JOptionPane.showInputDialog("Enter Pie Chart dataset with the item names (e.g. abc:43,def:56):");
        if (dataset != null && !dataset.isEmpty()) {
            pieChartData = CsvDAO.parseDataset(dataset);
            drawPieChart();
        }
    }
    private JPopupMenu createContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Save");
        menu.add(menuItem);

        menuItem.addActionListener(e -> {
            CsvDAO.savePieChartData(pieChartData,this
            );});
        return menu;
    }
    void drawPieChart() {
        System.out.println("Drawing Pie Chart");

        JFreeChart pieChart = new PieChart(pieChartData).showPie();
        ChartPanel chartPanel1 = new ChartPanel(pieChart);

        if (pieChartPanelComponent == null) {
            System.out.println("chartPanelComponent is null. Make sure it is properly initialized.");
            return;
        }
        this.pieChartPanelComponent.removeAll();
        pieChartPanelComponent.add(chartPanel1, BorderLayout.CENTER);
        pieChartPanelComponent.revalidate();
        pieChartPanelComponent.repaint();
    }
}