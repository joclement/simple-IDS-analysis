package de.tub.insin.ss17.grp1.training;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.util.ModelPersistence;
import de.tub.insin.ss17.grp1.util.Param;
import weka.classifiers.Classifier;
import weka.core.Instances;


public class Trainer {

    private static final Logger log = LoggerFactory.getLogger(Trainer.class);

    private static final String LINEAR_NEAREST_NEIGHBOUR_SEARCH = "lnns";

    private static final String[] CLASSIFIER_NAMES;

    static {
        CLASSIFIER_NAMES = new String[1];
        CLASSIFIER_NAMES[0] = LINEAR_NEAREST_NEIGHBOUR_SEARCH;
    }

    private MlAlgo classifier;

    public Trainer(String classifierIdentifier, List<Param> params) {
        this.setClassifier(classifierIdentifier, params);
    }

    private void setClassifier(String classifierIdentifier, List<Param> params) {
        switch (classifierIdentifier) {
            case LINEAR_NEAREST_NEIGHBOUR_SEARCH:
                this.classifier = new LinearNNClassifier(params);
                break;
            default:
                log.error("ERROR: this classifier doesn't exist");
                System.err.println("this classifier doesn't exist!");
                log.error("quit system");
                System.exit(-1);
        }
    }

    public void train(Instances trainingData) throws Exception {
        log.debug("start: train");
        this.classifier.train(trainingData);
        log.debug("finished: train");
    }

    public void save(File folder) throws IOException {
        Classifier classifier = this.classifier.getClassifier();
        ModelPersistence.save(classifier, folder, this.classifier.getFilename());
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
