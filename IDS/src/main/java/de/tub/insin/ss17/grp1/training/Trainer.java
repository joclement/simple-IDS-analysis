package de.tub.insin.ss17.grp1.training;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.List;

import de.tub.insin.ss17.grp1.util.Param;
import weka.core.Instances;

public class Trainer {

    private NNClassifier nnClassifier;
    
    private Serializer serializer;

    public Trainer(List<Param> params) {
        this.nnClassifier = new NNClassifier(params);
    }

    public void train(Instances trainingData) {
        this.nnClassifier.train(trainingData);
    }

    public void Serializer(File file) throws IOException {
        this.serializer = new Serializer(file);
    }
    //TODO Deserializer

}
