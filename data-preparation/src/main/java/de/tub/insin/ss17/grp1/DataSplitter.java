package de.tub.insin.ss17.grp1;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class DataSplitter {

    // TODO add option to split by other type than percentage of number of lines

    private Integer percentageTrain;

    private static final Logger log = LoggerFactory.getLogger(DataSplitter.class);

    public DataSplitter(Integer percentageTrain) {
        log.debug("-start: DataSplitter");
        assert percentageTrain < 100;
        assert percentageTrain > 0;
        this.percentageTrain = percentageTrain;
        log.debug("-finished: DataSplitter");
    }

    public List<File> split(File arff) throws Exception {
        log.debug("-start: List split");
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
        assert splittedArffs.size() == 2;

        log.debug("-finished: List split");
        return splittedArffs;
    }


}
