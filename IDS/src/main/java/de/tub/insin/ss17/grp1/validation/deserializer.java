package de.tub.insin.ss17.grp1.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import weka.classifiers.Classifier;
import weka.core.Instances;

public class deserializer {
    
    private static final String modelFilename = "training.model";

    private static final String trainFilepath = "./training/" + modelFilename;
    
    public deserializer(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("trainFilepath"));
        
        Classifier cls = (Classifier) ois.readObject();
        ois.close();
    }
     
}
