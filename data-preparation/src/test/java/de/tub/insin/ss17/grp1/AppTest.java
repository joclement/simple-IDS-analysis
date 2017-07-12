package de.tub.insin.ss17.grp1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class AppTest {

    private final static String destFolder;

    private final static String ctuFolder;

    private final static String scenarios;

    private final static int ARFF_ATTRIBUTE_COUNT = 15;

    private final static int trainingPercentage = 80;

    static {
        destFolder = "../IDS/src/main/resources/test/";
        ctuFolder = "src/main/resources/TestCTU13/";
        scenarios  = "6,11,12";
    }

    @Before
    private void beforeEachTest() {
        FileUtils.deleteQuietly(new File(destFolder));
    }

    @After
    private void afterEachTest() {

    }

    // TODO add checking for this test
    @Test
    public void testSplittedScenarios() throws Exception {
        String[] argv = {"-s", scenarios,
                         "-d", destFolder,
                         "--ctu", ctuFolder};
        App.main(argv);
        
    }

    @Test
    public void testSeparateTestScenario() throws Exception {
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
    
    private int totalLengthCsvs(String[] argv) throws Exception{
        DataCliManager manager = new DataCliManager();
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
            totLength+=data.size();
        }
        return totLength;
    }

    @Test
    public void testTrainingPercentage() throws Exception {
        String[] argv = {"-s", scenarios,
                         "-d", destFolder,
                         "--ctu", ctuFolder};

        int totLength = totalLengthCsvs(argv);
        App.main(argv);
        int trainingLength = totLength * trainingPercentage/100;
        
        DataSource source = new DataSource(destFolder+"training/data.arff");
        Instances data = source.getDataSet();
        assertEquals( trainingLength, data.size());
        
        source = new DataSource(destFolder+"test/data.arff");
        data = source.getDataSet();
        assertEquals(totLength - trainingLength, data.size());
        
    }

    //TODO fix test
    @Ignore
    @Test(expected = Exception.class)
    public void testNoArgs() throws Exception {
        String[] argv = {};
        App.main(argv);
    }
}
