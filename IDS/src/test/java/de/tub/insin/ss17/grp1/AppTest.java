package de.tub.insin.ss17.grp1;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Unit test for IDS.
 */
public class AppTest
{

    protected final static String BASIC_NN_PARAMS = "k=5,dist=20.0,distweight=none";

    protected final static String classifierName = "lnns";

    @Before
    public void beforeEachTest() {
        TestHelper.assertArff();
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

    // TODO add checks
    @Ignore
    @Test
    public void testOnlyTest() {
        String[] argv = {"-o", "test",
                         "-c", classifierName,
                         "-f", TestHelper.ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        IDSApp.main(argv);
    }

    // TODO add test for invalid args
    @Ignore
    @Test
    public void testWrongInput() {
    }

}
