package de.tub.insin.ss17.grp1.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
        try{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(classifier);
        oos.flush();
        oos.close();
        System.out.println("Speichern erfolgreich");
        }catch (IOException e){
            System.out.println("Speichern nicht erfolgreich");
        }
    }

    // TODO move deserialization here
    public static void load(File file,
                            String classifierDescription) 
                            throws FileNotFoundException, IOException, ClassNotFoundException{
        // Alle models auflisten
        File test = new File(MODEL_FOLDER_PATH);
        
        String[] dir = test.list();
        for (int i=0; i > dir.length; i++){
            System.out.println(dir[i]);
        }
        // TODO Auswahl des Users
        
        try{
        ObjectInputStream model = new ObjectInputStream(new FileInputStream(MODEL_FOLDER_PATH + classifierDescription + MODEL_FILE_EXTENSION));
        
        Classifier classifier = (Classifier) model.readObject();
        model.close();
        }catch (IOException e){
            System.out.println("Laden nicht erfolgreich");
        }
    }
}
