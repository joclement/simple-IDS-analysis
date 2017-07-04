package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
               description = "to specify to do just train, test. options: train, test")
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


    // TODO specify options for classifiers somewehere, in annotation it is not really possible
    @SuppressWarnings("unused")
    private static final String classifierNameDescription = Trainer.getClassifierNamesDescription();
    @Parameter(names = {"--classifierName", "-c"},
               description = "Name of the classifier, options: TODO",
               required = true)
    private String classifierName;

    public void run() {
        ArffLoader arffLoader = new ArffLoader(this.dataFolder);

        if (only != TEST) {
            log.info("--- start " + TRAIN + " ---");
            Trainer trainer = new Trainer(this.classifierName, prepare(this.mlParams));
            try {
                trainer.train(arffLoader.loadTraining());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("ERROR: failed to train");
                e.printStackTrace();
                log.error("quit program");
                System.exit(-1);
            }
            try {
                trainer.save(new File(this.dataFolder));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("ERROR: failed to save");
                e.printStackTrace();
                log.error("quit program");
                System.exit(-1);
            }
            log.info("--- finished " + TRAIN + " ---");
        }

        if (only != TRAIN) {
            log.info("--- start " + TEST + " ---");
            List<File> classifierFiles = ModelPersistence.loadAllFiles(new File(this.dataFolder));

            File classifierFile = this.decide(classifierFiles);
            Classifier classifier = null;
            try {
                classifier = ModelPersistence.load(classifierFile);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                log.error("quit program");
                System.exit(-1);
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                log.error("quit program");
                System.exit(-1);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                log.error("quit program");
                System.exit(-1);
            }
            ResultPersistence resultPersistence = new ResultPersistence(
                this.dataFolder, classifierFile.getName());

            try {
                Evaluater evaluater = new Evaluater(classifier, arffLoader.loadTraining());
                evaluater.evaluate(arffLoader.loadTest(), resultPersistence);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("ERROR: failed to evaluate");
                e.printStackTrace();
                log.error("Quit program");
                System.exit(-1);
            }
            log.info("--- finished " + TEST + " ---");
        }
    }

    private File decide(List<File> classifiers) {
        // TODO let it work correctly
        return classifiers.get(0);
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
}
