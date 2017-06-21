package de.tub.insin.ss17.grp1.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

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
    public static Classifier load(File classifierFile) throws FileNotFoundException, IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(classifierFile));
        Classifier classifier = (Classifier) ois.readObject();
        ois.close();

        return classifier;
    }

    public static List<Classifier> loadAll(File arffFolder) throws FileNotFoundException, ClassNotFoundException, IOException {

        File modelFolder = new File(arffFolder, MODEL_FOLDER_PATH);
        FilenameFilter fileExtensionFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(MODEL_FILE_EXTENSION);
            }
        };
        File[] classifierFiles = modelFolder.listFiles(fileExtensionFilter);

        List<Classifier> classifiers = new LinkedList<>();

        for (File classifierFile : classifierFiles) {
            classifiers.add(load(classifierFile));
        }

        return classifiers;
    }
}
