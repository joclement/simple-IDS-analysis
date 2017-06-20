package de.tub.insin.ss17.grp1.validation;

import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

import java.io.File;
import java.io.IOException;

import de.tub.insin.ss17.grp1.training.MlAlgo;
import de.tub.insin.ss17.grp1.util.ModelPersistence;

public class Tester {
    
    private test classifier;

    public Tester() {
        // TODO Auto-generated constructor stub
    }
    public void load(File folder) throws IOException {
       // Classifier classifier = ModelPersistence.load(folder, null); 
        Classifier classifier = this.classifier.getClassifier();
        ModelPersistence.load(folder, classifierDescription);
        
        
    }

    public void test(Instances testingData) {
        try {
            this.classifier.test(testingData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated method stub
        // TODO include class evaluation and validation
        // TODO load metrics
    }
    
    public void crossValidateModel(Classifier classifier, 
                                   Instances data, 
                                   int numFolds, 
                                   java.util.Random random){
     // TODO generate cross-validation method
    }
        
    
    // TODO Modify 
    public void save(File folder) throws IOException {
        Classifier classifier = this.classifier.getClassifier();
        ModelPersistence.save(classifier, folder, this.classifier.getFilename());
    }
        // TODO Auto-generated method stub
        // save test-results in folder 
        
    // TODO Generate Graphs
    // TODO find Graph Library
    

}
