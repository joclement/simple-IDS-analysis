package de.tub.insin.ss17.grp1.training;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import de.tub.insin.ss17.grp1.util.Param;
import weka.classifiers.Classifier;
import weka.core.Instances;

public interface MlAlgo {

    public void train(Instances trainingData) throws Exception;

    public String getFilename();

    public Classifier getClassifier();
}
