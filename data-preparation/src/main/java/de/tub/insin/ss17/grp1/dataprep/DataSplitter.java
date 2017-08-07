package de.tub.insin.ss17.grp1.dataprep;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Class to divide data into a test ARFF and a training ARFF.
 *
 * @author Joris Clement
 *
 */

public class DataSplitter {

    private Integer percentageTrain;

    /**
     * Constructor that cheks the parameter for validity
     *
     * @param percentageTrain
     *                  Integer which corresponds to the percentage of data
     *                  assigned to become the training set
     */
    public DataSplitter(Integer percentageTrain) {
        if (percentageTrain > 100 || percentageTrain < 0) {
            throw new IllegalArgumentException(
                    "the parameter percentageTrain has to be between 0 and 100");
        }
        this.percentageTrain = percentageTrain;
    }

    /**
     * Main method of Data Splitter
     *
     * @param arff Arff containing one or more scenarios
     * @return splittedArffs List of 2 arffs, one training , the other test.
     * @throws Exception
     */
    public List<File> split(File arff) throws Exception {
        List<File> splittedArffs = new LinkedList<File>();

        DataSource source = new DataSource(arff.getPath());
        source.getDataSet();
        Instances data = source.getDataSet();

        int totSize = data.size();
        int trainSize = (int) (totSize * percentageTrain / 100.0);
        int testSize = totSize - trainSize;

        Instances trainData = new Instances(data, 0, trainSize);
        Instances testData = new Instances(data, trainSize, testSize);

        File trainArff = Util.saveAsArff(trainData);
        File testArff = Util.saveAsArff(testData);

        splittedArffs.add(trainArff);
        splittedArffs.add(testArff);

        return splittedArffs;
    }
}
