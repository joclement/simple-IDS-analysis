package de.tub.insin.ss17.grp1.validation;


import de.tub.insin.ss17.grp1.util.ModelPersistence;
import de.tub.insin.ss17.grp1.training.*;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public interface test {
    
    public void test(Instances testingData) throws Exception;
    
    public Classifier getClassifier();
    // TODO generate load method 
    // TODO load classifier
    // laden der Datensätze .csv
    // laden des Models .model
    // laden der Arff datei?
    
    public void evaluation(Instances testingData);

    public String getFilename();
    
    
}
