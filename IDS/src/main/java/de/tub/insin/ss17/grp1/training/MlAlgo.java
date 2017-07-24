package de.tub.insin.ss17.grp1.training;

import weka.classifiers.Classifier;
import weka.core.Instances;


public interface MlAlgo {

    public void train(Instances trainingData);

    public String getFilename();

    public Classifier getClassifier();
}
