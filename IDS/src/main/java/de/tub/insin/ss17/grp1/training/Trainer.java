package de.tub.insin.ss17.grp1.training;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.List;

import de.tub.insin.ss17.grp1.util.ModelPersistence;
import de.tub.insin.ss17.grp1.util.Param;
import weka.classifiers.Classifier;
import weka.core.Instances;

public class Trainer {

    private static final String LINEAR_NEAREST_NEIGHBOUR_SEARCH = "lnns";

    private static final String[] CLASSIFIER_NAMES = new String[1];

    {
        CLASSIFIER_NAMES[0] = LINEAR_NEAREST_NEIGHBOUR_SEARCH;
    }

    private MlAlgo classifier;

    private String currentClassifierIdentifier;

    public Trainer(String classifierIdentifier, List<Param> params) {
        this.setClassifier(classifierIdentifier, params);
        this.currentClassifierIdentifier = classifierIdentifier;
    }

    private void setClassifier(String classifierIdentifier, List<Param> params) {

        switch (classifierIdentifier) {
            case LINEAR_NEAREST_NEIGHBOUR_SEARCH:
                this.classifier = new LinearNNClassifier(params);
                break;
            default:
                System.err.println("this classifier doesn't exist!");
                System.exit(-1);
        }
    }

    public void train(Instances trainingData) throws Exception {
        this.classifier.train(trainingData);
    }

    public void save(File folder) throws IOException {
        File file = new File(folder, this.classifier.getFilename() + ".model");
        Classifier classifier = this.classifier.getClassifier();
        ModelPersistence.save(classifier, file);
    }

    public static String[] getClassifierNames() {
        return CLASSIFIER_NAMES;
    }

    public static String getClassifierNamesDescription() {
        StringBuilder classifierNames = new StringBuilder(CLASSIFIER_NAMES[0]);

        for (int i = 1; i < CLASSIFIER_NAMES.length; i++) {
            classifierNames.append(", " + CLASSIFIER_NAMES[i]);
        }

        return classifierNames.toString();
    }
}
