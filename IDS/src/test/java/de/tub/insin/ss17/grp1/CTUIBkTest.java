package de.tub.insin.ss17.grp1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.tub.insin.ss17.grp1.training.CTUIBk;
import de.tub.insin.ss17.grp1.util.ClassIndexs;
import de.tub.insin.ss17.grp1.util.IDSSharedConstants;
import weka.core.Instances;

public class CTUIBkTest {

    CTUIBk ibk;
    ClassIndexs indexs;
    Instances trainingData;

    @Before
    public void beforeEachTest() {
        this.trainingData = TestHelper.loadTraining();
        this.ibk = new CTUIBk();
        this.indexs = new ClassIndexs(trainingData);
        this.ibk.setClassIndexs(indexs);
        try {
            this.ibk.buildClassifier(trainingData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testDistributionForInstance() {
        try {
            double[][] predictions = this.ibk.distributionsForInstances(trainingData);

            System.out.println(predictions.length);
            for (int i = 0; i < predictions.length; i++) {
                assertEquals(IDSSharedConstants.CLASS_COUNT, predictions[i].length);
                assertEquals(0.0, predictions[i][this.indexs.BACKGROUND], 0.0);
                double botnetProb = predictions[i][this.indexs.BOTNET];
                double normalProb = predictions[i][this.indexs.NORMAL];
                assertTrue(botnetProb <= 1.0);
                assertTrue(normalProb <= 1.0);
                assertTrue(botnetProb >= 0.0);
                assertTrue(normalProb >= 0.0);
                assertEquals(1.0, botnetProb + normalProb, 0.01);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
