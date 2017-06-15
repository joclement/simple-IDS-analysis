package de.tub.insin.ss17.grp1.training;

import java.util.List;
import java.util.function.Consumer;

import de.tub.insin.ss17.grp1.util.Param;
import weka.core.Instances;

public interface MlAlgo {

    public void train(Instances trainingData);

}
