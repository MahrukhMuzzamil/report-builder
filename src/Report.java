import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot; // Import the PiePlot class
import java.awt.Color;
import java.util.HashMap;

public class Report extends JFrame {

    private ChartPanel chartPanelComponent;
    private JPanel chartPanel;
    private HashMap<String, Integer> pieChartData;
    private HashMap<String, Color> pieChartColors; // Declare a HashMap to store the colors of the pie chart sections

    public Report() {
        setTitle("Report Builder");
        chartPanel = new JPanel();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chartPanelComponent = new ChartPanel(null);
        chartPanelComponent.setLayout(new BorderLayout());
        chartPanelComponent.setBackground(Color.WHITE);

        JButton pieChartButton = new JButton("Add Pie Chart");
        pieChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dataset = JOptionPane.showInputDialog("Enter Pie Chart dataset with the item names (e.g. abc:43,def:56):");
                if (dataset != null && !dataset.isEmpty()) {
                    pieChartData = CsvDAO.parseDataset(dataset);
                    drawPieChart();
                }
            }
        });

        JButton loadPieChartButton = new JButton("Load Pie Chart");
        loadPieChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pieChartData = CsvDAO.loadPieChart();
                drawPieChart();
            }
        });

        JButton saveButton = new JButton("Save Pie Chart Data");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CsvDAO.savePieChartData(pieChartData, Report.this);
            }
        });

        JButton colorButton = new JButton("Choose Colors");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel inputPanel = new JPanel(new GridLayout(pieChartData.size(), 2));
                for (String key : pieChartData.keySet()) {
                    inputPanel.add(new JLabel(key)); // Fix the syntax here
                    JTextField colorField = new JTextField();
                    inputPanel.add(colorField);
                }
                int option = JOptionPane.showConfirmDialog(Report.this, inputPanel, "Enter Keys and Colors", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    pieChartColors = new HashMap<>();
                    for (int i = 0; i < inputPanel.getComponentCount(); i += 2) {
                        String key = ((JLabel) inputPanel.getComponent(i)).getText();
                        String color = ((JTextField) inputPanel.getComponent(i + 1)).getText();
                        try {
                            Color c = Color.decode(color);
                            pieChartColors.put(key, c);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(Report.this, "Invalid color format. Please enter a color in the format \"#RRGGBB\".", "Error", JOptionPane.ERROR_MESSAGE); // Fix the syntax here
                        }
                    }
                }
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(pieChartButton);
        controlPanel.add(saveButton);
        controlPanel.add(loadPieChartButton);
        controlPanel.add(colorButton); // add the color button to the control panel
        controlPanel.add(chartPanel);

        setContentPane(controlPanel);
        setVisible(true);
    }

    private void drawPieChart() {
        System.out.println("Drawing Pie Chart");

        JFreeChart pieChart = new PieChart(pieChartData, pieChartColors).showPie(); // Pass the pie chart colors to the PieChart constructor
        ChartPanel chartPanel1 = new ChartPanel(pieChart);

        SwingUtilities.invokeLater(() -> {
            if (this.chartPanelComponent == null) {
                System.out.println("chartPanel is null. Make sure it is properly initialized.");
                return;
            }

            this.chartPanelComponent.removeAll();

// Add the ChartPanel to your existing chartPanel
            this.chartPanelComponent.add(chartPanel1, BorderLayout.CENTER);

            this.chartPanelComponent.revalidate();
            this.chartPanelComponent.repaint();
            chartPanel.add(chartPanelComponent);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Report().setVisible(true);
            }
        });
    }
}
