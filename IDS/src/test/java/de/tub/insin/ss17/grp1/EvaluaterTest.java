package de.tub.insin.ss17.grp1;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tub.insin.ss17.grp1.validation.Evaluater;
import weka.core.Instances;

public class EvaluaterTest {

    @Test
    public void testRemoveBackground() {
        try {
            Evaluater evaluater = new Evaluater(TestHelper.someClassifier(),
                                                TestHelper.loadTraining());

            Instances data = TestHelper.loadTest();
            evaluater.removeBackground(data);
            assertTrue(data.size() < TestHelper.loadTest().size());
            evaluater.removeBackground(TestHelper.loadTest());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
