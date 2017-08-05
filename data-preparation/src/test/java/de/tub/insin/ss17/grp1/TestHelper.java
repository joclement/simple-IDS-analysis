package de.tub.insin.ss17.grp1;

import java.io.File;
import java.util.List;

import com.beust.jcommander.JCommander;

import de.tub.insin.ss17.grp1.dataprep.DataCliManager;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TestHelper {

    protected static int totalLengthCsvs(String[] argv) throws Exception {
        DataCliManager manager = new DataCliManager();
        JCommander.newBuilder()
        .addObject(manager)
        .build()
        .parse(argv);
        List<File> csvs = manager.getScenarios();

        int totLength = 0;
        for(File csv : csvs){
            DataSource source = new DataSource(csv.getPath());
            source.getDataSet();
            Instances data = source.getDataSet();
            totLength += data.size();
        }
        return totLength;
    }
}
