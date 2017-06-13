package de.tub.insin.ss17.grp1;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class DataSplitter {

    private Integer percentageTrain;

    public DataSplitter(Integer percentageTrain) {
        assert percentageTrain < 100;
        assert percentageTrain > 0;
        this.percentageTrain = percentageTrain;
    }

    public List<File> split(File arff) {
        // TODO split the data by the given percentage
        // and put the result for the training in a training folder
        // and for the test in a test folder.
        // TODO Give both files the same name, FINAL_ARFF_FILENAME.
        List<File> splittedArffs = new LinkedList<File>();
        return splittedArffs;
    }

}
