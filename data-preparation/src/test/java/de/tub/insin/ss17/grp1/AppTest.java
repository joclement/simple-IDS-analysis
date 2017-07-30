package de.tub.insin.ss17.grp1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;

import org.junit.Before;
import org.junit.Test;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class AppTest {

    private final static String destFolder;

    private final static String ctuFolder;

    private final static String scenarios;

    private final static int ARFF_ATTRIBUTE_COUNT = 15;

    static {
        destFolder = "../IDS/src/main/resources/test/";
        ctuFolder = "src/main/resources/TestCTU13/";
        scenarios  = "6,11,12";
    }

    @Before
    public void beforeEachTest() {
        FileUtils.deleteQuietly(new File(destFolder));
    }

    @Test
    public void testHelp() throws Exception {
        String[] argv = {"-h"};
        App.main(argv);
    }

    @Test(expected = RuntimeException.class)
    public void testNoArgs() {
        String[] argv = {};
        App.main(argv);
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
            Instances trainInstances = new DataSource(trainingArff.getAbsolutePath()).getDataSet();
            assertEquals(ARFF_ATTRIBUTE_COUNT, trainInstances.numAttributes());

            Instances testInstances = new DataSource(testArff.getAbsolutePath()).getDataSet();
            assertEquals(ARFF_ATTRIBUTE_COUNT, testInstances.numAttributes());
            
            assertFalse("Data has missing values", trainInstances.contains(",,"));
            assertFalse("Data has missing values", testInstances.contains(",,"));
            
            assertFalse("portSrc and portDest start with \"0x\"",
                    trainInstances.contains(",0x"));
            assertFalse("portSrc and portDest start with \"0x\"",
                    testInstances.contains(",0x"));
            
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("Arff file loading failed",false);
        }
    }

    @Test
    public void testTrainingPercentage() throws Exception {
        final int trainingPercentage = 70;
        String[] argv = {"-s", scenarios,
                         "-d", destFolder,
                         "--ctu", ctuFolder,
                         "-p", "" + trainingPercentage};

        int totLength = TestHelper.totalLengthCsvs(argv);
        App.main(argv);
        int trainingLength = totLength * trainingPercentage/100;
        
        DataSource source = new DataSource(destFolder+"training/data.arff");
        Instances data = source.getDataSet();
        assertEquals( trainingLength, data.size());
        
        source = new DataSource(destFolder+"test/data.arff");
        data = source.getDataSet();
        assertEquals(totLength - trainingLength, data.size());
        
    }

}
