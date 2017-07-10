package de.tub.insin.ss17.grp1.validation;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

import java.util.Arrays;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.util.ClassIndexs;
import de.tub.insin.ss17.grp1.util.IDSSharedConstants;
import de.tub.insin.ss17.grp1.util.ResultPersistence;

public class Evaluater {

    private final static Logger log = LoggerFactory.getLogger(Evaluater.class);

    private final Classifier classifier;

    private final Evaluation evaluation;

    private ClassIndexs classIndexs;

    public Evaluater(Classifier classifier, Instances trainingData) throws Exception {
        this.classifier = classifier;
        this.evaluation = new Evaluation(trainingData);

        Attribute classAttr = trainingData.classAttribute();
        assert classAttr.isNominal();
        assert classAttr.numValues() == IDSSharedConstants.CLASS_COUNT;
        this.classIndexs = new ClassIndexs(trainingData);
    }

    public void removeBackground(Instances testData) {
        assert this.classIndexs.equals(new ClassIndexs(testData));

        Iterator<Instance> it = testData.iterator();
        while(it.hasNext()) {
            Instance instance = it.next();
            int classValue = (int) instance.classValue();
            assert instance.classValue() == classValue;

            if (classValue == this.classIndexs.BACKGROUND) {
                it.remove();
            }
        }
    }

    public void evaluate(Instances testData, ResultPersistence resultPersistence) throws Exception {
        log.debug("start: evaluate");
        this.removeBackground(testData);

        evaluation.evaluateModel(classifier, testData);

        Metrics metrics = new Metrics(evaluation.confusionMatrix(), this.classIndexs);

        resultPersistence.saveSummary(this.generateTextResult(metrics));
        Visualizer visualizer = new Visualizer(resultPersistence);
        visualizer.plotAll(metrics);
        log.debug("finished: evaluate");
    }

    private String generateTextResult(Metrics metrics) {
        StringBuilder result = new StringBuilder();

        // TODO add info about classifier, which is evaluated

        result.append("Results: " + System.lineSeparator());
        result.append(System.lineSeparator());
        result.append(evaluation.toSummaryString());
        result.append(System.lineSeparator());
        result.append("TP Count: " + metrics.tps());
        result.append(System.lineSeparator());
        result.append("FP Count: " + metrics.fps());
        result.append(System.lineSeparator());
        result.append("TN Count: " + metrics.tns());
        result.append(System.lineSeparator());
        result.append("FN Count: " + metrics.fns());
        result.append(System.lineSeparator());
        result.append("TP Ratio: " + metrics.truePositiveRate());
        result.append(System.lineSeparator());
        result.append("FP Ratio: " + metrics.falsePositiveRate());
        result.append(System.lineSeparator());
        result.append("TN Ratio: " + metrics.trueNegativeRate());
        result.append(System.lineSeparator());
        result.append("FN Ratio: " + metrics.falseNegativeRate());
        result.append(System.lineSeparator());

        result.append(this.printConfusionMatrix(this.evaluation.confusionMatrix()));

        return result.toString();
    }

    private String printConfusionMatrix(double[][] confusionMatrix) {
        StringBuilder confDesc = new StringBuilder();
        confDesc.append(System.lineSeparator());
        confDesc.append("Confusion Matrix: ");
        confDesc.append(System.lineSeparator());
        confDesc.append("Row, Column for " + IDSSharedConstants.BACKGROUND + " is: "
                + this.classIndexs.BACKGROUND);
        confDesc.append(System.lineSeparator());
        confDesc.append("Row, Column for " + IDSSharedConstants.NORMAL + " is: "
                + this.classIndexs.NORMAL);
        confDesc.append(System.lineSeparator());
        confDesc.append("Row, Column for " + IDSSharedConstants.BOTNET + " is: "
                + this.classIndexs.BOTNET);
        confDesc.append(System.lineSeparator());
        for (int i = 0; i < confusionMatrix.length; i++) {
            confDesc.append(Arrays.toString(confusionMatrix[i]));
            confDesc.append(System.lineSeparator());
        }
        return confDesc.toString();
    }
}
