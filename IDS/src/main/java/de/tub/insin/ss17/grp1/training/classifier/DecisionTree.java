package de.tub.insin.ss17.grp1.training.classifier;

import de.tub.insin.ss17.grp1.training.MlAlgo;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class DecisionTree implements MlAlgo {

    private J48 j48;

    public DecisionTree() {
        this.j48 = new J48();
    }

    @Override
    public void train(Instances trainingData) {
        ClassifierHelper.catchedBuildClassifier(this.j48, trainingData);
    }

    @Override
    public String getFilename() {
        StringBuilder filename = new StringBuilder("j48");

        return filename.toString();
    }

    @Override
    public Classifier getClassifier() {
        return this.j48;
    }

}
