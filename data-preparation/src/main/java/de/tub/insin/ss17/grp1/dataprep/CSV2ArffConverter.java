package de.tub.insin.ss17.grp1.dataprep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.shared.RuntimeWekaException;
import de.tub.insin.ss17.grp1.shared.SharedConstants;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;


/**
 * Converts .csv into .arff.
 *
 * @author Joris Clement
 * @author Philip Wilson
 *
 */
public class CSV2ArffConverter {

    private static final Logger log = LoggerFactory.getLogger(CSV2ArffConverter.class);

    private final static String NUMERIC_LIST = "5,8";

    private final static String BOTNET = "Botnet";
    private final static String NORMAL = "Normal";
    private final static String BACKGROUND = "Background";

    private static String findIp(String line, int attrIndex) throws IOException {
        int commas = 0;
        int start = 0;
        int counter = 0;
        boolean beginningIsFound = false;
        while (commas < attrIndex) {
            if (commas == attrIndex - 1 && !beginningIsFound) {
                start = counter;
                beginningIsFound = true;
            }
            if (line.charAt(counter) == ',') {
                commas++;
            }

            counter++;
        }
        return line.substring(start, counter - 1);
    }

    private static final String deleteLabelExcess(String line, String traffic) {
        StringBuilder toDel = new StringBuilder();
        line = line.replace("flow=", "");
        line = line.replace("From-", "");
        line = line.replace("To-", "");
        int index = line.lastIndexOf(traffic);
        while (index != line.length()) {
            toDel.append(line.charAt(index));
            index++;
        }
        String toDelete = toDel.toString();
        if (traffic.equals(BOTNET)) {
            line = line.replace(toDelete, SharedConstants.BOTNET);
        } else if (traffic.equals(NORMAL)) {
            line = line.replace(toDelete, SharedConstants.NORMAL);
        } else if (traffic.equals(BACKGROUND)) {
            line = line.replace(toDelete, SharedConstants.BACKGROUND);
        }

        return line;
    }

    private static final String hexaPortsToDecimal(String line) {
        while (line.contains("0x")) {
            int hexaIndex = line.lastIndexOf("0x");
            // Some hexa ports were larger than 65536
            if (line.charAt(hexaIndex + 6) == ',') {
                String hexa = line.substring(hexaIndex, hexaIndex + 6);
                line = line.replace(hexa, String.valueOf((Util.hex2decimal(hexa))));
            } else {
                line = "";
            }
        }
        return line;
    }

    private static final void deleteExcess(String line, String traffic,
                                           int lineNum, int totLines,
                                           File temp, boolean rB)
            throws IOException {
        if (!(traffic.equals(BACKGROUND) && rB)) {
            try {
                Writer fileOut = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(temp, true), SharedConstants.ENCODING));

                line = deleteLabelExcess(line, traffic);

                // Date format only accepts 3 digits after the seconds and not 6
                line = line.substring(0, 23) + line.substring(26);

                line = hexaPortsToDecimal(line);

                fileOut.write(line, 0, line.length());

                if (lineNum < totLines - 1) {
                    fileOut.write("\n", 0, "\n".length());
                }
                fileOut.close();
            } catch (IOException e) {
                throw new RuntimeWekaException(
                        "Failed to clean up OutputStream: " + e.getMessage());
            }
        }
    }

    private static final void parseLabel(File csv, boolean rB)
            throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(csv, SharedConstants.ENCODING);
        File temp = File.createTempFile("temp", ".csv");
        // now read the file line by line...
        int totLines = 0;
        LineNumberReader lnr = new LineNumberReader(
                new InputStreamReader(new FileInputStream(csv), SharedConstants.ENCODING));

        try {
            Writer fileOut = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(temp, true), SharedConstants.ENCODING));

            String first = lnr.readLine();

            if (first == null) {
                lnr.close();
                fileOut.close();
                scanner.close();
                throw new RuntimeException("string is null");
            }
            fileOut.write(first, 0, first.length());
            fileOut.write("\n", 0, "\n".length());
            fileOut.close();
        } catch (IOException e) {
            scanner.close();
            throw new RuntimeWekaException("Failed to clean up OutputStream: " + e.getMessage());
        }

        while (lnr.readLine() != null) {
            totLines++;
        }
        AVLTree tree = new AVLTree();

        int id = 0;

        scanner.nextLine();
        for (int j = 0; j < totLines; j++) {

            if (j == 0) {
                log.debug("0%");
            } else if (j == totLines / 4) {
                log.debug("25%");
            } else if (j == totLines / 2) {
                log.debug("50%");
            } else if (j == totLines * 3 / 4) {
                log.debug("75%");
            } else if (j == totLines - 1) {
                log.debug("100%");
            }

            String line = scanner.nextLine();

            if (!rB || !line.contains(BACKGROUND)) {
                String valueSrc = findIp(line, 4);
                String valueDest = findIp(line, 7);

                int position = tree.idOf(valueSrc);
                if (position != 0) {
                    line = line.replaceFirst(valueSrc, String.valueOf(position));
                } else {
                    id++;
                    tree.insert(valueSrc, id);
                    line = line.replaceFirst(valueSrc, String.valueOf(id));
                }

                position = tree.idOf(valueDest);
                if (position != 0) {
                    line = line.replaceFirst(valueDest, String.valueOf(position));
                } else {
                    id++;
                    tree.insert(valueDest, id);
                    line = line.substring(0, 28) +
                            line.substring(30).replaceFirst(valueDest, String.valueOf(id));
                }
            }

            if (line.contains(BOTNET)) {
                deleteExcess(line, BOTNET, j, totLines, temp, rB);
            } else if (line.contains(NORMAL)) {
                deleteExcess(line, NORMAL, j, totLines, temp, rB);
            } else if (line.contains(BACKGROUND)) {
                deleteExcess(line, BACKGROUND, j, totLines, temp, rB);
            }
        }
        tree.root = null;
        lnr.close();
        scanner.close();
        Files.copy(temp.toPath(), csv.toPath(), StandardCopyOption.REPLACE_EXISTING);
        if (!temp.delete()) {
            throw new RuntimeWekaException("tempFile wasnt deleted correctly");
        }
    }

    private static final void removeCsvHeader(List<File> csvsCopy) throws IOException {
        boolean flag = false;
        for (File csv : csvsCopy) {
            if (flag) {
                RandomAccessFile raf = new RandomAccessFile(csv, "rw");
                long writePosition = raf.getFilePointer();
                raf.readLine();
                long readPosition = raf.getFilePointer();

                byte[] buff = new byte[1024];
                int n;
                while (-1 != (n = raf.read(buff))) {
                    raf.seek(writePosition);
                    raf.write(buff, 0, n);
                    readPosition += n;
                    writePosition += n;
                    raf.seek(readPosition);
                }
                raf.setLength(writePosition);
                raf.close();
            }
            flag = true;
        }
    }

    private static final void transfer(final Reader source, final Writer destination)
            throws IOException {
        char[] buffer = new char[1024 * 16];
        int len = 0;
        while ((len = source.read(buffer)) >= 0) {
            destination.write(buffer, 0, len);
        }
    }

    private static void appendCSVs(final List<File> csvs, final File combination)
            throws IOException {
        for (File csv : csvs) {
            try (Reader source = new LineNumberReader(
                    new InputStreamReader(new FileInputStream(csv), SharedConstants.ENCODING));
                    Writer destination = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(combination, true), SharedConstants.ENCODING));) {
                log.debug("transfer {},{}", source, destination);
                transfer(source, destination);
            }
        }
    }

    private static File combine(List<File> csvs) throws IOException {
        File combination = File.createTempFile("combination", ".netflow");
        Files.copy(csvs.remove(0).toPath(), combination.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        log.debug("appendCSVs");
        appendCSVs(csvs, combination);
        combination.deleteOnExit();

        return combination;
    }

    private static File convert(File mergedSrcFile) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setNumericAttributes(NUMERIC_LIST);
        loader.setDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        loader.setDateAttributes("1");
        loader.setBufferSize(4800000);
        loader.setSource(mergedSrcFile);
        ArrayList<Instances> splitData = new ArrayList<>();
        Instances structure = loader.getStructure();
        log.debug("before getStructure");
        Instance next = loader.getNextInstance(structure);
        log.debug("after getStructure");

        while (next != null) {
            Instances temp = new Instances(structure, 0);
            int i = 0;
            while (next != null && i < 25000) {
                temp.add(next);
                next = loader.getNextInstance(structure);
                i++;
            }
            splitData.add(temp);
        }
        splitData.trimToSize();
        Instances fullData = new Instances(splitData.get(0), 0);
        for (Instances i : splitData) {
            fullData.addAll(i);
        }
        File arffTmp = Util.saveAsArff(fullData);
        return arffTmp;
    }

    /**
     * Main method of converter.
     *
     * @param csvs
     *            List of scenarios to be converted
     * @param removeBackground
     *            If true will not include Background Traffic into ARFF.
     *
     */
    public static File parse(List<File> csvs, boolean removeBackground) throws Exception {
        File combinedCsv = prepare(csvs, removeBackground);
        log.debug("convert");
        File combinedArff = convert(combinedCsv);
        if (!combinedCsv.delete()) {
            throw new RuntimeWekaException("tempFile wasnt deleted correctly");
        }
        log.debug("finished: File parse");
        return combinedArff;
    }

    private static File prepare(List<File> csvs, boolean rB)
            throws IOException, FileNotFoundException {
        log.debug("start: File parse");
        List<File> copyList = new ArrayList<>(csvs.size());
        for (File csv : csvs) {
            File copy = File.createTempFile("copy", ".csv");
            copyList.add(copy);
            Files.copy(csv.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        log.debug("removeCsvHeader");
        removeCsvHeader(copyList);
        log.debug("combine");
        File combinedCsv = combine(copyList);
        for (File copy : copyList) {
            if (!copy.delete()) {
                throw new RuntimeWekaException("tempFile wasnt deleted correctly");
            }
        }
        copyList.clear();
        log.debug("parseLabel");
        parseLabel(combinedCsv, rB);
        return combinedCsv;
    }
}
