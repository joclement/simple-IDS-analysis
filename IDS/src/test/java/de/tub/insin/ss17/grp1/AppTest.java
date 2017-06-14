package de.tub.insin.ss17.grp1;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for IDS.
 */
public class AppTest 
{

    private final static String ARFF_FOLDER = "./src/main/resources/testArffFolder/";

    private final static String NN_PARAMS = "k=5,dist=20.0";

    @Before
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }

    @After
    public void afterEachTest() {
        System.out.println("This is executed after each Test");
    }

    @Test
    public void testTrainCommand() {
        String[] argv = {"train",
                         "-f", ARFF_FOLDER,
                         "-p", NN_PARAMS};
        App.main(argv);
    }

}
