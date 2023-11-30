package DataAccess;

import Interface.BarGraphPanel;
import Interface.PieChartPanel;
import Interface.TablePanel;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;

public class CsvDAO {

    public static String[][] loadTableData(int[] arr) {
        String[][] data = null;
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                String[] dimensions = reader.readLine().split(",");
                if (dimensions.length == 2) {
                    arr[0] = Integer.parseInt(dimensions[0].trim());
                    arr[1] = Integer.parseInt(dimensions[1].trim());
                } else {
                    throw new IOException("Invalid file format: Expected format 'rows,columns'.");
                }

                data = new String[arr[0]][arr[1]];
                int count=0;
                    String line = reader.readLine();
                    System.out.println(line);
                    if (line != null) {
                        String[] values = line.split(",");
                        for (int i = 0; i < arr[0]; i++) {
                            for(int j=0;j<arr[i];j++){
                                data[i][j]=values[count++];
                            }
                        }
                    }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
    public static void saveTableData(String[][] data, int rows, int cols, TablePanel report){
        if (data==null) {
            JOptionPane.showMessageDialog(report, "No table data to save.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(report);

        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                writer.write(rows+",");
                writer.write(cols+"\n");
                System.out.println(rows+cols);
                for(int i=0;i<rows;i++){
                    for(int j=0;j<cols;j++){
                        writer.write(data[i][j]+",");
                    }
                }
                System.out.println("Table data saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(report, "Error saving table data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
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
    public static void savePieChartData(HashMap<String, Integer> pieChartData, PieChartPanel report) {
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
    public static void saveBarChartData(String xAxisLabel, String yAxisLabel, int number, HashMap<String, HashMap<String, Double>> barGraphData, BarGraphPanel report) {
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
}
