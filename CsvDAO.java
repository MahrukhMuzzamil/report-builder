package DataAccess;

import Buisness.LineGraph;
import Interface.Report;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CsvDAO {

    public static HashMap<String, Integer> parseDataset(String dataset) {
        HashMap<String, Integer> dataMap = new HashMap<>();

        String[] keyValuePairs = dataset.split(",");
        for (String pair : keyValuePairs) {
            String[] parts = pair.split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                int value = Integer.parseInt(parts[1].trim());
                dataMap.put(key, value);
            } else {
                System.err.println("Invalid format for pair: " + pair);
            }
        }

        return dataMap;
    }
    public static HashMap<String, Integer> loadPieChart() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                String line = reader.readLine();
                HashMap<String, Integer> dataMap = new HashMap<>();
                if (line != null) {
                    String[] keyValuePairs = line.split(",");
                    for (String pair : keyValuePairs) {
                        String[] parts = pair.split(":");
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            int value = Integer.parseInt(parts[1].trim());
                            dataMap.put(key, value);
                        }
                    }
                }
                return dataMap;
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        }

        return null;
    }

    public static void savePieChartData(HashMap<String, Integer> pieChartData, Report report) {
        if (pieChartData == null || pieChartData.isEmpty()) {
            JOptionPane.showMessageDialog(report, "No pie chart data to save.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(report);

        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                for (String s : pieChartData.keySet()) {
                    writer.write(s);
                    writer.write(":");
                    writer.write(pieChartData.get(s).toString());
                    writer.write(",");
                }
                System.out.println("Pie chart data saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(report, "Error saving pie chart data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public static void saveBarChartData(String xAxisLabel, String yAxisLabel, int number, HashMap<String, HashMap<String, Double>> barGraphData, Report report) {
        if (barGraphData == null || barGraphData.isEmpty()) {
            JOptionPane.showMessageDialog(report, "No bar graph data to save.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(report);

        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {

                writer.write("BarNumber,"+number+"\n");
                writer.write("X-Axis," + xAxisLabel + "\n");
                writer.write("Y-Axis," + yAxisLabel + "\n");

                 for (String category : barGraphData.keySet()) {
                    writer.write("Category," + category + "\n");

                    HashMap<String, Double> innerMap = barGraphData.get(category);
                    for (String series : innerMap.keySet()) {
                        writer.write(series + "," + innerMap.get(series) + "\n");
                    }
                }

                System.out.println("Bar graph data saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(report, "Error saving bar graph data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static HashMap<String, HashMap<String, Double>> loadBarChartData(String[] array) {
        HashMap<String, HashMap<String, Double>> barGraphData = new HashMap<>();
        boolean xAxisRead = false;
        boolean yAxisRead = false;
        boolean numberOfBarsRead=false;

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                String line;
                String category = null;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (!numberOfBarsRead && parts.length == 2 && "BarNumber".equals(parts[0])) {
                        array[2] = parts[1];
                        numberOfBarsRead = true;
                        continue;
                    }

                    if (!xAxisRead && parts.length == 2 && "X-Axis".equals(parts[0])) {
                        array[0] = parts[1];
                        xAxisRead = true;
                        continue;
                    }

                    if (!yAxisRead && parts.length == 2 && "Y-Axis".equals(parts[0])) {
                        array[1] = parts[1];
                        yAxisRead = true;
                        continue;
                    }

                    if (parts.length == 2 && "Category".equals(parts[0])) {

                        category = parts[1];
                        barGraphData.put(category, new HashMap<>());
                        continue;
                    }

                    if (xAxisRead && yAxisRead && category != null && parts.length == 2) {
                        barGraphData.get(category).put(parts[0], Double.parseDouble(parts[1]));
                    }
                }
                System.out.println("Bar graph data loaded successfully.");
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Error loading bar graph data.");
            }
        }

        return barGraphData;
    }
    public static List<double[]> parseLineGraphData(String filePath) throws IOException {
        List<double[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 2) {
                    try {
                        double x = Double.parseDouble(values[0].trim());
                        double y = Double.parseDouble(values[1].trim());
                        data.add(new double[]{x, y});
                    } catch (NumberFormatException e) {
                        // Handle invalid number format
                    }
                }
            }
        }
        return data;
    }

    public static Map<String, List<double[]>> loadLineGraphData() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        Map<String, List<double[]>> lineGraphData = new HashMap<>();

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String seriesName = parts[0].trim();
                        double xValue = Double.parseDouble(parts[1].trim());
                        double yValue = Double.parseDouble(parts[2].trim());

                        lineGraphData.computeIfAbsent(seriesName, k -> new ArrayList<>()).add(new double[]{xValue, yValue});
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return lineGraphData;
    }
    public static void saveLineGraphData(LineGraph lineGraph, Report report) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Line Graph Data");
        int userSelection = fileChooser.showSaveDialog(report);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                Map<String, List<double[]>> data = lineGraph.getData(); // Assuming LineGraph has a method getData()

                for (Map.Entry<String, List<double[]>> entry : data.entrySet()) {
                    String seriesName = entry.getKey();
                    for (double[] dataPoint : entry.getValue()) {
                        writer.write(seriesName + "," + dataPoint[0] + "," + dataPoint[1]);
                        writer.newLine();
                    }
                }

                JOptionPane.showMessageDialog(report, "Line graph data saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(report, "Error saving line graph data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
