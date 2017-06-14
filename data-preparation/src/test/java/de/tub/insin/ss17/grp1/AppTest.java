package de.tub.insin.ss17.grp1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class AppTest {

    private final static String destFolder = "../IDS/src/main/resources/test/";

    private final static String ctuFolder = "src/main/resources/TestCTU13/";

    private final static String scenarios = "6,11,12";

    private final static int ARFF_ATTRIBUTE_COUNT = 15;

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
    public void testSeperateTestScenario() {
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
            
            assertFalse("Data has missing values(replace with 0 most likely)",trainInstance.contains(",,"));
            assertFalse("Data has missing values(replace with 0 most likely)",testInstance.contains(",,"));
            
            assertFalse("portSrc and portDest start with \"0x\"",trainInstance.contains(",0x"));
            assertFalse("sPort and dPort start with \"0x\"",testInstance.contains(",0x"));
            
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("Arff file loading failed",false);
        }
    }

    //TODO fix test
    @Ignore
    @Test(expected = Exception.class)
    public void testNoArgs() throws Exception {
        String[] argv = {};
        App.main(argv);
    }
}
