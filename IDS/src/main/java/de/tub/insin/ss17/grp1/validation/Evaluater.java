package de.tub.insin.ss17.grp1.validation;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

import de.tub.insin.ss17.grp1.util.ClassIndexs;
import de.tub.insin.ss17.grp1.util.IDSSharedConstants;

public class Evaluater {

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
        for (int i = 0; i < testData.size(); i++) {
            Instance instance = testData.get(i);
            int classValue = (int) instance.classValue();
            assert instance.classValue() == classValue;

            if (classValue == this.classIndexs.BACKGROUND) {
                testData.remove(i);
            }
        }
    }

    public void evaluate(Instances testData) throws Exception {
        assert this.classIndexs.equals(new ClassIndexs(testData));

        this.removeBackground(testData);

        evaluation.evaluateModel(classifier, testData);

        // TODO improve the text based output
        System.out.println(this.generateTextResult());
    }

    private String generateTextResult() {
        StringBuilder result = new StringBuilder();

        // TODO add info about classifier, which is evaluated

        result.append("Results: " + System.lineSeparator());
        result.append(System.lineSeparator());
        result.append(evaluation.toSummaryString());
        result.append(System.lineSeparator());
        result.append("TP: " + evaluation.truePositiveRate(this.classIndexs.NORMAL));
        result.append("FP: " + evaluation.falsePositiveRate(this.classIndexs.NORMAL));
        result.append("TN: " + evaluation.trueNegativeRate(this.classIndexs.NORMAL));
        result.append("FN: " + evaluation.falseNegativeRate(this.classIndexs.NORMAL));

        return result.toString();
    }

}
