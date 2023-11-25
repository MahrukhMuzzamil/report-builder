import javax.swing.*;
import java.io.*;
import java.util.HashMap;

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

}
