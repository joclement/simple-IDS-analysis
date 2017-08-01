package de.tub.insin.ss17.grp1.evaluation;

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

    private final ResultPersistence resultPersistence;

    public Visualizer(ResultPersistence resultPersistence) {
        this.resultPersistence = resultPersistence;
    }

    private DefaultCategoryDataset generateCounts(Metrics metrics) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        final String rowKey = "1 Scenario";

        dataset.addValue(metrics.tps(), rowKey, "TPs");
        dataset.addValue(metrics.fps(), rowKey, "FPs");
        dataset.addValue(metrics.fns(), rowKey, "FNs");
        dataset.addValue(metrics.tns(), rowKey, "TNs");

        return dataset;
    }

    private DefaultCategoryDataset generateRatios(Metrics metrics) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        final String rowKey = "1 Scenario";

        dataset.addValue(metrics.recall(), rowKey, "Recall");
        dataset.addValue(metrics.falsePositiveRate(), rowKey, "False Positive Rate");
        dataset.addValue(metrics.falseNegativeRate(), rowKey, "False Negative Rate");
        dataset.addValue(metrics.specificity(), rowKey, "Specificity");
        dataset.addValue(metrics.accuracy(), rowKey, "Accuracy");
        dataset.addValue(metrics.precision(), rowKey, "Precision");

        return dataset;
    }

    private void plotCounts(Metrics metrics) {
        CategoryDataset dataset = generateCounts(metrics);
        JFreeChart barChart = ChartFactory.createBarChart("Some Plot", "", "Count",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);

        File file = this.resultPersistence.getFileForSaving("TpsFpsFnsTnsCounts.jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(file, barChart, IMG_WIDTH, IMG_HEIGHT);
        } catch (IOException e) {
            log.error("Failed to save a result plot to file,"
                    + " path: " + file.getAbsolutePath());
        }
    }

    private void plotRatios(Metrics metrics) {
        CategoryDataset dataset = generateRatios(metrics);
        JFreeChart barChart = ChartFactory.createBarChart("Some Plot", "", "Ratios",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);

        File file = this.resultPersistence.getFileForSaving("TpsFpsFnsTnsRatios.jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(file, barChart, IMG_WIDTH, IMG_HEIGHT);
        } catch (IOException e) {
            log.error("Failed to save a result plot to file,"
                    + " path: " + file.getAbsolutePath());
        }
    }

    public void plotAll(Metrics metrics) {
        plotCounts(metrics);
        plotRatios(metrics);
    }

}
