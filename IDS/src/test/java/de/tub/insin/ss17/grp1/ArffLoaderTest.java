package de.tub.insin.ss17.grp1;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import de.tub.insin.ss17.grp1.util.ArffLoader;
import weka.core.Instances;


public class ArffLoaderTest {

    @Before
    public void beforeEachTest() {
        TestHelper.assertArff();
    }

    @Test
    public void testBasic() {
        ArffLoader loader = new ArffLoader(TestHelper.ARFF_FOLDER);
        try {
            Instances testData = loader.loadTest();
            assertTrue(testData.classIndex() >= 0);
            assertTrue(testData.numClasses() >= 0);
            Instances trainingData = loader.loadTraining();
            assertTrue(trainingData.classIndex() >= 0);
            assertTrue(trainingData.numClasses() >= 0);
        } catch (Exception e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }
}
