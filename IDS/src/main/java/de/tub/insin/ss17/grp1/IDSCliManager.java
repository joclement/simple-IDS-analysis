package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;

import de.tub.insin.ss17.grp1.training.Trainer;
import de.tub.insin.ss17.grp1.util.ArffLoader;
import de.tub.insin.ss17.grp1.util.ModelPersistence;
import de.tub.insin.ss17.grp1.util.Param;
import de.tub.insin.ss17.grp1.util.ResultPersistence;
import de.tub.insin.ss17.grp1.validation.Evaluater;
import weka.classifiers.Classifier;

public class IDSCliManager {

    private final static Logger log = LoggerFactory.getLogger(IDSCliManager.class);

    private final static String TRAIN = "train";
    private final static String TEST = "test";

    // TODO check for correct value
    @Parameter(names = {"--only", "-o"},
               description = "to specify to do just train, test. options: " + TRAIN + ", " + TEST)
    private String only = null;

    @Parameter(names = {"--arffFolder", "-f"},
               description = "Path to the arff folder",
               required = true)
    private String dataFolder;

    // TODO find good way to specify ml parameters, f.x. for NN classifier
    @Parameter(names = {"--parameters", "-p"},
               description = "Parameters for the ml algorithm",
               required = true)
    private List<String> mlParams = new LinkedList<String>();


    // TODO specify options for classifiers somewhere, in annotation it is not really possible
    private final static String classifierNameDescription = Trainer.CLASSIFIER_NAMES_DESCRIPTION;
    @Parameter(names = {"--classifierName", "-c"},
               description = "Name of the classifier, options: " + classifierNameDescription,
               required = true)
    private String classifierName;

    @Parameter(names = {"--help", "-h"}, help = true)
    private boolean help;

    public void run() {
        ArffLoader arffLoader = new ArffLoader(this.dataFolder);

        File classifierFile = null;

        if (only != TEST) {
            log.info("--- start " + TRAIN + " ---");
            Trainer trainer = new Trainer(this.classifierName, prepare(this.mlParams));
            try {
                trainer.train(arffLoader.loadTraining());
            } catch (Exception e) {
                shutdown("Failed to train classifier.");
            }
            try {
                classifierFile = trainer.save(new File(this.dataFolder));
            } catch (Exception e) {
                shutdown("Failed to save.");
            }
            log.info("--- finished " + TRAIN + " ---");
        }

        if (only != TRAIN) {
            log.info("--- start " + TEST + " ---");
            List<File> classifierFiles = ModelPersistence.loadAllFiles(new File(this.dataFolder));
            if (classifierFiles.size() == 0) {
                shutdown("There are no classifiers to test."
                        + " You need to train classifiers before you can test one.");
            }


            if (classifierFile == null) {
                classifierFile = this.decide(classifierFiles);
            }
            Classifier classifier = null;
            try {
                classifier = ModelPersistence.load(classifierFile);
            } catch (FileNotFoundException e1) {
                shutdown("Implementation mistake, please contact the developers.");
            } catch (ClassNotFoundException e1) {
                shutdown("Implementation mistake, please contact the developers.");
            } catch (IOException e1) {
                shutdown("IO fault: " + e1.getMessage());
            }
            ResultPersistence resultPersistence = new ResultPersistence(
                this.dataFolder, classifierFile.getName());

            Evaluater evaluater = null;
            try {
                evaluater = new Evaluater(classifier, arffLoader.loadTraining());
            } catch (Exception e2) {
               shutdown("Failed to load training data for evaluation");
            }
            try {
                evaluater.evaluate(arffLoader.loadTest(), resultPersistence);
            } catch (Exception e1) {
               shutdown("Failed to do evaluation");
            }
            log.info("--- finished " + TEST + " ---");
        }
    }

    private File decide(List<File> classifiers) {
        // TODO untested
        assert(classifiers.size() > 0);

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
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.close();
        // TODO add good failure report for wrong integer input
        return classifiers.get(num);
    }

    public static List<Param> prepare(List<String> encodedParams) {
        List<Param> params = new LinkedList<Param>();

        for (String encodedParam : encodedParams) {
            String[] nameAndValue = encodedParam.split("=");
            assert nameAndValue.length == 2;
            Param param = new Param(nameAndValue[0], nameAndValue[1]);
            params.add(param);
        }

        return params;
    }

    private static void shutdown(String description) {
        if (!description.isEmpty()) {
            log.error(description);
        }
        log.error("Shutdown programm");
        System.exit(1);
    }

}
