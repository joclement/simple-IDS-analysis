package de.tub.insin.ss17.grp1;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;

import de.tub.insin.ss17.grp1.evaluation.Evaluater;
import de.tub.insin.ss17.grp1.shared.SharedConstants;
import de.tub.insin.ss17.grp1.training.Trainer;
import de.tub.insin.ss17.grp1.util.ArffLoader;
import de.tub.insin.ss17.grp1.util.ModelPersistence;
import de.tub.insin.ss17.grp1.util.ResultPersistence;
import weka.classifiers.Classifier;

public class IDSCliManager {

    private final static Logger log = LoggerFactory.getLogger(IDSCliManager.class);

    private final static String TRAIN = "train";
    private final static String TEST = "test";

    // @formatter:off

    @Parameter(names = {"--only", "-o"},
               description = "To specify to do just train or test, options: " + TRAIN + ", " + TEST)
    private String only = null;

    @Parameter(names = {"--arffFolder", "-f"},
               description = "Path to the arff folder.",
               required = true)
    private String dataFolder;

    @Parameter(names = {"--parameters", "-p"},
               description = "Parameters for the ml algorithm. "
                           + "Value depends on chosen classifier(--classifierName). "
                           + "Check the documentation or weka for the specific values.")
    private List<String> mlParams = new LinkedList<String>();


    private final static String classifierNameDescription = Trainer.CLASSIFIER_NAMES_DESCRIPTION;
    @Parameter(names = {"--classifier", "-c"},
               description = "Name of the classifier, options: " + classifierNameDescription)
    private String classifierName = Trainer.LINEAR_NN;

    @Parameter(names = {"--help", "-h"}, help = true)
    private boolean help;

    // @formatter:on

    public void run() {
        this.validateParams();

        ArffLoader arffLoader = new ArffLoader(this.dataFolder);
        File classifierFile = null;

        if (only != TEST) {
            log.info("--- start " + TRAIN + " ---");
            Trainer trainer = new Trainer(this.classifierName,
                                          IDSCliManager.prepareParams(this.mlParams));
            trainer.train(arffLoader.loadTraining());
            classifierFile = trainer.save(new File(this.dataFolder));
            log.info("--- finished " + TRAIN + " ---");
        }

        if (only != TRAIN) {
            log.info("--- start " + TEST + " ---");
            List<File> classifierFiles = ModelPersistence.loadAllFiles(new File(this.dataFolder));
            if (classifierFiles.size() == 0) {
                throw new RuntimeException("There are no classifiers to test."
                        + " You need to train classifiers before you can test one.");
            }

            if (classifierFile == null) {
                classifierFile = this.decide(classifierFiles);
            }
            Classifier classifier = ModelPersistence.load(classifierFile);
            ResultPersistence resultPersistence = new ResultPersistence(
                this.dataFolder, classifierFile.getName());

            Evaluater evaluater = new Evaluater(classifier, arffLoader.loadTraining());
            evaluater.evaluate(arffLoader.loadTest(), resultPersistence);
            log.info("--- finished " + TEST + " ---");
        }
    }

    private void validateParams() {
        validateParamOnly();
    }

    private void validateParamOnly() {
        if (only != null && !only.equals(TEST) && !only.equals(TRAIN)) {
            throw new IllegalArgumentException(
                    "Value of Parameter --only wrong. It is `" + only + "`.");
        }
    }

    public static List<String> prepareParams(List<String> paramsWithEqualSign) {
        List<String> params = new LinkedList<>();

        for (String param : paramsWithEqualSign) {
            List<String> idAndMaybeValue = Arrays.asList(param.split("="));
            if (idAndMaybeValue.size() > 2 || idAndMaybeValue.size() == 0) {
                throw new IllegalArgumentException("Parameter specification is invalid.");
            }
            for (String paramPart : idAndMaybeValue) {
                params.add(paramPart);
            }
        }
        return params;
    }

    private File decide(List<File> classifiers) {
        // TODO untested

        if (classifiers.size() == 1) {
            return classifiers.get(0);
        }

        System.out.println("Which classifier do you want to test?");
        System.out.println("These are the available classifiers:");
        for (int i = 0; i < classifiers.size(); i++) {
            File classifier = classifiers.get(i);
            System.out.println((i+1) + ". " + classifier.getName());
        }
        System.out.println("Please type the corresponding number: ");
        Scanner in = new Scanner(System.in, SharedConstants.ENCODING);
        int num = in.nextInt();
        in.close();
        if (num < 0 || num > classifiers.size()) {
            throw new IllegalArgumentException(
                    "This number `" + num + "` is not availble in the list of classifiers.");
        }
        return classifiers.get(num);
    }
}
