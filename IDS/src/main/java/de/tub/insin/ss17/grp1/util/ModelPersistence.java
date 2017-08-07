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

import de.tub.insin.ss17.grp1.shared.SharedUtil;
import weka.classifiers.Classifier;


/**
 * Class to manage the persistence(saving, loading) of the classifier models.
 *
 * @author Joris Clement
 *
 */
public class ModelPersistence {

    private static final String MODEL_FOLDER_PATH = "./model/";

    private static final String MODEL_FILE_EXTENSION = ".model";

    /**
     * save a classifier in a prepared folder in the arff folder using a given name description.
     *
     * @param classifier the classifier, which will be saved.
     * @param arffFolder the arff folder.
     * @param classifierDescription part of the classifier filename.
     * @return the saved classifier file.
     */
    public static File save(Classifier classifier,
                            File arffFolder,
                            String classifierDescription) {
        String modelPath = MODEL_FOLDER_PATH + classifierDescription + MODEL_FILE_EXTENSION;
        File model = new File(arffFolder, modelPath);
        SharedUtil.checkedMkDir(model.getParentFile());
        save(classifier, model);
        return model;
    }

    private static void save(Classifier classifier, File file) {
        try (ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(classifier);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find file to store classifier in."
                                     + "FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save classifier."
                                     + "IOException: " + e.getMessage());
        }
    }

    public static Classifier load(File classifierFile) {
        Classifier classifier = null;
        try (ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(classifierFile))) {
            classifier = (Classifier) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find file to load classifier from."
                                     + "FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load classifier."
                                     + "IOException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFound: " + e.getMessage());
        }

        return classifier;
    }

    public static List<Classifier> loadAll(File arffFolder) {
        List<File> classifierFiles = loadAllFiles(arffFolder);
        List<Classifier> classifiers = new LinkedList<Classifier>();
        for (File classifierFile : classifierFiles) {
            classifiers.add(load(classifierFile));
        }
        return classifiers;
    }

    /**
     * load all saved classifier model files.
     *
     * @param arffFolder the arff folder.
     * @return list of loaded classifier files.
     */
    public static List<File> loadAllFiles(File arffFolder) {
        FilenameFilter fileExtensionFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(MODEL_FILE_EXTENSION);
            }
        };

        File modelFolder = new File(arffFolder, MODEL_FOLDER_PATH);
        File[] classifierFiles = modelFolder.listFiles(fileExtensionFilter);
        if (classifierFiles == null) {
            throw new RuntimeException("Model Folder does not exist.");
        }
        return Arrays.asList(classifierFiles);
    }
}
