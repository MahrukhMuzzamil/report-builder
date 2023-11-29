package Buisness;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineGraph {

    private XYSeriesCollection dataset;
    private Map<String, XYSeries> seriesMap;

    public LineGraph() {
        dataset = new XYSeriesCollection();
        seriesMap = new HashMap<>();
    }

    public void addSeries(String seriesName) {
        XYSeries series = new XYSeries(seriesName);
        dataset.addSeries(series);
        seriesMap.put(seriesName, series);
    }

    public void addDataPoint(String seriesName, double xValue, double yValue) {
        if (seriesMap.containsKey(seriesName)) {
            XYSeries series = seriesMap.get(seriesName);
            series.add(xValue, yValue);
        }
    }

    public JFreeChart createLineChart(String title, String xAxisLabel, String yAxisLabel) {
        return ChartFactory.createXYLineChart(
                title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false
        );
    }

    public void loadData(Map<String, List<double[]>> dataMap) {
        for (String seriesName : dataMap.keySet()) {
            if (seriesMap.containsKey(seriesName)) {
                XYSeries series = seriesMap.get(seriesName);
                for (double[] dataPoint : dataMap.get(seriesName)) {
                    series.add(dataPoint[0], dataPoint[1]);
                }
            }
        }
    }
    public Map<String, List<double[]>> getData() {
        Map<String, List<double[]>> dataMap = new HashMap<>();

        for (Map.Entry<String, XYSeries> entry : seriesMap.entrySet()) {
            String seriesName = entry.getKey();
            XYSeries series = entry.getValue();
            List<double[]> seriesData = new ArrayList<>();

            for (int i = 0; i < series.getItemCount(); i++) {
                double xValue = series.getX(i).doubleValue();
                double yValue = series.getY(i).doubleValue();
                seriesData.add(new double[]{xValue, yValue});
            }

            dataMap.put(seriesName, seriesData);
        }

        return dataMap;
    }

    public List<String> getSeriesNames() {
        return new ArrayList<>(seriesMap.keySet());
    }
}
