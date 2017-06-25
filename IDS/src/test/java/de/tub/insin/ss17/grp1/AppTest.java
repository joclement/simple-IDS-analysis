package de.tub.insin.ss17.grp1;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Unit test for IDS.
 */
public class AppTest
{

    protected final static String ARFF_FOLDER = "./src/main/resources/test/";

    protected final static String BASIC_NN_PARAMS = "k=5,dist=20.0,distweight=none";

    protected final static String classifierName = "lnns";

    @Before
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }

    @After
    public void afterEachTest() {
        System.out.println("This is executed after each Test");
    }

    @Test
    public void testBasic() {
        String[] argv = {"-c", classifierName,
                         "-f", ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        App.main(argv);
    }

    // TODO add checks
    @Test
    public void testOnlyTrain() {
        String[] argv = {"-o", "train",
                         "-c", classifierName,
                         "-f", ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        App.main(argv);
    }

    // TODO add checks
    @Ignore
    @Test
    public void testTestCommand() {
        String[] argv = {"-o", "test",
                         "-c", classifierName,
                         "-f", ARFF_FOLDER,
                         "-p", BASIC_NN_PARAMS};
        App.main(argv);
    }

    // TODO add test for invalid args
    @Ignore
    @Test
    public void testWrongInput() {
    }

}
