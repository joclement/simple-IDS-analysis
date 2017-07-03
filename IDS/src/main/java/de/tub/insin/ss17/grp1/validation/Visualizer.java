package de.tub.insin.ss17.grp1.validation;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.util.ResultPersistence;

public class Visualizer {

    private static final Logger log = LoggerFactory.getLogger(Visualizer.class);

    private static final int IMG_WIDTH = 640;
    private static final int IMG_HEIGHT = 480;

    private final String TP = "TP";
    private final String FP = "FP";
    private final String FN = "FN";
    private final String TN = "TN";

    private final ResultPersistence resultPersistence;

    public Visualizer(ResultPersistence resultPersistence) {
        this.resultPersistence = resultPersistence;
    }

    private DefaultCategoryDataset generate(int tps, int fps, int fns, int tns) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

        final String rowKey = "1 Scenario";

        dataset.addValue(tps, rowKey, TP);
        dataset.addValue(fps, rowKey, FP);
        dataset.addValue(fns, rowKey, FN);
        dataset.addValue(tns, rowKey, TN);

        return dataset;
    }

    public void plot(int tps, int fps, int fns, int tns) {
        CategoryDataset dataset = generate(tps, fps, fns, tns);
        JFreeChart barChart = ChartFactory.createBarChart("Some Plot", "", "Count",
           dataset,PlotOrientation.VERTICAL,
           true, true, false);

        File file = this.resultPersistence.getFileForSaving("TpsFpsFnsTns.jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(file, barChart, IMG_WIDTH, IMG_HEIGHT);
        } catch (IOException e) {
            log.error("Failed to save a result plot to file,"
                    + " path: " + file.getAbsolutePath());
        }
    }

}
