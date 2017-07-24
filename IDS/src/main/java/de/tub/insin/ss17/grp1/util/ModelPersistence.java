package de.tub.insin.ss17.grp1.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import weka.classifiers.Classifier;


public class ModelPersistence {

    private static final String MODEL_FOLDER_PATH = "./model/";

    private static final String MODEL_FILE_EXTENSION = ".model";

    public static File save(Classifier classifier,
                            File arffFolder,
                            String classifierDescription) {
        String modelPath = MODEL_FOLDER_PATH + classifierDescription + MODEL_FILE_EXTENSION;
        File model = new File(arffFolder, modelPath);
        model.getParentFile().mkdirs();
        save(classifier, model);
        return model;
    }

    public static void save(Classifier classifier, File file) {
        try (ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(classifier);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find file to store classifier in."
                                     + "FileNotFoundException: " + e.getLocalizedMessage());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save classifier."
                                     + "IOException: " + e.getLocalizedMessage());
        }
    }

    // TODO move deserialization here
    public static Classifier load(File classifierFile) {
        Classifier classifier = null;
        try (ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(classifierFile))) {
            classifier = (Classifier) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find file to load classifier from."
                                     + "FileNotFoundException: " + e.getLocalizedMessage());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load classifier."
                                     + "IOException: " + e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFound: " + e.getLocalizedMessage());
        }

        return classifier;
    }

    public static List<Classifier> loadAll(File arffFolder) {

        List<File> classifierFiles = loadAllFiles(arffFolder);

        List<Classifier> classifiers = new LinkedList<>();

        for (File classifierFile : classifierFiles) {
            classifiers.add(load(classifierFile));
        }

        return classifiers;
    }

    public static List<File> loadAllFiles(File arffFolder) {

        File modelFolder = new File(arffFolder, MODEL_FOLDER_PATH);
        FilenameFilter fileExtensionFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(MODEL_FILE_EXTENSION);
            }
        };
        File[] classifierFiles = modelFolder.listFiles(fileExtensionFilter);
        return Arrays.asList(classifierFiles);
    }
}
