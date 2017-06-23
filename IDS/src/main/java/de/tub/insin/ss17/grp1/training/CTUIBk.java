package de.tub.insin.ss17.grp1.training;

import de.tub.insin.ss17.grp1.util.ClassIndexs;
import de.tub.insin.ss17.grp1.util.IDSSharedConstants;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;

public class CTUIBk extends IBk {

    private ClassIndexs classIndexs;

    public void setClassIndexs(ClassIndexs classIndexs) {
        this.classIndexs = classIndexs;
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        assert instance.classAttribute().isNominal();

        double[] predictions = super.distributionForInstance(instance);
        // TODO improve this check
        assert predictions.length == 3;

        predictions[classIndexs.BACKGROUND] = 0;

        double base = predictions[classIndexs.NORMAL] + predictions[classIndexs.BOTNET];
        predictions[classIndexs.NORMAL] /= base;
        predictions[classIndexs.BOTNET] /= base;

        return predictions;
    }
}
