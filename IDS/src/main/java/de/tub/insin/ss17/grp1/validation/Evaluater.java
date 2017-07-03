package de.tub.insin.ss17.grp1.validation;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

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

        resultPersistence.saveSummary(this.generateTextResult());
        Visualizer visualizer = new Visualizer(resultPersistence);
        visualizer.plot(this.tps(), this.fps(), this.fns(), this.tns());
        log.debug("finished: evaluate");
    }

    private String generateTextResult() {
        StringBuilder result = new StringBuilder();

        // TODO add info about classifier, which is evaluated

        result.append("Results: " + System.lineSeparator());
        result.append(System.lineSeparator());
        result.append(evaluation.toSummaryString());
        result.append(System.lineSeparator());
        result.append("TP: " + evaluation.truePositiveRate(this.classIndexs.NORMAL));
        result.append(System.lineSeparator());
        result.append("FP: " + evaluation.falsePositiveRate(this.classIndexs.NORMAL));
        result.append(System.lineSeparator());
        result.append("TN: " + evaluation.trueNegativeRate(this.classIndexs.NORMAL));
        result.append(System.lineSeparator());
        result.append("FN: " + evaluation.falseNegativeRate(this.classIndexs.NORMAL));
        result.append(System.lineSeparator());

        return result.toString();
    }

    private int tps() {
        double tps = this.evaluation.numTruePositives(this.classIndexs.NORMAL);
        assert(Math.round(tps) == tps);
        return Math.toIntExact(Math.round(tps));
    }

    private int fps() {
        double fps = this.evaluation.numFalsePositives(this.classIndexs.NORMAL);
        assert(Math.round(fps) == fps);
        return Math.toIntExact(Math.round(fps));
    }

    private int fns() {
        double fns = this.evaluation.numFalseNegatives(this.classIndexs.NORMAL);
        assert(Math.round(fns) == fns);
        return Math.toIntExact(Math.round(fns));
    }

    private int tns() {
        double tns = this.evaluation.numTrueNegatives(this.classIndexs.NORMAL);
        assert(Math.round(tns) == tns);
        return Math.toIntExact(Math.round(tns));
    }
}
