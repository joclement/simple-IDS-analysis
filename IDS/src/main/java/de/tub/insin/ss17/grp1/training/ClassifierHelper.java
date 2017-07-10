package de.tub.insin.ss17.grp1.training;

import de.tub.insin.ss17.grp1.util.ClassIndexs;

class ClassifierHelper {

    static void removeBackgroundFromPredictions(double[] predictions, ClassIndexs classIndexs) {
        assert predictions.length == 3;

        double base = predictions[classIndexs.NORMAL] + predictions[classIndexs.BOTNET];
        if (base == 0.0) {
            assert(predictions[classIndexs.BACKGROUND] == 1.0);
            return;
        }
        predictions[classIndexs.BACKGROUND] = 0;

        predictions[classIndexs.NORMAL] /= base;
        predictions[classIndexs.BOTNET] /= base;
    }
}
