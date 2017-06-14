package de.tub.insin.ss17.grp1.training;

import java.io.File;
import java.util.List;

import de.tub.insin.ss17.grp1.util.Param;
import weka.core.Instances;

public class Trainer {

    private NNClassifier nnClassifier;

    public Trainer(List<Param> params) {
        this.nnClassifier = new NNClassifier(params);
    }

    public void train(Instances trainingData) {
        this.nnClassifier.train(trainingData);
    }

    // TODO save the trained model somewhere
    // should this be called by train or a method on its own, good design???
    public void save(File file) {
    }

}
