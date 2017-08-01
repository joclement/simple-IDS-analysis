package de.tub.insin.ss17.grp1.training;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.training.classifier.BallTreeNNClassifier;
import de.tub.insin.ss17.grp1.training.classifier.DecisionTree;
import de.tub.insin.ss17.grp1.training.classifier.LinearNNClassifier;
import de.tub.insin.ss17.grp1.util.ModelPersistence;
import weka.classifiers.Classifier;
import weka.core.Instances;


public class Trainer {

    private static final Logger log = LoggerFactory.getLogger(Trainer.class);

    public static final String LINEAR_NN = "lnns";
    public static final String BALL_TREE_NN = "ballTreeNN";
    public static final String J48 = "j48";

    public static final String CLASSIFIER_NAMES_DESCRIPTION =
            LINEAR_NN + ", " +
            BALL_TREE_NN + ", " +
            J48;

    private MlAlgo classifier;

    public Trainer(String classifierIdentifier, List<String> params) {
        this.setClassifier(classifierIdentifier, params);
    }

    private void setClassifier(String classifierIdentifier, List<String> params) {
        switch (classifierIdentifier) {
            case LINEAR_NN:
                this.classifier = new LinearNNClassifier(params);
                break;
            case BALL_TREE_NN:
                this.classifier = new BallTreeNNClassifier(params);
                break;
            case J48:
                this.classifier = new DecisionTree();
                break;
            default:
                throw new IllegalArgumentException(
                    "there is no classifier with the name: " + classifierIdentifier);
        }
    }

    public void train(Instances trainingData) {
        log.debug("start: train");
        this.classifier.train(trainingData);
        log.debug("finished: train");
    }

    public File save(File folder) {
        Classifier classifier = this.classifier.getClassifier();
        return ModelPersistence.save(classifier, folder, this.classifier.getFilename());
    }
}
