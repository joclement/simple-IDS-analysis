
package de.tub.insin.ss17.grp1;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.List;

public class CSV2ArffConverter {

    private static final void transfer(final Reader source, final Writer destination) throws IOException {
        char[] buffer = new char[1024 * 16];
        int len = 0;
        while ((len = source.read(buffer)) >= 0) {
            destination.write(buffer, 0, len);
        }
    }

    private static void appendCSVs(final List<File> csvs, final File combination)
                 throws IOException{

        for (File csv : csvs) {
             try (Reader source = new LineNumberReader(new FileReader(csv));
                  Writer destination = new BufferedWriter(new FileWriter(combination)); ) {
                 transfer(source, destination);
             }
        }
    }

    private static File combine(List<File> csvs) throws IOException{

        File combination = File.createTempFile("combination", ".netflow");
        Files.copy(csvs.remove(0).toPath(), combination.toPath(), StandardCopyOption.REPLACE_EXISTING);

        appendCSVs(csvs, combination);

        return combination;
    }

    private static File convert(File mergedSrcFile) throws IOException {
        // TODO code is copied from weka website
        CSVLoader loader = new CSVLoader();
        loader.setSource(mergedSrcFile);
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        File arffTmp = File.createTempFile("combinedArff", ".arff");
        saver.setFile(arffTmp);
        saver.setDestination(arffTmp);
        saver.writeBatch();

        return arffTmp;
    }

    public static File parse(List<File> csvs) throws IOException {
        File combinedCsv = combine(csvs);
        File combinedArff = convert(combinedCsv);
        return combinedArff;
    }
}
