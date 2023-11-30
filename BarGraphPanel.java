package Interface;

import Buisness.BarGraph;
import DataAccess.CsvDAO;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class BarGraphPanel extends JPanel {
    private int barWidth = 50;
    private int barHeight = 25;
    private double chartScaleFactor = 0.8;
    private int barX = 50;
    private int barY = 100;
    private Point dragStart;
    private boolean resizing = false;
    private Point resizeStart;
    private BarGraph barGraph;
    private JFreeChart barChart;
    private String xAxisLabel = " ";
    private String yAxisLabel = " ";
    private String[] axisNames;
    int numberOfBars = -1;
    String[] series;
    private HashMap<String, HashMap<String, Double>> barGraphData;
    private ChartPanel chartPanel;
    String category = "";

    public BarGraphPanel() {
        barGraph = new BarGraph();
        barGraphData = new HashMap<>();


        barChart = barGraph.createBarChart("Bar Chart", xAxisLabel, yAxisLabel);
        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
        plot.getDomainAxis().setCategoryMargin(0);

        chartPanel = new ChartPanel(barChart);

        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        chartPanel.setPreferredSize(new Dimension(
                (int) (chartPanel.getPreferredSize().width * chartScaleFactor),
                (int) (chartPanel.getPreferredSize().height * chartScaleFactor)
        ));

        add(chartPanel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    createContextMenu().show(BarGraphPanel.this, e.getX(), e.getY());
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isInsideBar(e.getPoint())) {
                    dragStart = e.getPoint();
                    resizing = isNearHandle(e.getPoint());
                    if (resizing) {
                        resizeStart = e.getPoint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
                resizing = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    if (resizing) {
                        int dx = e.getX() - resizeStart.x;
                        int dy = e.getY() - resizeStart.y;
                        barWidth += dx;
                        barHeight += dy;
                        resizeStart = e.getPoint();
                    } else {
                        int dx = e.getX() - dragStart.x;
                        int dy = e.getY() - dragStart.y;
                        barX += dx;
                        barY += dy;
                        dragStart = e.getPoint();
                    }
                    repaint();
                    updateChart();
                }
            }
        });
    }
    private JPopupMenu createContextMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Save");
        menu.add(menuItem);

        menuItem.addActionListener(e -> {
            CsvDAO.saveBarChartData(axisNames[0],axisNames[1],numberOfBars,barGraphData,this);
        });
        return menu;
    }
    private boolean isInsideBar(Point point) {
        return point.x >= barX && point.x <= barX + barWidth &&
                point.y >= barY && point.y <= barY + barHeight;
    }
    private boolean isNearHandle(Point point) {
        int handleX = barX + barWidth - 8;
        int handleY = barY + barHeight - 8;
        return point.x >= handleX && point.x <= barX + barWidth &&
                point.y >= handleY && point.y <= barY + barHeight;
    }

    public void addBarChart(){
        collectUserData();
        updateChart();
    }

    private void updateChart() {
        barChart = barGraph.createBarChart("", xAxisLabel, yAxisLabel);
        chartPanel.setChart(barChart);
    }

    private void setNumberOfBars() {
        String input = JOptionPane.showInputDialog("Enter the number of bars:");
        try {
            barGraphData = CsvDAO.loadBarChartData(axisNames);
            numberOfBars = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }

    private void collectUserData() {
        if (axisNames == null) {
            axisNames = new String[2];
        }
        if (numberOfBars == -1) {
            setNumberOfBars();
            series = new String[numberOfBars];
            Arrays.fill(series, "");
        }

        if ((numberOfBars) <= 0) {
            JOptionPane.showMessageDialog(this, "Number of bars should be greater than 0.");
            return;
        }

        if (Objects.equals(xAxisLabel, " ") && Objects.equals(yAxisLabel, " ")) {
            xAxisLabel = JOptionPane.showInputDialog("Enter X-Axis Label:");
            if (xAxisLabel == null || xAxisLabel.isEmpty()) {
                return;
            }

            axisNames[0] = xAxisLabel;

            yAxisLabel = JOptionPane.showInputDialog("Enter Y-Axis Label:");
            if (yAxisLabel == null || yAxisLabel.isEmpty()) {
                return;
            }
            axisNames[1] = yAxisLabel;

            CategoryPlot plot = (CategoryPlot) barChart.getPlot();
            plot.getDomainAxis().setLabel(axisNames[0]);
            plot.getRangeAxis().setLabel(axisNames[1]);
        }

        HashMap<String, Double> innerMap = new HashMap<>();

        category = JOptionPane.showInputDialog("Enter Category for Bar ");

        if (category != null && !category.isEmpty()) {

            for (int i = 0; i < numberOfBars; i++) {

                if (series[i].isEmpty()) {
                    series[i] = JOptionPane.showInputDialog("Enter Series for Bar " + (i + 1) + ":");
                }
                if (series[i] != null && !series[i].isEmpty()) {
                    String value = JOptionPane.showInputDialog("Enter Value for Bar " + (i + 1) + ":");
                    if (value != null && !value.isEmpty()) {
                        try {
                            double valueNum = Double.parseDouble(value);
                            barGraph.addData(series[i], category, valueNum);
                            innerMap.put(series[i], valueNum);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid Value. Please enter a valid number.");
                        }
                    }
                }
            }
            barGraphData.put(category, innerMap);
        }
    }
    public void loadFromFile(){
        axisNames=new String[3];
        barGraphData= CsvDAO.loadBarChartData(axisNames);
        assert axisNames[2]!=null;
        numberOfBars=Integer.parseInt(axisNames[2]);
        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
        plot.getDomainAxis().setLabel(axisNames[0]);
        plot.getRangeAxis().setLabel(axisNames[1]);
        barGraph.loadData(barGraphData);
        xAxisLabel=axisNames[0];
        yAxisLabel=axisNames[1];
        series= barGraph.getSeriesNames();
        barChart = barGraph.createBarChart("Bar Chart", axisNames[0], axisNames[1]);
        CategoryPlot plot1 = (CategoryPlot) barChart.getPlot();
        plot.getDomainAxis().setCategoryMargin(0.2); // Adjust the margin as needed
    }
}
