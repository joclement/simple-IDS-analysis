package de.tub.insin.ss17.grp1.training;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.List;

import de.tub.insin.ss17.grp1.util.Param;
import weka.core.Instances;

public class Serializer {
    
    private static final String modelFilename = "training.model";

    private static final String trainFilepath = "./training/" + modelFilename;
    
    private NNClassifier nnClassifier;
    
    public Serializer(File file) throws IOException {

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(trainFilepath));
        oos.writeObject(nnClassifier);
        oos.flush();
        oos.close();
    }



}
