package de.tub.insin.ss17.grp1.util;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO improve this loading operations
public class ArffLoader {
    private static final Logger log = LoggerFactory.getLogger(ArffLoader.class);

    private static final String ARFF_FILENAME = "data.arff";

    private static final String TRAIN_FILEPATH = "./training/" + ARFF_FILENAME;

    private static final String TEST_FILEPATH = "./test/" + ARFF_FILENAME;

    private final File arffFolder;

    public ArffLoader(String path) {
        log.debug("start: ArffLoader");
        this.arffFolder = new File(path);
        log.debug("finished: ArffLoader");
    }

    public Instances loadTraining() throws Exception {
        log.debug("start: loadTraining");
        File trainFile = new File(this.arffFolder, TRAIN_FILEPATH);
        log.debug("finished: loadTraining");
        return this.load(trainFile);
    }

    public Instances loadTest() throws Exception {
        log.debug("start: loadTest");
        File testFile = new File(this.arffFolder, TEST_FILEPATH);
        log.debug("finished: loadTest");
        return this.load(testFile);
    }

    private Instances load(File file) throws Exception {
        log.debug("start: load");
        DataSource source = new DataSource(file.getAbsolutePath());
        Instances data = source.getDataSet();

        data.setClassIndex(data.numAttributes() - 1);
        log.debug("finished: load");
        return data;
    }
}