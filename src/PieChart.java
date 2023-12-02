package Buisness;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.util.HashMap;

public class PieChart {
    private DefaultPieDataset pieDataset;
    private HashMap<String, Integer> dataSet;

    public PieChart(HashMap<String, Integer> data) {
        dataSet = data;
    }
    public JFreeChart showPie() {
        pieDataset = new DefaultPieDataset();
        for (String s : dataSet.keySet()) {
            String name = s;
            String valueStr = dataSet.get(s).toString();
            double amt = !valueStr.isEmpty() ? Double.valueOf(valueStr) : 0.0;
            pieDataset.setValue(name, amt);
        }
        return ChartFactory.createPieChart("PIE CHART", pieDataset, true, true, true);
    }
}
