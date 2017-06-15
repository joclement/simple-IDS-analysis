package de.tub.insin.ss17.grp1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class AppTest {

    private final static String destFolder = "../IDS/"
    		+ "src/main/"
    		+ "resources/test/";

    private final static String ctuFolder = "src/main/resources/TestCTU13/";

    private final static String scenarios = "6,11,12";

    private final static int ARFF_ATTRIBUTE_COUNT = 15;
    
    private final static int trainingPercentage = 80;
    
    {
    	StringBuilder builder = new StringBuilder();
    	builder.append(".." + File.separator);
    }

    @Before
    public void beforeEachTest() {
        FileUtils.deleteQuietly(new File(destFolder));
    }

    @After
    public void afterEachTest() {
        System.out.println("This is executed after each Test");
    }

    // TODO add checking for this test
    @Test
    public void testSplittedScenarios() {
        String[] argv = {"-s", scenarios,
                         "-d", destFolder,
                         "--ctu", ctuFolder};
        App.main(argv);
        
    }

    @Test
    public void testSeparateTestScenario() {
        String[] argv = {"-s", scenarios,
                         "-d", destFolder,
                         "--ctu", ctuFolder,
                         "-t"};
        App.main(argv);

        File destArffFolder = new File(destFolder);
        assertTrue(destArffFolder.exists());
        File trainingArff = new File(destArffFolder, "training/data.arff");
        assertTrue(trainingArff.exists());
        File testArff = new File(destArffFolder, "test/data.arff");
        assertTrue(testArff.exists());

        // TODO improve checks on arff data
        try {
            Instances trainInstance = new DataSource(trainingArff.getAbsolutePath()).getDataSet();
            assertEquals(ARFF_ATTRIBUTE_COUNT, trainInstance.numAttributes());

            Instances testInstance = new DataSource(testArff.getAbsolutePath()).getDataSet();
            assertEquals(ARFF_ATTRIBUTE_COUNT, testInstance.numAttributes());
            
            assertFalse("Data has missing values",trainInstance.contains(",,"));
            assertFalse("Data has missing values",testInstance.contains(",,"));
            
            assertFalse("portSrc and portDest start with \"0x\"",
            		trainInstance.contains(",0x"));
            assertFalse("portSrc and portDest start with \"0x\"",
            		testInstance.contains(",0x"));
            
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("Arff file loading failed",false);
        }
    }
    
    @Test
    public void testTrainingPercentage() throws Exception {
        String[] argv = {"-s", scenarios,
                         "-d", destFolder,
                         "--ctu", ctuFolder};
        App.main(argv);
        CliManager manager = new CliManager();
        JCommander.newBuilder()
        .addObject(manager)
        .build()
        .parse(argv);
        List<File> csvs = manager.getScenarios();
        int totLength =0;
        for(File csv:csvs){
            DataSource source = new DataSource(csv.getPath());
            source.getDataSet();
            Instances data = source.getDataSet();
            System.out.println(data.size());
        	totLength+=data.size();
        }
        DataSource source = new DataSource(destFolder+"training/data.arff");
        source.getDataSet();
        Instances data = source.getDataSet();
        
        int trainingLength = totLength * trainingPercentage/100;
        
        assertEquals( trainingLength, data.size() - 19 );
        source = new DataSource(destFolder+"test/data.arff");
        source.getDataSet();
        data = source.getDataSet();
        assertEquals(data.size() - 19,totLength - trainingLength);
        
    }

    //TODO fix test
    @Ignore
    @Test(expected = Exception.class)
    public void testNoArgs() throws Exception {
        String[] argv = {};
        App.main(argv);
    }
}
