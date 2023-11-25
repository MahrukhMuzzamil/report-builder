import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color;
import java.util.HashMap;

public class PieChart {
    private DefaultPieDataset pieDataset;
    private HashMap<String, Integer> dataSet;
    private HashMap<String, Color> colors;

    public PieChart(HashMap<String, Integer> data, HashMap<String, Color> colors) {
        dataSet = data;
        this.colors = colors;
    }

    public JFreeChart showPie() {
        pieDataset = new DefaultPieDataset();
        for (String s : dataSet.keySet()) {
            String name = s;
            String valueStr = dataSet.get(s).toString();
            double amt = !valueStr.isEmpty() ? Double.valueOf(valueStr) : 0.0;
            pieDataset.setValue(name, amt);
        }
        JFreeChart chart = ChartFactory.createPieChart("PIE CHART", pieDataset, true, true, true);
        PiePlot plot = (PiePlot) chart.getPlot();
        for (String key : colors.keySet()) {
            if (pieDataset.getKeys().contains(key)) { // Check if the color is provided for a valid key
                plot.setSectionPaint(key, colors.get(key));
            } else {
                System.out.println("Key '" + key + "' does not exist in the pie chart data.");
            }
        }
        return chart;
    }
}
