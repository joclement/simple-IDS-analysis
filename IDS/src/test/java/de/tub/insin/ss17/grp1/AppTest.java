package de.tub.insin.ss17.grp1;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import de.tub.insin.ss17.grp1.training.Trainer;
import de.tub.insin.ss17.grp1.util.ModelPersistence;


/**
 * Unit test for IDS.
 */
public class AppTest
{

    protected final static String BASIC_NN_PARAMS = "-K=5";

    protected final static String classifierName = "lnns";

    @Before
    public void beforeEachTest() {
        TestHelper.assertArff();
        FileUtils.deleteQuietly(new File(TestHelper.arffFolder(), "results"));
        FileUtils.deleteQuietly(new File(TestHelper.arffFolder(), "model"));
    }

    @Test
    public void testHelp() {
        String[] argv = {"-h"};
        IDSApp.main(argv);
    }

    @Test
    public void testBoth() {
        String[] argv = {"-c", classifierName,
                         "-f", TestHelper.ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        IDSApp.main(argv);
        File[] results = new File(TestHelper.arffFolder(), "results").listFiles();
        assertEquals(1, results.length);
    }

    @Test
    public void testOnlyTrain() {
        String[] argv = {"-o", "train",
                         "-c", classifierName,
                         "-f", TestHelper.ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        IDSApp.main(argv);
        assertEquals(1, ModelPersistence.loadAll(TestHelper.arffFolder()).size());
    }

    @Test(expected = RuntimeException.class)
    public void testNoArgs() {
        String[] argv = {};
        IDSApp.main(argv);
    }

    @Test
    public void testJ48() {
        String[] argv = {"-c", Trainer.J48,
                         "-f", TestHelper.ARFF_FOLDER};
        IDSApp.main(argv);
    }
}
