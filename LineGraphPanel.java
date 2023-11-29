package Interface;

import Buisness.LineGraph;
import DataAccess.CsvDAO;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class LineGraphPanel extends JPanel {
    private LineGraph lineGraph;
    private JFreeChart lineChart;
    private ChartPanel chartPanel;
    private String xAxisLabel;
    private String yAxisLabel;
    private String[] series;

    public LineGraphPanel() {
        lineGraph = new LineGraph();
        xAxisLabel = "X Axis";
        yAxisLabel = "Y Axis";
        lineChart = lineGraph.createLineChart("Line Chart", xAxisLabel, yAxisLabel);
        chartPanel = new ChartPanel(lineChart);
        add(chartPanel, BorderLayout.CENTER);
    }

    public void addLineChart() {
        String seriesName = JOptionPane.showInputDialog(this, "Enter series name:");
        String xValuesStr = JOptionPane.showInputDialog(this, "Enter X values (comma-separated):");
        String yValuesStr = JOptionPane.showInputDialog(this, "Enter Y values (comma-separated):");

        if (seriesName != null && xValuesStr != null && yValuesStr != null) {
            double[] xValues = Arrays.stream(xValuesStr.split(","))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            double[] yValues = Arrays.stream(yValuesStr.split(","))
                    .mapToDouble(Double::parseDouble)
                    .toArray();

            lineGraph.addSeries(seriesName);
            for (int i = 0; i < xValues.length; i++) {
                lineGraph.addDataPoint(seriesName, xValues[i], yValues[i]);
            }
            updateChart();
        }
    }

    public void loadFromFile() {
        // Assuming CsvDAO.loadLineGraphData returns a Map<String, List<double[]>>
        Map<String, List<double[]>> lineGraphData = CsvDAO.loadLineGraphData();
        lineGraph.loadData(lineGraphData);
        updateChart();
    }

    public void saveLineGraph(Report report) {
        // Assuming CsvDAO has a method to save line graph data
        CsvDAO.saveLineGraphData(lineGraph, report);
    }

    private void updateChart() {
        lineChart = lineGraph.createLineChart("Line Chart", xAxisLabel, yAxisLabel);
        chartPanel.setChart(lineChart);
    }

    // Add other necessary methods and functionalities as per your application's requirements
}
