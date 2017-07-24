package de.tub.insin.ss17.grp1.training.classifier;

import de.tub.insin.ss17.grp1.shared.RuntimeWekaException;
import de.tub.insin.ss17.grp1.util.ClassIndexs;
import de.tub.insin.ss17.grp1.util.IDSSharedConstants;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

class ClassifierHelper {

    static void removeBackgroundFromPredictions(double[] predictions, ClassIndexs classIndexs) {
        if (predictions.length > IDSSharedConstants.CLASS_COUNT || predictions.length == 1) {
            throw new IllegalArgumentException("Class Attribute of arff file has wrong format.");
        } else if (predictions.length == IDSSharedConstants.CLASS_COUNT - 1 && !classIndexs.hasBackground()) {
            return;
        }

        double base = predictions[classIndexs.NORMAL] + predictions[classIndexs.BOTNET];
        if (base == 0.0) {
            assert(predictions[classIndexs.BACKGROUND] == 1.0);
            return;
        }

        if(classIndexs.hasBackground()) {
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
