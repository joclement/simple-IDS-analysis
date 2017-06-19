
package de.tub.insin.ss17.grp1;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class CSV2ArffConverter {
	
	private final static String NOMINAL_LIST = "5,8";
	
	private final static String botnet = "Botnet";
	private final static String normal = "Normal";
	private final static String background = "Background";
	
	private static final void deleteExcess(int index, String line,String traffic,File csv, int lineNum
			,int totLines, File temp) throws IOException{
	    String toDelete = "";

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
	        
	        if(line.contains(botnet)) {
	        	deleteExcess(6,line,botnet,csv,lineNum, totLines,temp);
	        }
	        else if(line.contains(normal)) { 
	        	deleteExcess(6,line,normal,csv,lineNum,totLines,temp);
	        }
	        else if(line.contains(background)) { 
	        	deleteExcess(10,line,background,csv,lineNum,totLines,temp);
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
                
            transfer(source, destination);
             }
        }
    }

    private static File combine(List<File> csvs) throws IOException {
        File combination = File.createTempFile("combination", ".netflow");
        Files.copy(csvs.remove(0).toPath(), combination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        appendCSVs(csvs, combination);

        return combination;
    }

    private static File convert(File mergedSrcFile) throws Exception  {
        // TODO code is copied from weka website
        CSVLoader loader = new CSVLoader();
        loader.setNominalAttributes(NOMINAL_LIST);
        loader.setSource(mergedSrcFile);
        Instances data = loader.getDataSet();

        File arffTmp = Util.saveAsArff(data);

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
