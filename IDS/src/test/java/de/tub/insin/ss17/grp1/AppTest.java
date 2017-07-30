package de.tub.insin.ss17.grp1;

import org.junit.Before;
import org.junit.Test;

import de.tub.insin.ss17.grp1.training.Trainer;


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
    }

    @Test
    public void testHelp() {
        String[] argv = {"-h"};
        IDSApp.main(argv);
    }

    // TODO add checks
    @Test
    public void testBoth() {
        String[] argv = {"-c", classifierName,
                         "-f", TestHelper.ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        IDSApp.main(argv);
    }

    // TODO add checks
    @Test
    public void testOnlyTrain() {
        String[] argv = {"-o", "train",
                         "-c", classifierName,
                         "-f", TestHelper.ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        IDSApp.main(argv);
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
