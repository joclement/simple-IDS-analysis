package de.tub.insin.ss17.grp1.training.classifier;

import de.tub.insin.ss17.grp1.util.ClassIndexs;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;


/**
 * Special CTU nearest neighbor classifier. Because of the 3 labels in the CTU data-set,
 * the distribution prediction for an instance needs to be adapted,
 * because at the end the only 2 labels, which count are NORMAL and BOTNET.
 * This overrode function `distributionForInstance` removes the background probability,
 * if it is possible.
 *
 * @author Joris Clement
 *
 */
public class CTUIBk extends IBk {

    private static final long serialVersionUID = 1L;

    private ClassIndexs classIndexs;

    public void setClassIndexs(ClassIndexs classIndexs) {
        this.classIndexs = classIndexs;
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        double[] predictions = super.distributionForInstance(instance);
        ClassifierHelper.removeBackgroundFromPredictions(predictions, classIndexs);

        return predictions;
    }

}
