package de.tub.insin.ss17.grp1.training;

import de.tub.insin.ss17.grp1.util.ClassIndexs;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;

public class CTUIBk extends IBk implements HasClassIndexs {

    private static final long serialVersionUID = 1L;

    private ClassIndexs classIndexs;

    @Override
    public void setClassIndexs(ClassIndexs classIndexs) {
        this.classIndexs = classIndexs;
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        assert instance.classAttribute().isNominal();

        double[] predictions = super.distributionForInstance(instance);
        ClassifierHelper.removeBackgroundFromPredictions(predictions, classIndexs);

        return predictions;
    }

}
