package de.tub.insin.ss17.grp1;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSV2ArffConverter {

    private static final Logger log = LoggerFactory.getLogger(CSV2ArffConverter.class);

    private final static String NUMERIC_LIST = "5,8";

    private final static boolean REMOVE_BACKGROUND = false;

    private final static String BOTNET = DataSharedConstants.BOTNET;
    private final static String NORMAL = DataSharedConstants.NORMAL;
    private final static String BACKGROUND = DataSharedConstants.BACKGROUND;

    private static final void deleteExcess(int index, String line, String traffic,
            File csv, int lineNum, int totLines, File temp) throws IOException {

        String toDelete = "";
        line = line.replace("flow=", "");
        line = line.replace("From-", "");
        line = line.replace("To-", "");
        index += line.lastIndexOf(traffic);
        while(index != line.length()) {
            toDelete += line.charAt(index);
            index++;
        }
        line = line.substring(0, 21) + line.substring(25);
        FileOutputStream fileOut = new FileOutputStream(temp,true);
        while(line.contains("0x")) {
            int hexaIndex = line.lastIndexOf("0x");
            String hexa = line.substring(hexaIndex, hexaIndex + 6 );
            line = line.replace(hexa, String.valueOf((Util.hex2decimal(hexa))));
        }
        if(traffic == BACKGROUND && REMOVE_BACKGROUND) {
            fileOut.write("".getBytes());
        }
        else {
            line = line.replace(toDelete, "");
            fileOut.write(line.getBytes(),0,line.length());
            if(lineNum < totLines-1){
                fileOut.write("\n".getBytes(),0,"\n".length());
            }
        }
        fileOut.close();
    }

    private static final void parseLabel(File csv) throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(csv);
        File temp = File.createTempFile("temp",".csv");
        //now read the file line by line...
        int totLines = 0;
        int lineNum = 0;
        LineNumberReader lnr = new LineNumberReader(new FileReader(csv));
        while (lnr.readLine() != null) {
            totLines++;
        }
        for(int j =0; j<=totLines-1;j++){

            String line = scanner.nextLine();

            if(line.contains(BOTNET)) {
                deleteExcess(6,line,BOTNET,csv,lineNum, totLines,temp);
            }
            else if(line.contains(NORMAL)) {
                deleteExcess(6,line,NORMAL,csv,lineNum,totLines,temp);
            }
            else if(line.contains(BACKGROUND)) {
                deleteExcess(10,line,BACKGROUND,csv,lineNum,totLines,temp);
            }
            else{
                FileOutputStream fileOut = new FileOutputStream(temp,true);
                fileOut.write(line.getBytes(),0,line.length());
                fileOut.write("\n".getBytes(),0,"\n".length());
                fileOut.close();
            }
            lineNum++;
        }
        lnr.close();
        scanner.close();
        Files.copy(temp.toPath(), csv.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private static final void removeCsvHeader(List<File> csvsCopy) throws IOException {
        boolean flag = false;
        for (File csv: csvsCopy ){
            if(flag){
                //TODO Code is copied from StackOverflow :)
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

    private static final void transfer(final Reader source, final Writer destination) throws IOException  {
        char[] buffer = new char[1024 * 16];
        int len = 0;
        while ((len = source.read(buffer)) >= 0) {
            destination.write(buffer, 0, len);
        }
    }

    private static void appendCSVs(final List<File> csvs, final File combination)
        throws IOException {
        for (File csv : csvs) {
            try (Reader source = new LineNumberReader(new FileReader(csv));
                    Writer destination = new BufferedWriter(new FileWriter(combination, true)); ) {
                log.debug("transfer {},{}", source, destination);
                transfer(source, destination);
                    }
        }
    }

    private static File combine(List<File> csvs) throws IOException {
        File combination = File.createTempFile("combination", ".netflow");
        Files.copy(csvs.remove(0).toPath(), combination.toPath(), StandardCopyOption.REPLACE_EXISTING);

        log.debug("appendCSVs");
        appendCSVs(csvs, combination);

        return combination;
    }

    private static File convert(File mergedSrcFile) throws Exception  {
        // TODO code is copied from weka website
        CSVLoader loader = new CSVLoader();
        loader.setNumericAttributes(NUMERIC_LIST);
        loader.setDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        loader.setDateAttributes("1");
        loader.setBufferSize(4800000);
        loader.setSource(mergedSrcFile);
        List<Instances> splitData = new ArrayList<>();
        Instance next = loader.getNextInstance(loader.getStructure());
        while(next != null){
            Instances temp = new Instances(loader.getStructure(), 0);
            int i = 0;
            while(next != null && i<25000){
                temp.add(next);
                next = loader.getNextInstance(loader.getStructure());
                i++;
            }
            splitData.add(temp);
        }
        Instances fullData = new Instances(splitData.get(0),0);
        for(Instances i : splitData){
            fullData.addAll(i);
        }
        File arffTmp = Util.saveAsArff(fullData);
        return arffTmp;
    }

    public static File parse(List<File> csvs) throws Exception {
        log.debug("start: File parse");
        List<File> copyList = new ArrayList<File>();
        for(File csv : csvs){
            File copy = File.createTempFile("copy",".csv");
            copyList.add(copy);
            Files.copy(csv.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        log.debug("removeCsvHeader");
        removeCsvHeader(copyList);
        log.debug("combine");
        File combinedCsv = combine(copyList);
        log.debug("parseLabel");
        parseLabel(combinedCsv);
        log.debug("convert");
        File combinedArff = convert(combinedCsv);

        copyList.clear();
        log.debug("finished: File parse");
        return combinedArff;
    }
}
