package de.tub.insin.ss17.grp1;

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

    private final static String NOMINAL_LIST = "5,8";

    private final static String BOTNET = "Botnet";
    private final static String NORMAL = "Normal";
    private final static String BACKGROUND = "Background";

    private static final Logger log = LoggerFactory.getLogger(CSV2ArffConverter.class);

    private static final void deleteExcess(int index, String line,String traffic,File csv, int lineNum
            ,int totLines, File temp) throws IOException{
        String toDelete = "";
        line = line.replace("flow=", "");
        line = line.replace("From-", "");
        line = line.replace("To-", "");
        index += line.lastIndexOf(traffic);
        while(index != line.length()){
            toDelete += line.charAt(index);
            index++;
        }
        FileOutputStream fileOut = new FileOutputStream(temp,true);
        line = line.replace(toDelete, "");

        fileOut.write(line.getBytes(),0,line.length());
        if(lineNum < totLines-1){
            fileOut.write("\n".getBytes(),0,"\n".length());
        }

        fileOut.close();
    }

    private static final void parseLabel(File csv) throws FileNotFoundException, IOException{
        log.debug("-start parse label-");
        Scanner scanner = new Scanner(csv);
        File temp = File.createTempFile("temp",".csv");
        //now read the file line by line...
        int totLines = 0;
        int lineNum = 0;
        LineNumberReader lnr = new LineNumberReader(new FileReader(csv));
        while (lnr.readLine() != null){
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
        log.debug("-finished parse label-");
    }

    private static final void removeCsvHeader(List<File> csvsCopy) throws IOException {
        int cnt = 1;
        log.debug("-start removeCsvHeader-");
        boolean flag = false;
        for (File csv: csvsCopy ){
            log.debug("-start for-");
            if(flag){
                log.debug("-start if-");
                //TODO Code is copied from StackOverflow :)
                RandomAccessFile raf = new RandomAccessFile(csv, "rw");
                long writePosition = raf.getFilePointer();
                raf.readLine();
                long readPosition = raf.getFilePointer();

                byte[] buff = new byte[1024];
                int n;
                while (-1 != (n = raf.read(buff))) {
                    
                    log.debug("-start while loop nr.:{}",cnt);
                    raf.seek(writePosition);
                    raf.write(buff, 0, n);
                    readPosition += n;
                    writePosition += n;
                    raf.seek(readPosition);
                    log.debug("-finished while loop nr.:{}", cnt);
                    cnt++;
                }
                raf.setLength(writePosition);
                raf.close();
                log.debug("-finished if-");
            }
            log.debug("-finished for-");
           flag = true;
        }
        log.debug("-finished removeCsvHeader-");
    }

    private static final void transfer(final Reader source, final Writer destination) throws IOException  {
        log.debug("-start transfer-");
        char[] buffer = new char[1024 * 16];
        int len = 0;
        while ((len = source.read(buffer)) >= 0) {
            destination.write(buffer, 0, len);
        }
        log.debug("-finished transfer-");
    }

    private static void appendCSVs(final List<File> csvs, final File combination)
                 throws IOException {
        log.debug("-start appendCSVs-");
        for (File csv : csvs) {
             try (Reader source = new LineNumberReader(new FileReader(csv));
            Writer destination = new BufferedWriter(new FileWriter(combination, true)); ) {

            transfer(source, destination);
             }
        }
        log.debug("-finished appendCSVs-");
    }

    private static File combine(List<File> csvs) throws IOException {
        log.debug("-start File combine-");
        File combination = File.createTempFile("combination", ".netflow");
        Files.copy(csvs.remove(0).toPath(), combination.toPath(), StandardCopyOption.REPLACE_EXISTING);

        appendCSVs(csvs, combination);

        log.debug("-finished File combine with: {}", combination);
        return combination;
    }

    private static File convert(File mergedSrcFile) throws Exception  {
        // TODO code is copied from weka website
        log.debug("-start File convert-");
        CSVLoader loader = new CSVLoader();
        loader.setNominalAttributes(NOMINAL_LIST);
        loader.setSource(mergedSrcFile);
        Instances data = loader.getDataSet();

        File arffTmp = Util.saveAsArff(data);
        
        log.debug("-finished File convert with: {}", arffTmp);
        return arffTmp;
    }

    public static File parse(List<File> csvs) throws Exception {
        List<File> copyList = new ArrayList<File>();
        for(File csv : csvs){
            File copy = File.createTempFile("copy",".csv");
            copyList.add(copy);
            Files.copy(csv.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        removeCsvHeader(copyList);

        File combinedCsv = combine(copyList);
        parseLabel(combinedCsv);

        File combinedArff = convert(combinedCsv);

        copyList.clear();
        return combinedArff;
    }
}
