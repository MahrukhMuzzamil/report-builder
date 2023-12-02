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
    private JComboBox<String> seriesComboBox; // For selecting series

    public LineGraphPanel() {
        lineGraph = new LineGraph();
        xAxisLabel = "X Axis";
        yAxisLabel = "Y Axis";
        lineChart = lineGraph.createLineChart("Line Chart", xAxisLabel, yAxisLabel);
        chartPanel = new ChartPanel(lineChart);

        seriesComboBox = new JComboBox<>(); // Initialize the combo box for series selection
        JButton colorButton = new JButton("Choose Color");
        colorButton.addActionListener(e -> chooseSeriesColor());

        JPanel controlPanel = new JPanel(); // Panel for holding control elements
        controlPanel.add(seriesComboBox);
        controlPanel.add(colorButton);

        add(chartPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH); // Add control panel to the south
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
    private void chooseSeriesColor() {
        String seriesName = (String) seriesComboBox.getSelectedItem();
        if (seriesName != null) {
            Color color = ColorChooser.chooseColor(this);
            if (color != null) {
                lineGraph.setSeriesColor(seriesName, color);
                refreshChart();
            }
        }
    }

    private void refreshChart() {
        chartPanel.repaint(); // Repaint the chart panel to reflect the color change
    }
    private void updateChart() {
        lineChart = lineGraph.createLineChart("Line Chart", xAxisLabel, yAxisLabel);
        chartPanel.setChart(lineChart);
        updateSeriesComboBox(); // Update the series combo box whenever the chart is updated
    }

    private void updateSeriesComboBox() {
        seriesComboBox.removeAllItems();
        for (String seriesName : lineGraph.getSeriesNames()) { // Assuming getSeriesNames() method exists
            seriesComboBox.addItem(seriesName);
        }
    }
    public void saveLineGraph(Report report) {
        // Assuming CsvDAO has a method to save line graph data
        CsvDAO.saveLineGraphData(lineGraph, report);
    }



    // Add other necessary methods and functionalities as per your application's requirements
}
