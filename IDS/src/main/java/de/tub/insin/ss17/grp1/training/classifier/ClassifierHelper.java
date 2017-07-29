package de.tub.insin.ss17.grp1.training.classifier;

import de.tub.insin.ss17.grp1.shared.RuntimeWekaException;
import de.tub.insin.ss17.grp1.shared.SharedConstants;
import de.tub.insin.ss17.grp1.util.ClassIndexs;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;


class ClassifierHelper {

    static void removeBackgroundFromPredictions(double[] predictions, ClassIndexs classIndexs) {
        if (predictions.length > SharedConstants.CLASS_COUNT || predictions.length == 1) {
            throw new IllegalArgumentException("Class Attribute of arff file has wrong format.");
        } else if (predictions.length == SharedConstants.CLASS_COUNT - 1
                && !classIndexs.hasBackground()) {
            return;
        }

        double base = predictions[classIndexs.NORMAL] + predictions[classIndexs.BOTNET];
        if (base == 0.0) {
            if (predictions[classIndexs.BACKGROUND] == 1.0) {
                throw new RuntimeException("Predictions are wrong.");
            }
            return;
        }

        if (classIndexs.hasBackground()) {
            predictions[classIndexs.BACKGROUND] = 0;
        }

        predictions[classIndexs.NORMAL] /= base;
        predictions[classIndexs.BOTNET] /= base;
    }

    static void catchedBuildClassifier(AbstractClassifier classifier, Instances trainingData) {
        try {
            classifier.buildClassifier(trainingData);
        } catch (Exception e) {
            throw new RuntimeWekaException("Failed to build classifier. "
                    + e.getLocalizedMessage());
        }
    }
}
