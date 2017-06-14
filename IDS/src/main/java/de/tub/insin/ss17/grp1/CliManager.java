package de.tub.insin.ss17.grp1;

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
    private final static String BOTH = "both";

    @Parameter(description = "command, options: train, test or both")
    private String command;

    @Parameter(names = {"--arffFolder", "-f"},
               description = "Path to the arff folder",
               required = true)
    private String dataFolder;

    // TODO find good way to specify ml parameters, f.x. for NN classifier
    @Parameter(names = {"--parameters", "-p"},
               description = "Parameters for the ml algorithm",
               required = true)
    private List<String> mlParams = new LinkedList<String>();

    public void run() {
        ArffLoader arffLoader = new ArffLoader(this.dataFolder);

        Trainer trainer = new Trainer(prepare(this.mlParams));

        Tester tester = new Tester();

        if (command != TEST) {
            try {
                trainer.train(arffLoader.loadTraining());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(-1);
            }
        }

        if (command != TRAIN) {
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
