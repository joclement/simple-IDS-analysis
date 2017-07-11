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

    public static final String LINEAR_NEAREST_NEIGHBOUR_SEARCH = "lnns";

    public static final String CLASSIFIER_NAMES_DESCRIPTION =
            LINEAR_NEAREST_NEIGHBOUR_SEARCH;

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
                log.error("there is no classifier with the name: {}", classifierIdentifier);
                System.exit(1);
        }
    }

    public void train(Instances trainingData) throws Exception {
        log.debug("start: train");
        this.classifier.train(trainingData);
        log.debug("finished: train");
    }

    public File save(File folder) throws IOException {
        Classifier classifier = this.classifier.getClassifier();
        return ModelPersistence.save(classifier, folder, this.classifier.getFilename());
    }
}
