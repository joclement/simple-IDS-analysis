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

    private final Classifier classifier;

    private final Evaluation evaluation;

    private ClassIndexs classIndexs;

    private final static Logger log = LoggerFactory.getLogger(Evaluater.class);

    public Evaluater(Classifier classifier, Instances trainingData) throws Exception {
        log.debug("start: Evaluater");
        this.classifier = classifier;
        this.evaluation = new Evaluation(trainingData);

        Attribute classAttr = trainingData.classAttribute();
        assert classAttr.isNominal();
        assert classAttr.numValues() == IDSSharedConstants.CLASS_COUNT;
        this.classIndexs = new ClassIndexs(trainingData);
        log.debug("finished: Evaluater");
    }

    public void removeBackground(Instances testData) {
        log.debug("start: removeBackground");
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
        log.debug("finished: removeBackground");
    }

    public void evaluate(Instances testData, ResultPersistence resultPersistence) throws Exception {
        log.debug("start: evaluate");
        this.removeBackground(testData);

        evaluation.evaluateModel(classifier, testData);

        resultPersistence.saveTxtSummary(this.generateTextResult());
        log.debug("finished: evaluate");
    }

    private String generateTextResult() {
        log.debug("start: generateTextResult");
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

        log.debug("finished: generateTextResult");
        return result.toString();
    }

}
