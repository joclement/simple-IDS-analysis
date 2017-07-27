package de.tub.insin.ss17.grp1;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tub.insin.ss17.grp1.shared.SharedConstants;
import de.tub.insin.ss17.grp1.validation.Evaluater;
import weka.core.Instance;
import weka.core.Instances;

public class EvaluaterTest {

    @Test
    public void testRemoveBackground() {
        try {
            Evaluater evaluater = new Evaluater(TestHelper.quickClassifier(),
                                                TestHelper.loadTraining());

            Instances data = TestHelper.loadTest();
            evaluater.removeBackground(data);
            assertTrue(data.size() < TestHelper.loadTest().size());
            for (Instance instance : data) {
                String classValue = instance.stringValue(instance.classIndex());
                assertFalse(SharedConstants.BACKGROUND.equals(classValue));
                assertTrue(SharedConstants.NORMAL.equals(classValue) ||
                           SharedConstants.BOTNET.equals(classValue));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
