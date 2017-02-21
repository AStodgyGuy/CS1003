import java.util.ArrayList;
import java.awt.Color;
import java.awt.BasicStroke;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class GraphPlotter extends ApplicationFrame{

    /*
     * Constructor for Graph Plotter object
    */
    public GraphPlotter(ArrayList<Integer> al, String fileName) {
        super("Zipf's Distribution Law");
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
            "Zipf's Law" ,
            "Rank" ,
            "Frequency" ,
            createDataSet(al, fileName) ,
            PlotOrientation.VERTICAL ,
            true, true, false
        );

        //UI for the graph panel
        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));

        //add the data to the chart
        final XYPlot plot = xylineChart.getXYPlot();

        //set the x and y axis to a logarithmic scale
        final NumberAxis xAxis = new LogarithmicAxis("Rank");
        final NumberAxis yAxis = new LogarithmicAxis("Frequency");
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        //color the generated line red
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        plot.setRenderer(renderer);
        setContentPane(chartPanel);
    }

    /*
     * Method to generate data for particular book
    */
    private XYDataset createDataSet(ArrayList<Integer> al, String fileName) {
        //generates an XY series data set for book, it maps position i in array list to integer value at position i
        final XYSeries data = new XYSeries(fileName);
            for (int i = 0; i < al.size(); i++) {
                //since log scales can't have 0, shift the index by 1
                data.add(i + 1, al.get(i));
            }
        //add the data to a collection and retun it
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(data);
        return dataset;
    }
}