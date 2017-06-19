package de.tub.insin.ss17.grp1.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import weka.classifiers.Classifier;

public class ModelPersistence {

    private static final String MODEL_FOLDER_PATH = "./model/";

    private static final String MODEL_FILE_EXTENSION = ".model";

    public static void save(Classifier classifier,
                            File arffFolder,
                            String classifierDescription) throws IOException {
        String modelPath = MODEL_FOLDER_PATH + classifierDescription + MODEL_FILE_EXTENSION;
        File model = new File(arffFolder, modelPath);
        model.getParentFile().mkdirs();
        save(classifier, model);
    }

    public static void save(Classifier classifier, File file) throws IOException {

        // TODO I think some rework for the try, catch, throw IOException is necessary here for 
        // good closing of the stream
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(classifier);
        oos.flush();
        oos.close();
    }

    // TODO move deserialization here
}
