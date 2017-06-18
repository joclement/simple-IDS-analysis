package de.tub.insin.ss17.grp1;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;

import de.tub.insin.ss17.grp1.training.Trainer;
import de.tub.insin.ss17.grp1.util.ArffLoader;
import de.tub.insin.ss17.grp1.util.Param;
import de.tub.insin.ss17.grp1.validation.Tester;

public class CliManager {

    private final static String TRAIN = "train";
    private final static String TEST = "test";

    @Parameter(description = "specify list of commands, options: train, test",
               required = true)
    private List<String> commands;

    @Parameter(names = {"--arffFolder", "-f"},
               description = "Path to the arff folder",
               required = true)
    private String dataFolder;

    // TODO find good way to specify ml parameters, f.x. for NN classifier
    @Parameter(names = {"--parameters", "-p"},
               description = "Parameters for the ml algorithm",
               required = true)
    private List<String> mlParams = new LinkedList<String>();


    private static final String classifierNameDescription = Trainer.getClassifierNamesDescription();
    // TODO specify options for classifiers somewehere, in annotation it is not really possible
    @Parameter(names = {"--classifierName", "-c"},
               description = "Name of the classifier, options: TODO",
               required = true)
    private String classifierName;

    public void run() {
        ArffLoader arffLoader = new ArffLoader(this.dataFolder);

        Trainer trainer = new Trainer(this.classifierName, prepare(this.mlParams));

        Tester tester = new Tester();

        if (commands.contains(TRAIN)) {
            try {
                trainer.train(arffLoader.loadTraining());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(-1);
            }
            try {
                trainer.save(new File(this.dataFolder));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(-1);
            }
        }

        if (commands.contains(TEST)) {
            try {
                tester.test(arffLoader.loadTest());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private List<Param> prepare(List<String> encodedParams) {
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
