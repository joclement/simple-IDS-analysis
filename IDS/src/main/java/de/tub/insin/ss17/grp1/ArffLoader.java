package de.tub.insin.ss17.grp1;

import weka.core.converters.ArffLoader.ArffReader;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


public class ArffLoader{

    public ArffLoader(String path) throws Exception {
        //C:\Users\darth\workspace\grp1-InSiN-master-29fa84279ed20bad5599e38ea657f362a4f23682\grp1-InSiN-master-29fa84279ed20bad5599e38ea657f362a4f23682\IDS\src\main\java\de\tub\insin\ss17\grp1
        DataSource source = new DataSource(path);
        Instances data = source.getDataSet();
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);

        //Nur ein beispiel um zu ausprobieren
        ClassificationViaRegression cVr = new ClassificationViaRegression();
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(cVr, data, 5, new Random(1));
        System.out.print(eval.toSummaryString());
    }

    public static void main(String[] argv) throws Exception {

    }
}
