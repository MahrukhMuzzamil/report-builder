package Buisness;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.HashMap;
import java.util.List;

public class BarGraph {

    private DefaultCategoryDataset dataset;
    public BarGraph() {
        dataset = new DefaultCategoryDataset();
    }
    public void addData(String series, String category, double value) {
        dataset.addValue(value, series, category);
    }
    public JFreeChart createBarChart(String chartTitle, String categoryAxisLabel, String valueAxisLabel) {
        return ChartFactory.createBarChart(
                chartTitle,
                categoryAxisLabel,
                valueAxisLabel,
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }
    public  String[] getSeriesNames() {
        List<Comparable<?>> keys = dataset.getRowKeys();
        String[] seriesNames = new String[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            seriesNames[i] = keys.get(i).toString();
        }
        return seriesNames;
    }
    public DefaultCategoryDataset getDataset() {
        return dataset;
    }
    public void clearData() {
        dataset.clear();
    }
    public void loadData(HashMap<String, HashMap<String, Double>> data) {
        for (String series : data.keySet()) {
            for (String category : data.get(series).keySet()) {
                double value = data.get(series).get(category);
                dataset.addValue(value, series, category);
            }
        }
    }
}
