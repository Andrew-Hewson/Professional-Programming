import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * @author Andrew Hewson
 * @date 22 Aug 2020
 * @version 1.0
 */
public class Main extends JFrame implements Runnable {
   
   private static final long serialVersionUID = 6181612111777646203L;
   private List<Datum> data = new ArrayList<Datum>();
   private String chartTitle = "Chart Title";
   private ChartPanel chartPanel;
   private final String CHART_DATA_FILE_NAME = "data.txt";

   /**
    * Main constructor
    */
   public Main() {
      init();
   }
   
   /**
    * init will initialize the data, chart and frame
    */
   private void init() {
      initData(CHART_DATA_FILE_NAME);
      initChart();
      initFrame();
   }
   
   /**
    * initData will read the data from the dataFileName 
    * argument and parse the data
    * @param dataFileName the name of the file to be parsed
    */
   private void initData(String dataFileName) {
      log("Data init...");
      try {
         BufferedReader fin = new BufferedReader(new FileReader(dataFileName));
         String line = "";
         while ((line = fin.readLine()) != null) {
            if (line.startsWith("#")) {
               continue;  // ignore comment in file
            }
            String[] tokenize = line.split("=");
            if (tokenize.length != 2 || tokenize[0].length() == 0) {
               log("Error: needs to be name=value => " + line);
               continue; // ignore malformed datum
            }
            if (tokenize[0].equals("Title")) {
               chartTitle = tokenize[1];
               continue;
            }
            log("Info: name: " + tokenize[0] + " value: " + tokenize[1]);
            String name = tokenize[0];
            int value = 0;
            try {
               value = Integer.parseInt(tokenize[1]);
            } catch (NumberFormatException e) {
               log("Error: value needs to be integer => " + line);
               continue; // ignore malformed datum
            }
            data.add(new Datum(name, value));
         }
         fin.close();
      } catch (FileNotFoundException e) {
         log("Error: File not found => " + CHART_DATA_FILE_NAME);
         e.printStackTrace();
      } catch (IOException e) {
         log("Error: IO Exception");
         e.printStackTrace();
      }
      log("Data init complete.");
   }
   
   /**
    * initChart creates the chart
    */
   private void initChart() {
      DefaultPieDataset dataset = new DefaultPieDataset();
      for (Datum datum : data) {
         dataset.setValue(datum.name, datum.value);
      }
      JFreeChart chart = ChartFactory.createPieChart3D(chartTitle, dataset, false, true, false);
      chartPanel = new ChartPanel(chart);
      chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      chartPanel.setBackground(Color.WHITE);
   }
   
   /**
    * initFrame creates the frame
    */
   private void initFrame() {
      add(chartPanel);
      pack();
      setTitle("Pie chart");
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
   
   /**
    * Log a given message to console
    * @param msg
    */
   private void log(String msg) {
      System.out.println(msg);
   }
   
   /**
    * Makes the window visible
    */
   @Override
   public void run() {
      setVisible(true);
   }

   /**
    * Insertion point
    * @param args
    */
   public static void main(String[] args) {
      javax.swing.SwingUtilities.invokeLater(
         new Main()
      );
   }
}

/**
 * A struct type class to hold a single data name value pair
 * @author Andrew Hewson
 * 22 Aug 2020
 */
class Datum {
   String name;
   int value;
   
   /**
    * Creates a datum with a given name and value
    * @param name
    * @param value
    */
   public Datum(String name, int value) {
      this.name = name;
      this.value = value;
   }
}
